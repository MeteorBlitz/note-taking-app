package com.example.note_taking_app.ui.edit

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.note_taking_app.R
import com.example.note_taking_app.databinding.ActivityEditNoteBinding
import com.example.note_taking_app.model.Note
import com.example.note_taking_app.ui.main.NoteViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditNoteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditNoteBinding
    private val noteViewModel: NoteViewModel by viewModels()
    private var noteId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Toolbar setup
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            title = "Edit Note"
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
        binding.toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white))

        // Get note ID from intent
        noteId = intent.getIntExtra("NOTE_ID", -1)

        if (noteId == -1) {
            Toast.makeText(this, "Error: Invalid note ID", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Show loading while fetching
        binding.progressBar.visibility = View.VISIBLE
        binding.btnSave.isEnabled = false

        // Load note data
        noteViewModel.getNoteById(noteId).observe(this) { note ->
            binding.progressBar.visibility = View.GONE
            binding.btnSave.isEnabled = true

            if (note == null) {
                Toast.makeText(this, "Note not found", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                binding.etTitle.setText(note.title)
                binding.etDescription.setText(note.content)
            }
        }

        // Save logic
        binding.btnSave.setOnClickListener {
            val title = binding.etTitle.text.toString().trim()
            val content = binding.etDescription.text.toString().trim()

            if (title.isBlank()) {
                binding.etTitle.error = "Title required"
                binding.etTitle.requestFocus()
                return@setOnClickListener
            }

            if (content.isBlank()) {
                binding.etDescription.error = "Content required"
                binding.etDescription.requestFocus()
                return@setOnClickListener
            }

            val updatedNote = Note(id = noteId, title = title, content = content)
            noteViewModel.updateNote(updatedNote)
            Toast.makeText(this, "Note updated!", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressedDispatcher.onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
