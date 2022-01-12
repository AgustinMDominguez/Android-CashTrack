package org.amdoige.cashtrack.core.database

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Clock
import java.time.Instant

@Entity(tableName = "movements_table")
data class Movement(
    @PrimaryKey val timestamp: Instant,
    val amount: Double,
    val title: String,
    val description: String? = null
) {
    companion object {
        fun newImmediateMovement(
            amount: Double,
            title: String,
            description: String? = null
        ): Movement {
            val newInstant = Instant.now(Clock.systemUTC())
            return Movement(newInstant, amount, title, description)
        }
    }
}
