package org.amdoige.cashtrack.mainscreen

import androidx.lifecycle.*
import kotlinx.coroutines.launch

class UIStateViewModel : ViewModel() {
    private val _currentScreen: MutableLiveData<MainFragments?> = null

    val currentScreen: LiveData<MainFragments?>
        get() = _currentScreen

    fun switchScreens() {
        viewModelScope.launch {
            val screen = currentScreen.value ?: MainFragments.MOVEMENTS
            _currentScreen.value = when (screen) {
                MainFragments.MOVEMENTS -> MainFragments.BILLFOLDS
                MainFragments.BILLFOLDS -> MainFragments.MOVEMENTS
            }
        }
    }

    companion object {
        class Factory() :
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