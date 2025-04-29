package com.example.note_taking_app

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.note_taking_app.data.local.NoteDao
import com.example.note_taking_app.data.local.NoteDatabase
import com.example.note_taking_app.model.Note
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class NoteDaoTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: NoteDatabase
    private lateinit var noteDao: NoteDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(
            context,
            NoteDatabase::class.java
        ).allowMainThreadQueries().build()
        noteDao = database.noteDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertNoteSavesDataCorrectly() = runBlocking {
        val note = Note(id = 1, title = "Test Title", content = "Test Content", timestamp = System.currentTimeMillis())
        noteDao.insert(note)

        val allNotes = noteDao.getAllNotes().getOrAwaitValue()
        assertTrue(allNotes.contains(note))
    }

    @Test
    fun deleteNoteRemovesDataCorrectly() = runBlocking {
        val note = Note(id = 1, title = "Test Title", content = "Test Content", timestamp = System.currentTimeMillis())
        noteDao.insert(note)
        noteDao.delete(note)

        val allNotes = noteDao.getAllNotes().getOrAwaitValue()
        assertFalse(allNotes.contains(note))
    }

    @Test
    fun updateNoteChangesDataCorrectly() = runBlocking {
        val note = Note(id = 1, title = "Old Title", content = "Old Content", timestamp = System.currentTimeMillis())
        noteDao.insert(note)

        val updatedNote = note.copy(title = "New Title")
        noteDao.update(updatedNote)

        val allNotes = noteDao.getAllNotes().getOrAwaitValue()
        assertEquals("New Title", allNotes.find { it.id == 1 }?.title)
    }
}