package com.example.note_taking_app.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.content.edit
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.note_taking_app.R
import com.example.note_taking_app.databinding.ActivityMainBinding
import com.example.note_taking_app.ui.addnote.AddNoteActivity
import com.example.note_taking_app.ui.edit.EditNoteActivity
import com.example.note_taking_app.utils.Resource
import com.example.note_taking_app.utils.SwipeToDeleteCallback
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val noteViewModel: NoteViewModel by viewModels()
    private lateinit var adapter: NoteAdapter
    private lateinit var binding: ActivityMainBinding
    var isDarkTheme = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize ViewBinding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Check saved theme state and apply it
        val sharedPreferences = getSharedPreferences("app_preferences", MODE_PRIVATE)
        isDarkTheme = sharedPreferences.getBoolean("isDarkTheme", false)

        // Apply the initial theme
        if (isDarkTheme) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        // Set up toolbar
        setSupportActionBar(binding.toolbar)
        // Set title and toolbar appearance
        supportActionBar?.title = "Add Note"
        binding.toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white))

        // Set up RecyclerView with View Binding
        binding.rvNotes.layoutManager = LinearLayoutManager(this)

        // Initialize the RecyclerView adapter (only once)
        adapter = NoteAdapter(
            { note -> noteViewModel.deleteNote(note) },
            { note ->
                val intent = Intent(this, EditNoteActivity::class.java)
                intent.putExtra("NOTE_ID", note.id)
                startActivity(intent)
            }
        )

        // Set the adapter to RecyclerView
        binding.rvNotes.adapter = adapter

        // Add swipe-to-delete using ItemTouchHelper
        val swipeCallback = SwipeToDeleteCallback(this, adapter, noteViewModel, binding.rvNotes)
        ItemTouchHelper(swipeCallback).attachToRecyclerView(binding.rvNotes)

        // Observe the LiveData for changes in the notes list
        noteViewModel.notes.observe(this, Observer { resource ->
            when (resource) {
                is Resource.Loading -> {
                    // Show loading indicator
                    Log.d("MainActivity", "Loading notes...")
                    binding.progressBar.visibility = View.VISIBLE
                    binding.rvNotes.visibility = View.GONE
                    binding.emptyText.visibility = View.GONE
                }
                is Resource.Success -> {
                    // Hide progress bar and show notes
                    binding.progressBar.visibility = View.GONE
                    val noteList = resource.data

                    if (noteList.isEmpty()) {
                        binding.emptyText.visibility = View.VISIBLE
                        binding.rvNotes.visibility = View.GONE
                    } else {
                        binding.emptyText.visibility = View.GONE
                        binding.rvNotes.visibility = View.VISIBLE
                        // Update the adapter with the new list of notes
                        adapter.submitList(noteList)  // Use submitList() for efficient updates
                    }
                }
                is Resource.Error -> {
                    // Show error message
                    Log.e("MainActivity", "Error loading notes: ${resource.exception.message}")
                    binding.progressBar.visibility = View.GONE
                    binding.emptyText.visibility = View.VISIBLE
                    binding.emptyText.text = "Error loading notes: ${resource.exception.message}"
                    binding.rvNotes.visibility = View.GONE
                }
            }
        })

        // Fetch notes on start (if needed)
        noteViewModel.fetchAllNotes()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

        // Set the initial icon based on the current theme
        val currentTheme = AppCompatDelegate.getDefaultNightMode()
        val themeIcon = if (currentTheme == AppCompatDelegate.MODE_NIGHT_YES) {
            R.drawable.ic_sun  // Set the sun icon for dark mode
        } else {
            R.drawable.ic_dark_mode  // Set the moon icon for light mode
        }

        menu?.findItem(R.id.action_toggle_theme)?.setIcon(themeIcon) // Set the icon dynamically

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_add -> {
                // Start AddNoteActivity to add a new note
                val intent = Intent(this, AddNoteActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.action_toggle_theme -> {
                toggleTheme(item) // Toggle the theme and update the icon
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    // Toggle the theme and store it in SharedPreferences
    private fun toggleTheme(item: MenuItem) {
        // Toggle the dark/light mode
        isDarkTheme = !isDarkTheme

        // Save the theme state to SharedPreferences
        val sharedPreferences = getSharedPreferences("app_preferences", MODE_PRIVATE)
        sharedPreferences.edit() { putBoolean("isDarkTheme", isDarkTheme) }

        // Apply the theme change immediately
        if (isDarkTheme) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            item.setIcon(R.drawable.ic_sun) // Change icon to sun (light mode icon)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            item.setIcon(R.drawable.ic_dark_mode) // Change icon to moon (dark mode icon)
        }
    }
}