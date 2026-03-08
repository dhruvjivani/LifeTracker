package com.conestoga.lifetracker

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@Config(sdk = [34])
@RunWith(RobolectricTestRunner::class)
class DatabaseHelperTest {
    private lateinit var dbHelper: DatabaseHelper

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        dbHelper = DatabaseHelper(context)
    }

    @After
    fun tearDown() {
        dbHelper.close()
    }

    @Test
    fun testInsertAndGetAllData() {
        dbHelper.insertData("Test Record")
        val data = dbHelper.getAllData()
        assertTrue(data.isNotEmpty())
        assertEquals("Test Record", data[0].second)
    }

    @Test
    fun testUpdateData() {
        dbHelper.insertData("Original")
        val id = dbHelper.getAllData()[0].first
        val rows = dbHelper.updateData(id, "Updated")
        val updatedName = dbHelper.getAllData()[0].second
        assertEquals(1, rows)
        assertEquals("Updated", updatedName)
    }

    @Test
    fun testDeleteData() {
        dbHelper.insertData("To Delete")
        val id = dbHelper.getAllData()[0].first
        val rows = dbHelper.deleteData(id)
        assertTrue(dbHelper.getAllData().isEmpty())
        assertEquals(1, rows)
    }

    @Test
    fun testOnUpgrade() {
        val db = dbHelper.writableDatabase
        dbHelper.onUpgrade(db, 1, 2)
        // Verify table still exists by trying to query it
        val cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='records'", null)
        assertTrue(cursor.moveToFirst())
        cursor.close()
    }
}
