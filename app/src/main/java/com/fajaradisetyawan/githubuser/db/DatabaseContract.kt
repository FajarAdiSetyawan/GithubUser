package com.fajaradisetyawan.githubuser.db

import android.net.Uri
import android.provider.BaseColumns

object DatabaseContract {

    const val AUTHORITY = "com.fajaradisetyawan.githubuser"
    const val SCHEME = "content"

    class UserColum : BaseColumns{
        companion object{
            const val TABLE_NAME = "user"
            const val _ID = "_id"
            const val USERNAME = "username"
            const val TYPE = "type"
            const val AVATAR = "avatar"

            val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME)
                    .authority(AUTHORITY)
                    .appendPath(TABLE_NAME)
                    .build()
        }
    }
}