 package com.example.mysubmission3.db

import android.net.Uri
import android.provider.BaseColumns

object DatabaseContract {

    const val AUTHORITY = "com.example.mysubmission3"
    const val SCHEME = "content"

    class FavColumns : BaseColumns {
        companion object{
            const val TABLE_NAME ="favorite"
            const val _ID ="_id"
            const val USERNAME ="username"
            const val URL ="url"
            const val IMAGE ="image"

            // untuk membuat URI content://com.example.mysubmission3/favorite
            val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME)
                    .authority(AUTHORITY)
                    .appendPath(TABLE_NAME)
                    .build()
        }
    }
}