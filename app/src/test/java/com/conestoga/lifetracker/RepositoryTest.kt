package com.conestoga.lifetracker

import com.conestoga.lifetracker.data.dao.NoteDao
import com.conestoga.lifetracker.data.entity.NoteEntity
import com.conestoga.lifetracker.data.repository.NoteRepository
import com.conestoga.lifetracker.domain.model.Note
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.kotlin.any

class RepositoryTest {
    private lateinit var repository: NoteRepository
    private lateinit var noteDao: NoteDao

    @Before
    fun setUp() {
        noteDao = mock(NoteDao::class.java)
        repository = NoteRepository(noteDao)
    }

    @Test
    fun testInsertNote() = runBlocking {
        val note = Note(title = "Test")
        `when`(noteDao.insertNote(any())).thenReturn(1L)
        
        val result = repository.insertNote(note)
        assertEquals(1L, result)
    }

    @Test
    fun testGetAllNotes() = runBlocking {
        val entities = listOf(NoteEntity(1, "Title 1", 100L))
        `when`(noteDao.getAllNotes()).thenReturn(entities)
        
        val result = repository.getAllNotes()
        assertEquals(1, result.size)
        assertEquals("Title 1", result[0].title)
    }
}
