package com.fandy.news.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
class RemoteKey(
    @PrimaryKey
    val articleId: String,
    val typeArticle: String,
    val nextKey: Int?,
    val prevKey: Int?
)