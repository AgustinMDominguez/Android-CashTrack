package org.amdoige.cashtrack.core

import org.amdoige.cashtrack.billfolds.data.WalletsRepository
import android.content.Context
import org.amdoige.cashtrack.core.database.CashTrackDatabase

object WalletsRepositoryProvider {
    private var instance: WalletsRepository? = null

    fun getRepository(context: Context): WalletsRepository {
        var repositoryInstance = instance
        if (repositoryInstance == null) {
            repositoryInstance = WalletsRepository(CashTrackDatabase.getInstance(context))
            instance = repositoryInstance
        }
        return repositoryInstance
    }
}
