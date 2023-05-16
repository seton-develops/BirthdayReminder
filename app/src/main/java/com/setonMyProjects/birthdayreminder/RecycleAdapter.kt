package com.setonMyProjects.birthdayreminder

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.setonMyProjects.birthdayreminder.R
import java.time.Clock
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import kotlin.math.roundToInt


class RecycleAdapter(private val context: Context,
                     private val profileList: ArrayList<ProfileData>):
    RecyclerView.Adapter<RecycleAdapter.RecycleViewHolder>() {

    inner class RecycleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name: TextView = itemView.findViewById(R.id.tvProfileFullName)
        var date: TextView =  itemView.findViewById(R.id.tvProfileBirthdayDate)
        var age: TextView = itemView.findViewById(R.id.tvProfileAge)
        var editButton: Button = itemView.findViewById(R.id.buttonProfileEdit)
        var delButton: Button = itemView.findViewById(R.id.buttonProfileDelete)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecycleViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.profile_layout,
                                                                parent,
                                                                false)
        return RecycleViewHolder(view)
    }


    override fun onBindViewHolder(holder: RecycleViewHolder, position: Int) {
        holder.name.text = profileList[position].getName()
        holder.date.text = profileList[position].getDate()
        holder.age.text = profileList[position].getAge().toString()

        val clock = Clock.systemDefaultZone()

        holder.editButton.setOnClickListener {

            var (height, width) = getScreenDimensions(context)
            height = (height*0.95).roundToInt()
            width = (width*0.95).roundToInt()


            val dialog = AlertDialog.Builder(context)
            val popupView = LayoutInflater.from(context)
                .inflate(R.layout.activity_birthday_details_pop_up, null)

            //widgets from popup layout
            val popupSaveButton = popupView.findViewById<Button>(R.id.buttonSave)
            val popupCancelButton = popupView.findViewById<Button>(R.id.buttonCancel)
            val popupFullName = popupView.findViewById<EditText>(R.id.etPopupFullName)
            val popupBirthday = popupView.findViewById<TextView>(R.id.etPopupBirthday)

            //Preset editText widgets to what is currently there
            popupFullName.setText(profileList[position].getName())
            popupBirthday.setText(profileList[position].getDate())

            val dialogInstance = dialog.setView(popupView).create()
            dialogInstance.show()
            dialogInstance.window?.setLayout(width, height)


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

            showDateListener(context, popupBirthday,yearInt, monthInt, dateInt)
            popupBirthday.setOnClickListener {

                val monthInt2 = popupBirthday.text
                    .subSequence(0,2)
                    .trimStart('0')
                    .toString()
                    .toInt() -1

                val dateInt2 = popupBirthday.text
                    .subSequence(3,5)
                    .trimStart('0')
                    .toString()
                    .toInt()

                val yearInt2 = popupBirthday.text
                    .subSequence(6,10)
                    .toString()
                    .toInt()
                showDateListener(context, popupBirthday, yearInt2, monthInt2, dateInt2)
            }

            //Dismiss if cancel button is clicked
            popupCancelButton.setOnClickListener {
                dialogInstance.dismiss()
            }

            //Save Changes
            popupSaveButton.setOnClickListener {
                val formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy")

                try {

                    /**
                     * check if entered date is after current date
                     * check if date entered is a valid date
                     */

                    val parsedBirthday: LocalDate = LocalDate.parse(popupBirthday.text, formatter)
                    if (LocalDate.parse(popupBirthday.text, formatter).isAfter(
                            LocalDate.now(ZoneId.of(clock.zone.toString()))
                        )) {
                        VibratingToast(context, "Date occurs after current date", 3)
                        return@setOnClickListener
                    }
                    else if (calcAge(popupBirthday.text.toString()) > OLDEST_AGE_YRS) {
                        VibratingToast(context, "AGE LIMIT EXCEEDED (125 YRS)", 3)
                        return@setOnClickListener
                    }
                    else if (!parsedBirthday.isLeapYear &&
                        parsedBirthday.monthValue == 2 &&
                        popupBirthday.text.toString().substring(3,5) == "29") {

                        VibratingToast(context, "ENTERED YEAR IS NOT A LEAP YEAR. ", 3)
                        return@setOnClickListener
                    }

                    //Check if user entered a name
                    if (popupFullName.text.toString().trim().isEmpty()) {
                        VibratingToast(context, "PLEASE ENTER A NAME", 3)
                        return@setOnClickListener
                    }

                    //Create a database helper to allow us to interface with a database
                    val dbHelper = BirthdayDatabaseHelper(context)

                    //edit row in database
                    dbHelper.editProfile(popupFullName.text.toString(),
                        popupBirthday.text.toString(),
                        position)

                    VibratingToast(context, "Success. Birthday Profile Changed", 3)

                    //Change arrayList to update recyclerView
                    profileList[position].setName(popupFullName.text.toString())
                    profileList[position].setDate(popupBirthday.text.toString())
                    profileList[position].refreshAge()

                    //Update the notification
                    BirthdayNotificationService(context).showNotification(
                        context = context,
                        name = profileList[position].getName(),
                        age = profileList[position].getAge(),
                        id = profileList[position].getIndex(),
                        birthday = stringDateToLocalDate(profileList[position].getDate())
                    )

                    notifyDataSetChanged()

                    dialogInstance.dismiss()
                }
                catch (e: DateTimeParseException) {
                    VibratingToast(context, "Invalid Date Format! Use MM/dd/yyyy", 3)
                }
            }

        }



        holder.delButton.setOnClickListener() {
            val dbHelper = BirthdayDatabaseHelper(context)
            dbHelper.deleteProfile(position)
            profileList.removeAt(position)
            notifyDataSetChanged()
        }
    }



    override fun getItemCount(): Int {
        return profileList.size
    }


}