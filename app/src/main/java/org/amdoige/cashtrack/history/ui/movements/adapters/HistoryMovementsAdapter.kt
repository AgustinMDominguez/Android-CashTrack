package org.amdoige.cashtrack.history.ui.movements.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import org.amdoige.cashtrack.R
import org.amdoige.cashtrack.core.database.Movement
import org.amdoige.cashtrack.databinding.SingleMovementViewBinding


class HistoryMovementsAdapter :
    PagingDataAdapter<Movement, HistoryMovementsAdapter.ViewHolder>(Movement.Companion.Comparator) {

    private lateinit var context: Context

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        context = recyclerView.context
    }

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
            holder.bind(item, context)
        } else {
            holder.bindPlaceholder()
        }
    }

    class ViewHolder(private val itemBinding: SingleMovementViewBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(movement: Movement, context: Context) {
            val drawableId: Int = movement.logo ?: R.drawable.ic_cashtrack_wallet_1
            val drawableIcon = AppCompatResources.getDrawable(context, drawableId)

            itemBinding.transactionIcon.setImageDrawable(drawableIcon)
            itemBinding.transactionTitle.text = movement.title
            itemBinding.transactionAmount.text = movement.getAmountString()

            if (movement.description.isNotEmpty()) {
                itemBinding.transactionDescription.visibility = View.VISIBLE
                itemBinding.transactionDescription.text = movement.description
            } else {
                itemBinding.transactionDescription.visibility = View.GONE
            }
        }

        fun bindPlaceholder() {}
    }
}