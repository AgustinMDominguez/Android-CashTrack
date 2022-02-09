package org.amdoige.cashtrack.history.ui.movements.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.amdoige.cashtrack.core.database.Wallet
import org.amdoige.cashtrack.databinding.SingleHistoryWalletViewBinding

class HistoryWalletsAdapter :
    ListAdapter<Wallet, HistoryWalletsAdapter.ViewHolder>(HistoryWalletDiffCallback()) {
    private lateinit var context: Context

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        context = recyclerView.context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding = SingleHistoryWalletViewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), context)
    }

    class ViewHolder(private val itemBinding: SingleHistoryWalletViewBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(wallet: Wallet, context: Context) {
            val drawableIcon = AppCompatResources.getDrawable(context, wallet.logo)
            itemBinding.walletIcon.setImageDrawable(drawableIcon)
            itemBinding.walletCurrentBalance.text = wallet.getBalanceString()

            val limitPeriodString: String = when(wallet.limitPeriod) {
                'd' -> "day"
                'w' -> "week"
                'm' -> "month"
                else -> "--"
            }
            if(wallet.limit >= 0.0) {
                val limitString = "${wallet.limit} /$limitPeriodString"
                itemBinding.walletLimit.text = limitString
            } else {
                itemBinding.walletLimit.visibility = View.INVISIBLE
            }

            itemBinding.walletName.text = wallet.name
        }
    }

    class HistoryWalletDiffCallback : DiffUtil.ItemCallback<Wallet>() {
        override fun areItemsTheSame(oldItem: Wallet, newItem: Wallet): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Wallet, newItem: Wallet): Boolean {
            return oldItem == newItem
        }
    }
}
