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
import com.fandy.news.util.formatDate
import com.fandy.news.util.formatTitle
import com.fandy.news.util.loadImageOrDefault
import com.fandy.news.util.loadOrGone

class HeadlineArticleAdapter :
    PagingDataAdapter<Article, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == 0) {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ArticleItemMainBinding.inflate(layoutInflater, parent, false)
            return HeadlineArticleItemMainViewHolder(binding)
        } else {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ArticleItemOtherBinding.inflate(layoutInflater, parent, false)
            return HeadlineArticleItemOtherViewHolder(binding)
        }
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val article = getItem(position) ?: return
        if (getItemViewType(position) == 0) {
            (viewHolder as HeadlineArticleItemMainViewHolder).bind(article)
        } else {
            (viewHolder as HeadlineArticleItemOtherViewHolder).bind(article)
        }
    }


    override fun getItemViewType(position: Int): Int {
        return 1
    }


    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<Article> =
            object : DiffUtil.ItemCallback<Article>() {
                override fun areItemsTheSame(oldItem: Article, newItem: Article) =
                    oldItem == newItem

                override fun areContentsTheSame(oldItem: Article, newItem: Article) =
                    oldItem.id == newItem.id
            }
    }

}

class HeadlineArticleItemMainViewHolder(private val binding: ArticleItemMainBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(articleItem: Article) {
        binding.run {
            title.loadOrGone(articleItem.title.formatTitle())
            source.loadOrGone(articleItem.source.name)
            publishedAt.loadOrGone(articleItem.date.formatDate())
            articleImage.loadImageOrDefault(articleItem.imgUrl)

            setOnClickListener(articleItem)
        }
    }

    private fun setOnClickListener(articleItem: Article) {
        binding.detailItem.setOnClickListener { view ->
            navigateToDetail(articleItem, view)
        }
    }

    private fun navigateToDetail(articleItem: Article, view: View) {
        val directions =
            HeadlineFragmentDirections.actionHeadlineFragmentToArticleFragment(articleItem)
        view.findNavController().navigate(directions)
    }

}


class HeadlineArticleItemOtherViewHolder(private val binding: ArticleItemOtherBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(articleItem: Article) {
        binding.run {
            title.loadOrGone(articleItem.title.formatTitle())
            source.loadOrGone(articleItem.source.name)
            publishedAt.loadOrGone(articleItem.date.formatDate())
            articleImage.loadImageOrDefault(articleItem.imgUrl)

            setOnClickListener(articleItem)
        }
    }

    private fun setOnClickListener(articleItem: Article) {
        binding.detailItem.setOnClickListener { view ->
            navigateToDetail(articleItem, view)
        }
    }

    private fun navigateToDetail(articleItem: Article, view: View) {
        val directions =
            HeadlineFragmentDirections.actionHeadlineFragmentToArticleFragment(articleItem)
        view.findNavController().navigate(directions)
    }

}



