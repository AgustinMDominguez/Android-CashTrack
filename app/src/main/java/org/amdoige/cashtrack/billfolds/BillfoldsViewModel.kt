package org.amdoige.cashtrack.billfolds

import androidx.lifecycle.*
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import org.amdoige.cashtrack.billfolds.data.WalletsRepository
import org.amdoige.cashtrack.core.WalletsRepositoryProvider
import org.amdoige.cashtrack.core.database.Wallet
import org.amdoige.cashtrack.mainscreen.SharedViewModel

class BillfoldsViewModel(private val walletsRepository: WalletsRepository) : ViewModel() {
    private val _wallets: MutableLiveData<List<Wallet>> = MutableLiveData(listOf())

    val wallets: LiveData<List<Wallet>>
        get() = _wallets

    init {
        updateWalletsFromDatabase()
    }

    private fun updateWalletsFromDatabase() {
        viewModelScope.launch { _wallets.postValue(walletsRepository.getAllWalletsWithBalance()) }
    }

    companion object {
        class Factory(private val walletsRepository: WalletsRepository) :
            ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("unchecked_cast")
                if (modelClass.isAssignableFrom(BillfoldsViewModel::class.java)) {
                    return BillfoldsViewModel(walletsRepository) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    }
}
