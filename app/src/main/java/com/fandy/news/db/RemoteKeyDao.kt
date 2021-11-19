package com.fandy.news.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RemoteKeyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(keys: List<RemoteKey>)

    @Query("SELECT * FROM remote_keys WHERE articleId = :articleId AND typeArticle= :typeArticle")
    suspend fun remoteKeyByArticle(articleId: String, typeArticle: String): RemoteKey

    @Query("DELETE FROM remote_keys WHERE typeArticle= :typeArticle")
    suspend fun clearRemoteKeys(typeArticle: String)


}