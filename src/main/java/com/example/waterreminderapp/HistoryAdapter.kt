package com.example.waterreminderapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class HistoryAdapter(
    private val onItemClicked: (DailyWaterIntake) -> Unit,
    private val onDeleteClicked: (DailyWaterIntake) -> Unit
) : ListAdapter<DailyWaterIntake, HistoryAdapter.ViewHolder>(DailyWaterIntakeDiffCallback()) {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val tvDate: TextView = view.findViewById(R.id.tvDate)
        private val tvAmount: TextView = view.findViewById(R.id.tvTotalAmount)
        val btnDelete: ImageButton = view.findViewById(R.id.btnDeleteHistory)

        fun bind(item: DailyWaterIntake, onItemClicked: (DailyWaterIntake) -> Unit) {
            tvDate.text = item.date
            tvAmount.text = item.totalAmount.toString() + " ml"
            itemView.setOnClickListener { onItemClicked(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_history, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, onItemClicked)
        holder.btnDelete.setOnClickListener { onDeleteClicked(item) }
    }
}

class DailyWaterIntakeDiffCallback : DiffUtil.ItemCallback<DailyWaterIntake>() {
    override fun areItemsTheSame(oldItem: DailyWaterIntake, newItem: DailyWaterIntake): Boolean {
        return oldItem.date == newItem.date
    }

    override fun areContentsTheSame(oldItem: DailyWaterIntake, newItem: DailyWaterIntake): Boolean {
        return oldItem == newItem
    }
}