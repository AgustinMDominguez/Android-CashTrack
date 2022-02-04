package org.amdoige.cashtrack.mainscreen

import androidx.lifecycle.*
import kotlinx.coroutines.launch

class UIStateViewModel : ViewModel() {
    private val _currentScreen: MutableLiveData<MainFragments?> = MutableLiveData(null)
    val currentScreen: LiveData<MainFragments?>
        get() = _currentScreen

    private val _addButtonPressed: MutableLiveData<Boolean> = MutableLiveData(false)
    val addButtonPressed: LiveData<Boolean>
        get() = _addButtonPressed

    fun pressAddButton() {
        viewModelScope.launch { _addButtonPressed.value = true }
    }

    fun releaseAddButton() {
        viewModelScope.launch { _addButtonPressed.value = false }
    }

    fun toMovementsScreen() {
        viewModelScope.launch { _currentScreen.value = MainFragments.MOVEMENTS }
    }

    fun toBillfoldsScreen() {
        viewModelScope.launch { _currentScreen.value = MainFragments.BILLFOLDS }
    }

    fun ackScreenSwitch() {
        viewModelScope.launch { _currentScreen.postValue(null) }
    }

    companion object {
        class Factory :
            ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("unchecked_cast")
                if (modelClass.isAssignableFrom(UIStateViewModel::class.java)) {
                    return UIStateViewModel() as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    }
}