package org.amdoige.cashtrack.core.database

import android.content.res.Resources
import androidx.recyclerview.widget.DiffUtil
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.amdoige.cashtrack.R

@Entity(tableName = "wallet_table")
data class Wallet(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,
    var name: String = "Wallet",
    var balance: Double = 0.0,
    var limit: Double = -1.0,
    var logo: String = Resources.getSystem().getString(R.string.default_wallet_logo),
    var color: Int = Resources.getSystem().getColor(R.color.default_wallet_color, null),
) {

    companion object {
        object Comparator : DiffUtil.ItemCallback<Wallet>() {
            override fun areItemsTheSame(p0: Wallet, p1: Wallet): Boolean = p0.id == p1.id

            override fun areContentsTheSame(p0: Wallet, p1: Wallet): Boolean = p0 == p1
        }
    }
}
