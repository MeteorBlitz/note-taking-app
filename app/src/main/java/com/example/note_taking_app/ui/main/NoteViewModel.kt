package com.example.note_taking_app.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.note_taking_app.data.repository.NoteRepository
import com.example.note_taking_app.model.Note
import com.example.note_taking_app.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(private val repository: NoteRepository) : ViewModel() {

    // LiveData wrapped in Resource to handle loading, success, and error states
    private val _notes = MutableLiveData<Resource<List<Note>>>()
    val notes: LiveData<Resource<List<Note>>> = _notes

    init {
        fetchAllNotes()
    }

    fun fetchAllNotes() {
        _notes.value = Resource.Loading()
        viewModelScope.launch {
            try {
                val data = repository.getAllNotes()  // Ensure getAllNotes returns LiveData<List<Note>>
                data.observeForever { list ->
                    _notes.value = Resource.Success(list)
                }
            } catch (e: Exception) {
                _notes.value = Resource.Error(e)
            }
        }
    }

    // Add a new note
    fun addNote(note: Note) {
        viewModelScope.launch {
            repository.insert(note) // Insert note in the database
            fetchAllNotes() // Re-fetch the notes after adding
        }
    }

    // Delete a note
    fun deleteNote(note: Note) {
        viewModelScope.launch {
            repository.delete(note) // Delete the note from the database
            fetchAllNotes() // Re-fetch the notes after deletion
        }
    }

    // Update a note
    fun updateNote(note: Note) {
        viewModelScope.launch {
            repository.update(note) // Update the note in the database
            fetchAllNotes() // Re-fetch the notes after update
        }
    }

    // Fetch a note by ID
    fun getNoteById(id: Int): LiveData<Note?> {
        return repository.getNoteById(id)
    }
}