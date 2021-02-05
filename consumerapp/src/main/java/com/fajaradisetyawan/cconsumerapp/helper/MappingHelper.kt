package com.fajaradisetyawan.cconsumerapp.helper

import android.database.Cursor
import com.fajaradisetyawan.cconsumerapp.db.DatabaseContract
import com.fajaradisetyawan.cconsumerapp.model.GithubUser

object MappingHelper {
    fun mapCursorToArrayList(cursor: Cursor?) : ArrayList<GithubUser>{
        val githubUser = ArrayList<GithubUser>()

        cursor?.apply {
            while (moveToNext()){
                val id = getInt(getColumnIndexOrThrow(DatabaseContract.UserColum._ID))
                val username = getString(getColumnIndexOrThrow(DatabaseContract.UserColum.USERNAME))
                val type = getString(getColumnIndexOrThrow(DatabaseContract.UserColum.TYPE))
                val avatar = getString(getColumnIndexOrThrow(DatabaseContract.UserColum.AVATAR))
                githubUser.add(GithubUser(id, username, null, type, avatar))
            }
        }
        return githubUser
    }
}