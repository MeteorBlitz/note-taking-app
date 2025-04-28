package com.example.note_taking_app.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.note_taking_app.R
import com.example.note_taking_app.databinding.ActivityMainBinding
import com.example.note_taking_app.ui.addnote.AddNoteActivity
import com.example.note_taking_app.ui.edit.EditNoteActivity
import com.example.note_taking_app.utils.setStatusBarColorCompat
import dagger.hilt.android.AndroidEntryPoint
import androidx.core.content.edit

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val noteViewModel: NoteViewModel by viewModels()
    private lateinit var adapter: NoteAdapter
    private lateinit var binding: ActivityMainBinding  // Declare binding variable
    var isDarkTheme = false  // Default Light mode

    override fun onCreate(savedInstanceState: Bundle?) {
        //WindowCompat.setDecorFitsSystemWindows(window, true)
        super.onCreate(savedInstanceState)

        // Initialize ViewBinding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)  // Use the root view from ViewBinding

        // Check saved theme state and apply it
        val sharedPreferences = getSharedPreferences("app_preferences", MODE_PRIVATE)
        isDarkTheme = sharedPreferences.getBoolean("isDarkTheme", false) // Default to light mode

        // Apply the initial theme
        if (isDarkTheme) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        // Set up toolbar
        setSupportActionBar(binding.topAppBar)

        setStatusBarColorCompat(R.color.colorPrimaryLight)


        // Set up RecyclerView with View Binding
        binding.rvNotes.layoutManager = LinearLayoutManager(this)

        noteViewModel.allNotes.observe(this, Observer {
            if (it.isEmpty()) {
                binding.emptyText.visibility = View.VISIBLE
                binding.rvNotes.visibility = View.GONE
            } else {
                binding.emptyText.visibility = View.GONE
                binding.rvNotes.visibility = View.VISIBLE
                adapter = NoteAdapter(it ,{ note ->
                    noteViewModel.deleteNote(note)
                },{
                    val intent = Intent(this, EditNoteActivity::class.java)
                    intent.putExtra("NOTE_ID", it.id)
                    startActivity(intent)
                })
                binding.rvNotes.adapter = adapter
            }
        })
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