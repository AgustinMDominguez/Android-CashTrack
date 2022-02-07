package org.amdoige.cashtrack.core

import android.content.Context
import org.amdoige.cashtrack.R

class SharedPreferencesManager {
    private val context = CashTrackApplication.applicationContext
    private val defaultWalletIdKey = context.getString(R.string.pref_default_wallet_id_key)
    private val preferences = CashTrackApplication.applicationContext.getSharedPreferences(
        context.getString(R.string.application_shared_preferences_file_key),
        Context.MODE_PRIVATE
    )

    fun getDefaultWalletId(): Long? {
        val defaultWalletId = preferences.getLong(
            context.getString(R.string.pref_default_wallet_id_key),
            -1L
        )
        return if (defaultWalletId == -1L) null else defaultWalletId
    }

    fun setDefaultWalletId(walletId: Long) = with(preferences.edit()) {
        putLong(defaultWalletIdKey, walletId)
        apply()
    }
}