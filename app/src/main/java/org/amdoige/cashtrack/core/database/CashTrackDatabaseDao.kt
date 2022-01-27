package org.amdoige.cashtrack.core.database

import androidx.room.*
import org.amdoige.cashtrack.core.database.daos.MovementDao
import org.amdoige.cashtrack.core.database.daos.WalletDao

@Dao
interface CashTrackDatabaseDao : WalletDao, MovementDao