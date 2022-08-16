package com.example.birthdayreminder

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import java.util.*
import androidx.recyclerview.widget.RecyclerView

import kotlin.properties.Delegates

class CustomAdapter(private val dataSet: ArrayList<ProfileData>) :
    RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var fullName: TextView
        var birthday: TextView
        private var editButton: Button
        private var delButton: Button


        init {
            fullName = view.findViewById(R.id.tvProfileFullName)
            birthday = view.findViewById(R.id.tvProfileBirthdayDate)
            editButton = view.findViewById(R.id.buttonProfileEdit)
            delButton = view.findViewById(R.id.buttonProfileDelete)

        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.profile, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.fullName.text = dataSet[position].getName()
        viewHolder.birthday.text = dataSet[position].toSimpleString()
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}
