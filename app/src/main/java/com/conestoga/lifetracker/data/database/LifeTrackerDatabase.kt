package com.conestoga.lifetracker.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.conestoga.lifetracker.data.dao.NoteDao
import com.conestoga.lifetracker.data.entity.NoteEntity

/**
 * Room database class for LifeTracker application.
 * Manages SQLite database operations through Room ORM.
 */
@Database(entities = [NoteEntity::class], version = 1, exportSchema = false)
abstract class LifeTrackerDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao

    companion object {
        @Volatile
        private var INSTANCE: LifeTrackerDatabase? = null

        /**
         * Get singleton instance of the database.
         *
         * @param context Application context
         * @return LifeTrackerDatabase instance
         */
        fun getInstance(context: Context): LifeTrackerDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LifeTrackerDatabase::class.java,
                    "lifetracker_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
