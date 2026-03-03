package com.conestoga.lifetracker

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.conestoga.lifetracker.data.database.LifeTrackerDatabase
import com.conestoga.lifetracker.data.dao.NoteDao
import com.conestoga.lifetracker.data.entity.NoteEntity
import com.conestoga.lifetracker.data.repository.NoteRepository
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.Assert.*
import androidx.room.Room
import android.content.Context
import androidx.test.core.app.ApplicationProvider

/**
 * Integration tests for database operations using Room.
 * Tests actual database interactions using in-memory database.
 */
class DatabaseIntegrationTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: LifeTrackerDatabase
    private lateinit var noteDao: NoteDao
    private lateinit var noteRepository: NoteRepository

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, LifeTrackerDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        noteDao = database.noteDao()
        noteRepository = NoteRepository(noteDao)
    }

    @After
    fun tearDown() {
        database.close()
    }

    // ============ DATABASE & REPOSITORY INTEGRATION TESTS ============

    /**
     * Test Case 1: Insert and retrieve note
     * Verifies data flow from repository through DAO to database
     */
    @Test
    fun testInsertAndRetrieveNote() = runBlocking {
        // Arrange
        val testNote = NoteEntity(id = 0, title = "Integration Test Note", createdAt = 1000L)

        // Act - Insert
        val insertedId = noteDao.insertNote(testNote)
        val retrievedNote = noteDao.getNoteById(insertedId.toInt())

        // Assert
        assertNotNull("Retrieved note should not be null", retrievedNote)
        assertEquals("Title should match", "Integration Test Note", retrievedNote?.title)
        assertEquals("CreatedAt should match", 1000L, retrievedNote?.createdAt)
    }

    /**
     * Test Case 2: Insert, update and verify changes
     * Verifies that updates are persisted in database
     */
    @Test
    fun testInsertUpdateAndVerify() = runBlocking {
        // Arrange
        val originalNote = NoteEntity(title = "Original Title")
        val insertedId = noteDao.insertNote(originalNote).toInt()

        // Act - Update
        val updatedNote = NoteEntity(id = insertedId, title = "Updated Title", createdAt = originalNote.createdAt)
        noteDao.updateNote(updatedNote)
        val retrievedNote = noteDao.getNoteById(insertedId)

        // Assert
        assertEquals("Title should be updated", "Updated Title", retrievedNote?.title)
    }

    /**
     * Test Case 3: Insert multiple notes and retrieve all
     * Verifies that multiple records are stored and retrieved correctly
     */
    @Test
    fun testInsertMultipleAndRetrieveAll() = runBlocking {
        // Arrange
        val note1 = NoteEntity(title = "Note 1", createdAt = 1000L)
        val note2 = NoteEntity(title = "Note 2", createdAt = 2000L)
        val note3 = NoteEntity(title = "Note 3", createdAt = 3000L)

        // Act
        noteDao.insertNote(note1)
        noteDao.insertNote(note2)
        noteDao.insertNote(note3)
        val allNotes = noteDao.getAllNotes()

        // Assert
        assertEquals("Should have 3 notes", 3, allNotes.size)
        assertEquals("First note title", "Note 3", allNotes[0].title) // Newest first
        assertEquals("Second note title", "Note 2", allNotes[1].title)
        assertEquals("Third note title", "Note 1", allNotes[2].title)
    }

    /**
     * Test Case 4: Delete note and verify it's gone
     * Verifies that deleted records are removed from database
     */
    @Test
    fun testInsertDeleteAndVerify() = runBlocking {
        // Arrange
        val note = NoteEntity(title = "To Delete")
        val insertedId = noteDao.insertNote(note).toInt()

        // Act
        val deletedCount = noteDao.deleteNoteById(insertedId)
        val retrievedNote = noteDao.getNoteById(insertedId)

        // Assert
        assertEquals("Delete should affect 1 row", 1, deletedCount)
        assertNull("Note should be deleted from database", retrievedNote)
    }

    /**
     * Test Case 5: Repository insert and retrieve flow
     * Verifies complete repository operation integration
     */
    @Test
    fun testRepositoryInsertAndRetrieve() = runBlocking {
        // Arrange
        val testNote = com.conestoga.lifetracker.domain.model.Note(
            title = "Repository Test",
            createdAt = 5000L
        )

        // Act
        val insertedId = noteRepository.insertNote(testNote)
        val retrievedNote = noteRepository.getNoteById(insertedId.toInt())

        // Assert
        assertNotNull("Retrieved note should not be null", retrievedNote)
        assertEquals("Title should match", "Repository Test", retrievedNote?.title)
    }

    /**
     * Test Case 6: Repository update through domain model
     * Verifies repository properly converts domain models to entities
     */
    @Test
    fun testRepositoryUpdateFlow() = runBlocking {
        // Arrange - Insert first
        val originalNote = com.conestoga.lifetracker.domain.model.Note(
            title = "Original",
            createdAt = 6000L
        )
        val insertedId = noteRepository.insertNote(originalNote).toInt()

        // Act - Update
        val updatedNote = com.conestoga.lifetracker.domain.model.Note(
            id = insertedId,
            title = "Updated via Repository",
            createdAt = 6000L
        )
        val updateResult = noteRepository.updateNote(updatedNote)
        val retrievedNote = noteRepository.getNoteById(insertedId)

        // Assert
        assertEquals("Update should affect 1 row", 1, updateResult)
        assertEquals("Title should be updated", "Updated via Repository", retrievedNote?.title)
    }

    /**
     * Test Case 7: Get all notes through repository
     * Verifies repository retrieves and converts all database records
     */
    @Test
    fun testRepositoryGetAllNotes() = runBlocking {
        // Arrange - Insert multiple notes through repository
        val note1 = com.conestoga.lifetracker.domain.model.Note(title = "Repo Note 1")
        val note2 = com.conestoga.lifetracker.domain.model.Note(title = "Repo Note 2")
        noteRepository.insertNote(note1)
        noteRepository.insertNote(note2)

        // Act
        val allNotes = noteRepository.getAllNotes()

        // Assert
        assertEquals("Should have 2 notes", 2, allNotes.size)
        assertTrue("Notes should be from repository", 
            allNotes.any { it.title == "Repo Note 1" && it.title == "Repo Note 2" })
    }

    /**
     * Test Case 8: Delete through repository and get all
     * Verifies repository delete integrates with database
     */
    @Test
    fun testRepositoryDeleteIntegration() = runBlocking {
        // Arrange - Insert notes
        val note1 = com.conestoga.lifetracker.domain.model.Note(title = "Keep")
        val note2 = com.conestoga.lifetracker.domain.model.Note(title = "Delete")
        noteRepository.insertNote(note1)
        val deleteId = noteRepository.insertNote(note2).toInt()

        // Act
        noteRepository.deleteNoteById(deleteId)
        val allNotes = noteRepository.getAllNotes()

        // Assert
        assertEquals("Should have 1 note after delete", 1, allNotes.size)
        assertEquals("Remaining note should be 'Keep'", "Keep", allNotes[0].title)
    }

    /**
     * Test Case 9: Edge case - Large dataset insertion and retrieval
     * Verifies performance with multiple notes
     */
    @Test
    fun testLargeDatasetInsertion() = runBlocking {
        // Arrange - Insert 50 notes
        repeat(50) { i ->
            val note = com.conestoga.lifetracker.domain.model.Note(
                title = "Note $i",
                createdAt = (1000L + i)
            )
            noteRepository.insertNote(note)
        }

        // Act
        val allNotes = noteRepository.getAllNotes()

        // Assert
        assertEquals("Should have 50 notes", 50, allNotes.size)
        assertEquals("First note should be Note 49", "Note 49", allNotes[0].title)
    }

    /**
     * Test Case 10: Edge case - Empty database operations
     * Verifies handling of operations on empty database
     */
    @Test
    fun testEmptyDatabaseOperations() = runBlocking {
        // Act
        val allNotes = noteRepository.getAllNotes()
        val singleNote = noteRepository.getNoteById(1)
        val deleteResult = noteRepository.deleteNoteById(1)

        // Assert
        assertEquals("Empty database should return empty list", 0, allNotes.size)
        assertNull("Getting non-existent note should return null", singleNote)
        assertEquals("Delete on empty database should return 0", 0, deleteResult)
    }
}
