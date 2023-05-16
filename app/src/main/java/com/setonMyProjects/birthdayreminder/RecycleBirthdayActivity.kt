package com.setonMyProjects.birthdayreminder

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.setonMyProjects.birthdayreminder.R
import java.util.*


class RecycleBirthdayActivity: AppCompatActivity() {

    private lateinit var recycleView: RecyclerView

    companion object {
        lateinit var recyclerViewData: ArrayList<ProfileData>
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_birthdays)

        recycleView = findViewById(R.id.birthdayRecycleView)

        val dbHelper: BirthdayDatabaseHelper = BirthdayDatabaseHelper(this)
        recyclerViewData = dbHelper.profileList

        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val myRecyclerViewAdapter = RecycleAdapter(this, recyclerViewData)
        recycleView.adapter = myRecyclerViewAdapter
        recycleView.layoutManager = layoutManager

        val divider = DividerItemDecoration(this@RecycleBirthdayActivity, DividerItemDecoration.VERTICAL)
        recycleView.addItemDecoration(divider)

        myRecyclerViewAdapter.notifyDataSetChanged()


        var returnButton = findViewById<ImageView>(R.id.recycleViewLeftArrow)
        returnButton.setOnClickListener {
            val intent = Intent(this@RecycleBirthdayActivity, MainActivity::class.java)
            startActivity(intent)
        }
    }





}