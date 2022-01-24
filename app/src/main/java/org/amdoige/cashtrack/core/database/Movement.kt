package org.amdoige.cashtrack.core.database

import androidx.recyclerview.widget.DiffUtil
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movements_table")
data class Movement(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,
    var amount: Double = 0.0,
    var milliseconds: Long = System.currentTimeMillis(),
    var title: String = "",
    var description: String = ""
) {

//    fun getInstant(): Instant? {
//        return try {
//            val split = movementTimestamp.split("|", limit = 2)
//            val epochSecond: Long = split[0].toLong()
//            val nanoSecond: Long = split[1].toLong()
//            Instant.ofEpochSecond(epochSecond, nanoSecond)
//        } catch (exception: Exception) {
//            null
//        }
//    }

    companion object {
        object Comparator : DiffUtil.ItemCallback<Movement>() {
            override fun areItemsTheSame(p0: Movement, p1: Movement): Boolean = p0.id == p1.id

            override fun areContentsTheSame(p0: Movement, p1: Movement): Boolean = p0 == p1
        }

//        @Ignore
//        fun fromInstant(
//            instant: Instant,
//            amount: Double,
//            title: String,
//            description: String
//        ): Movement {
//            return Movement(InstantUtils.toString(instant), amount, title, description)
//        }
//
//        @Ignore
//        fun newImmediateMovement(
//            amount: Double,
//            title: String,
//            description: String
//        ): Movement {
//            val newInstant = Instant.now(Clock.systemUTC())
//            return fromInstant(newInstant, amount, title, description)
//        }
    }
}
