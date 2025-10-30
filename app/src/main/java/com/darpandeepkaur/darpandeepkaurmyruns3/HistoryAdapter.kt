package com.darpandeepkaur.darpandeepkaurmyruns3

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.darpandeepkaur.darpandeepkaurmyruns3.database.ExerciseEntry

class HistoryAdapter(
    private var entries: List<ExerciseEntry>,
    private val itemClick: (ExerciseEntry) -> Unit
) : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    inner class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val summaryText: TextView = itemView.findViewById(R.id.summaryText)

        fun bind(entry: ExerciseEntry) {
            summaryText.text = "Activity: ${entry.activityType}, Duration: ${entry.duration}, Distance: ${entry.distance}"
            itemView.setOnClickListener { itemClick(entry) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.history_list_item, parent, false)
        return HistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(entries[position])
    }

    override fun getItemCount(): Int = entries.size

    fun updateData(newEntries: List<ExerciseEntry>) {
        entries = newEntries
        notifyDataSetChanged()
    }
}