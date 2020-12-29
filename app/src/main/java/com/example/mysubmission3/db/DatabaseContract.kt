 package com.example.mysubmission3.db

import android.provider.BaseColumns

internal class DatabaseContract {

    internal class FavColumns : BaseColumns {
        companion object{
            const val TABLE_NAME ="note"
            const val _ID ="_id"
            const val USERNAME ="username"
            const val URL ="url"
            const val IMAGE ="image"
        }
    }
}