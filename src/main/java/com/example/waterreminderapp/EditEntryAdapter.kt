package com.example.waterreminderapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class EditEntryAdapter(
    private val onEditClicked: (WaterIntake) -> Unit,
    private val onDeleteClicked: (WaterIntake) -> Unit
) : ListAdapter<WaterIntake, EditEntryAdapter.ViewHolder>(WaterIntakeDiffCallback()) {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val tvTime: TextView = view.findViewById(R.id.tvTime)
        private val tvAmount: TextView = view.findViewById(R.id.tvAmount)
        private val btnEdit: ImageButton = view.findViewById(R.id.btnEdit)
        private val btnDelete: ImageButton = view.findViewById(R.id.btnDelete)

        fun bind(item: WaterIntake, onEdit: (WaterIntake) -> Unit, onDelete: (WaterIntake) -> Unit) {
            tvTime.text = item.date.substringAfter(" ")
            tvAmount.text = "${item.amount} ml"
            btnEdit.setOnClickListener { onEdit(item) }
            btnDelete.setOnClickListener { onDelete(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_entry, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, onEditClicked, onDeleteClicked)
    }
}

class WaterIntakeDiffCallback : DiffUtil.ItemCallback<WaterIntake>() {
    override fun areItemsTheSame(oldItem: WaterIntake, newItem: WaterIntake): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: WaterIntake, newItem: WaterIntake): Boolean {
        return oldItem == newItem
    }
}