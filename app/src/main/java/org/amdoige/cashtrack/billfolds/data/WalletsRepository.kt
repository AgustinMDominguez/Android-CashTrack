package org.amdoige.cashtrack.billfolds.data

import kotlinx.coroutines.*
import org.amdoige.cashtrack.core.SharedPreferencesManager
import org.amdoige.cashtrack.core.database.CashTrackDatabase
import org.amdoige.cashtrack.core.database.Movement
import org.amdoige.cashtrack.core.database.Wallet
import timber.log.Timber
import java.util.*
import kotlin.math.min

class WalletsRepository(private val cashTrackDatabase: CashTrackDatabase) {
    private var walletsListCache: List<Wallet>? = null
    private var walletsMapCache: MutableMap<Long, Wallet> = mutableMapOf()

    suspend fun getAllWallets(): List<Wallet> = withContext(Dispatchers.IO) {
        var myWallets = walletsListCache
        if (myWallets.isNullOrEmpty()) {
            myWallets = cashTrackDatabase.dao.getWallets()
            walletsListCache = myWallets
            myWallets.forEach { walletsMapCache[it.id] = it }
        }
        myWallets
    }

    fun invalidateWalletCache() {
        walletsListCache = null
        walletsMapCache = mutableMapOf()
    }

    suspend fun getAllWalletsWithBalance(): List<Wallet> = withContext(Dispatchers.IO) {
        var myWallets = walletsListCache
        if (myWallets.isNullOrEmpty() || myWallets[0].balance == null) {
            myWallets = getAllWallets()
            val asyncJobs = mutableListOf<Deferred<Unit>>()
            myWallets.forEach {
                asyncJobs.add(async {
                    it.balance = getWalletBalance(it)
                    walletsMapCache[it.id] = it
                })
            }
            asyncJobs.awaitAll()
            walletsListCache = myWallets
        }
        myWallets
    }

    suspend fun getWalletById(walletId: Long): Wallet? {
        val cachedWallet = walletsMapCache[walletId]
        return if (cachedWallet == null) {
            val fetchedWallet = withContext(Dispatchers.IO) {
                cashTrackDatabase.dao.getWallet(walletId)
            }
            if (fetchedWallet != null) {
                walletsMapCache[walletId] = fetchedWallet
            }
            fetchedWallet
        } else {
            Timber.i("Wallet cached!")
            cachedWallet
        }
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
