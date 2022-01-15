package org.amdoige.cashtrack.core.database

import androidx.room.*
import java.time.Instant

@Dao
interface MovementsDatabaseDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insert(movement: Movement)

    @Update
    fun update(movement: Movement)

    @Query("SELECT * FROM movements_table WHERE timestamp = :key")
    fun get(key: Instant): Movement?

    @Delete
    fun delete(movement: Movement)

    @Query("SELECT COUNT(timestamp) from movements_table")
    fun databaseSize(): Int

    @Query("SELECT * FROM movements_table WHERE timestamp BETWEEN :start AND :end ORDER BY timestamp DESC")
    fun range(start: Instant, end: Instant): List<Movement>

    @Query("SELECT * FROM movements_table WHERE timestamp BETWEEN :start AND :end ORDER BY timestamp DESC LIMIT :limit")
    fun rangeWithLimit(start: Instant, end: Instant, limit: Int): List<Movement>

    @Query("SELECT FIRST(timestamp) FROM movements_table")
    fun firstTimestamp(): Instant

    @Query("SELECT LAST(timestamp) FROM movements_table")
    fun lastTimestamp(): Instant
}
