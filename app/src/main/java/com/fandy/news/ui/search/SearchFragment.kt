package com.fandy.news.ui.search

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.paging.CombinedLoadStates
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import com.fandy.news.R
import com.fandy.news.databinding.SearchFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
@ExperimentalPagingApi
class SearchFragment : Fragment() {
    private val viewModel: SearchViewModel by hiltNavGraphViewModels(R.id.navgraph)
    private var _binding: SearchFragmentBinding? = null
    private val binding get() = _binding!!
    private var job: Job? = null
    private lateinit var adapter: SearchArticleAdapter
    private val args: SearchFragmentArgs by navArgs()
    private val keywordSearch: String by lazy { args.keywordSearch }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SearchFragmentBinding.inflate(inflater, container, false)

        initAdapter()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (activity as AppCompatActivity?)!!.setSupportActionBar(binding.homeToolbar)
        setHasOptionsMenu(true)

        getAllNews(keywordSearch)
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.top_app_bar, menu)

        val searchItem = menu.findItem(R.id.news_search)
        val searchView = searchItem?.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null && query.isNotBlank()) {
                    getAllNews(query)
                }
                return true
            }

            override fun onQueryTextChange(query: String?): Boolean {
                return true
            }
        })

    }

    private fun initAdapter() {
        adapter = SearchArticleAdapter()
        binding.articleList.adapter = adapter

        adapter.addLoadStateListener { loadState ->
            binding.articleList.isVisible = loadState.refresh is LoadState.NotLoading
            binding.progress.isVisible = loadState.refresh is LoadState.Loading
            manageErrors(loadState)
        }
    }

    private fun getAllNews(querySearch: String) {
        job?.cancel()
        job = lifecycleScope.launch {
            viewModel.search(querySearch, "en")
                .collectLatest { adapter.submitData(it) }
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