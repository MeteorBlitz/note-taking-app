package com.example.note_taking_app.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.note_taking_app.databinding.ItemNoteBinding
import com.example.note_taking_app.model.Note
import com.example.note_taking_app.utils.formatTimestamp

class NoteAdapter(private val notes: List<Note>,
                  private val onDelete: (Note) -> Unit,
                  private val onEditClick: (Note) -> Unit)
    : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val binding = ItemNoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = notes[position]
        holder.bind(note)
    }

    override fun getItemCount(): Int = notes.size

    inner class NoteViewHolder(private val binding: ItemNoteBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(note: Note) {
            binding.noteTitle.text = note.title
            binding.noteContent.text = note.content
            val formattedTimestamp = formatTimestamp(note.timestamp)
            binding.noteTimestamp.text = formattedTimestamp
            binding.noteDelete.setOnClickListener {
                onDelete(note) // Handle delete on click
            }
            binding.noteEdit.setOnClickListener { onEditClick(note) }
        }
    }
}