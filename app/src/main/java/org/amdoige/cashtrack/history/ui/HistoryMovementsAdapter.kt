package org.amdoige.cashtrack.history.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.amdoige.cashtrack.R
import org.amdoige.cashtrack.core.database.Movement
import timber.log.Timber

class HistoryMovementsAdapter : RecyclerView.Adapter<HistoryMovementsAdapter.ViewHolder>() {

    var movements = listOf<Movement>()
        set(value) {
            Timber.i("Adapter Movement Size: ${value.size}")
            field = value
            notifyDataSetChanged() // FIXME
        }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.text_movement_view, parent, false) as TextView
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryMovementsAdapter.ViewHolder, position: Int) {
        val movement = movements[position]
        holder.textView.text = movement.toString()
    }

    override fun getItemCount(): Int = movements.size

    class ViewHolder(val textView: TextView) : RecyclerView.ViewHolder(textView) {
    }
}