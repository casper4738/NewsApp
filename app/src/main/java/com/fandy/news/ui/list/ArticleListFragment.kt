package com.fandy.news.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.CombinedLoadStates
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import com.fandy.news.R
import com.fandy.news.databinding.ArticleListFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
@ExperimentalPagingApi
class ArticleListFragment : Fragment() {
    private val viewModel: ArticleListViewModel by hiltNavGraphViewModels(R.id.navgraph)
    private var job: Job? = null
    private lateinit var adapter: ArticleAdapter
    private var _binding: ArticleListFragmentBinding? = null
    private val dropdownOptions = arrayOf("Top Headlines", "All News")

    private val binding
        get() = _binding!!

    override
    fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ArticleListFragmentBinding.inflate(inflater, container, false)

        initAdapter()
        setupDropdown()

        return binding.root
    }

    private fun setupDropdown() {

        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_spinner_item, dropdownOptions
        )

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinner.setAdapter(adapter)
        binding.spinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                when (position) {
                    0 -> {
                        getTopHeadlines()
                    }
                    1 -> {
                        getAllNews()
                    }
                }
            } // to close the onItemSelected

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }

    }

    private fun initAdapter() {
        adapter = ArticleAdapter()
        binding.articleList.adapter = adapter

        adapter.addLoadStateListener { loadState ->
            binding.articleList.isVisible = loadState.refresh is LoadState.NotLoading
            binding.progress.isVisible = loadState.refresh is LoadState.Loading
            manageErrors(loadState)
        }
    }

    private fun getAllNews() {
        job?.cancel()

        job = lifecycleScope.launch {
            viewModel.loadAllArticles("tesla", "", "").collectLatest { adapter.submitData(it) }
        }

    }

    private fun getTopHeadlines() {
        job?.cancel()

        job = lifecycleScope.launch {
            viewModel.loadTopArticles("business", "").collectLatest { adapter.submitData(it) }
        }

    }

    private fun manageErrors(loadState: CombinedLoadStates) {
        binding.errorText.isVisible = loadState.refresh is LoadState.Error
        binding.retryButton.isVisible = loadState.refresh is LoadState.Error
        binding.retryButton.setOnClickListener { adapter.retry() }

        val errorState = loadState.source.append as? LoadState.Error
            ?: loadState.source.prepend as? LoadState.Error
            ?: loadState.append as? LoadState.Error
            ?: loadState.prepend as? LoadState.Error

        errorState?.let {
            val errorText = resources.getString(R.string.error) + it.error.toString()
            binding.errorText.text = errorText
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}