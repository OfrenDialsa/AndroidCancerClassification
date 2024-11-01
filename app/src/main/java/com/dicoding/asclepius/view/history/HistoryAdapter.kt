package com.dicoding.asclepius.view.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.asclepius.data.HistoryRepository
import com.dicoding.asclepius.data.local.entity.HistoryEntity
import com.dicoding.asclepius.databinding.ItemHistoryBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HistoryAdapter(private val repository: HistoryRepository) :
    ListAdapter<HistoryEntity, HistoryAdapter.HistoryViewHolder>(EventDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val event = getItem(position)
        holder.bind(event, repository) { deletedEvent ->
            // When an item is deleted, we can remove it from the adapter
            removeItem(deletedEvent)
        }
    }

    // Method to remove an item from the adapter
    private fun removeItem(event: HistoryEntity) {
        CoroutineScope(Dispatchers.Main).launch {
            // This assumes the item exists in the current list
            val currentList = currentList.toMutableList()
            val index = currentList.indexOf(event)
            if (index != -1) {
                currentList.removeAt(index) // Remove from the mutable list
                submitList(currentList) // Update the adapter's list
            }
        }
    }

    class HistoryViewHolder(private val binding: ItemHistoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(event: HistoryEntity, repository: HistoryRepository, onDelete: (HistoryEntity) -> Unit) {
            binding.tvHistoryResult.text = event.result
            val formattedDate = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
                .format(Date(event.timestamp)) // Convert Long to Date

            binding.tvHistoryTimestamp.text = formattedDate
            Glide.with(binding.root.context)
                .load(event.imageUri)
                .into(binding.imgItemPhoto)

            binding.ivDelete.setOnClickListener {
                // Perform delete in a coroutine scope
                CoroutineScope(Dispatchers.IO).launch {
                    repository.delete(event) // Delete from the database
                    withContext(Dispatchers.Main) {
                        onDelete(event) // Notify the adapter to remove the item
                    }
                }
            }
        }
    }

    private class EventDiffCallback : DiffUtil.ItemCallback<HistoryEntity>() {
        override fun areItemsTheSame(oldItem: HistoryEntity, newItem: HistoryEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: HistoryEntity, newItem: HistoryEntity): Boolean {
            return oldItem == newItem
        }
    }
}