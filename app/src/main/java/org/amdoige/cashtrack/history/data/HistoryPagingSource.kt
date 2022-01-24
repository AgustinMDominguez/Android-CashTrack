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
            Timber.e("Error fetching History Page", e)
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

//class ExamplePagingSource(
//    val backend: ExampleBackendService,
//    val query: String
//) : PagingSource<Int, User>() {
//    override suspend fun load(
//        params: LoadParams<Int>
//    ): LoadResult<Int, User> {
//        try {
//            // Start refresh at page 1 if undefined.
//            val nextPageNumber = params.key ?: 1
//            val response = backend.searchUsers(query, nextPageNumber)
//            return LoadResult.Page(
//                data = response.users,
//                prevKey = null, // Only paging forward.
//                nextKey = response.nextPageNumber
//            )
//        } catch (e: Exception) {
//            // Handle errors in this block and return LoadResult.Error if it is an
//            // expected error (such as a network failure).
//        }
//    }
//
//    override fun getRefreshKey(state: PagingState<Int, User>): Int? {
//        // Try to find the page key of the closest page to anchorPosition, from
//        // either the prevKey or the nextKey, but you need to handle nullability
//        // here:
//        //  * prevKey == null -> anchorPage is the first page.
//        //  * nextKey == null -> anchorPage is the last page.
//        //  * both prevKey and nextKey null -> anchorPage is the initial page, so
//        //    just return null.
//        return state.anchorPosition?.let { anchorPosition ->
//            val anchorPage = state.closestPageToPosition(anchorPosition)
//            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
//        }
//    }
//}