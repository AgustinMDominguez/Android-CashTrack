package org.amdoige.cashtrack.history

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.amdoige.cashtrack.core.database.Movement
import org.amdoige.cashtrack.core.database.MovementsDatabase
import java.time.Instant

class HistoryRepository(private val movementsDatabase: MovementsDatabase) {

    suspend fun getAmountOfMovements(): Int = withContext(Dispatchers.IO) {
        movementsDatabase.dao.databaseSize()
    }

    suspend fun getMovements(
        from: Instant? = null,
        to: Instant? = null,
        amountLimit: Int? = null
    ): List<Movement> = withContext(Dispatchers.IO) {
        val start = from ?: movementsDatabase.dao.firstTimestamp()
        val end = to ?: movementsDatabase.dao.lastTimestamp()
        if (amountLimit == null) {
            movementsDatabase.dao.range(start, end)
        } else {
            movementsDatabase.dao.rangeWithLimit(start, end, amountLimit)
        }
    }

    suspend fun postMovement(movement: Movement) = withContext(Dispatchers.IO) {
        if (movementsDatabase.dao.get(movement.timestamp) == null) {
            movementsDatabase.dao.insert(movement)
        } else {
            movementsDatabase.dao.update(movement)
        }
    }

    suspend fun deleteMovement(movement: Movement) = withContext(Dispatchers.IO) {
        movementsDatabase.dao.delete(movement)
    }

    suspend fun contains(movement: Movement): Boolean = withContext(Dispatchers.IO) {
        movementsDatabase.dao.get(movement.timestamp)?.equals(movement) ?: false
    }

    suspend fun isTimestampUsed(timestamp: Instant): Boolean = withContext(Dispatchers.IO) {
        movementsDatabase.dao.get(timestamp) != null
    }
}
