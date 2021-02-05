package com.fajaradisetyawan.githubuser.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.fajaradisetyawan.githubuser.db.DatabaseContract.AUTHORITY
import com.fajaradisetyawan.githubuser.db.DatabaseContract.UserColum.Companion.CONTENT_URI
import com.fajaradisetyawan.githubuser.db.DatabaseContract.UserColum.Companion.TABLE_NAME
import com.fajaradisetyawan.githubuser.db.GithubHelper

class GithubProvider : ContentProvider() {
    companion object {
        private const val USER = 1
        private const val USER_ID = 2
        private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH)
        private lateinit var githubHelper: GithubHelper
    }

    init {
        // content: // com.fajaradisetyawan/user
        uriMatcher.addURI(AUTHORITY, TABLE_NAME, USER)

        // content: // com.fajaradisetyawan/user/id
        uriMatcher.addURI(AUTHORITY, "$TABLE_NAME/#", USER_ID)
    }

    override fun onCreate(): Boolean {
        githubHelper = GithubHelper.getInstance(context as Context)
        githubHelper.open()
        return true
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?, selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        return when(uriMatcher.match(uri)){
            USER -> githubHelper.queryAll()
            USER_ID -> githubHelper.queryById(uri.lastPathSegment.toString())
            else -> null
        }
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val added: Long = when (USER) {
            uriMatcher.match(uri) -> githubHelper.insert(values)
            else -> 0
        }

        context?.contentResolver?.notifyChange(CONTENT_URI, null)

        return Uri.parse("$CONTENT_URI/$added")
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        return 0
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        val delete: Int = when(USER_ID){
            uriMatcher.match(uri) -> githubHelper.deleteById(uri.lastPathSegment.toString())
            else -> 0
        }

        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return delete
    }
}