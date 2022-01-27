package org.amdoige.cashtrack.history.data

import androidx.paging.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.amdoige.cashtrack.core.database.Movement
import org.amdoige.cashtrack.core.database.CashTrackDatabase
import timber.log.Timber

class HistoryRepository(private val cashTrackDatabase: CashTrackDatabase) {
    private val pagingIntermediary = PagingDatabaseIntermediary(cashTrackDatabase)
    private var pagingSource = HistoryPagingSource(pagingIntermediary::pageLoader)

    fun getValidPagingSource(): PagingSource<Int, Movement> {
        if (pagingSource.invalid) {
            Timber.i("Recreating PagingSource")
            pagingSource = HistoryPagingSource(pagingIntermediary::pageLoader)
        }
        return pagingSource
    }

    private fun invalidatePagingSource() {
        pagingIntermediary.invalidate()
        pagingSource.invalidate()
    }

    suspend fun getBalance(): Double = withContext(Dispatchers.IO) {
        cashTrackDatabase.dao.getBalance()
    }

    suspend fun postMovement(movement: Movement) {
        withContext(Dispatchers.IO) {
            if (cashTrackDatabase.dao.getMovement(movement.id) == null) {
                cashTrackDatabase.dao.insert(movement)
            } else {
                cashTrackDatabase.dao.update(movement)
            }
        }
        invalidatePagingSource()
    }

    suspend fun deleteAllMovements() {
        withContext(Dispatchers.IO) { cashTrackDatabase.dao.clear() }
        invalidatePagingSource()
    }

    suspend fun contains(movement: Movement): Boolean = withContext(Dispatchers.IO) {
        cashTrackDatabase.dao.getMovement(movement.id)?.equals(movement) ?: false
    }
}
