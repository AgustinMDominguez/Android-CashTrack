package org.amdoige.cashtrack.history.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.amdoige.cashtrack.R
import org.amdoige.cashtrack.core.database.Wallet

class HistoryWalletsAdapter : RecyclerView.Adapter<HistoryWalletsAdapter.ViewHolder>() {
    var wallets = listOf<Wallet>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView.text = wallets[position].toString()
    }

    override fun getItemCount(): Int = wallets.size

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
}
