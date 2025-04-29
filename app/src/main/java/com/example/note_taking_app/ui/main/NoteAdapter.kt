package com.example.note_taking_app.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.note_taking_app.databinding.ItemNoteBinding
import com.example.note_taking_app.model.Note
import com.example.note_taking_app.utils.formatTimestamp

class NoteAdapter(
    private val onDelete: (Note) -> Unit,
    private val onEdit: (Note) -> Unit
) : ListAdapter<Note, NoteAdapter.NoteViewHolder>(NoteDiffCallback()) {

    // ViewHolder setup
    inner class NoteViewHolder(private val binding: ItemNoteBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(note: Note) {
            binding.noteTitle.text = note.title
            binding.noteContent.text = note.content
            val formattedTimestamp = formatTimestamp(note.timestamp)
            binding.noteTimestamp.text = formattedTimestamp

            binding.noteDelete.setOnClickListener {
                onDelete(note)
            }

            binding.noteEdit.setOnClickListener {
                onEdit(note)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val binding = ItemNoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = getItem(position)
        holder.bind(note)
    }

    // DiffUtil Callback for efficient list comparison
    class NoteDiffCallback : DiffUtil.ItemCallback<Note>() {
        override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem.id == newItem.id // Assumes Note has a unique id
        }

        override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem == newItem
        }
    }
}