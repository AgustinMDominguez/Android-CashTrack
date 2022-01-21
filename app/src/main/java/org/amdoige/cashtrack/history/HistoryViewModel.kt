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
            val moreMovements = historyRepository.getMovements(
                fromMilli = lastMovementOrNull()?.milliseconds,
                amountLimit = batchFetchSize
            )
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

    fun newMovement(
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
            _movements.value = listOf()
            fetchMoreMovements()
        }
    }

    fun newImmediateMovement(amount: Double, title: String, description: String? = "") {
        val timestampMilli = System.currentTimeMillis()
        newMovement(timestampMilli, amount, title, description)
    }

//    fun updateMovement(
//        movement: Movement,
//        timestamp: Long? = null,
//        amount: Double? = null,
//        title: String? = null,
//        description: String? = null
//    ) {
//
//    }

//    fun addMovement(movement: Movement, overwriteOnTimestampUsed: Boolean = true) {
//        viewModelScope.launch {
//            val timestampIsUsed = historyRepository.isTimestampUsed(movement.movementTimestamp)
//            if (!timestampIsUsed || overwriteOnTimestampUsed) {
//                historyRepository.postMovement(movement)
//            }
//        }
//    }

    fun lastMovementOrNull(): Movement? {
        return try {
            _movements.value?.last()
        } catch (emptyListException: NoSuchElementException) {
            null
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
