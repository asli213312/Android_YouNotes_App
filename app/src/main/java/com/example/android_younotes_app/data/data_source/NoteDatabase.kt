package com.example.android_younotes_app.data.data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.android_younotes_app.domain.models.Note

@Database(
    entities = [Note::class],
    version = 1
)
abstract class NoteDatabase() : RoomDatabase() {

    abstract val dao: NoteDao

    companion object {
        const val DATABASE_NAME = "notes.db"
    }
}