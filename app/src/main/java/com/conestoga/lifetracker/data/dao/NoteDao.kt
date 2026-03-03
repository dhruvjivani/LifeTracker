package com.conestoga.lifetracker.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.conestoga.lifetracker.data.entity.NoteEntity

/**
 * Data Access Object (DAO) for Note operations.
 * Provides database operation methods for notes.
 */
@Dao
interface NoteDao {
    /**
     * Insert a new note into the database.
     *
     * @param note The note to insert
     * @return The ID of the inserted note
     */
    @Insert
    suspend fun insertNote(note: NoteEntity): Long

    /**
     * Update an existing note.
     *
     * @param note The note to update
     * @return Number of rows affected
     */
    @Update
    suspend fun updateNote(note: NoteEntity): Int

    /**
     * Delete a note by ID.
     *
     * @param id The ID of the note to delete
     * @return Number of rows affected
     */
    @Query("DELETE FROM notes WHERE id = :id")
    suspend fun deleteNoteById(id: Int): Int

    /**
     * Get a note by ID.
     *
     * @param id The ID of the note
     * @return The note if found, null otherwise
     */
    @Query("SELECT * FROM notes WHERE id = :id")
    suspend fun getNoteById(id: Int): NoteEntity?

    /**
     * Get all notes ordered by creation date (newest first).
     *
     * @return List of all notes
     */
    @Query("SELECT * FROM notes ORDER BY createdAt DESC")
    suspend fun getAllNotes(): List<NoteEntity>

    /**
     * Delete a note.
     *
     * @param note The note to delete
     * @return Number of rows affected
     */
    @Delete
    suspend fun deleteNote(note: NoteEntity): Int
}
