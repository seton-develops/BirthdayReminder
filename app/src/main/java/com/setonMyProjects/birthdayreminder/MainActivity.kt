package com.setonMyProjects.birthdayreminder

import android.app.Dialog
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.setonMyProjects.birthdayreminder.R
import java.time.Clock
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import kotlin.collections.ArrayList
import kotlin.math.roundToInt

const val OLDEST_AGE_YRS = 125

class MainActivity : AppCompatActivity() {
    //main page
    private lateinit var addBirthdayButton: Button

    //popup menu
    private lateinit var popupSaveButton: Button
    private lateinit var popupCancelButton: Button

    private lateinit var mainActivityRightButton: ImageView

    companion object {
        private var startedApplicationFlag: Boolean = true
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        createNotificationChannel()

        addBirthdayButton = findViewById(R.id.addBirthdayButton)
        mainActivityRightButton = findViewById(R.id.mainActivityRightArrow)

        Thread(Runnable {
            this.runOnUiThread(java.lang.Runnable {
                highlightMonth(this)
                val dbHelper = BirthdayDatabaseHelper(this)

                if (dbHelper.profileList.isNotEmpty()) {
                    updateCountTextViews(this, dbHelper.profileList, true)
                }
                else {
                    updateCountTextViews(this, dbHelper.profileList, false)
                }
            })
        }).start()


        addBirthdayButton.setOnClickListener {
            createPopupForm()
        }

        mainActivityRightButton.setOnClickListener {
            changeToRecycleView()
        }

        /**
         * Should only activate once per application.
         * Resets all notification alarms to avoid updating information on them
         */
        if (startedApplicationFlag) {
            BirthdayNotificationService(this).resetNotifications(this)
            startedApplicationFlag = false
        }


    }




    /**
     * changes activity to next page containing recyclerview
     */
    private fun changeToRecycleView() {
        val intent = Intent(this@MainActivity, RecycleBirthdayActivity::class.java)
        startActivity(intent)
    }




    /**
     * Creates a notification channel
     * notifications remind user of birthdays on the day of
     */
    private fun createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //Create notification channel instance
            val channel = NotificationChannel(
                BirthdayNotificationService.NOTIF_CHANNEL_ID,
                "Birthday Notification",
                NotificationManager.IMPORTANCE_HIGH
            )
            channel.description = "Notification channel for birthday reminder app"
            channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC

            //Create channel
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE)
                    as NotificationManager
            notificationManager.createNotificationChannel(channel)
            notificationManager.deleteNotificationChannel("BirthdayAppChannel1")
        }

    }



    /**
     * Creates a popup window
     * Logic behind saving user entries
     */
    private fun createPopupForm() {
        var (height, width) = getScreenDimensions(this)
        height = (height*0.95).roundToInt()
        width = (width*0.95).roundToInt()

        val dialog = AlertDialog.Builder(this)
        val popupView = layoutInflater.inflate(R.layout.activity_birthday_details_pop_up, null)


        popupCancelButton = popupView.findViewById(R.id.buttonCancel)
        popupSaveButton = popupView.findViewById(R.id.buttonSave)


        val dialogInstance = dialog.setView(popupView).create()
        dialogInstance.show()
        dialogInstance.window?.setLayout(width, height)

        val popupBirthday: TextView = popupView.findViewById(R.id.etPopupBirthday)


        showDateListener(this, popupBirthday) //fine
        popupBirthday.setOnClickListener {
            Log.i("TEST 123", popupBirthday.text.toString())
            if (popupBirthday.text.toString().equals("")) {
                showDateListener(this, popupBirthday)
            }
            else {
                //Edit text was initialized by date picker
                //We need to initialize the date picker to the previously set date
                Log.i("TEST 456", popupBirthday.text.toString())
                val monthInt = popupBirthday.text
                    .subSequence(0,2)
                    .trimStart('0')
                    .toString()
                    .toInt() -1

                val dateInt = popupBirthday.text
                    .subSequence(3,5)
                    .trimStart('0')
                    .toString()
                    .toInt()

                val yearInt = popupBirthday.text
                    .subSequence(6,10)
                    .toString()
                    .toInt()

                showDateListener(this, popupBirthday,yearInt, monthInt, dateInt)
            }
        }


        popupCancelButton.setOnClickListener {
            dialogInstance.dismiss()
        }

        popupSaveButton.setOnClickListener {
            val formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy")

            val popupFullName: EditText = popupView.findViewById(R.id.etPopupFullName)


            try {
                /**
                 * check if entered date is after current date
                 * check if date entered is a valid date
                 */


                val parsedBirthday: LocalDate = LocalDate.parse(popupBirthday.text, formatter)
                val clock = Clock.systemDefaultZone()
                val currentDate = LocalDate.now(ZoneId.of(clock.zone.toString()))

                //if else block to cover fail cases
                if (parsedBirthday.isAfter(currentDate)) { //user chose date in future
                    VibratingToast(this, "DATE OCCURS AFTER CURRENT DATE", 3)
                    return@setOnClickListener
                }
                else if (calcAge(popupBirthday.text.toString()) > OLDEST_AGE_YRS) {
                    VibratingToast(this, "AGE LIMIT EXCEEDED (125 YRS)", 3)
                    return@setOnClickListener
                }
                else if (!parsedBirthday.isLeapYear &&
                    parsedBirthday.monthValue == 2 &&
                    popupBirthday.text.toString().substring(3,5) == "29") {

                    VibratingToast(this, "ENTERED YEAR IS NOT A LEAP YEAR.", 3)
                    return@setOnClickListener
                }

                //Check if user entered a name
                if (popupFullName.text.toString().trim().isEmpty()) {
                    VibratingToast(this, "PLEASE ENTER A NAME", 3)
                    return@setOnClickListener
                }


                //Create a database helper to allow us to interface with a database
                val dbHelper = BirthdayDatabaseHelper(this@MainActivity)

                //Add to database
                dbHelper.addProfile(popupFullName.text.toString(),
                                    popupBirthday.text.toString())
                VibratingToast(this, "Success. Birthday Added", 3)

                val arr: ArrayList<ProfileData> = dbHelper.profileList
                if (arr.isNotEmpty()) {
                    updateCountTextViews(this, arr, true)
                }
                else {
                    updateCountTextViews(this, arr, false)
                }

                dialogInstance.dismiss()


            }
            catch (e: DateTimeParseException) {
                VibratingToast(this, "Invalid Date Format! Use MM/dd/yyyy", 3)
            }

        }

    }




}

