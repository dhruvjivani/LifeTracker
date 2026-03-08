package com.conestoga.lifetracker

import com.conestoga.lifetracker.domain.model.Note
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Unit tests for the Note domain model.
 */
class NoteModelTest {

    @Test
    fun testNoteInitialization() {
        val currentTime = System.currentTimeMillis()
        val note = Note(id = 1, title = "Test Note", createdAt = currentTime)
        
        assertEquals(1, note.id)
        assertEquals("Test Note", note.title)
        assertEquals(currentTime, note.createdAt)
    }

    @Test
    fun testNoteDefaultValues() {
        val note = Note(title = "Default Note")
        
        assertEquals(0, note.id)
        assertEquals("Default Note", note.title)
        // createdAt should be recent
        val now = System.currentTimeMillis()
        assert(now - note.createdAt < 1000)
    }
    
    @Test
    fun testNoteEquality() {
        val note1 = Note(1, "Title", 100L)
        val note2 = Note(1, "Title", 100L)
        val note3 = Note(2, "Title", 100L)
        
        assertEquals(note1, note2)
        assert(note1 != note3)
    }
}
