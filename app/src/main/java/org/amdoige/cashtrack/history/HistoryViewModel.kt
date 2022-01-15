package org.amdoige.cashtrack.history

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import org.amdoige.cashtrack.core.database.Movement

class HistoryViewModel(private val historyRepository: HistoryRepository) : ViewModel() {
    private val batchFetchSize = 20

    private val _movements: MutableLiveData<List<Movement>> = MutableLiveData()
    val movements: LiveData<List<Movement>>
        get() = _movements

    val movementsSize: LiveData<Int> = Transformations.map(movements) { list -> list.size }

    init {
        fetchMoreMovements()
    }

    fun fetchMoreMovements() {
        viewModelScope.launch {
            val from = when {
                _movements.value.isNullOrEmpty() -> null
                else -> _movements.value?.last()?.timestamp
            }
            val moreMovements =
                historyRepository.getMovements(from = from, amountLimit = batchFetchSize)
            val newList: List<Movement> = when {
                _movements.value.isNullOrEmpty() -> moreMovements
                else -> listOf(
                    _movements.value ?: listOf(),
                    moreMovements.subList(1, moreMovements.size)
                ).flatten()
            }
            _movements.postValue(newList)
        }
    }

    fun addMovement(movement: Movement, overwriteOnTimestampUsed: Boolean = true) {
        viewModelScope.launch {
            val timestampIsUsed = historyRepository.isTimestampUsed(movement.timestamp)
            if (!timestampIsUsed || overwriteOnTimestampUsed) {
                historyRepository.postMovement(movement)
            }
        }
    }
}
