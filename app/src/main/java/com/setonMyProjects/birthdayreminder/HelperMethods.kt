package com.setonMyProjects.birthdayreminder


import android.app.DatePickerDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.util.Log
import android.view.ViewDebug
import android.widget.TextView
import com.setonMyProjects.birthdayreminder.R
import java.time.Clock
import java.time.LocalDate
import java.time.Period
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.roundToInt


/**
 * Determines the time in years between current date and given date
 * @param birthday A string representing date that will be compared to the current LocalDate
 * @return Integer representing the difference between the two dates in years
 */
fun calcAge(birthday: String): Int {
    val formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy")

    val currDate = LocalDate.now()
    val age = Period.between(currDate, LocalDate.parse(birthday, formatter))

    return StrictMath.abs(age.years)
}


fun stringDateToLocalDate(dateString: String): LocalDate {
    val formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy")
    return LocalDate.parse(dateString, formatter)
}


fun compareLocalDates(date: LocalDate): Boolean {
    val clock = Clock.systemDefaultZone()
    val currentDate = LocalDate.now(ZoneId.of(clock.zone.toString()))
    val calendarNow = Calendar.getInstance()

    return if (date.month < currentDate.month) {
        false
    }
    else if (date.month == currentDate.month && date.dayOfMonth < currentDate.dayOfMonth) {
        false
    }
    else if (date.month == currentDate.month && date.dayOfMonth > currentDate.dayOfMonth) {
        true
    }
    else date.month == currentDate.month &&
            date.dayOfMonth == currentDate.dayOfMonth &&
            calendarNow.get(Calendar.HOUR_OF_DAY) <= HOUR_OF_DAY_NOTIFICATION &&
            calendarNow.get(Calendar.MINUTE) < MINUTE_OF_DAY_NOTIFICATION


}


fun updateCountTextViews(activity: MainActivity, arr: ArrayList<ProfileData>, isSetToZero: Boolean) {
    val janCount: TextView = activity.findViewById(R.id.tvJanuaryValueMA)
    val febCount: TextView = activity.findViewById(R.id.tvFebruaryValueMA)
    val marCount: TextView = activity.findViewById(R.id.tvMarchValueMA)
    val aprCount: TextView = activity.findViewById(R.id.tvAprilValueMA)
    val mayCount: TextView = activity.findViewById(R.id.tvMayValueMA)
    val junCount: TextView = activity.findViewById(R.id.tvJuneValueMA)
    val julCount: TextView = activity.findViewById(R.id.tvJulyValueMA)
    val augCount: TextView = activity.findViewById(R.id.tvAugustValueMA)
    val sepCount: TextView = activity.findViewById(R.id.tvSeptemberValueMA)
    val octCount: TextView = activity.findViewById(R.id.tvOctoberValueMA)
    val novCount: TextView = activity.findViewById(R.id.tvNovemberValueMA)
    val decCount: TextView = activity.findViewById(R.id.tvDecemberValueMA)


    var jan = 0
    var feb = 0
    var mar = 0
    var apr = 0
    var may = 0
    var jun = 0
    var jul = 0
    var aug = 0
    var sep = 0
    var oct = 0
    var nov = 0
    var dec = 0

    if (isSetToZero) {
        for (i in arr) {
            when (i.getDate().substring(0, 2)) {
                "01" -> jan++
                "02" -> feb++
                "03" -> mar++
                "04" -> apr++
                "05" -> may++
                "06" -> jun++
                "07" -> jul++
                "08" -> aug++
                "09" -> sep++
                "10" -> oct++
                "11" -> nov++
                "12" -> dec++

            }
        }
    }

    janCount.text = jan.toString()
    febCount.text = feb.toString()
    marCount.text = mar.toString()
    aprCount.text = apr.toString()
    mayCount.text = may.toString()
    junCount.text = jun.toString()
    julCount.text = jul.toString()
    augCount.text = aug.toString()
    sepCount.text = sep.toString()
    octCount.text = oct.toString()
    novCount.text = nov.toString()
    decCount.text = dec.toString()
}



fun highlightMonth(activity: MainActivity) {
    val date = LocalDate.now()
    val currMonth = date.month.toString()

    var currMonthID: TextView = activity.findViewById<TextView>(R.id.tvJanuaryLabelMA)


    when (currMonth.substring(0, 3)) {
        "JAN" -> currMonthID = activity.findViewById<TextView>(R.id.tvJanuaryLabelMA)
        "FEB" -> currMonthID = activity.findViewById<TextView>(R.id.tvFebruaryLabelMA)
        "MAR" -> currMonthID = activity.findViewById<TextView>(R.id.tvMarchLabelMA)
        "APR" -> currMonthID = activity.findViewById<TextView>(R.id.tvAprilLabelMA)
        "MAY" -> currMonthID = activity.findViewById<TextView>(R.id.tvMayLabelMA)
        "JUN" -> currMonthID = activity.findViewById<TextView>(R.id.tvJuneLabelMA)
        "JUL" -> currMonthID = activity.findViewById<TextView>(R.id.tvJulyLabelMA)
        "AUG" -> currMonthID = activity.findViewById<TextView>(R.id.tvAugustLabelMA)
        "SEP" -> currMonthID = activity.findViewById<TextView>(R.id.tvSeptemberLabelMA)
        "OCT" -> currMonthID = activity.findViewById<TextView>(R.id.tvOctoberLabelMA)
        "NOV" -> currMonthID = activity.findViewById<TextView>(R.id.tvNovemberLabelMA)
        "DEC" -> currMonthID = activity.findViewById<TextView>(R.id.tvDecemberLabelMA)
    }

    currMonthID.setBackgroundResource(R.drawable.roundedcorners)
}


fun getScreenDimensions(context: Context): Pair<Int,Int> {
    val height = context.resources.displayMetrics.heightPixels
    val width  = context.resources.displayMetrics.widthPixels
    return Pair(height,width)
}


fun showDateListener(context: Context, tvBirthday: TextView): String {
    val c = Calendar.getInstance()
    val year = c.get(Calendar.YEAR)
    val month = c.get(Calendar.MONTH)
    val day = c.get(Calendar.DAY_OF_MONTH)

    var selectString = ""
    val dpd = DatePickerDialog(context,
        DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->

            val monthOfYear2 = monthOfYear + 1

            var monthString = "$monthOfYear2"
            var dayString = "$dayOfMonth"

            if (monthOfYear < 10) {
                monthString = "0$monthString"
            }
            if (dayOfMonth < 10) {
                dayString = "0$dayString"
            }
            selectString = "$monthString/$dayString/$year"
            tvBirthday.text = selectString
        },
        year,
        month,
        day
    )

    dpd.show()

    return selectString
}


fun showDateListener(context: Context, tvBirthday: TextView,
                     year: Int, month: Int, day: Int): String {

    var selectString = ""
    val dpd = DatePickerDialog(context,
        DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->

            val monthOfYear2 = monthOfYear + 1

            var monthString = "$monthOfYear2"
            var dayString = "$dayOfMonth"

            if (monthOfYear < 10) {
                monthString = "0$monthString"
            }
            if (dayOfMonth < 10) {
                dayString = "0$dayString"
            }
            selectString = "$monthString/$dayString/$year"
            tvBirthday.text = selectString
        },
        year,
        month,
        day
    )

    dpd.show()

    return selectString
}



