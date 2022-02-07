package org.amdoige.cashtrack.billfolds.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.amdoige.cashtrack.core.SharedPreferencesManager
import org.amdoige.cashtrack.core.database.CashTrackDatabase
import org.amdoige.cashtrack.core.database.Wallet
import timber.log.Timber
import java.util.*
import kotlin.math.min

class WalletsRepository(private val cashTrackDatabase: CashTrackDatabase) {
    // TODO: Implement Wallet Cache
    suspend fun getAllWallets(): List<Wallet> = withContext(Dispatchers.IO) {
        cashTrackDatabase.dao.getWallets()
    }

    suspend fun getWalletBalance(wallet: Wallet, currentFundBalance: Double? = null): Double {
        var startMilli: Long
        var endMilli: Long
        withContext(Dispatchers.Default) {
            val nowMilli = System.currentTimeMillis()
            val calendar = Calendar.getInstance()
            calendar.time = Date(nowMilli)
            when (wallet.limitPeriod) {
                'w' -> {
                    calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
                }
                'm' -> {
                    calendar.set(Calendar.DAY_OF_MONTH, 1)
                }
                else -> {
                }
            }
            calendar.apply {
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }
            Timber.i("getWalletBalance between $calendar and now")
            startMilli = calendar.timeInMillis
            endMilli = nowMilli + 5000L
        }
        val movementSum = withContext(Dispatchers.IO) {
            cashTrackDatabase.dao.getWalletMovementSumFromRange(wallet.id, startMilli, endMilli)
        }
        val fundBalance = currentFundBalance ?: withContext(Dispatchers.IO) {
            cashTrackDatabase.dao.getBalance()
        }
        return min(movementSum, fundBalance)
    }

    suspend fun getDefaultWallet(): Wallet {
        val preferencesManager = SharedPreferencesManager()
        val defaultWalletId =
            withContext(Dispatchers.IO) { preferencesManager.getDefaultWalletId() }

        return when (defaultWalletId) {
            null -> makeAWalletDefault(preferencesManager)
            else -> withContext(Dispatchers.IO) {
                cashTrackDatabase.dao.getWallet(defaultWalletId) ?: makeAWalletDefault(
                    preferencesManager
                )
            }
        }
    }

    suspend fun setDefaultWallet(wallet: Wallet) {
        val preferencesManager = SharedPreferencesManager()
        withContext(Dispatchers.IO) { preferencesManager.setDefaultWalletId(wallet.id) }
    }

    private suspend fun makeAWalletDefault(
        preferencesManager: SharedPreferencesManager = SharedPreferencesManager()
    ): Wallet {
        val defaultWallet = withContext(Dispatchers.IO) {
            when (val aWallet = cashTrackDatabase.dao.getAWallet()) {
                null -> Wallet()
                else -> aWallet
            }
        }
        return withContext(Dispatchers.IO) {
            cashTrackDatabase.dao.insert(defaultWallet)
            preferencesManager.setDefaultWalletId(defaultWallet.id)
            defaultWallet
        }
    }
}
