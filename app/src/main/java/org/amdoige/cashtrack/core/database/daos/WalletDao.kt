package org.amdoige.cashtrack.core.database.daos

import androidx.room.Dao
import androidx.room.Query
import org.amdoige.cashtrack.core.database.Wallet

@Dao
interface WalletDao {
    @Query("SELECT * FROM wallet_table")
    fun getWallets(): List<Wallet>
}