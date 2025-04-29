package com.example.note_taking_app.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.note_taking_app.data.local.NoteDao
import com.example.note_taking_app.model.Note
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NoteRepository @Inject constructor(
    private val noteDao: NoteDao
) : INoteRepository {

    override fun getAllNotes(): LiveData<List<Note>> = noteDao.getAllNotes()

    override suspend fun insert(note: Note) = noteDao.insert(note)

    override suspend fun delete(note: Note) = noteDao.delete(note)

    override suspend fun update(note: Note) = noteDao.update(note)

    override fun getNoteById(id: Int): LiveData<Note?> = noteDao.getNoteById(id)
}
/*

@Singleton
class NoteRepository @Inject constructor(private val noteDao: NoteDao) {

    fun getAllNotes(): LiveData<List<Note>> {
        return noteDao.getAllNotes()
    }

    // Insert a note
    suspend fun insert(note: Note) {
        noteDao.insert(note)
    }

    // Delete a note
    suspend fun delete(note: Note) {
        noteDao.delete(note)
    }

    // Update a note
    suspend fun update(note: Note) {
        noteDao.update(note)
    }

    // Get a note by ID
    fun getNoteById(id: Int): LiveData<Note?> {
        return noteDao.getNoteById(id)
    }
}
 */