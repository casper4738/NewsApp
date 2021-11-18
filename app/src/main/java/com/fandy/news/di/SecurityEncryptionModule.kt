package com.fandy.news.di

import com.fandy.news.security.SecurityEncryption
import com.fandy.news.security.SecurityEncryptionImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SecurityEncryptionModule {

    @Provides
    @Singleton
    fun provideEncryptionAES(): SecurityEncryption {
        return SecurityEncryptionImpl()
    }

}