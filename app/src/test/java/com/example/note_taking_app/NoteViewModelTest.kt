package com.example.note_taking_app

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.note_taking_app.model.Note
import com.example.note_taking_app.ui.main.NoteViewModel
import com.example.note_taking_app.utils.Resource
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class NoteViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: NoteViewModel
    private lateinit var fakeRepository: FakeNoteRepository
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        fakeRepository = FakeNoteRepository()
        viewModel = NoteViewModel(fakeRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `add note updates live data`() = runTest {
        val note = Note(id = 1, title = "Test Title", content = "Test Content", timestamp = System.currentTimeMillis())
        viewModel.addNote(note)

        advanceUntilIdle() // Important: wait for coroutine to finish

        val successResult = viewModel.notes.value as? Resource.Success<List<Note>>
        val data = successResult?.data

        val foundNote = data?.find { it.id == 1 }
        assertTrue(foundNote?.title == "Test Title")
    }

    @Test
    fun `delete note updates live data`() = runTest {
        val note = Note(id = 2, title = "Delete Me", content = "To be deleted", timestamp = System.currentTimeMillis())
        viewModel.addNote(note)
        advanceUntilIdle()

        viewModel.deleteNote(note)
        advanceUntilIdle()

        val successResult = viewModel.notes.value as? Resource.Success<List<Note>>
        val data = successResult?.data

        val foundNote = data?.find { it.id == 2 }
        assertNull(foundNote) // Note should no longer exist
    }

    @Test
    fun `update note updates live data`() = runTest {
        val note = Note(id = 3, title = "Old Title", content = "Old", timestamp = System.currentTimeMillis())
        viewModel.addNote(note)
        advanceUntilIdle()

        val updatedNote = note.copy(title = "New Title")
        viewModel.updateNote(updatedNote)
        advanceUntilIdle()

        val successResult = viewModel.notes.value as? Resource.Success<List<Note>>
        val data = successResult?.data

        val foundNote = data?.find { it.id == 3 }
        assertEquals("New Title", foundNote?.title)
    }
}