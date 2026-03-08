package com.conestoga.lifetracker

import android.content.Context
import android.widget.LinearLayout
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * Unit tests for DBAdapter.
 * Tests list management and adapter state.
 */
@Config(sdk = [34])
@RunWith(RobolectricTestRunner::class)
class DBAdapterTest {
    private lateinit var context: Context
    private lateinit var adapter: DBAdapter
    private var deleteId: Int? = null
    private var editId: Int? = null
    private var editText: String? = null
    private val dataList = mutableListOf(Pair(1, "Note 1"), Pair(2, "Note 2"))

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        adapter = DBAdapter(dataList, { id -> deleteId = id }, { id, text -> 
            editId = id
            editText = text
        })
    }

    @Test
    fun testGetItemCount() {
        assertEquals(2, adapter.itemCount)
    }

    @Test
    fun testUpdateData() {
        val newData = listOf(Pair(1, "New Note 1"), Pair(2, "New Note 2"), Pair(3, "Note 3"))
        adapter.updateData(newData)
        assertEquals(3, adapter.itemCount)
    }

    @Test
    fun testOnBindViewHolder_SetsTextAndCallbacks() {
        val parent = LinearLayout(context)
        val holder = adapter.onCreateViewHolder(parent, 0)
        
        adapter.onBindViewHolder(holder, 0)
        
        assertEquals("Note 1", holder.dataText.text.toString())
        
        holder.deleteBtn.performClick()
        assertEquals(1, deleteId)
        
        holder.editBtn.performClick()
        assertEquals(1, editId)
        assertEquals("Note 1", editText)
    }
}
