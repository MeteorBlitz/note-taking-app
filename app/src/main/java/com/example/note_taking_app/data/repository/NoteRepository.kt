package com.example.note_taking_app.data.repository

import androidx.lifecycle.LiveData
import com.example.note_taking_app.data.local.NoteDao
import com.example.note_taking_app.model.Note
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NoteRepository @Inject constructor(private val noteDao: NoteDao) {

    val allNotes: LiveData<List<Note>> = noteDao.getAllNotes()

    suspend fun insert(note: Note) {
        noteDao.insert(note)
    }

    suspend fun delete(note: Note) {
        noteDao.delete(note)
    }

    // Update
    suspend fun update(note: Note) {
        noteDao.update(note)
    }

    // Fetch a note by ID
    fun getNoteById(id: Int): LiveData<Note?> {
        return noteDao.getNoteById(id)
    }
}