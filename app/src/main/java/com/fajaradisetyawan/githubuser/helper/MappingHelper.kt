package com.fajaradisetyawan.githubuser.helper

import android.database.Cursor
import com.fajaradisetyawan.githubuser.db.DatabaseContract
import com.fajaradisetyawan.githubuser.model.GithubUser

object MappingHelper {
    fun mapCursorToArrayList(userCursor: Cursor?): ArrayList<GithubUser>{
        val githubUser = ArrayList<GithubUser>()

        userCursor?.apply {
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