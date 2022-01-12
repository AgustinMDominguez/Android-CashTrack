package org.amdoige.cashtrack.history

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
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
    ): List<Movement> {
        return withContext(Dispatchers.IO) {
            val start = from ?: movementsDatabase.dao.firstTimestamp()
            val end = to ?: movementsDatabase.dao.lastTimestamp()
            if (amountLimit == null) {
                movementsDatabase.dao.range(start, end)
            } else {
                movementsDatabase.dao.rangeWithLimit(start, end, amountLimit)
            }
        }
    }

    suspend fun getMovementBefore(movement: Movement): Movement? {
        val movementList = getMovements(to = movement.timestamp, amountLimit = 2)
        return if (movementList.size < 2) null else movementList[0]
    }

    suspend fun getMovementAfter(movement: Movement): Movement? {
        val movementList = getMovements(from = movement.timestamp, amountLimit = 2)
        return if (movementList.size < 2) null else movementList[1]
    }

    suspend fun postMovement(movement: Movement) = withContext(Dispatchers.IO) {
        movementsDatabase.dao.insert(movement)
    }
}