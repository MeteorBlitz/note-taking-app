package com.example.note_taking_app.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.note_taking_app.data.repository.INoteRepository
import com.example.note_taking_app.model.Note
import com.example.note_taking_app.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(private val repository: INoteRepository) : ViewModel() {

    // LiveData wrapped in Resource to handle loading, success, and error states
    private val _notes = MutableLiveData<Resource<List<Note>>>()
    val notes: LiveData<Resource<List<Note>>> = _notes

    private val _operationStatus = MutableLiveData<Resource<Unit>>()
    val operationStatus: LiveData<Resource<Unit>> = _operationStatus

    init {
        fetchAllNotes()
    }

    // Fetch all notes from the repository
    fun fetchAllNotes() {
        _notes.value = Resource.Loading()
        viewModelScope.launch {
            try {
                val data = repository.getAllNotes()
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
        _operationStatus.value = Resource.Loading()
        viewModelScope.launch {
            try {
                repository.insert(note) // Perform the insert
                _operationStatus.value = Resource.Success(Unit) // Indicate success with Unit
                fetchAllNotes() // Re-fetch after adding
            } catch (e: Exception) {
                _operationStatus.value = Resource.Error(e)
            }
        }
    }

    // Delete a note
    fun deleteNote(note: Note) {
        _operationStatus.value = Resource.Loading()
        viewModelScope.launch {
            try {
                repository.delete(note) // Perform the delete
                _operationStatus.value = Resource.Success(Unit) // Indicate success with Unit
                fetchAllNotes() // Re-fetch after deletion
            } catch (e: Exception) {
                _operationStatus.value = Resource.Error(e)
            }
        }
    }

    // Update a note
    fun updateNote(note: Note) {
        _operationStatus.value = Resource.Loading()
        viewModelScope.launch {
            try {
                repository.update(note) // Perform the update
                _operationStatus.value = Resource.Success(Unit) // Indicate success with Unit
                fetchAllNotes() // Re-fetch after update
            } catch (e: Exception) {
                _operationStatus.value = Resource.Error(e)
            }
        }
    }

    // Fetch a note by ID
    fun getNoteById(id: Int): LiveData<Note?> {
        return repository.getNoteById(id)
    }
}