package org.amdoige.cashtrack.core.database

import androidx.recyclerview.widget.DiffUtil
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movements_table")
data class Movement(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,
    var walletId: Long,
    var amount: Double = 0.0,
    var milliseconds: Long = System.currentTimeMillis(),
    var title: String = "",
    var description: String = "",
) {
    companion object {
        object Comparator : DiffUtil.ItemCallback<Movement>() {
            override fun areItemsTheSame(p0: Movement, p1: Movement): Boolean = p0.id == p1.id

            override fun areContentsTheSame(p0: Movement, p1: Movement): Boolean = p0 == p1
        }
    }
}
