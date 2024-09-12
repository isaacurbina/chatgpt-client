package com.iucoding.chatgptclient

import android.app.Application
import timber.log.Timber

class ChatGPTApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}
