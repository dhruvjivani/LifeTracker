package com.conestoga.lifetracker

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class DatabaseActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var inputText: EditText
    private lateinit var btnAdd: ImageButton
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var dbAdapter: DBAdapter

    private var isEditing = false
    private var editingNoteId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_database)

        recyclerView = findViewById(R.id.dbRecyclerView)
        inputText = findViewById(R.id.editDbInput)
        btnAdd = findViewById(R.id.btnAddDb)
        recyclerView.layoutManager = LinearLayoutManager(this)

        databaseHelper = DatabaseHelper(this)

        dbAdapter = DBAdapter(
            databaseHelper.getAllData().toMutableList(),
            onDelete = { id ->
                databaseHelper.deleteData(id)
                refreshList()
            },
            onEdit = { id, text ->
                inputText.setText(text)
                isEditing = true
                editingNoteId = id
                btnAdd.setImageResource(android.R.drawable.ic_menu_save)
            }
        )

        recyclerView.adapter = dbAdapter

        btnAdd.setOnClickListener {
            val input = inputText.text.toString()
            if (input.isNotBlank()) {
                if (isEditing) {
                    databaseHelper.updateData(editingNoteId!!, input)
                    isEditing = false
                    editingNoteId = null
                    btnAdd.setImageResource(android.R.drawable.ic_input_add)
                } else {
                    databaseHelper.insertData(input)
                }
                inputText.text.clear()
                refreshList()
            } else {
                Toast.makeText(this, "Please enter some text", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun refreshList() {
        dbAdapter.updateData(databaseHelper.getAllData())
    }


}
