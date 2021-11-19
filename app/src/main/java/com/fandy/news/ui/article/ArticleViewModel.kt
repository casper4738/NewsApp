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

    //    private val _articleId = MutableLiveData<String>()
    private val _shareArticleEvent = MutableLiveData<SingleEvent<String>>()
    private val _openWebsiteEvent = MutableLiveData<SingleEvent<String>>()

//    val articleLiveData: LiveData<Article> = _articleId.switchMap { id->
//        repository.getArticle(id).asLiveData()
//    }

    val shareArticleEvent: LiveData<SingleEvent<String>>
        get() = _shareArticleEvent

    val openWebsiteEvent: LiveData<SingleEvent<String>>
        get() = _openWebsiteEvent


//    fun fetchArticle(id: String) {
//        _articleId.value = id
//    }

    fun shareArticle(articleUrl: String) {
        _shareArticleEvent.value = SingleEvent(articleUrl)
    }

    fun openWebsite(articleUrl: String) {
        _openWebsiteEvent.value = SingleEvent(articleUrl)
    }
}