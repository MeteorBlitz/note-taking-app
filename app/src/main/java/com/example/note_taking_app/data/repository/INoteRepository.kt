package com.example.note_taking_app.data.repository

import androidx.lifecycle.LiveData
import com.example.note_taking_app.model.Note

interface INoteRepository {
    fun getAllNotes(): LiveData<List<Note>>
    suspend fun insert(note: Note)
    suspend fun delete(note: Note)
    suspend fun update(note: Note)
    fun getNoteById(id: Int): LiveData<Note?>
}