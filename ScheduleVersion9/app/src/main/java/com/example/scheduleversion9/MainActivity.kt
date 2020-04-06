package com.example.scheduleversion9

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.firestore.FirebaseFirestore

import android.icu.util.Calendar
import android.view.View
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
//import jdk.nashorn.internal.runtime.ECMAException.getException
//import androidx.test.orchestrator.junit.BundleJUnitUtils.getResult
//import org.junit.experimental.results.ResultMatchers.isSuccessful
import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.view.Gravity
import android.view.OrientationEventListener
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.inputmethod.InputMethodManager
import com.google.common.primitives.UnsignedBytes.toInt
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap


class MainActivity : AppCompatActivity() {

    var db = FirebaseFirestore.getInstance()

    private lateinit var navList: Button;
    private lateinit var navCreate: Button;

    private lateinit var alarmList: ConstraintLayout;
    private lateinit var alarmArange: LinearLayout;

    private lateinit var alarmCreate: ConstraintLayout;
//    internal var alarmPlacement: ConstraintSet;

    private lateinit var saveAlarm: Button;
    private lateinit var courseInput: EditText;
    private lateinit var dayInput: EditText;
    private lateinit var monthInput: EditText;
    private lateinit var yearInput: EditText;
    private lateinit var hourInput: EditText;
    private lateinit var minuteInput: EditText;
    private lateinit var ampm: Button;

    var current = Calendar.getInstance().getTime();

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navList = findViewById(R.id.nav_list) as Button;
        navCreate = findViewById(R.id.nav_create) as Button;

        alarmList = findViewById(R.id.alarm_list) as ConstraintLayout
        alarmArange = findViewById(R.id.alarm_arrange) as LinearLayout
        alarmCreate = findViewById(R.id.alarm_create) as ConstraintLayout

        saveAlarm = findViewById(R.id.save_alarm) as Button;
        courseInput = findViewById(R.id.course_input) as EditText;
        dayInput = findViewById(R.id.day_input) as EditText;
        monthInput = findViewById(R.id.month_input) as EditText;
        yearInput = findViewById(R.id.year_input) as EditText;
        hourInput = findViewById(R.id.hour_input) as EditText;
        minuteInput = findViewById(R.id.minute_input) as EditText;
        ampm = findViewById(R.id.ampm) as Button;

        navList.setOnClickListener()
        {
            changeToList();
        }
        navCreate.setOnClickListener()
        {
            changeToCreate();
        }

        saveAlarm.setOnClickListener()
        {
            if(courseInput.text.toString() == ""
                ||dayInput.text.toString() == ""
                ||monthInput.text.toString() == ""
                ||yearInput.text.toString() == ""
                ||hourInput.text.toString() == ""
                ||minuteInput.text.toString() == "")
            {
                Toast.makeText(this, "Form Incomplete", Toast.LENGTH_LONG).show()
            }
            else if(dayInput.text.toString().toInt() == null ||
                monthInput.text.toString().toInt() == null ||
                yearInput.text.toString().toInt() == null ||
                hourInput.text.toString().toInt() == null ||
                minuteInput.text.toString().toInt() == null)
            {
                Toast.makeText(this, "Non-number Input", Toast.LENGTH_LONG).show()
            }
            else if(dayInput.text.toString().toInt() < 1 ||
                monthInput.text.toString().toInt() < 1 ||
                yearInput.text.toString().toInt() < 0 ||
                hourInput.text.toString().toInt() < 0 ||
                minuteInput.text.toString().toInt() < 0 ||
                dayInput.text.toString().toInt() > 31 ||
                monthInput.text.toString().toInt() > 12 ||
                yearInput.text.toString().toInt() > 99 ||
                hourInput.text.toString().toInt() >  12 ||
                minuteInput.text.toString().toInt() > 59)
            {
                Toast.makeText(this, "Time out of range", Toast.LENGTH_LONG).show()
            }
            else
            {
                hideKeyboard(this);
                databaseSubmission();
                changeToList(); // change to view the list
                databaseRead();
            }
        }

        ampm.setOnClickListener()
        {
            if(ampm.text == "am")
            {
                ampm.text = "pm"
            }
            else
            {
                ampm.text = "am"
            }
        }

