package com.example.note_taking_app.data.repository

import androidx.lifecycle.LiveData
import com.example.note_taking_app.model.Note
import com.example.note_taking_app.utils.Resource

interface INoteRepository {
    fun getAllNotes(): LiveData<List<Note>>
    suspend fun insert(note: Note) : Resource<Unit>
    suspend fun delete(note: Note) : Resource<Unit>
    suspend fun update(note: Note) : Resource<Unit>
    fun getNoteById(id: Int): LiveData<Note?>
}