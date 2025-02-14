package com.modar.task

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ModarTaskApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        instance = this
    }

    companion object {
        lateinit var instance: ModarTaskApplication
            private set
    }

}