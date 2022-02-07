package org.amdoige.cashtrack.history.ui.viewmodels

import androidx.lifecycle.*
import androidx.paging.*
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import org.amdoige.cashtrack.billfolds.data.WalletsRepository
import org.amdoige.cashtrack.core.database.Movement
import org.amdoige.cashtrack.core.database.Wallet
import org.amdoige.cashtrack.history.data.HistoryRepository
import java.text.DecimalFormat

class HistoryViewModel(
    private val historyRepository: HistoryRepository,
    private val walletsRepository: WalletsRepository
) : ViewModel() {
    private val modelPageSize = 30

    val movementsPagingData = Pager(
        PagingConfig(pageSize = modelPageSize),
        null,
        { historyRepository.getValidPagingSource() }
    )
        .liveData
        .cachedIn(viewModelScope)

    private val _balance: MutableLiveData<Double> = MutableLiveData(0.0)
    val balanceString: LiveData<String> = Transformations.map(_balance) { balance: Double ->
        val sign = if (balance > 0.0) "" else "-"
        val roundedAmount = DecimalFormat("#.00").format(_balance.value ?: 0.0)
        "$sign $ $roundedAmount"
    }

    private val _wallets: MutableLiveData<List<Wallet>> = MutableLiveData(listOf())

    val wallets: LiveData<List<Wallet>>
        get() = _wallets

    init {
        updateWalletsFromDatabase()
        updateBalance()
    }

    private fun updateWalletsFromDatabase() {
        viewModelScope.launch { _wallets.postValue(walletsRepository.getAllWalletsWithBalance()) }
    }

    private fun updateBalance() {
        viewModelScope.launch { _balance.value = historyRepository.getBalance() }
    }

    fun newMovement(newMovement: Movement) {
        viewModelScope.launch {
            historyRepository.postMovement(newMovement)
            val currentBalance = _balance.value
            if (currentBalance == null) {
                updateBalance()
            } else {
                _balance.value = currentBalance + newMovement.amount
            }
        }
    }

    fun deleteAllMovements() {
        viewModelScope.launch {
            historyRepository.deleteAllMovements()
            updateBalance()
        }
    }

    companion object {
        class Factory(
            private val historyRepository: HistoryRepository,
            private val walletsRepository: WalletsRepository
        ) : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("unchecked_cast")
                if (modelClass.isAssignableFrom(HistoryViewModel::class.java)) {
                    return HistoryViewModel(historyRepository, walletsRepository) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    }
}