        databaseRead();
    }

    override fun onConfigurationChanged(newConfig: Configuration)
    {
        super.onConfigurationChanged(newConfig);
        databaseRead();
    }


    private fun databaseSubmission()
    {
        var hour = hourInput.text.toString().toInt();
        if(ampm.text == "pm" || hour != 12)
        {
            hour += 12;
        }

        val due = dayInput.text.toString().trim() + "/" +
                monthInput.text.toString().trim() + "/20" +
                yearInput.text.toString().trim() + " " +
                hour + ":" +
                dayInput.text.toString().trim();

        val newSchedule = HashMap<String, Any>();
        newSchedule.put("course", courseInput.text.toString().trim());
        newSchedule.put("due", due);

        // Add a new schedule with a generated ID
        db.collection("schedules")
            .add(newSchedule)
            .addOnSuccessListener { documentReference ->
                Toast.makeText(this, "Alarm saved with ID: " + documentReference.id, Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error: " + e.message, Toast.LENGTH_LONG).show()
            }
    }

    private fun databaseRead()
    {
        alarmArange.removeAllViewsInLayout();

        db.collection("schedules")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
//                        Log.d(FragmentActivity.TAG, document.id + " => " + document.data)
                        val newAlarm = Button(applicationContext) // make a new item

//                        val params = LinearLayout.LayoutParams(
//                            LinearLayout.LayoutParams.WRAP_CONTENT,
//                            LinearLayout.LayoutParams.WRAP_CONTENT
//                        ).apply {
//                            weight = 1.0f
//                            gravity = Gravity.BOTTOM
//                        }
//
//                        newAlarm.layoutParams = (params);

                        var padding = (alarmArange.height - newAlarm.height)/ 15 ;
                        var holdDue = (document.get("due"));
                        val CurrentDate = current.time;

                        val date1: Date
                        val date2: Date

                        val dates = SimpleDateFormat("dd/MM/yyyy hh:mm")

                        //Setting dates
                        //date1 = dates.parse(CurrentDate)
                        date2 = dates.parse(holdDue.toString())

                        //Comparing dates

                        val difference = date2.getTime() - CurrentDate;
                        val due = (difference / (24 * 60 * 60 * 1000)).toInt();

                        newAlarm.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
//
//                        if(due == null || due == "")
//                        {
//                            padding =  newAlarm.paddingBottom; // padding size if no due date is found or is due/ past due
//                        }
//                        else {
//                            due = due.toString();
//                            due = due.toInt();

                            if(due == null )
                            {
                                padding =  newAlarm.paddingBottom; // max padding size if no due date is found
                            }
                            else if (due <= 0 &&
                                0 >= difference)
                            {
                                padding = newAlarm.paddingBottom + (padding * (14)) - 40;// max padding size if is due/ past due
                                newAlarm.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                            }
                            else if (due <= 0 )
                            {
                                padding = newAlarm.paddingBottom + (padding * (14)) - 40;// max padding size if is due/ past due
                            }
                            else if (due >= 14)
                            {
                                padding =  newAlarm.paddingBottom;
                            }
                            else
                            {
                                padding = newAlarm.paddingBottom  + (padding * (14 - due)) - 40; // padding set to corrospond with the number of days till due;
                            }
//                        }


                        newAlarm.setPadding(newAlarm.paddingLeft, newAlarm.paddingTop, newAlarm.paddingRight, padding);
                        newAlarm.text = document.get("course").toString(); // set the text of the new item
                        newAlarm.contentDescription = document.get("due").toString();

                        while(newAlarm.height >= alarmArange.height){
                            if(due == 1){
                                padding -= 20;
                            }
                            padding--;
                            newAlarm.setPadding(newAlarm.paddingLeft, newAlarm.paddingTop, newAlarm.paddingRight, padding);
                        }

                        newAlarm.setOnClickListener(){
                            daysTillDue(newAlarm.contentDescription.toString())
                        }

                        courseInput.setText(""); // clear the text of the input
                        dayInput.setText(""); // clear the text of the input
                        monthInput.setText(""); // clear the text of the input
                        yearInput.setText(""); // clear the text of the input
                        hourInput.setText(""); // clear the text of the input
                        minuteInput.setText(""); // clear the text of the input
                        ampm.text = "am"

//                        newAlarm.gravity = 50;
                        alarmArange.addView(newAlarm); // put the item in the list
//                        newAlarm.gravity = 50;
                    }
                } else {
//                    Log.w(FragmentActivity.TAG, "Error getting documents.", task.exception)
                }
            }
    }

    fun changeToList()
    {
        hideKeyboard(this);
        alarmCreate.visibility = View.GONE;
        alarmList.visibility = View.VISIBLE;
    }
    fun changeToCreate()
    {
        hideKeyboard(this);
        alarmList.visibility = View.GONE;
        alarmCreate.visibility = View.VISIBLE;
    }

    // sourced from
    // https://stackoverflow.com/questions/1109022/close-hide-android-soft-keyboard
    fun hideKeyboard(activity: Activity) {
        val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        //Find the currently focused view, so we can grab the correct window token from it.
        var view = activity.currentFocus
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = View(activity)
        }
        imm!!.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun daysTillDue(FinalDate: String)
    {
        // sourced from https://stackoverflow.com/questions/21285161/android-difference-between-two-dates
        val CurrentDate = current.time;

        val date1: Date
        val date2: Date

        val dates = SimpleDateFormat("dd/MM/yyyy hh:mm")

        //Setting dates
        //date1 = dates.parse(CurrentDate)
        date2 = dates.parse(FinalDate)

        //Comparing dates

        val difference = date2.getTime() - CurrentDate;
        val differenceDays = difference / (24 * 60 * 60 * 1000);
        val differenceHours = (difference / ( 60 * 60 * 1000)) % 24;

        var toasty = "";

        //Convert long to String
        if(differenceDays <= 0 && differenceHours <= 0)
        {
            toasty = "Past Due"
        }
        else if(differenceDays.toInt() == 1 && differenceHours.toInt() == 1)
        {
            toasty = "Due in: " + differenceDays.toInt().toString() + " Day, " + differenceHours.toInt().toString() + " Hour";
        }
        else if(differenceDays.toInt() == 1)
        {
            toasty = "Due in: " + differenceDays.toInt().toString() + " Day, " + differenceHours.toInt().toString() + " Hours";
        }
        else if(differenceHours.toInt() == 1)
        {
            toasty = "Due in: " + differenceDays.toInt().toString() + " Days, " + differenceHours.toInt().toString() + " Hour";
        }
        else {
            toasty = "Due in: " + differenceDays.toInt().toString() + " Days, " + differenceHours.toInt().toString() + " Hours";
        }

        Toast.makeText(this, toasty.toString(), Toast.LENGTH_LONG).show()
    }
}
