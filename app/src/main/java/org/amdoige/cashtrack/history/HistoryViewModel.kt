package org.amdoige.cashtrack.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.amdoige.cashtrack.core.database.Movement

class HistoryViewModel(private val historyRepository: HistoryRepository) : ViewModel() {

    private val _movements: MutableLiveData<List<Movement>> = MutableLiveData()
    val movements: LiveData<List<Movement>>
        get() = _movements

    init {
        fetchMoreMovements()
    }

    fun fetchMoreMovements() {
        viewModelScope.launch {
            val from = when {
                _movements.value.isNullOrEmpty() -> null
                else -> _movements.value?.last()?.timestamp
            }
            val moreMovements = historyRepository.getMovements(from = from, amountLimit = 20)
            val newList: List<Movement> = when {
                _movements.value.isNullOrEmpty() -> moreMovements
                else -> listOf(_movements.value ?: listOf(), moreMovements).flatten()
            }
            _movements.postValue(newList)
        }
    }
}