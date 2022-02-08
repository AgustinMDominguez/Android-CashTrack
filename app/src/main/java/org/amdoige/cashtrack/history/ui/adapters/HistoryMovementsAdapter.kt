package org.amdoige.cashtrack.history.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import org.amdoige.cashtrack.R
import org.amdoige.cashtrack.core.database.Movement
import org.amdoige.cashtrack.databinding.SingleMovementViewBinding


class HistoryMovementsAdapter :
    PagingDataAdapter<Movement, HistoryMovementsAdapter.ViewHolder>(Movement.Companion.Comparator) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val itemBinding = SingleMovementViewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item)
        } else {
            holder.bindPlaceholder()
        }
    }

    class ViewHolder(private val itemBinding: SingleMovementViewBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(movement: Movement) {
            itemBinding.movementTestTextView.text = movement.toString()
        }

        fun bindPlaceholder() {}
    }
}