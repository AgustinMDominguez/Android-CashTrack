package org.amdoige.cashtrack.history.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.amdoige.cashtrack.R
import org.amdoige.cashtrack.core.database.Wallet
import org.amdoige.cashtrack.databinding.SingleHistoryWalletViewBinding

class HistoryWalletsAdapter :
    ListAdapter<Wallet, HistoryWalletsAdapter.ViewHolder>(HistoryWalletDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding = SingleHistoryWalletViewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(private val itemBinding: SingleHistoryWalletViewBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(wallet: Wallet) {
            itemBinding.walletTestTextView.text = wallet.toString()
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
