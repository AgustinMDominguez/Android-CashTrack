package org.amdoige.cashtrack.history.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import org.amdoige.cashtrack.R
import org.amdoige.cashtrack.core.database.Movement


class HistoryMovementsAdapter :
    PagingDataAdapter<Movement, HistoryMovementsAdapter.ViewHolder>(Movement.Companion.Comparator) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.text_movement_view, parent, false) as TextView
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            bind(item, holder)
        } else {
            bindPlaceholder(holder)
        }
    }

    private fun bind(movement: Movement, holder: ViewHolder) {
        holder.textView.text = movement.toString()
    }

    private fun bindPlaceholder(holder: ViewHolder) {
        holder.textView.text = "placeholder"
    }

    class ViewHolder(val textView: TextView) : RecyclerView.ViewHolder(textView)
}