package com.example.android_younotes_app.data.data_source

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.android_younotes_app.domain.models.Note
import com.example.android_younotes_app.domain.models.NoteCategory
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Query("SELECT * FROM note")
    fun getNotes(): Flow<List<Note>>

    @Query("SELECT * FROM note WHERE id = :id")
    suspend fun getNoteById(id: Int): Note?

    @Query("UPDATE Note SET isPinned = :isPinned WHERE id = :noteId")
    suspend fun bookmarkNote(noteId: Int, isPinned: Boolean)

    @Query("UPDATE Note SET isDeleted = :isDeleted WHERE id = :noteId")
    suspend fun deleteNoteInThrash(noteId: Int, isDeleted: Boolean)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)
}