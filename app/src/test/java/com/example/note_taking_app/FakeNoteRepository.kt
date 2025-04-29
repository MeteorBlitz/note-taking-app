package com.example.note_taking_app

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.note_taking_app.data.repository.INoteRepository
import com.example.note_taking_app.model.Note

class FakeNoteRepository : INoteRepository {

    private val notesList = mutableListOf<Note>()
    private val notesLiveData = MutableLiveData<List<Note>>(notesList)

    override fun getAllNotes(): LiveData<List<Note>> {
        return notesLiveData
    }

    override suspend fun insert(note: Note) {
        notesList.add(note)
        notesLiveData.postValue(notesList)
    }

    override suspend fun delete(note: Note) {
        notesList.remove(note)
        notesLiveData.postValue(notesList)
    }

    override suspend fun update(note: Note) {
        val index = notesList.indexOfFirst { it.id == note.id }
        if (index != -1) {
            notesList[index] = note
            notesLiveData.postValue(notesList)
        }
    }

    override fun getNoteById(id: Int): LiveData<Note?> {
        val note = notesList.find { it.id == id }
        val result = MutableLiveData<Note?>()
        result.value = note
        return result
    }

}
