package com.conestoga.lifetracker

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, "lifetracker.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE records(id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS records")
        onCreate(db)
    }

    fun insertData(name: String) {
        val db = writableDatabase
        val values = ContentValues()
        values.put("name", name)
        db.insert("records", null, values)
    }

    fun updateData(id: Int, name: String): Int {
        val db = writableDatabase
        val values = ContentValues()
        values.put("name", name)
        return db.update("records", values, "id=?", arrayOf(id.toString()))
    }

    fun deleteData(id: Int): Int {
        val db = writableDatabase
        return db.delete("records", "id=?", arrayOf(id.toString()))
    }

    fun getAllData(): List<Pair<Int, String>> {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM records", null)
        val list = mutableListOf<Pair<Int, String>>()
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(0)
                val name = cursor.getString(1)
                list.add(Pair(id, name))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return list
    }
}
