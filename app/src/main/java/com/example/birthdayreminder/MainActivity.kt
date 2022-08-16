package com.example.birthdayreminder

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.*

class MainActivity : AppCompatActivity() {
    //main page
    private lateinit var addBirthdayButton: Button

    //popup menu

    private lateinit var popupSaveButton: Button
    private lateinit var popupCancelButton: Button
    private lateinit var mainActivityRightButton: ImageView




    //from second activity

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        addBirthdayButton = findViewById(R.id.addBirthdayButton)
        mainActivityRightButton = findViewById(R.id.mainActivityRightArrow)

        Thread(Runnable {
            this.runOnUiThread(java.lang.Runnable {
                updateCountTextViews()
            })
        }).start()


        addBirthdayButton.setOnClickListener {
            createPopupForm()
        }

        mainActivityRightButton.setOnClickListener {
            changeToRecycleView()
        }



    }

    private fun changeToRecycleView() {
        val intent = Intent(this@MainActivity, RecycleBirthdayActivity::class.java)
        startActivity(intent)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createPopupForm() {
        val dialog = AlertDialog.Builder(this)
        val popupView = layoutInflater.inflate(R.layout.activity_birthday_details_pop_up, null)

        popupCancelButton = popupView.findViewById(R.id.buttonCancel)
        popupSaveButton = popupView.findViewById(R.id.buttonSave)

        val dialogInstance = dialog.setView(popupView).create()
        dialogInstance.show()

        popupCancelButton.setOnClickListener {
            dialogInstance.dismiss()
        }

        popupSaveButton.setOnClickListener {
            val formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy")
            val myValidDate: LocalDate

            val popupFullName: EditText = popupView.findViewById(R.id.tvFullName)
            val popupBirthday: EditText = popupView.findViewById(R.id.tvBirthdayDate)

            try {
                myValidDate = LocalDate.parse(popupBirthday.text, formatter)
                val newData: ProfileData = ProfileData(popupFullName.text.toString(),  myValidDate)

                //Create a database helper to allow us to interface with a database
                val dbHelper: BirthdayDatabaseHelper = BirthdayDatabaseHelper(this@MainActivity)

                //Add to database
                dbHelper.addProfile(newData)
                Toast.makeText(this@MainActivity, "Success. Birthday Added", Toast.LENGTH_LONG).show()
                dialogInstance.dismiss()

            }
            catch (e: DateTimeParseException) {
                Toast.makeText(this@MainActivity, "Invalid Date Format! Use MM/dd/yyyy", Toast.LENGTH_LONG).show()
            }



        }


    }



    private fun updateCountTextViews() {
        val JanCount: TextView = findViewById(R.id.tvJanuaryValueMA)
        val FebCount: TextView = findViewById(R.id.tvFebruaryValueMA)
        val MarCount: TextView = findViewById(R.id.tvMarchValueMA)
        val AprCount: TextView = findViewById(R.id.tvAprilValueMA)
        val MayCount: TextView = findViewById(R.id.tvMayValueMA)
        val JunCount: TextView = findViewById(R.id.tvJuneValueMA)
        val JulCount: TextView = findViewById(R.id.tvJulyValueMA)
        val AugCount: TextView = findViewById(R.id.tvAugustValueMA)
        val SepCount: TextView = findViewById(R.id.tvSeptemberValueMA)
        val OctCount: TextView = findViewById(R.id.tvOctoberValueMA)
        val NovCount: TextView = findViewById(R.id.tvNovemberValueMA)
        val DecCount: TextView = findViewById(R.id.tvDecemberValueMA)


        JanCount.setText(0.toString())
        FebCount.setText(0.toString())
        MarCount.setText(0.toString())
        AprCount.setText(0.toString())
        MayCount.setText(0.toString())
        JunCount.setText(0.toString())
        JulCount.setText(0.toString())
        AugCount.setText(0.toString())
        SepCount.setText(0.toString())
        OctCount.setText(0.toString())
        NovCount.setText(0.toString())
        DecCount.setText(0.toString())



    }

    private fun getCountMap(): HashMap<Int,Int> {
        return RecycleBirthdayActivity.countMonthsMap
    }

}

