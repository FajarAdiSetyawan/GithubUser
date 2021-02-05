package com.fajaradisetyawan.githubuser.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.fajaradisetyawan.githubuser.db.DatabaseContract.UserColum.Companion.TABLE_NAME

internal class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION){
    companion object{
        private const val DATABASE_NAME = "db_user"
        private const val DATABASE_VERSION = 2
        private const val SQL_CREATE_TABLE_USER = "CREATE TABLE $TABLE_NAME" +
                " (${DatabaseContract.UserColum._ID} INTEGER PRIMARY KEY," +
                " ${DatabaseContract.UserColum.USERNAME} TEXT NOT NULL," +
                " ${DatabaseContract.UserColum.TYPE} TEXT NOT NULL," +
                " ${DatabaseContract.UserColum.AVATAR} TEXT NOT NULL)"

    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_TABLE_USER)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }
}