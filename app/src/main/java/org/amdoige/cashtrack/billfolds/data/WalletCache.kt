package org.amdoige.cashtrack.billfolds.data

import org.amdoige.cashtrack.core.database.Wallet

class WalletCache() {
    private var walletsListCache: MutableList<Wallet>? = null
    private var walletsMapCache: MutableMap<Long, Wallet> = mutableMapOf()
    private var updated: Boolean = false

    private fun invalidate() {
        updated = false
        walletsListCache = mutableListOf()
        walletsMapCache = mutableMapOf()
    }

    fun cache(wallets: List<Wallet>) {
        invalidate()
        walletsListCache = wallets.toMutableList()
        wallets.forEach { walletsMapCache[it.id] = it }
    }

    fun update(wallet: Wallet) {
        walletsMapCache[wallet.id] = wallet
        val walletList = walletsListCache
        walletList?.let {
            val replaceIndex = walletList.indexOfFirst { it.id == wallet.id }
            try {
                walletList[replaceIndex] = wallet
                walletsListCache = walletList
            } catch (e: IndexOutOfBoundsException) {
            }
        }
    }

    fun get(walletId: Long): Wallet? = walletsMapCache[walletId]

    fun getAll(): List<Wallet>? = walletsListCache
}