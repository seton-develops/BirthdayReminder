package com.example.birthdayreminder

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.time.LocalDate
import java.util.*
import kotlin.collections.HashMap


class RecycleBirthdayActivity: AppCompatActivity() {

    private lateinit var recycleView: RecyclerView

    companion object {
        lateinit var recyclerViewData: ArrayList<ProfileData>
        var countMonthsMap: HashMap<Int, Int> = HashMap<Int, Int>()


    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_birthdays)

        recycleView = findViewById(R.id.birthdayRecycleVIew)

        recyclerViewData = ArrayList<ProfileData>(20)

        //recyclerViewData.add(ProfileData("Adam Williams", LocalDate.of(2020, 12, 22)))


        //SORT ARRAY LIST
        var recyclerViewData2 = recyclerViewData.sortedWith(compareBy(){ it.getDate().month})
        recyclerViewData = ArrayList(recyclerViewData2)



        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val myRecyclerViewAdapter = CustomAdapter(recyclerViewData)
        recycleView.adapter = myRecyclerViewAdapter
        recycleView.layoutManager = layoutManager

        val divider = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)


        recycleView.addItemDecoration(divider)

        myRecyclerViewAdapter.notifyDataSetChanged()


        var returnButton = findViewById<ImageView>(R.id.recycleViewLeftArrow)
        returnButton.setOnClickListener {
            val intent = Intent(this@RecycleBirthdayActivity, MainActivity::class.java)
            startActivity(intent)
        }
    }



    fun updateCount(month: Int) {
        countMonthsMap[month] = countMonthsMap[month]!! + 1
    }

    fun returnMonthCountMap(): HashMap<Int,Int> {
        return countMonthsMap
    }

    protected fun getBirthdayList() : ArrayList<ProfileData> {
        return recyclerViewData
    }

}