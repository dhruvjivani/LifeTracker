package com.conestoga.lifetracker

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.conestoga.lifetracker.data.dao.NoteDao
import com.conestoga.lifetracker.data.database.LifeTrackerDatabase
import com.conestoga.lifetracker.data.entity.NoteEntity
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@Config(sdk = [34])
@RunWith(RobolectricTestRunner::class)
class RoomDatabaseTest {
    private lateinit var db: LifeTrackerDatabase
    private lateinit var dao: NoteDao

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, LifeTrackerDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        dao = db.noteDao()
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun testInsertAndGetNote() = runBlocking {
        val note = NoteEntity(title = "Room Note")
        dao.insertNote(note)
        val allNotes = dao.getAllNotes()
        assertEquals(1, allNotes.size)
        assertEquals("Room Note", allNotes[0].title)
    }

    @Test
    fun testUpdateNote() = runBlocking {
        val note = NoteEntity(title = "Old Title")
        val id = dao.insertNote(note).toInt()
        val updatedNote = NoteEntity(id = id, title = "New Title")
        dao.updateNote(updatedNote)
        val fetched = dao.getNoteById(id)
        assertEquals("New Title", fetched?.title)
    }

    @Test
    fun testDeleteNote() = runBlocking {
        val note = NoteEntity(title = "To Delete")
        val id = dao.insertNote(note).toInt()
        val fetched = dao.getNoteById(id)
        assertNotNull(fetched)
        dao.deleteNote(fetched!!)
        val allNotes = dao.getAllNotes()
        assertEquals(0, allNotes.size)
    }
    
    @Test
    fun testDeleteNoteById() = runBlocking {
        val id = dao.insertNote(NoteEntity(title = "Delete Me")).toInt()
        dao.deleteNoteById(id)
        assertEquals(0, dao.getAllNotes().size)
    }

    @Test
    fun testDatabaseSingleton() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val instance1 = LifeTrackerDatabase.getInstance(context)
        val instance2 = LifeTrackerDatabase.getInstance(context)
        assertNotNull(instance1)
        assertEquals(instance1, instance2)
    }
}
