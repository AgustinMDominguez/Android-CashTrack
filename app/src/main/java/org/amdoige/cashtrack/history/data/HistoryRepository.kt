package org.amdoige.cashtrack.history.data

import androidx.paging.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.amdoige.cashtrack.core.database.Movement
import org.amdoige.cashtrack.core.database.MovementsDatabase
import timber.log.Timber

class HistoryRepository(private val movementsDatabase: MovementsDatabase) {
    private var pagingSource = HistoryPagingSource(::pagingSourceLoader)

    fun getValidPagingSource(): PagingSource<Int, Movement> {
        if (pagingSource.invalid) {
            Timber.i("Recreating PagingSource")
            pagingSource = HistoryPagingSource(::pagingSourceLoader)
        }
        return pagingSource
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
        pagingSource.invalidate()
    }

    suspend fun deleteAllMovements() {
        withContext(Dispatchers.IO) { movementsDatabase.dao.clear() }
        pagingSource.invalidate()
    }

    suspend fun contains(movement: Movement): Boolean = withContext(Dispatchers.IO) {
        movementsDatabase.dao.get(movement.id)?.equals(movement) ?: false
    }

    private suspend fun pagingSourceLoader(page: Int, pageSize: Int): List<Movement> {
        return withContext(Dispatchers.IO) {
            val offset = (page - 1) * pageSize
            // FIXME: See dao method
            val movements: List<Movement> = movementsDatabase.dao.getPage(pageSize, offset)
            Timber.i("Call to getPage($pageSize,$offset) returned ${movements.size} movements")
            movements
        }
    }
}
