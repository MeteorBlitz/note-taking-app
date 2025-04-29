package com.example.note_taking_app

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.note_taking_app.data.local.NoteDao
import com.example.note_taking_app.model.Note

class FakeNoteDao : NoteDao {

    private val notes = mutableListOf<Note>()

    private val notesLiveData = MutableLiveData<List<Note>>()

    init {
        notesLiveData.value = notes
    }

    override suspend fun insert(note: Note) {
        notes.add(note)
        notesLiveData.value = notes
    }

    override suspend fun delete(note: Note) {
        notes.remove(note)
        notesLiveData.value = notes
    }

    override suspend fun update(note: Note) {
        val index = notes.indexOfFirst { it.id == note.id }
        if (index != -1) {
            notes[index] = note
        }
        notesLiveData.value = notes
    }

    override fun getAllNotes(): LiveData<List<Note>> = notesLiveData

    override fun getNoteById(id: Int): LiveData<Note?> {
        return MutableLiveData(notes.find { it.id == id })
    }

}