package com.example.note_taking_app

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.note_taking_app.data.repository.INoteRepository
import com.example.note_taking_app.model.Note
import com.example.note_taking_app.utils.Resource

class FakeNoteRepository : INoteRepository {

    private val notesList = mutableListOf<Note>()
    private val notesLiveData = MutableLiveData<List<Note>>(notesList)

    override fun getAllNotes(): LiveData<List<Note>> {
        return notesLiveData
    }

    override suspend fun insert(note: Note): Resource<Unit> {
        return try {
            notesList.add(note)
            notesLiveData.postValue(notesList)
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

    override suspend fun delete(note: Note): Resource<Unit> {
        return try {
            notesList.remove(note)
            notesLiveData.postValue(notesList)
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

    override suspend fun update(note: Note): Resource<Unit> {
        return try {
            val index = notesList.indexOfFirst { it.id == note.id }
            if (index != -1) {
                notesList[index] = note
                notesLiveData.postValue(notesList)
                Resource.Success(Unit)
            } else {
                Resource.Error(Exception("Note not found"))
            }
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

    override fun getNoteById(id: Int): LiveData<Note?> {
        val note = notesList.find { it.id == id }
        val result = MutableLiveData<Note?>()
        result.value = note
        return result
    }

}
