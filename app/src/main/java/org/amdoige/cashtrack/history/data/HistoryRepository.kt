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

    suspend fun getAmountOfMovements(): Int = withContext(Dispatchers.IO) {
        movementsDatabase.dao.databaseSize()
    }

    suspend fun getMovements(
        fromMilli: Long? = null,
        toMilli: Long? = null,
        amountLimit: Int? = null
    ): List<Movement> = withContext(Dispatchers.IO) {
        val startMilli = fromMilli ?: movementsDatabase.dao.firstTimestamp()
        val endMilli = toMilli ?: movementsDatabase.dao.lastTimestamp()
        if (amountLimit == null) {
            movementsDatabase.dao.range(startMilli, endMilli)
        } else {
            movementsDatabase.dao.rangeWithLimit(startMilli, endMilli, amountLimit)
        }
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

    suspend fun deleteMovement(movement: Movement) = withContext(Dispatchers.IO) {
        movementsDatabase.dao.delete(movement)
    }

    suspend fun deleteAllMovements() {
        withContext(Dispatchers.IO) { movementsDatabase.dao.clear() }
        pagingSource.invalidate()
    }

    suspend fun contains(movement: Movement): Boolean = withContext(Dispatchers.IO) {
        movementsDatabase.dao.get(movement.id)?.equals(movement) ?: false
    }

    suspend fun isTimestampUsed(timestampMilli: Long): Boolean = withContext(Dispatchers.IO) {
        movementsDatabase.dao.range(timestampMilli, timestampMilli).isNotEmpty()
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
