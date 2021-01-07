package com.example.consumerapp.helper

import android.database.Cursor
import com.example.consumerapp.db.DatabaseContract
import com.example.consumerapp.entity.ModelData

object MappingHelper {

    fun mapCursorToArrayList(notesCursor: Cursor?): ArrayList<ModelData> {
        val notesList = ArrayList<ModelData>()

        notesCursor?.apply {
            while (moveToNext()) {
                val id = getString(getColumnIndexOrThrow(DatabaseContract.FavColumns._ID))
                val username = getString(getColumnIndexOrThrow(DatabaseContract.FavColumns.USERNAME))
                val url = getString(getColumnIndexOrThrow(DatabaseContract.FavColumns.URL))
                val image = getString(getColumnIndexOrThrow(DatabaseContract.FavColumns.IMAGE))
                notesList.add(ModelData(id, username, url, image))
            }
        }
        return notesList
    }
}