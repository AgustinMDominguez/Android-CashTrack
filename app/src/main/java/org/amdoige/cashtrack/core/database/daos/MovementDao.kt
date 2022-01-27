package org.amdoige.cashtrack.core.database.daos

import androidx.room.*
import org.amdoige.cashtrack.core.database.Movement

@Dao
interface MovementDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insert(movement: Movement)

    @Update
    fun update(movement: Movement)

    @Query("SELECT * FROM movements_table WHERE id = :key")
    fun getMovement(key: Long): Movement?

    @Delete
    fun delete(movement: Movement)

    @Query("DELETE FROM movements_table")
    fun clear()

    @Query("SELECT COUNT(id) FROM movements_table")
    fun databaseSize(): Int

    @Query("SELECT * FROM movements_table WHERE milliseconds BETWEEN :startMilli AND :endMilli ORDER BY milliseconds DESC")
    fun range(startMilli: Long, endMilli: Long): List<Movement>

    @Query("SELECT * FROM movements_table WHERE milliseconds BETWEEN :startMilli AND :endMilli ORDER BY milliseconds DESC LIMIT :limit")
    fun rangeWithLimit(startMilli: Long, endMilli: Long, limit: Int): List<Movement>

    @Query("SELECT milliseconds FROM movements_table ORDER BY milliseconds ASC LIMIT 1")
    fun firstTimestamp(): Long

    @Query("SELECT milliseconds FROM movements_table ORDER BY milliseconds DESC LIMIT 1")
    fun lastTimestamp(): Long

    @Query("SELECT SUM(amount) FROM movements_table")
    fun getBalance(): Double

    @Query("SELECT * FROM movements_table ORDER BY milliseconds DESC LIMIT :limit OFFSET :offset")
    fun getPageByOffset(limit: Int, offset: Int): List<Movement>

    @Query("SELECT * FROM movements_table WHERE milliseconds <= :comparison ORDER BY milliseconds DESC LIMIT :limit")
    fun getPageByComparison(comparison: Long, limit: Int): List<Movement>

    @Query("SELECT * FROM movements_table ORDER BY milliseconds")
    fun getAllPages(): List<Movement>
}