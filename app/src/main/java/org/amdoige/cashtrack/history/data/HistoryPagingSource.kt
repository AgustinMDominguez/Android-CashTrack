package org.amdoige.cashtrack.history.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import org.amdoige.cashtrack.core.database.Movement
import timber.log.Timber
import kotlin.reflect.KSuspendFunction2

class HistoryPagingSource(private val loader: KSuspendFunction2<Int, Int, List<Movement>>) :
    PagingSource<Int, Movement>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movement> {
        return try {
            val pageNumber = params.key ?: 1
            val newData: List<Movement> = loader(pageNumber, params.loadSize)
            val prevKey = if (pageNumber == 1) null else pageNumber - 1
            val nextKey = if (newData.size < params.loadSize) null else pageNumber + 1
            LoadResult.Page(newData, prevKey, nextKey)
        } catch (e: Exception) {
            Timber.e("Error fetching History Page: $e", e)
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Movement>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}
