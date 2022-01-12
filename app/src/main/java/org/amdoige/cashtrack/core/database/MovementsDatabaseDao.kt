package org.amdoige.cashtrack.core.database

import android.text.method.MovementMethod
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import java.time.Instant

@Dao
interface MovementsDatabaseDao {
    @Insert
    fun insert(movement: Movement)

    @Update
    fun update(movement: Movement)

    @Query("SELECT * FROM movements_table WHERE timestamp = :key")
    fun get(key: Instant): Movement?

    @Query("DELETE * FROM movements_table WHERE timestamp = :key")
    fun delete(key: Instant)

    @Query("SELECT COUNT(timestamp) from movements_table")
    fun databaseSize(): Int

    @Query("SELECT * FROM movements_table WHERE timestamp BETWEEN :start AND :end ORDER BY timestamp")
    fun range(start: Instant, end: Instant): List<Movement>

    @Query("SELECT * FROM movements_table WHERE timestamp BETWEEN :start AND :end ORDER BY timestamp LIMIT :limit")
    fun rangeWithLimit(start: Instant, end: Instant, limit: Int): List<Movement>

    @Query("SELECT FIRST(timestamp) FROM movements_table")
    fun firstTimestamp(): Instant

    @Query("SELECT LAST(timestamp) FROM movements_table")
    fun lastTimestamp(): Instant
}
