package com.fandy.news.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.fandy.news.databinding.ArticleItemMainBinding
import com.fandy.news.databinding.ArticleItemOtherBinding
import com.fandy.news.model.Article
import com.fandy.news.model.ArticleHome
import com.fandy.news.util.formatDate
import com.fandy.news.util.formatTitle
import com.fandy.news.util.loadImageOrDefault
import com.fandy.news.util.loadOrGone

class HomeArticleAdapter :
    PagingDataAdapter<ArticleHome, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == 0) {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ArticleItemMainBinding.inflate(layoutInflater, parent, false)
            return ArticleItemMainViewHolder(binding)
        } else {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ArticleItemOtherBinding.inflate(layoutInflater, parent, false)
            return ArticleItemOtherViewHolder(binding)
        }
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val article = getItem(position) ?: return
        if (getItemViewType(position) == 0) {
            (viewHolder as ArticleItemMainViewHolder).bind(article)
        } else {
            (viewHolder as ArticleItemOtherViewHolder).bind(article)
        }
    }


    override fun getItemViewType(position: Int): Int {
        if (position == 0) {
            return 0
        } else {
            return 1
        }
    }


    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<ArticleHome> =
            object : DiffUtil.ItemCallback<ArticleHome>() {
                override fun areItemsTheSame(oldItem: ArticleHome, newItem: ArticleHome) =
                    oldItem == newItem

                override fun areContentsTheSame(oldItem: ArticleHome, newItem: ArticleHome) =
                    oldItem.id == newItem.id
            }
    }

}

class ArticleItemMainViewHolder(private val binding: ArticleItemMainBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(articleItem: ArticleHome) {
        binding.run {
            title.loadOrGone(articleItem.title.formatTitle())
            source.loadOrGone(articleItem.source.name)
            publishedAt.loadOrGone(articleItem.date.formatDate())
            articleImage.loadImageOrDefault(articleItem.imgUrl)

            setOnClickListener(articleItem)
        }
    }

    private fun setOnClickListener(articleItem: ArticleHome) {
        binding.detailItem.setOnClickListener { view ->
            navigateToDetail(articleItem, view)
        }
    }

    private fun navigateToDetail(articleItem: ArticleHome, view: View) {

        val directions =
            HomeFragmentDirections.actionArticleListFragmentToArticleFragment(
                Article(
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
                )

            )
        view.findNavController().navigate(directions)
    }

}


class ArticleItemOtherViewHolder(private val binding: ArticleItemOtherBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(articleItem: ArticleHome) {
        binding.run {
            title.loadOrGone(articleItem.title.formatTitle())
            source.loadOrGone(articleItem.source.name)
            publishedAt.loadOrGone(articleItem.date.formatDate())
            articleImage.loadImageOrDefault(articleItem.imgUrl)

            setOnClickListener(articleItem)
        }
    }

    private fun setOnClickListener(articleItem: ArticleHome) {
        binding.detailItem.setOnClickListener { view ->
            navigateToDetail(articleItem, view)
        }
    }

    private fun navigateToDetail(articleItem: ArticleHome, view: View) {
        val directions =
            HomeFragmentDirections.actionArticleListFragmentToArticleFragment(Article(
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



