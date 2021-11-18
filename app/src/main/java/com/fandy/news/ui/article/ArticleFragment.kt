package com.fandy.news.ui.article

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.VisibleForTesting
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ShareCompat
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.fandy.news.R
import com.fandy.news.databinding.ArticleFragmentBinding
import com.fandy.news.model.Article
import com.fandy.news.util.*
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ArticleFragment : Fragment() {
    @VisibleForTesting
    private val articleViewModel: ArticleViewModel by hiltNavGraphViewModels(R.id.navgraph)
    private val args: ArticleFragmentArgs by navArgs()
    private val article: Article by lazy { args.selectedArticle }
    private var _binding: ArticleFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ArticleFragmentBinding.inflate(inflater, container, false)

        setupOpenWebsiteButton()

        articleViewModel.fetchArticle(article.id)

        articleViewModel.articleLiveData.observe(viewLifecycleOwner) { article ->
            displayArticle(article)
        }

        articleViewModel.shareArticleEvent.observe(viewLifecycleOwner, EventObserver { articleUrl ->
            shareArticle(articleUrl)
        })

        articleViewModel.openWebsiteEvent.observe(viewLifecycleOwner, EventObserver { articleUrl ->
            openWebsite(articleUrl)
        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        (activity as AppCompatActivity?)!!.setSupportActionBar(binding.toolbar)
        setHasOptionsMenu(true)

        binding.toolbar?.setNavigationOnClickListener {
            view.findNavController().navigate(R.id.homeFragment)
        }
    }

    private fun setupOpenWebsiteButton() {
        binding.article.continueReadingButton.setOnClickListener {
            articleViewModel.openWebsite(article.url)
        }
    }

    private fun displayArticle(selectedArticle: Article) {
        binding.apply {
            article.author.loadOrGone(selectedArticle.author)
            article.title.loadOrGone(selectedArticle.title.formatTitle())
            article.content.loadOrGone(selectedArticle.content.formatContent())
            article.description.loadOrGone(selectedArticle.description)
            article.date.loadOrGone(selectedArticle.date.formatDateRemoveTime())
            article.source.loadOrGone(selectedArticle.source.name)
            Glide.with(requireContext()).load(selectedArticle.imgUrl).into(articleImage)
        }
    }

    private fun shareArticle(articleUrl: String) {
        val mimeType = "text/plain"
        ShareCompat.IntentBuilder
            .from(requireActivity())
            .setType(mimeType)
            .setChooserTitle(resources.getString(R.string.share_article))
            .setText(articleUrl)
            .startChooser()
    }

    private fun openWebsite(articleUrl: String) {
        val webPage: Uri = Uri.parse(articleUrl)
        val intent = Intent(Intent.ACTION_VIEW, webPage)
        if (intent.resolveActivity(requireContext().packageManager) != null)
            startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}