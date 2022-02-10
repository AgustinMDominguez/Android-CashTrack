package org.amdoige.cashtrack.core.classes

import androidx.lifecycle.LifecycleOwner

class PulseEvent {
    private val event = EventLivedata<Boolean>()

    fun emit() = event.emit(true)

    fun queue() = event.queue(true)

    fun observe(owner: LifecycleOwner, handler: () -> Unit) {
        event.observe(owner) { handler() }
    }
}
