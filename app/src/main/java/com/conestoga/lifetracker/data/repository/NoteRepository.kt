package com.conestoga.lifetracker.data.repository

import com.conestoga.lifetracker.data.dao.NoteDao
import com.conestoga.lifetracker.data.entity.NoteEntity
import com.conestoga.lifetracker.domain.model.Note

/**
 * Repository for managing Note operations.
 * Acts as a data abstraction layer between the domain and data layers.
 */
class NoteRepository(private val noteDao: NoteDao) {
    
    /**
     * Insert a new note.
     *
     * @param note The note to insert
     * @return The ID of the inserted note
     */
    suspend fun insertNote(note: Note): Long {
        val entity = NoteEntity(title = note.title, createdAt = note.createdAt)
        return noteDao.insertNote(entity)
    }

    /**
     * Update an existing note.
     *
     * @param note The note to update
     * @return Number of rows affected
     */
    suspend fun updateNote(note: Note): Int {
        val entity = NoteEntity(id = note.id, title = note.title, createdAt = note.createdAt)
        return noteDao.updateNote(entity)
    }

    /**
     * Delete a note by ID.
     *
     * @param id The ID of the note to delete
     * @return Number of rows affected
     */
    suspend fun deleteNoteById(id: Int): Int {
        return noteDao.deleteNoteById(id)
    }

    /**
     * Get a note by ID.
     *
     * @param id The ID of the note
     * @return The note if found, null otherwise
     */
    suspend fun getNoteById(id: Int): Note? {
        val entity = noteDao.getNoteById(id)
        return entity?.let { 
            Note(id = it.id, title = it.title, createdAt = it.createdAt) 
        }
    }

    /**
     * Get all notes.
     *
     * @return List of all notes
     */
    suspend fun getAllNotes(): List<Note> {
        return noteDao.getAllNotes().map {
            Note(id = it.id, title = it.title, createdAt = it.createdAt)
        }
    }
}
