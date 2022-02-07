package org.amdoige.cashtrack.history.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.amdoige.cashtrack.R
import org.amdoige.cashtrack.core.database.Wallet

class HistoryWalletsAdapter :
    ListAdapter<Wallet, HistoryWalletsAdapter.ViewHolder>(HistoryWalletDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView.text = getItem(position).toString()
    }

    class ViewHolder(val textView: TextView) : RecyclerView.ViewHolder(textView) {
        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater
                    .inflate(
                        R.layout.single_history_wallet_view, parent, false
                    ) as TextView
                return ViewHolder(view)
            }
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
