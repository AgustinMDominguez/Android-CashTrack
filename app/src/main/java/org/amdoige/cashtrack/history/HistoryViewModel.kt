package org.amdoige.cashtrack.history

import androidx.lifecycle.*
import androidx.paging.*
import kotlinx.coroutines.launch
import org.amdoige.cashtrack.core.database.Movement
import org.amdoige.cashtrack.history.data.HistoryRepository
import java.text.DecimalFormat

class HistoryViewModel(private val historyRepository: HistoryRepository) : ViewModel() {
    private val modelPageSize = 10

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

    init {
        updateBalance()
    }

    private fun updateBalance() {
        viewModelScope.launch { _balance.value = historyRepository.getBalance() }
    }

    private fun newMovement(
        timestampMilli: Long,
        amount: Double,
        title: String,
        description: String? = null,
    ) {
        val newMovement = Movement(
            milliseconds = timestampMilli,
            amount = amount,
            title = title,
            description = description ?: ""
        )
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

    fun newImmediateMovement(amount: Double, title: String, description: String? = "") {
        val timestampMilli = System.currentTimeMillis()
        newMovement(timestampMilli, amount, title, description)
    }

    fun deleteAllMovements() {
        viewModelScope.launch {
            historyRepository.deleteAllMovements()
            updateBalance()
        }
    }

    companion object {
        class Factory(private val historyRepository: HistoryRepository) :
            ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("unchecked_cast")
                if (modelClass.isAssignableFrom(HistoryViewModel::class.java)) {
                    return HistoryViewModel(historyRepository) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    }
}
