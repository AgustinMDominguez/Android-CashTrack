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
    private var walletsListCache: MutableList<Wallet>? = null
    private var walletsMapCache: MutableMap<Long, Wallet> = mutableMapOf()

    suspend fun getAllWallets(): List<Wallet> = withContext(Dispatchers.IO) {
        var myWallets = walletsListCache
        if (myWallets.isNullOrEmpty()) {
            myWallets = cashTrackDatabase.dao.getWallets().toMutableList()
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
        var myWallets = walletsListCache?.toList()
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
            walletsListCache = myWallets.toMutableList()
        }
        myWallets
    }

    suspend fun getWalletById(walletId: Long, forceBalanceFetched: Boolean = false): Wallet? {
        val cachedWallet = walletsMapCache[walletId]
        val returnWallet = if (cachedWallet == null) {
            val fetchedWallet = withContext(Dispatchers.IO) {
                cashTrackDatabase.dao.getWallet(walletId)
            }
            if (fetchedWallet != null) {
                walletsMapCache[walletId] = fetchedWallet
            }
            fetchedWallet
        } else {
            cachedWallet
        }
        return if (returnWallet != null && forceBalanceFetched && returnWallet.balance == null) {
            returnWallet.balance = getWalletBalance(returnWallet)
            walletsMapCache[returnWallet.id] = returnWallet
            returnWallet
        } else {
            returnWallet
        }
    }

    suspend fun updateWalletFromDatabase(walletId: Long) = withContext(Dispatchers.IO) {
        cashTrackDatabase.dao.getWallet(walletId)?.let { fetchedWallet ->
            fetchedWallet.balance = getWalletBalance(fetchedWallet)
            walletsMapCache[walletId] = fetchedWallet
            walletsListCache?.let { listCache ->
                val replaceIndex = listCache.indexOfFirst { it.id == walletId }
                if (replaceIndex >= 0) {
                    listCache[replaceIndex] = fetchedWallet
                } else {
                    Timber.e(
                        "updateWalletFromDatabase tried to update repository cache " +
                                "with index out of bounds"
                    )
                }
            }
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
