package org.amdoige.cashtrack.mainscreen

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import org.amdoige.cashtrack.core.classes.EventLivedata
import org.amdoige.cashtrack.core.classes.PulseEvent
import org.amdoige.cashtrack.core.database.Movement
import org.amdoige.cashtrack.core.database.Wallet

class SharedViewModel : ViewModel() {
    val addEvent = PulseEvent()
    val walletCreationEvent = EventLivedata<Wallet>()
    val movementCreationEvent = EventLivedata<Movement>()
    val switchScreenEvent = EventLivedata<MainFragments>()

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