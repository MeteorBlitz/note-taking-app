package com.example.note_taking_app.ui.addnote

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.note_taking_app.R
import com.example.note_taking_app.databinding.ActivityAddNoteBinding
import com.example.note_taking_app.model.Note
import com.example.note_taking_app.ui.main.NoteViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddNoteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddNoteBinding
    // Get the NoteViewModel
    private val noteViewModel: NoteViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize ViewBinding
        binding = ActivityAddNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up toolbar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "Add Note"
        binding.toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white))

        // Enable the Up button (back arrow)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)


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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                // Handle the back button press using the new method
                onBackPressedDispatcher.onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
