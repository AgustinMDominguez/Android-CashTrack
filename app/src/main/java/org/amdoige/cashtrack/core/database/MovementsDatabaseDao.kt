package org.amdoige.cashtrack.core.database

import androidx.room.*

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

    @Query("SELECT * FROM movements_table ORDER BY milliseconds DESC LIMIT :limit OFFSET :offset") // FIXME https://www2.sqlite.org/cvstrac/wiki?p=ScrollingCursor
    fun getPage(limit: Int, offset: Int): List<Movement>

    @Query("SELECT * FROM movements_table ORDER BY milliseconds")
    fun getAllPages(): List<Movement>
}
