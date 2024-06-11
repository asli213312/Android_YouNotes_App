package com.example.android_younotes_app.data.data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.android_younotes_app.domain.models.Note

@Database(
    entities = [Note::class],
    version = 1
)
abstract class NoteDatabase() : RoomDatabase() {

    abstract val dao: NoteDao

    companion object {
        const val DATABASE_NAME = "notes.db"

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "CREATE TABLE IF NOT EXISTS Note_temp (" +
                            "lastChanged INTEGER NOT NULL, " +
                            "timeCreated INTEGER NOT NULL, " +
                            "title TEXT NOT NULL, " +
                            "tag TEXT," +
                            "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                            "content TEXT NOT NULL, " +
                            "isPinned TEXT" +
                            ")"
                )

                db.execSQL(
                    "INSERT INTO Note_temp (id, title, content, lastChanged, timeCreated) " +
                            "SELECT id, title, content, lastChanged, timeCreated FROM Note"
                )

                // Удаляем старую таблицу
                db.execSQL("DROP TABLE Note")

                // Переименовываем новую таблицу обратно в Note
                db.execSQL("ALTER TABLE Note_temp RENAME TO Note")
            }
        }
    }
}