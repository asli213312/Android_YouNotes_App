package com.example.android_younotes_app.di

import android.app.Application
import androidx.room.Room
import com.example.android_younotes_app.data.data_source.NoteDatabase
import com.example.android_younotes_app.data.repository.NoteRepositoryImpl
import com.example.android_younotes_app.domain.repository.NoteRepository
import com.example.android_younotes_app.domain.use_cases.AddNote
import com.example.android_younotes_app.domain.use_cases.BookmarkNote
import com.example.android_younotes_app.domain.use_cases.GetNotes
import com.example.android_younotes_app.domain.use_cases.NoteUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesNoteDatabase(app: Application): NoteDatabase {
        return Room.databaseBuilder(
            app,
            NoteDatabase::class.java,
            NoteDatabase.DATABASE_NAME,
        ).build()
    }

    @Provides
    @Singleton
    fun providesNoteRepository(db: NoteDatabase): NoteRepository {
        return NoteRepositoryImpl(db.dao)
    }

    @Provides
    @Singleton
    fun providesNoteUseCases(repository: NoteRepository): NoteUseCases {
        return NoteUseCases(
            addNote = AddNote(repository),
            bookmarkNote = BookmarkNote(repository),
            getNotes = GetNotes(repository)
        )
    }
}