package org.amdoige.cashtrack.billfolds.data

import kotlinx.coroutines.*
import org.amdoige.cashtrack.core.classes.SharedPreferencesManager
import org.amdoige.cashtrack.core.database.CashTrackDatabase
import org.amdoige.cashtrack.core.database.Movement
import org.amdoige.cashtrack.core.database.Wallet
import timber.log.Timber
import java.util.*
import kotlin.math.min

class WalletsRepository(private val cashTrackDatabase: CashTrackDatabase) {
    private val walletCache = WalletCache()

    private suspend fun getAllWallets(): List<Wallet> = withContext(Dispatchers.IO) {
        var cacheWallets: List<Wallet>? = walletCache.getAll()
        if (cacheWallets == null) {
            cacheWallets = cashTrackDatabase.dao.getWallets()
            walletCache.cache(cacheWallets)
        }
        cacheWallets
    }

    suspend fun getAllWalletsWithBalance(): List<Wallet> = withContext(Dispatchers.IO) {
        var myWallets = getAllWallets()
        if (myWallets.isEmpty()) {
            makeAWalletDefault()
            myWallets = getAllWallets()
        }
        if (myWallets[0].balance == null) {
            myWallets = addBalanceToWallets(myWallets)
            walletCache.cache(myWallets)
        }
        myWallets
    }

    private suspend fun addBalanceToWallets(wallets: List<Wallet>): List<Wallet> {
        return withContext(Dispatchers.IO) {
            val asyncJobs = mutableListOf<Deferred<Unit>>()
            wallets.forEach { asyncJobs.add(async { it.balance = getWalletBalance(it) }) }
            asyncJobs.awaitAll()
            wallets
        }
    }

    suspend fun getWalletById(walletId: Long, forceBalanceFetched: Boolean = false): Wallet? {
        val wallet = walletCache.get(walletId) ?: withContext(Dispatchers.IO) {
            cashTrackDatabase.dao.getWallet(walletId)
        }
        if (wallet != null && forceBalanceFetched && wallet.balance == null) {
            wallet.balance = withContext(Dispatchers.IO) { getWalletBalance(wallet) }
        }
        return wallet
    }

    suspend fun notifyNewMovementInWallet(walletId: Long) = withContext(Dispatchers.IO) {
        cashTrackDatabase.dao.getWallet(walletId)?.let { walletCache.update(it) }
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
            Timber.i("getWalletBalance between ${calendar.toInstant()} and now")
            startMilli = calendar.timeInMillis
            endMilli = nowMilli + 10000L
        }
        val movementSum = withContext(Dispatchers.IO) {
            cashTrackDatabase.dao.getWalletMovementSumFromRange(wallet.id, startMilli, endMilli)
        }
        val fundBalance = currentFundBalance ?: withContext(Dispatchers.IO) {
            cashTrackDatabase.dao.getBalance()
        }
        return min(movementSum, fundBalance)
    }

    suspend fun addWalletInfoToMovements(movements: List<Movement>): List<Movement> {
        return withContext(Dispatchers.IO) {
            val asyncJobs = mutableListOf<Deferred<Unit>>()
            movements.forEach { movement ->
                val deferred: Deferred<Unit> = async {
                    getWalletById(movement.walletId)?.let { wallet ->
                        movement.color = wallet.color
                        movement.logo = wallet.logo
                    }
                }
                asyncJobs.add(deferred)
            }
            asyncJobs.awaitAll()
            movements
        }
    }

    suspend fun getDefaultWallet(): Wallet {
        val preferencesManager = SharedPreferencesManager()
        val defaultWalletId =
            withContext(Dispatchers.IO) { preferencesManager.getDefaultWalletId() }
        Timber.i("Default Wallet Id: $defaultWalletId")

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
    ): Wallet = withContext(Dispatchers.IO) {
        val defaultWallet = when (val aWallet = cashTrackDatabase.dao.getAWallet()) {
            null -> {
                val newWallet = Wallet()
                cashTrackDatabase.dao.insert(newWallet)
                newWallet
            }
            else -> aWallet
        }
        preferencesManager.setDefaultWalletId(defaultWallet.id)
        defaultWallet
    }
}
