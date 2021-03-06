package org.amdoige.cashtrack.core.classes

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

class EventLivedata<T> {
    private val liveData: MutableLiveData<T?> = MutableLiveData(null)

    fun observe(owner: LifecycleOwner, handler: (T) -> Unit) {
        val observer = Observer<T?> {
            if (it != null) {
                handler(it)
                liveData.value = null
            }
        }
        liveData.observe(owner, observer)
    }

    fun isHandled(): Boolean = liveData.value == null

    fun emit(data: T) {
        liveData.value = data
    }

    fun queue(data: T) = liveData.postValue(data)
}
