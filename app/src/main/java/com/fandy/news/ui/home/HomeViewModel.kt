package com.fandy.news.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.fandy.news.model.ArticleHome
import com.fandy.news.repository.NewsRepository
import com.fandy.news.util.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * ViewModel for the [HomeFragment] screen.
 * The ViewModel works with the [NewsRepository]
 * to get the list of articles.
 */
@ExperimentalPagingApi
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: NewsRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private var currentNews: Flow<PagingData<ArticleHome>>? = null
    private val _categoryLocalizedLiveData: MutableLiveData<Int> =
        MutableLiveData(getLastSavedLocalizedCategory())


    fun loadAllArticles(keyword: String, language: String): Flow<PagingData<ArticleHome>> {

        val lastResult = currentNews
        if (lastResult != null && !shouldRefresh(keyword, ""))
            return lastResult

        val newNews =
            repository.fetchEverythingArticles(keyword, language).cachedIn(viewModelScope)
        currentNews = newNews

        //Save new filters after checks are made to establish, if the news should be refreshed.
        saveLanguageFiltering(keyword)
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


    fun saveLanguageFiltering(language: String) {
        savedStateHandle.set(SAVED_STATE_LANGUAGE, language)
    }

    private fun saveCategoryLocalized() {
        _categoryLocalizedLiveData.value?.let {
            savedStateHandle.set(SAVED_STATE_LOCAL_TITLE, it)
        }
    }
}