package org.amdoige.cashtrack.core

import android.app.Application
import android.content.Context
import timber.log.Timber

@Suppress("unused")
class CashTrackApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        CashTrackApplication.applicationContext = applicationContext
        Timber.plant(Timber.DebugTree())
    }

    companion object {
        lateinit var applicationContext: Context
    }
}
