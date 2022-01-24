package org.amdoige.cashtrack.history.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import org.amdoige.cashtrack.R
import org.amdoige.cashtrack.core.database.Movement
import timber.log.Timber
import java.lang.IndexOutOfBoundsException

//class UserAdapter(diffCallback: DiffUtil.ItemCallback<User>) :
//    PagingDataAdapter<User, UserViewHolder>(diffCallback) {
//    override fun onCreateViewHolder(
//        parent: ViewGroup,
//        viewType: Int
//    ): UserViewHolder {
//        return UserViewHolder(parent)
//    }
//
//    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
//        val item = getItem(position)
//        // Note that item may be null. ViewHolder must support binding a
//        // null item as a placeholder.
//        holder.bind(item)
//    }
//}

//class HistoryMovementsAdapter : RecyclerView.Adapter<HistoryMovementsAdapter.ViewHolder>() {
class HistoryMovementsAdapter() :
    PagingDataAdapter<Movement, HistoryMovementsAdapter.ViewHolder>(Movement.Companion.Comparator) {

//    var movements = listOf<Movement>()
//        set(value) {
//            Timber.i("Adapter Movement Size: ${value.size}")
//            field = value
//            notifyDataSetChanged() // FIXME
//        }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.text_movement_view, parent, false) as TextView
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {
            getItem(position)?.let { movement ->
                holder.textView.text = movement.toString()
            }
        } catch (e: IndexOutOfBoundsException) {
            holder.textView.text = "null"
        }
    }

    class ViewHolder(val textView: TextView) : RecyclerView.ViewHolder(textView)
}