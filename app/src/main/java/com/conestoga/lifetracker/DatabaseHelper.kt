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
        writableDatabase.use { db ->
            val values = ContentValues()
            values.put("name", name)
            db.insert("records", null, values)
        }
    }

    fun updateData(id: Int, name: String): Int {
        return writableDatabase.use { db ->
            val values = ContentValues()
            values.put("name", name)
            db.update("records", values, "id=?", arrayOf(id.toString()))
        }
    }

    fun deleteData(id: Int): Int {
        return writableDatabase.use { db ->
            db.delete("records", "id=?", arrayOf(id.toString()))
        }
    }

    fun getAllData(): List<Pair<Int, String>> {
        val list = mutableListOf<Pair<Int, String>>()
        readableDatabase.use { db ->
            db.rawQuery("SELECT * FROM records", null).use { cursor ->
                if (cursor.moveToFirst()) {
                    do {
                        val id = cursor.getInt(0)
                        val name = cursor.getString(1)
                        list.add(Pair(id, name))
                    } while (cursor.moveToNext())
                }
            }
        }
        return list
    }
}
