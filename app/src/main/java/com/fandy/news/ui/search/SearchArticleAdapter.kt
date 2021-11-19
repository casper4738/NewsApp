package com.fandy.news.ui.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.fandy.news.databinding.ArticleItemSearchBinding
import com.fandy.news.model.Article
import com.fandy.news.model.ArticleSearch
import com.fandy.news.util.formatDate
import com.fandy.news.util.formatTitle
import com.fandy.news.util.loadImageOrDefault
import com.fandy.news.util.loadOrGone

class SearchArticleAdapter :
    PagingDataAdapter<ArticleSearch, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ArticleItemSearchBinding.inflate(layoutInflater, parent, false)
        return SearchArticleItemSearchViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val article = getItem(position) ?: return
        (viewHolder as SearchArticleItemSearchViewHolder).bind(article)
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<ArticleSearch> =
            object : DiffUtil.ItemCallback<ArticleSearch>() {
                override fun areItemsTheSame(oldItem: ArticleSearch, newItem: ArticleSearch) =
                    oldItem == newItem

                override fun areContentsTheSame(oldItem: ArticleSearch, newItem: ArticleSearch) =
                    oldItem.id == newItem.id
            }
    }

}


class SearchArticleItemSearchViewHolder(private val binding: ArticleItemSearchBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(articleItem: ArticleSearch) {
        binding.run {
            title.loadOrGone(articleItem.title.formatTitle())
            source.loadOrGone(articleItem.source.name)
            publishedAt.loadOrGone(articleItem.date.formatDate())
            articleImage.loadImageOrDefault(articleItem.imgUrl)

            setOnClickListener(articleItem)
        }
    }

    private fun setOnClickListener(articleItem: ArticleSearch) {
        binding.detailItem.setOnClickListener { view ->
            navigateToDetail(articleItem, view)
        }
    }

    private fun navigateToDetail(articleItem: ArticleSearch, view: View) {
        val directions =
            SearchFragmentDirections.actionSearchFragmentToArticleFragment(Article(
                id = articleItem.id,
                url = articleItem.url,
                author = articleItem.author,
                title = articleItem.title,
                description = articleItem.description,
                imgUrl = articleItem.imgUrl,
                date = articleItem.date,
                content = articleItem.content,
                source = articleItem.source,
                category = articleItem.category,
                language = articleItem.language,
                keyword = articleItem.keyword,
                from_date = articleItem.from_date,
                to_date = articleItem.to_date
            ))
        view.findNavController().navigate(directions)
    }

}



