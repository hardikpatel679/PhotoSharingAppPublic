package com.photo.sharing.base

import android.app.Application
import android.content.ContextWrapper
import com.photo.sharing.di.AppComponent
import com.photo.sharing.di.AppModule
import com.photo.sharing.di.DaggerAppComponent
import com.pixplicity.easyprefs.library.Prefs

class PhotoSharingApplication : Application(), ComponentProvider {

    override val component: AppComponent by lazy {
        DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()
    }

    override fun onCreate() {
        super.onCreate()
        Prefs.Builder()
            .setContext(this)
            .setMode(ContextWrapper.MODE_PRIVATE)
            .setPrefsName(packageName)
            .setUseDefaultSharedPreference(true)
            .build()
    }
}

interface ComponentProvider {
    val component: AppComponent
}