package com.example.note_taking_app.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.note_taking_app.R
import com.example.note_taking_app.databinding.ActivityMainBinding
import com.example.note_taking_app.ui.addnote.AddNoteActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val noteViewModel: NoteViewModel by viewModels()
    private lateinit var adapter: NoteAdapter
    private lateinit var binding: ActivityMainBinding  // Declare binding variable

    override fun onCreate(savedInstanceState: Bundle?) {
        //WindowCompat.setDecorFitsSystemWindows(window, true)
        super.onCreate(savedInstanceState)

        // Initialize ViewBinding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)  // Use the root view from ViewBinding

        // Set up toolbar
        setSupportActionBar(binding.topAppBar)


        // Set up RecyclerView with View Binding
        binding.rvNotes.layoutManager = LinearLayoutManager(this)

        noteViewModel.allNotes.observe(this, Observer {
            adapter = NoteAdapter(it) { note ->
                noteViewModel.deleteNote(note)
            }
            binding.rvNotes.adapter = adapter  // Use binding to set the adapter
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_add -> {
                // Start AddNoteActivity to add a new note
                val intent = Intent(this, AddNoteActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}