package com.example.note_taking_app.ui.addnote

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.note_taking_app.R
import com.example.note_taking_app.databinding.ActivityAddNoteBinding
import com.example.note_taking_app.model.Note
import com.example.note_taking_app.ui.main.NoteViewModel

class AddNoteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddNoteBinding
    private lateinit var noteViewModel: NoteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize ViewBinding
        binding = ActivityAddNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get the NoteViewModel
        noteViewModel = ViewModelProvider(this)[NoteViewModel::class.java]

        // Set up Save Button Click Listener
        binding.btnSave.setOnClickListener {
            val title = binding.etTitle.text.toString()
            val content = binding.etContent.text.toString()

            if (title.isNotBlank() && content.isNotBlank()) {
                // Create a new note
                val newNote = Note(
                    title = title,
                    content = content
                )

                // Insert the new note into the database using ViewModel
                noteViewModel.addNote(newNote)

                // Finish the activity and return to the previous screen
                finish()
            } else {
                Toast.makeText(this, "Please enter both title and content", Toast.LENGTH_SHORT).show()
            }
        }
    }
}