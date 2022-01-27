package org.amdoige.cashtrack.core.database.daos

import androidx.room.*
import org.amdoige.cashtrack.core.database.Wallet

@Dao
interface WalletDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insert(wallet: Wallet)

    @Update
    fun update(wallet: Wallet)

    @Query("SELECT * FROM wallet_table")
    fun getWallets(): List<Wallet>

    @Query("SELECT * FROM wallet_table WHERE id = :key")
    fun getWallet(key: Long): Wallet?

    @Delete
    fun delete(wallet: Wallet)
}