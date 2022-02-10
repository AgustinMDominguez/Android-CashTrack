package org.amdoige.cashtrack.core.database

import androidx.recyclerview.widget.DiffUtil
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import org.amdoige.cashtrack.R
import java.text.DecimalFormat

@Entity(tableName = "wallet_table")
data class Wallet(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,
    var name: String = "Wallet",
    var limit: Double = -1.0,
    var limitPeriod: Char = 'm',
    var logo: Int = R.drawable.ic_cashtrack_wallet_1,
    var color: Int = R.color.default_wallet_color,
    @Ignore
    var balance: Double? = null
) {

    fun getBalanceString(): String = "$ ${DecimalFormat("#.00").format(balance ?: 0.0)}"

    companion object {
        object Comparator : DiffUtil.ItemCallback<Wallet>() {
            override fun areItemsTheSame(p0: Wallet, p1: Wallet): Boolean = p0.id == p1.id

            override fun areContentsTheSame(p0: Wallet, p1: Wallet): Boolean = p0 == p1
        }
    }
}
