package org.amdoige.cashtrack.core.database

import androidx.room.*
import java.time.Instant

@Dao
interface MovementsDatabaseDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insert(movement: Movement)

    @Update
    fun update(movement: Movement)

    @Query("SELECT * FROM movements_table WHERE id = :key")
    fun get(key: Long): Movement?

    @Delete
    fun delete(movement: Movement)

    @Query("SELECT COUNT(id) from movements_table")
    fun databaseSize(): Int

    @Query("SELECT * FROM movements_table WHERE milliseconds BETWEEN :startMilli AND :endMilli ORDER BY milliseconds DESC")
    fun range(startMilli: Long, endMilli: Long): List<Movement>

    @Query("SELECT * FROM movements_table WHERE milliseconds BETWEEN :startMilli AND :endMilli ORDER BY milliseconds DESC LIMIT :limit")
    fun rangeWithLimit(startMilli: Long, endMilli: Long, limit: Int): List<Movement>

    @Query("SELECT milliseconds FROM movements_table ORDER BY milliseconds ASC LIMIT 1")
    fun firstTimestamp(): Long

    @Query("SELECT milliseconds FROM movements_table ORDER BY milliseconds DESC LIMIT 1")
    fun lastTimestamp(): Long
}
