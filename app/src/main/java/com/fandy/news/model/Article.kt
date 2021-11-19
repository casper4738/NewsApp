package com.fandy.news.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Article (
    val id: String,
    val url: String,
    val author: String,
    val title: String,
    val description: String,
    val imgUrl: String,
    val date: String,
    val content: String,
    val source: Source,
    var category: String = "",
    var language: String = "",
    var keyword: String = "",
    var from_date: String = "",
    var to_date: String = ""
): Parcelable