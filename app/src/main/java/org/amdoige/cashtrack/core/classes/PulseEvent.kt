package org.amdoige.cashtrack.core.classes

import androidx.lifecycle.LifecycleOwner

class PulseEvent : EventLivedata<Boolean>() {
    fun emit() = emit(true)

    fun queue() = queue(true)

    fun observePulse(owner: LifecycleOwner, handler: () -> Unit) {
        super.observe(owner) { handler() }
    }
}
