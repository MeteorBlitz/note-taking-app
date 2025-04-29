package com.example.note_taking_app.ui.addnote

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.example.note_taking_app.R
import com.example.note_taking_app.databinding.ActivityAddNoteBinding
import com.example.note_taking_app.model.Note
import com.example.note_taking_app.ui.main.NoteViewModel
import com.example.note_taking_app.utils.Resource
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
            val title = binding.etTitle.text.toString().trim()
            val content = binding.etContent.text.toString().trim()

            // Validation for empty fields
            when {
                title.isBlank() -> {
                    Toast.makeText(this, "Title cannot be empty", Toast.LENGTH_SHORT).show()
                }
                content.isBlank() -> {
                    Toast.makeText(this, "Content cannot be empty", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    // Create a new note
                    val newNote = Note(
                        title = title,
                        content = content
                    )

                    // Insert the new note into the database using ViewModel
                    noteViewModel.addNote(newNote)

                    // Observe operation status to handle success or error
                    noteViewModel.operationStatus.observe(this, Observer { resource ->
                        when (resource) {
                            is Resource.Loading -> {
                                // Optionally show a loading indicator
                                binding.progressBar.visibility = View.VISIBLE
                            }
                            is Resource.Success -> {
                                // Successfully added the note
                                binding.progressBar.visibility = View.GONE
                                Toast.makeText(this, "Note added successfully", Toast.LENGTH_SHORT).show()
                                finish() // Close the activity
                            }
                            is Resource.Error -> {
                                // Handle error
                                binding.progressBar.visibility = View.GONE
                                Toast.makeText(this, "Error adding note: ${resource.exception.message}", Toast.LENGTH_SHORT).show()
                            }
                        }
                    })
                }
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

