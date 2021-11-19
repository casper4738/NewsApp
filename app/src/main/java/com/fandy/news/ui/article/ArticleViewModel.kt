package com.fandy.news.ui.article

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fandy.news.repository.NewsRepository
import com.fandy.news.util.SingleEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ArticleViewModel @Inject constructor(
    private val repository: NewsRepository
) : ViewModel() {

    private val _shareArticleEvent = MutableLiveData<SingleEvent<String>>()
    private val _openWebsiteEvent = MutableLiveData<SingleEvent<String>>()

    val shareArticleEvent: LiveData<SingleEvent<String>>
        get() = _shareArticleEvent

    val openWebsiteEvent: LiveData<SingleEvent<String>>
        get() = _openWebsiteEvent

    fun shareArticle(articleUrl: String) {
        _shareArticleEvent.value = SingleEvent(articleUrl)
    }

    fun openWebsite(articleUrl: String) {
        _openWebsiteEvent.value = SingleEvent(articleUrl)
    }
}