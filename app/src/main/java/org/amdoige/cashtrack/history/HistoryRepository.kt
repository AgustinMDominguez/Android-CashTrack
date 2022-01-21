package org.amdoige.cashtrack.history

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.amdoige.cashtrack.core.database.Movement
import org.amdoige.cashtrack.core.database.MovementsDatabase

class HistoryRepository(private val movementsDatabase: MovementsDatabase) {

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

    suspend fun postMovement(movement: Movement) = withContext(Dispatchers.IO) {
        if (movementsDatabase.dao.get(movement.id) == null) {
            movementsDatabase.dao.insert(movement)
        } else {
            movementsDatabase.dao.update(movement)
        }
    }

    suspend fun deleteMovement(movement: Movement) = withContext(Dispatchers.IO) {
        movementsDatabase.dao.delete(movement)
    }

    suspend fun contains(movement: Movement): Boolean = withContext(Dispatchers.IO) {
        movementsDatabase.dao.get(movement.id)?.equals(movement) ?: false
    }

    suspend fun isTimestampUsed(timestampMilli: Long): Boolean = withContext(Dispatchers.IO) {
        movementsDatabase.dao.range(timestampMilli, timestampMilli).isNotEmpty()
    }
}
