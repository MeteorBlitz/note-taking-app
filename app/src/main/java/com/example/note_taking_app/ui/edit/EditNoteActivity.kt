package com.example.note_taking_app.ui.edit

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.note_taking_app.R
import com.example.note_taking_app.databinding.ActivityEditNoteBinding
import com.example.note_taking_app.model.Note
import com.example.note_taking_app.ui.main.NoteViewModel
import kotlin.getValue

class EditNoteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditNoteBinding
    private val noteViewModel: NoteViewModel by viewModels()
    private var noteId: Long = -1  // To hold the ID of the note to edit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // Get the note ID passed from the previous screen
        noteId = intent.getLongExtra("NOTE_ID", -1)

        if (noteId != -1L) {
            // Fetch the note from the database and display it for editing
            noteViewModel.getNoteById(noteId).observe(this) { note ->
                binding.etTitle.setText(note?.title)
                binding.etDescription.setText(note?.content)
            }
        }

        binding.btnSave.setOnClickListener {
            val title = binding.etTitle.text.toString()
            val description = binding.etDescription.text.toString()

            if (title.isNotEmpty() && description.isNotEmpty()) {
                val updatedNote = Note(id = noteId.toInt(), title = title, content = description)
                noteViewModel.updateNote(updatedNote)
                Toast.makeText(this, "Note updated!", Toast.LENGTH_SHORT).show()
                finish()  // Close the activity and go back to the main screen
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }
}