package com.example.birthdayreminder

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteException
import android.provider.ContactsContract

class BirthdayDatabaseHelper( context: Context):
    SQLiteOpenHelper(context,DATABASE_NAME,null,DATABASE_VERSION) {

    companion object {
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "BirthdayDatabase"
        private val TABLE_NAME = "BirthdayTable"
        private val KEY_ID = "id"
        private val KEY_NAME = "name"
        private val KEY_BIRTHDAY = "birthday"
    }


    public fun addProfile(data: ProfileData) {
        val db: SQLiteDatabase = this.writableDatabase
        var profContent: ContentValues = ContentValues()

        profContent.put(KEY_NAME, data.getName())
        profContent.put(KEY_BIRTHDAY, data.getDateAsString())

        db.insert(TABLE_NAME, null ,profContent)
    }


    //Code to create a new table
    override fun onCreate(p0: SQLiteDatabase) {
        val query = ("CREATE TABLE " + TABLE_NAME + " ("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                KEY_NAME + " TEXT," +
                KEY_BIRTHDAY + " TEXT" + ")")


        p0.execSQL(query)

    }

    //In case version number of the database changes
    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }

    public fun addProfileToTable() {

    }

}