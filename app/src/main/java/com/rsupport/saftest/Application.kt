package com.rsupport.saftest

import android.app.Application

class Application: Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
    }
    companion object {
        lateinit var instance: Application
    }
}