package com.setonMyProjects.birthdayreminder

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import java.time.LocalDate
import java.util.*

const val HOUR_OF_DAY_NOTIFICATION = 9
const val MINUTE_OF_DAY_NOTIFICATION = 30
class BirthdayNotificationService(context: Context) {

    companion object {
        const val NOTIF_CHANNEL_ID = "BirthdayAppChannel2"
    }



    fun showNotification(context: Context,
                          name: String,
                          age: Int,
                          id: Int,
                          birthday: LocalDate) {

        val alarmIntent = Intent(context, BirthdayReceiver::class.java)
        alarmIntent.putExtra("id",id)
        alarmIntent.putExtra("name", name)
        alarmIntent.putExtra("age", age)

        /**
         * Use the ID as the UNIQUE requestCode.
         */
        val alarmPendingIntent = PendingIntent.getBroadcast(
                                                            context,
                                                            id,
                                                            alarmIntent,
                                                            PendingIntent.FLAG_IMMUTABLE)

        val birthdayAlarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager


        if (compareLocalDates(birthday)) {
            birthdayAlarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                calcTimeForNotification(birthday, isCurrentYear = true),
                alarmPendingIntent)
        }
        else {
            birthdayAlarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                calcTimeForNotification(birthday, isCurrentYear = false),
                alarmPendingIntent)
        }


    }

    fun cancelNotification(context: Context, id: Int) {
        val birthdayAlarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmIntent = Intent(context, BirthdayReceiver::class.java)
        val alarmPendingIntent = PendingIntent.getBroadcast(
            context,
            id,
            alarmIntent,
            PendingIntent.FLAG_IMMUTABLE)

        birthdayAlarmManager.cancel(alarmPendingIntent)
    }

    fun resetNotifications(context: Context) {
        val dbHelper = BirthdayDatabaseHelper(context)
        val arr: ArrayList<ProfileData> = dbHelper.profileList

        if (arr.isNotEmpty()) {
            for (profile in arr) {
                showNotification(
                    context,
                    name = profile.getName(),
                    age = profile.getAge(),
                    id = profile.getIndex(),
                    birthday = stringDateToLocalDate(profile.getDate())
                )
            }
        }


    }



    /**
     * Helper function to calculate the milisec from Jan 1, 1970 UTC to chosen date
     * Used for scheduling Alarm for future
     * @param birthday : LocalDate representation of birthday
     */
    private fun calcTimeForNotification(birthday: LocalDate, isCurrentYear : Boolean): Long {

        val futureDate = Calendar.getInstance()

        var setYear = LocalDate.now().year
        if (!isCurrentYear) {
            setYear++
        }



        //set year, month, hour, day
        futureDate.set(
            setYear,
            birthday.monthValue - 1,
            birthday.dayOfMonth,
            HOUR_OF_DAY_NOTIFICATION,
            MINUTE_OF_DAY_NOTIFICATION
        )


        return futureDate.timeInMillis
    }


}