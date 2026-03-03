package com.conestoga.lifetracker

import com.conestoga.lifetracker.data.dao.NoteDao
import com.conestoga.lifetracker.data.entity.NoteEntity
import com.conestoga.lifetracker.data.repository.NoteRepository
import com.conestoga.lifetracker.domain.model.Note
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

/**
 * Unit tests for database operations using the NoteRepository.
 * Tests CRUD operations (Create, Read, Update, Delete) for notes.
 * Uses Mockito to mock the DAO layer.
 */
class DatabaseOperationsTest {
    @Mock
    private lateinit var mockNoteDao: NoteDao

    private lateinit var noteRepository: NoteRepository

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        noteRepository = NoteRepository(mockNoteDao)
    }

    // ============ INSERT OPERATIONS ============

    /**
     * Test Case 1: Positive case - Insert valid note
     * Verifies that inserting a note returns a valid ID
     */
    @Test
    fun testInsertNote_Success() = runBlocking {
        // Arrange
        val noteToInsert = Note(id = 0, title = "Test Note", createdAt = 1000L)
        whenever(mockNoteDao.insertNote(NoteEntity(title = "Test Note", createdAt = 1000L)))
            .thenReturn(1L)

        // Act
        val result = noteRepository.insertNote(noteToInsert)

        // Assert
        assertEquals("Insert should return ID 1", 1L, result)
        verify(mockNoteDao).insertNote(NoteEntity(title = "Test Note", createdAt = 1000L))
    }

    /**
     * Test Case 2: Edge case - Insert note with empty title
     * Verifies that empty titles are handled
     */
    @Test
    fun testInsertNote_EmptyTitle() = runBlocking {
        // Arrange
        val noteWithEmptyTitle = Note(id = 0, title = "", createdAt = 2000L)
        whenever(mockNoteDao.insertNote(NoteEntity(title = "", createdAt = 2000L)))
            .thenReturn(2L)

        // Act
        val result = noteRepository.insertNote(noteWithEmptyTitle)

        // Assert
        assertEquals("Insert should still return valid ID", 2L, result)
    }

    /**
     * Test Case 3: Positive case - Insert multiple notes
     * Verifies that multiple notes can be inserted sequentially
     */
    @Test
    fun testInsertMultipleNotes() = runBlocking {
        // Arrange
        val note1 = Note(id = 0, title = "First Note", createdAt = 1000L)
        val note2 = Note(id = 0, title = "Second Note", createdAt = 2000L)

        whenever(mockNoteDao.insertNote(NoteEntity(title = "First Note", createdAt = 1000L)))
            .thenReturn(1L)
        whenever(mockNoteDao.insertNote(NoteEntity(title = "Second Note", createdAt = 2000L)))
            .thenReturn(2L)

        // Act
        val result1 = noteRepository.insertNote(note1)
        val result2 = noteRepository.insertNote(note2)

        // Assert
        assertEquals("First insert should return ID 1", 1L, result1)
        assertEquals("Second insert should return ID 2", 2L, result2)
    }

    // ============ UPDATE OPERATIONS ============

    /**
     * Test Case 4: Positive case - Update existing note
     * Verifies that updating a note returns 1 (rows affected)
     */
    @Test
    fun testUpdateNote_Success() = runBlocking {
        // Arrange
        val noteToUpdate = Note(id = 1, title = "Updated Note", createdAt = 1000L)
        whenever(mockNoteDao.updateNote(NoteEntity(id = 1, title = "Updated Note", createdAt = 1000L)))
            .thenReturn(1)

        // Act
        val result = noteRepository.updateNote(noteToUpdate)

        // Assert
        assertEquals("Update should affect 1 row", 1, result)
        verify(mockNoteDao).updateNote(NoteEntity(id = 1, title = "Updated Note", createdAt = 1000L))
    }

    /**
     * Test Case 5: Negative case - Update non-existent note
     * Verifies that updating non-existent note returns 0
     */
    @Test
    fun testUpdateNote_NonExistent() = runBlocking {
        // Arrange
        val noteToUpdate = Note(id = 999, title = "Non-existent", createdAt = 1000L)
        whenever(mockNoteDao.updateNote(NoteEntity(id = 999, title = "Non-existent", createdAt = 1000L)))
            .thenReturn(0)

        // Act
        val result = noteRepository.updateNote(noteToUpdate)

        // Assert
        assertEquals("Update should affect 0 rows", 0, result)
    }

    /**
     * Test Case 6: Positive case - Update with long title
     * Edge case: verifies handling of very long note titles
     */
    @Test
    fun testUpdateNote_LongTitle() = runBlocking {
        // Arrange
        val longTitle = "A".repeat(500) // 500 characters
        val noteToUpdate = Note(id = 5, title = longTitle, createdAt = 1000L)
        whenever(mockNoteDao.updateNote(NoteEntity(id = 5, title = longTitle, createdAt = 1000L)))
            .thenReturn(1)

        // Act
        val result = noteRepository.updateNote(noteToUpdate)

        // Assert
        assertEquals("Update should handle long titles", 1, result)
    }

    // ============ DELETE OPERATIONS ============

    /**
     * Test Case 7: Positive case - Delete existing note
     * Verifies that deleting a note returns 1 (rows affected)
     */
    @Test
    fun testDeleteNote_Success() = runBlocking {
        // Arrange
        whenever(mockNoteDao.deleteNoteById(1))
            .thenReturn(1)

        // Act
        val result = noteRepository.deleteNoteById(1)

        // Assert
        assertEquals("Delete should affect 1 row", 1, result)
        verify(mockNoteDao).deleteNoteById(1)
    }

    /**
     * Test Case 8: Negative case - Delete non-existent note
     * Verifies that deleting non-existent note returns 0
     */
    @Test
    fun testDeleteNote_NonExistent() = runBlocking {
        // Arrange
        whenever(mockNoteDao.deleteNoteById(999))
            .thenReturn(0)

        // Act
        val result = noteRepository.deleteNoteById(999)

        // Assert
        assertEquals("Delete should affect 0 rows", 0, result)
    }

    /**
     * Test Case 9: Edge case - Delete with ID 0
     * Verifies handling of invalid ID
     */
    @Test
    fun testDeleteNote_InvalidId() = runBlocking {
        // Arrange
        whenever(mockNoteDao.deleteNoteById(0))
            .thenReturn(0)

        // Act
        val result = noteRepository.deleteNoteById(0)

        // Assert
        assertEquals("Delete with invalid ID should return 0", 0, result)
    }

    // ============ READ OPERATIONS ============

    /**
     * Test Case 10: Positive case - Get all notes
     * Verifies that retrieving all notes returns correct data
     */
    @Test
    fun testGetAllNotes_Success() = runBlocking {
        // Arrange
        val mockNotes = listOf(
            NoteEntity(id = 1, title = "Note 1", createdAt = 1000L),
            NoteEntity(id = 2, title = "Note 2", createdAt = 2000L)
        )
        whenever(mockNoteDao.getAllNotes())
            .thenReturn(mockNotes)

        // Act
        val result = noteRepository.getAllNotes()

        // Assert
        assertEquals("Should return 2 notes", 2, result.size)
        assertEquals("First note should have ID 1", 1, result[0].id)
        assertEquals("Second note should have ID 2", 2, result[1].id)
    }

    /**
     * Test Case 11: Edge case - Get all notes when empty
     * Verifies handling of empty database
     */
    @Test
    fun testGetAllNotes_Empty() = runBlocking {
        // Arrange
        whenever(mockNoteDao.getAllNotes())
            .thenReturn(emptyList())

        // Act
        val result = noteRepository.getAllNotes()

        // Assert
        assertEquals("Should return empty list", 0, result.size)
    }

    /**
     * Test Case 12: Positive case - Get note by ID
     * Verifies that getting specific note returns correct data
     */
    @Test
    fun testGetNoteById_Success() = runBlocking {
        // Arrange
        val mockNote = NoteEntity(id = 1, title = "Test Note", createdAt = 1000L)
        whenever(mockNoteDao.getNoteById(1))
            .thenReturn(mockNote)

        // Act
        val result = noteRepository.getNoteById(1)

        // Assert
        assertNotNull("Result should not be null", result)
        assertEquals("ID should be 1", 1, result?.id)
        assertEquals("Title should be 'Test Note'", "Test Note", result?.title)
    }

    /**
     * Test Case 13: Negative case - Get non-existent note
     * Verifies that getting non-existent note returns null
     */
    @Test
    fun testGetNoteById_NotFound() = runBlocking {
        // Arrange
        whenever(mockNoteDao.getNoteById(999))
            .thenReturn(null)

        // Act
        val result = noteRepository.getNoteById(999)

        // Assert
        assertNull("Result should be null for non-existent note", result)
    }

    /**
     * Test Case 14: Positive case - Get note with large ID
     * Edge case: verifies handling of large IDs
     */
    @Test
    fun testGetNoteById_LargeId() = runBlocking {
        // Arrange
        val largeId = 999999
        val mockNote = NoteEntity(id = largeId, title = "Note with large ID", createdAt = 1000L)
        whenever(mockNoteDao.getNoteById(largeId))
            .thenReturn(mockNote)

        // Act
        val result = noteRepository.getNoteById(largeId)

        // Assert
        assertEquals("Should handle large IDs", largeId, result?.id)
    }
}
