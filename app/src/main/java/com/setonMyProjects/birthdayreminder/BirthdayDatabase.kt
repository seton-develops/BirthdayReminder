package com.setonMyProjects.birthdayreminder

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.content.ContentValues
import android.database.Cursor
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class BirthdayDatabaseHelper( context: Context):
    SQLiteOpenHelper(context, DATABASE_NAME,null, DATABASE_VERSION) {

    private val context = context
    var profileList = getProfiles()

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "BirthdayDatabase"
        private const val TABLE_NAME = "BirthdayTable"
        private const val KEY_ID = "id"
        private const val KEY_NAME = "name"
        private const val KEY_BIRTHDAY = "birthday"
    }

    /**
     * Delete all profiles from database
     */
    fun deleteAllProfiles() {
        val db: SQLiteDatabase = this.writableDatabase
        db.execSQL("DELETE FROM $TABLE_NAME")
        db.close()
    }

    /**
     * Delete specific profile from database based on position
     * Position is given by delete button in viewholder in recyclerview
     */
    fun deleteProfile(position: Int) {
        val db: SQLiteDatabase = this.writableDatabase
        db.execSQL("DELETE FROM $TABLE_NAME WHERE $KEY_ID = " + profileList[position].getIndex())
        db.close()

        BirthdayNotificationService(context).cancelNotification(
            context,
            profileList[position].getIndex()
        )


    }

    /**
     * Edit a profile's birthday or name
     * Position is given by delete button in viewholder in recyclerview
     */
    fun editProfile(newName: String, newDate: String, position: Int) {
        val db: SQLiteDatabase = this.writableDatabase
        val profContent = ContentValues()

        profContent.put(KEY_NAME, newName)
        profContent.put(KEY_BIRTHDAY, newDate)

        db.update(
            TABLE_NAME,
            profContent,
            "$KEY_ID = " +  profileList[position].getIndex(),
            null
        )
        db.close()

        BirthdayNotificationService(context).showNotification(
            context,
            name = profileList[position].getName(),
            age = profileList[position].getAge(),
            id = profileList[position].getIndex(),
            birthday = stringDateToLocalDate(profileList[position].getDate())
        )

    }


    /**
     * return an arraylist of profiles from the database for the recyclerView
     */
    private fun getProfiles(): ArrayList<ProfileData> {
        val query = "SELECT * FROM $TABLE_NAME"
        val db: SQLiteDatabase = this.readableDatabase
        val cur: Cursor = db.rawQuery(query, null)

        //arrayList to hold profiles for recyclerView
        val arr = ArrayList<ProfileData>()

        if (cur.moveToFirst()) {
            while (!cur.isAfterLast) {
                arr.add(ProfileData(cur.getString(1), cur.getString(2), cur.getInt(0)))
                cur.moveToNext()
            }
            cur.close()
            db.close()
            return ArrayList(arr.sortedWith(compareBy(){ it.getDate()}))
        }

        cur.close()
        db.close()
        return arrayListOf<ProfileData>()

    }




    fun addProfile(name: String, birthday: String) {
        val db: SQLiteDatabase = this.writableDatabase
        var profContent: ContentValues = ContentValues()

        profContent.put(KEY_NAME, name)
        profContent.put(KEY_BIRTHDAY, birthday)

        val lastIndex = db.insert(TABLE_NAME, null ,profContent).toInt()
        val service = BirthdayNotificationService(context)

        val formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy")
        val birthdayLocalDate = LocalDate.parse(birthday, formatter)

        service.showNotification(context, name, calcAge(birthday), lastIndex, birthdayLocalDate )
        db.close()

        //inefficient way to update profileList but should work
        profileList = getProfiles()
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




}