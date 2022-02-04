package org.amdoige.cashtrack.billfolds

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import org.amdoige.cashtrack.billfolds.data.WalletsRepository
import org.amdoige.cashtrack.core.database.Wallet

class BillfoldsViewModel(private val walletsRepository: WalletsRepository) : ViewModel() {
    private val _wallets: MutableLiveData<List<Wallet>> = MutableLiveData(listOf())

    val wallets: LiveData<List<Wallet>>
        get() = _wallets

    init {
        updateWalletsFromDatabase()
    }

    private fun updateWalletsFromDatabase() {
        viewModelScope.launch {
            val wallets = walletsRepository.getAllWallets()
            // TODO: Move this implementation to Repository Cache
            val asyncJobs = mutableListOf<Deferred<Unit>>()
            wallets.forEach {
                asyncJobs.add(async { it.balance = walletsRepository.getWalletBalance(it) })
            }
            asyncJobs.awaitAll()
            _wallets.postValue(wallets)
        }
    }
}