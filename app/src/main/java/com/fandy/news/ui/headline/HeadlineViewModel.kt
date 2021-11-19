package com.fandy.news.ui.headline

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.fandy.news.model.ArticleTopHeadlines
import com.fandy.news.repository.NewsRepository
import com.fandy.news.util.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ExperimentalPagingApi
@HiltViewModel
class HeadlineViewModel @Inject constructor(
    private val repository: NewsRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private var currentNews: Flow<PagingData<ArticleTopHeadlines>>? = null
    private val _categoryLocalizedLiveData: MutableLiveData<Int> =
        MutableLiveData(getLastSavedLocalizedCategory())

    fun loadTopArticles(
        category: String,
        language: String = ""
    ): Flow<PagingData<ArticleTopHeadlines>> {
        val lastResult = currentNews
        if (lastResult != null && !shouldRefresh(language, category))
            return lastResult

        val newNews =
            repository.fetchTopHeadlinesArticles(language, category).cachedIn(viewModelScope)
        currentNews = newNews

        //Save new filters after checks are made to establish, if the news should be refreshed.
        saveCategoryFiltering(category)
        saveLanguageFiltering(language)
        saveCategoryLocalized()

        return newNews
    }

    private fun shouldRefresh(language: String, category: String): Boolean {
        return category != getLastSavedCategory()
                || language != getLastSavedLanguage()
    }

    fun getLastSavedCategory() = savedStateHandle
        .getLiveData<String>(
            SAVED_STATE_CATEGORY
        ).value ?: DEFAULT_STATE_CATEGORY

    fun getLastSavedLanguage() = savedStateHandle
        .getLiveData<String>(
            SAVED_STATE_LANGUAGE
        ).value ?: DEFAULT_LANGUAGE

    private fun getLastSavedLocalizedCategory() = savedStateHandle
        .getLiveData<Int>(
            SAVED_STATE_LOCAL_TITLE
        ).value ?: DEFAULT_TITLE


    fun saveCategoryFiltering(category: String) {
        savedStateHandle.set(SAVED_STATE_CATEGORY, category)
    }

    fun saveLanguageFiltering(language: String) {
        savedStateHandle.set(SAVED_STATE_LANGUAGE, language)
    }

    private fun saveCategoryLocalized() {
        _categoryLocalizedLiveData.value?.let {
            savedStateHandle.set(SAVED_STATE_LOCAL_TITLE, it)
        }
    }
}