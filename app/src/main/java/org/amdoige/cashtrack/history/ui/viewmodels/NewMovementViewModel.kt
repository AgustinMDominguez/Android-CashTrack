package org.amdoige.cashtrack.history.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.amdoige.cashtrack.billfolds.data.WalletsRepository
import org.amdoige.cashtrack.core.database.Movement

class NewMovementViewModel(private val walletsRepository: WalletsRepository) : ViewModel() {
    val walletId: MutableLiveData<Long?> = MutableLiveData(null)
    val amount: MutableLiveData<Double> = MutableLiveData(0.0)
    val milliseconds: MutableLiveData<Long?> = MutableLiveData(null)
    val title: MutableLiveData<String> = MutableLiveData("Movement")
    val description: MutableLiveData<String?> = MutableLiveData(null)


    init {
        fetchDefaultWalletId()
    }

    private fun fetchDefaultWalletId() {
        viewModelScope.launch { walletId.postValue(walletsRepository.getDefaultWallet().id) }
    }

    fun getCreatedMovement(): Movement? {
        val createdMovementWalletId = walletId.value
        if (createdMovementWalletId != null) {
            return Movement(
                walletId = createdMovementWalletId,
                amount = amount.value ?: 0.0,
                milliseconds = milliseconds.value ?: System.currentTimeMillis(),
                title = title.value ?: "Movement",
                description = description.value ?: ""
            )
        }
        return null
    }

    companion object {
        class Factory(
            private val walletsRepository: WalletsRepository
        ) : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("unchecked_cast")
                if (modelClass.isAssignableFrom(NewMovementViewModel::class.java)) {
                    return NewMovementViewModel(walletsRepository) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    }
}