package org.amdoige.cashtrack.mainscreen

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import org.amdoige.cashtrack.core.database.Movement
import org.amdoige.cashtrack.core.database.Wallet

class SharedViewModel : ViewModel() {
    private val _walletCreated: MutableLiveData<Wallet?> = MutableLiveData(null)
    val walletCreated: LiveData<Wallet?>
        get() = _walletCreated

    private val _movementCreated: MutableLiveData<Movement?> = MutableLiveData(null)
    val movementCreated: LiveData<Movement?>
        get() = _movementCreated

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

    fun newMovement(movement: Movement) {
        viewModelScope.launch { _movementCreated.postValue(movement) }
    }

    fun ackNewMovement() {
        viewModelScope.launch { _movementCreated.value = null }
    }

    fun newWallet(wallet: Wallet) {
        viewModelScope.launch { _walletCreated.postValue(wallet) }
    }

    fun ackNewWallet() {
        viewModelScope.launch { _walletCreated.value = null }
    }

    companion object {
        class Factory :
            ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("unchecked_cast")
                if (modelClass.isAssignableFrom(SharedViewModel::class.java)) {
                    return SharedViewModel() as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    }
}