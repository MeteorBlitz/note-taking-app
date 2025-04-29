package com.example.note_taking_app

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.note_taking_app.data.repository.NoteRepository
import com.example.note_taking_app.model.Note
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class NoteRepositoryTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var fakeDao: FakeNoteDao
    private lateinit var repository: NoteRepository

    @Before
    fun setup() {
        fakeDao = FakeNoteDao()
        repository = NoteRepository(fakeDao)
    }

    @Test
    fun `insert note updates list`() = runTest {
        val note = Note(id = 1, title = "Test", content = "Test Content", timestamp = System.currentTimeMillis())
        repository.insert(note)

        val allNotes = repository.getAllNotes().getOrAwaitValue()
        assertTrue(allNotes.contains(note))
    }

    @Test
    fun `delete note removes from list`() = runTest {
        val note = Note(id = 1, title = "Test", content = "Test Content", timestamp = System.currentTimeMillis())
        repository.insert(note)
        repository.delete(note)

        val allNotes = repository.getAllNotes().getOrAwaitValue()
        assertFalse(allNotes.contains(note))
    }

    @Test
    fun `update note modifies data`() = runTest {
        val note = Note(id = 1, title = "Old Title", content = "Old Content", timestamp = System.currentTimeMillis())
        repository.insert(note)

        val updatedNote = note.copy(title = "New Title")
        repository.update(updatedNote)

        val allNotes = repository.getAllNotes().getOrAwaitValue()
        assertEquals("New Title", allNotes.find { it.id == 1 }?.title)
    }


}