package com.fandy.news.ui.headline

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.CombinedLoadStates
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import com.fandy.news.R
import com.fandy.news.databinding.HeadlineFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
@ExperimentalPagingApi
class HeadlineFragment : Fragment() {
    private val viewModel: HeadlineViewModel by hiltNavGraphViewModels(R.id.navgraph)
    private var _binding: HeadlineFragmentBinding? = null
    private val binding get() = _binding!!
    private var job: Job? = null
    private lateinit var adapter: HeadlineArticleAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = HeadlineFragmentBinding.inflate(inflater, container, false)

        initAdapter()
        getTopHeadlines()

        return binding.root
    }


    private fun initAdapter() {
        adapter = HeadlineArticleAdapter()
        binding.articleList.adapter = adapter

        adapter.addLoadStateListener { loadState ->
            binding.articleList.isVisible = loadState.refresh is LoadState.NotLoading
            binding.progress.isVisible = loadState.refresh is LoadState.Loading
            manageErrors(loadState)
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

}