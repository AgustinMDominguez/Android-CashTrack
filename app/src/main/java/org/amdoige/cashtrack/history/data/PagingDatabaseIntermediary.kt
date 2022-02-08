package org.amdoige.cashtrack.history.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.amdoige.cashtrack.core.database.Movement
import org.amdoige.cashtrack.core.database.CashTrackDatabase
import timber.log.Timber
import kotlin.reflect.KSuspendFunction1

class PagingDatabaseIntermediary(
    private val cashTrackDatabase: CashTrackDatabase,
    private val loaderMap: KSuspendFunction1<List<Movement>, List<Movement>>
) {
    private var pageMap: MutableMap<Int, Long> = mutableMapOf() // page -> movement timestamp
    private var currentPageSize: Int? = null

    suspend fun pageLoader(page: Int, pageSize: Int): List<Movement> {
        return loaderMap(loadPage(page, pageSize))
    }

    private suspend fun loadPage(page: Int, pageSize: Int): List<Movement> {
        when {
            currentPageSize == null || currentPageSize == 3 * pageSize -> {
                currentPageSize = pageSize
            }
            pageSize != currentPageSize -> {
                invalidate()
            }
        }

        return withContext(Dispatchers.IO) {
            val movementsPlusOne: List<Movement> = when {
                page == 1 -> {
                    cashTrackDatabase.dao.getPageByOffset(pageSize + 1, 0)
                }
                pageMap.containsKey(page) -> {
                    cashTrackDatabase.dao.getPageByComparison(pageMap.getValue(page), pageSize + 1)
                }
                else -> {
                    val offset = (page - 1) * pageSize
                    cashTrackDatabase.dao.getPageByOffset(pageSize + 1, offset)
                }
            }
            if (movementsPlusOne.size == pageSize + 1) {
                pageMap.putIfAbsent(page + 1, movementsPlusOne.last().milliseconds)
                movementsPlusOne.subList(0, movementsPlusOne.size - 1)
            } else {
                movementsPlusOne
            }
        }
    }

    fun invalidate() {
        Timber.i("Invalidating PagingDataBaseIntermediary")
        pageMap = mutableMapOf()
        currentPageSize = null
    }
}