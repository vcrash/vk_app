package com.test.vkapp

import android.app.Application
import com.test.vkapp.di.appModules
import com.vk.sdk.VKSdk
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class VKApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(applicationContext)
            modules(appModules)
        }
        VKSdk.initialize(this)
    }

}