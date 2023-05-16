package com.setonMyProjects.birthdayreminder


class ProfileData(private var name: String, private var birthday: String, private val dbIndex: Int) {


    private var age: Int = 0
    init {
        this.age = calcAge(birthday)
    }

    fun setName(newName: String) {
        this.name = newName
    }

    fun getName() : String {
        return this.name
    }

    fun refreshAge() {
        this.age = calcAge(birthday)
    }

    fun setDate(newDate: String) {
        this.birthday = newDate
    }

    fun getDate(): String {
        return this.birthday
    }

    fun getAge(): Int {
        return this.age
    }

    fun getIndex() :Int {
        return this.dbIndex
    }


}