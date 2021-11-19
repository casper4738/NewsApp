package com.fandy.news.ui.headline

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
import com.fandy.news.model.ArticleTopHeadlines
import com.fandy.news.util.formatDate
import com.fandy.news.util.formatTitle
import com.fandy.news.util.loadImageOrDefault
import com.fandy.news.util.loadOrGone

class HeadlineArticleAdapter :
    PagingDataAdapter<ArticleTopHeadlines, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ArticleItemOtherBinding.inflate(layoutInflater, parent, false)
        return HeadlineArticleItemOtherViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val article = getItem(position) ?: return
        (viewHolder as HeadlineArticleItemOtherViewHolder).bind(article)
    }


    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<ArticleTopHeadlines> =
            object : DiffUtil.ItemCallback<ArticleTopHeadlines>() {
                override fun areItemsTheSame(
                    oldItem: ArticleTopHeadlines,
                    newItem: ArticleTopHeadlines
                ) =
                    oldItem == newItem

                override fun areContentsTheSame(
                    oldItem: ArticleTopHeadlines,
                    newItem: ArticleTopHeadlines
                ) =
                    oldItem.id == newItem.id
            }
    }

}

class HeadlineArticleItemOtherViewHolder(private val binding: ArticleItemOtherBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(articleItem: ArticleTopHeadlines) {
        binding.run {
            title.loadOrGone(articleItem.title.formatTitle())
            source.loadOrGone(articleItem.source.name)
            publishedAt.loadOrGone(articleItem.date.formatDate())
            articleImage.loadImageOrDefault(articleItem.imgUrl)

            setOnClickListener(articleItem)
        }
    }

    private fun setOnClickListener(articleItem: ArticleTopHeadlines) {
        binding.detailItem.setOnClickListener { view ->
            navigateToDetail(articleItem, view)
        }
    }

    private fun navigateToDetail(articleItem: ArticleTopHeadlines, view: View) {
        val directions =
            HeadlineFragmentDirections.actionHeadlineFragmentToArticleFragment(
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



