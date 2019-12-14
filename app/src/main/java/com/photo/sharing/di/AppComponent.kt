package com.photo.sharing.di

import com.photo.sharing.viewmodel.FriendsListViewModel
import com.photo.sharing.viewmodel.SplashViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    fun inject(viewModel: SplashViewModel)
    fun inject(viewModel: FriendsListViewModel)
}