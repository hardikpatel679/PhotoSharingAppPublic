package com.photo.sharing.di

import android.app.Application
import com.photo.sharing.repository.FriendsListRepository
import com.photo.sharing.repository.SplashRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val application: Application) {

    @Provides
    @Singleton
    fun provideSplashRepo(): SplashRepository {
        return SplashRepository(application = application)
    }

    @Provides
    @Singleton
    fun provideFriendsRepo(): FriendsListRepository {
        return FriendsListRepository(application = application)
    }
}