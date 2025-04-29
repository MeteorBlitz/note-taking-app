package com.example.note_taking_app.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.note_taking_app.data.local.NoteDao
import com.example.note_taking_app.model.Note
import com.example.note_taking_app.utils.Resource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NoteRepository @Inject constructor(
    private val noteDao: NoteDao
) : INoteRepository {

    // LiveData remains unchanged because it's an immediate data stream
    override fun getAllNotes(): LiveData<List<Note>> = noteDao.getAllNotes()

    // Insert operation with error handling
    override suspend fun insert(note: Note): Resource<Unit> {
        return try {
            noteDao.insert(note)
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e) // Return the exception if any error occurs
        }
    }

    // Delete operation with error handling
    override suspend fun delete(note: Note): Resource<Unit> {
        return try {
            noteDao.delete(note)
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

    // Update operation with error handling
    override suspend fun update(note: Note): Resource<Unit> {
        return try {
            noteDao.update(note)
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

    // Fetch a single note by ID (no changes needed since it’s just returning LiveData)
    override fun getNoteById(id: Int): LiveData<Note?> = noteDao.getNoteById(id)
}