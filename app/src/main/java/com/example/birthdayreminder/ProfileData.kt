package com.example.birthdayreminder

import android.os.Build
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*
import kotlin.properties.Delegates

class ProfileData {
    private lateinit var name: String
    private lateinit var birthday: LocalDate



    constructor(name: String, birthday: LocalDate) {

        this.name = name
        this.birthday = birthday
    }

    fun setName(newName: String) {
        this.name = newName
    }

    fun getName() : String {
        return this.name
    }

    fun setDate(newDate: LocalDate) {
        this.birthday = newDate
    }

    fun getDate(): LocalDate {
        return this.birthday
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getDateAsString(): String {
        return toSimpleString()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun toSimpleString() : String {
        return this.birthday.monthValue.toString() + "/" + this.birthday.dayOfMonth.toString() + "/" + this.birthday.year.toString()
    }



}