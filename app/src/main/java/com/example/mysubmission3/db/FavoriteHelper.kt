package com.example.mysubmission3.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import com.example.mysubmission3.db.DatabaseContract.FavColumns.Companion.TABLE_NAME
import com.example.mysubmission3.db.DatabaseContract.FavColumns.Companion.USERNAME
import com.example.mysubmission3.db.DatabaseContract.FavColumns.Companion._ID

class FavoriteHelper(context: Context){

    private var dataBaseHelper: DatabaseHelper = DatabaseHelper(context)
    private lateinit var database: SQLiteDatabase

    companion object {
        private const val DATABASE_TABLE = TABLE_NAME
        private var INSTANCE: FavoriteHelper? = null
        fun getInstance(context: Context): FavoriteHelper =
                INSTANCE ?: synchronized(this) {
                    INSTANCE ?: FavoriteHelper(context)
                }
    }

    @Throws(SQLException::class)
    fun open() {
        database = dataBaseHelper.writableDatabase
    }
    fun close() {
        dataBaseHelper.close()
        if (database.isOpen)
            database.close()
    }

    fun queryAll(): Cursor {
        return database.query(
                TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                "$_ID ASC")
    }
    fun queryById(id: String): Cursor {
        return database.query(
                TABLE_NAME,
                null,
                "$_ID = ?",
                arrayOf(id),
                null,
                null,
                null,
                null)
    }
    fun queryByUsername(username: String): Cursor {
        return database.query(
                TABLE_NAME,
                null,
                "$USERNAME = ?",
                arrayOf(username),
                null,
                null,
                null,
                null)
    }
    fun insert(values: ContentValues?): Long {
        return database.insert(TABLE_NAME, null, values)
    }
    fun update(id: String, values: ContentValues?): Int {
        return database.update(TABLE_NAME, values, "$_ID = ?", arrayOf(id))
    }
    fun deleteByUser(username: String): Int {
        return database.delete(TABLE_NAME, "$USERNAME = '$username'", null)
    }
}