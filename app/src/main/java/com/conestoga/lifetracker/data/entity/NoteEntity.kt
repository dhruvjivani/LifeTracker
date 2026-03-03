package com.conestoga.lifetracker.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entity class representing a Note in the database.
 * Used for SQLite persistence via Room ORM.
 */
@Entity(tableName = "notes")
data class NoteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val createdAt: Long = System.currentTimeMillis()
)
