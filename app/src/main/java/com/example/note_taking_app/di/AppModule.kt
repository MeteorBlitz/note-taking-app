package com.example.note_taking_app.di

import android.content.Context
import androidx.room.Room
import com.example.note_taking_app.data.local.NoteDao
import com.example.note_taking_app.data.local.NoteDatabase
import com.example.note_taking_app.data.repository.INoteRepository
import com.example.note_taking_app.data.repository.NoteRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideNoteDatabase(@ApplicationContext context: Context): NoteDatabase {
        return Room.databaseBuilder(
            context,
            NoteDatabase::class.java,
            "note_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideNoteDao(noteDatabase: NoteDatabase): NoteDao {
        return noteDatabase.noteDao()
    }

    // Provide the NoteRepository as an INoteRepository
    @Provides
    @Singleton
    fun provideNoteRepository(noteDao: NoteDao): INoteRepository {
        return NoteRepository(noteDao) // Return your NoteRepository
    }
}