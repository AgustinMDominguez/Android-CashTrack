package org.amdoige.cashtrack.core

import android.app.Application
import timber.log.Timber

@Suppress("unused")
class CashTrackApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}
