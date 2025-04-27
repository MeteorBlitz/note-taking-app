package com.example.note_taking_app.ui.main

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.note_taking_app.R
import com.example.note_taking_app.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var noteViewModel: NoteViewModel
    private lateinit var adapter: NoteAdapter
    private lateinit var binding: ActivityMainBinding  // Declare binding variable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize ViewBinding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)  // Use the root view from ViewBinding

        noteViewModel = ViewModelProvider(this).get(NoteViewModel::class.java)

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
                // Open activity to add a note (this can be an Intent to a new screen)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}