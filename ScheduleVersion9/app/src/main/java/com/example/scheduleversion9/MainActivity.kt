package com.example.scheduleversion9

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.firestore.FirebaseFirestore

import android.icu.util.Calendar
import android.view.View
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet

class MainActivity : AppCompatActivity() {

    var db = FirebaseFirestore.getInstance()

    private lateinit var alarmList: ConstraintLayout;
    private lateinit var alarmArange: LinearLayout;

    private lateinit var alarmCreate: ConstraintLayout;
//    internal var alarmPlacement: ConstraintSet;

    private lateinit var saveAlarm: Button;
    private lateinit var courseInput: EditText;

    var c = Calendar.getInstance().getTime()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        alarmList = findViewById(R.id.alarm_list) as ConstraintLayout
        alarmArange = findViewById(R.id.alarm_arange) as LinearLayout
        alarmCreate = findViewById(R.id.alarm_create) as ConstraintLayout

        saveAlarm = findViewById(R.id.save_alarm) as Button;
        courseInput = findViewById(R.id.course_input) as EditText;

        saveAlarm.setOnClickListener()
        {
            val newAlarm = Button(applicationContext) // make a new item

            newAlarm.text = courseInput.text; // set the text of the new item
            courseInput.setText(""); // clear the text of the input

            alarmArange.addView(newAlarm); // put the item in the list

            changeToList(); // change to view the list
        }
    }

    private fun changeToList()
    {
        alarmCreate.visibility = View.GONE;
        alarmList.visibility = View.VISIBLE;
    }
}
