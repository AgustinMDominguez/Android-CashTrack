package org.amdoige.cashtrack.history.data

import androidx.paging.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.amdoige.cashtrack.core.database.Movement
import org.amdoige.cashtrack.core.database.MovementsDatabase
import timber.log.Timber

class HistoryRepository(private val movementsDatabase: MovementsDatabase) {
    private val pagingIntermediary = PagingDatabaseIntermediary(movementsDatabase)
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
        movementsDatabase.dao.getBalance()
    }

    suspend fun postMovement(movement: Movement) {
        withContext(Dispatchers.IO) {
            if (movementsDatabase.dao.get(movement.id) == null) {
                movementsDatabase.dao.insert(movement)
            } else {
                movementsDatabase.dao.update(movement)
            }
        }
        invalidatePagingSource()
    }

    suspend fun deleteAllMovements() {
        withContext(Dispatchers.IO) { movementsDatabase.dao.clear() }
        invalidatePagingSource()
    }

    suspend fun contains(movement: Movement): Boolean = withContext(Dispatchers.IO) {
        movementsDatabase.dao.get(movement.id)?.equals(movement) ?: false
    }
}
