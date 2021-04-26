package com.demo.base

import android.app.Application
import android.content.Context

class BaseApplication : Application() {

    companion object {
        lateinit var INSTANCE: Application
    }


    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
    }
}