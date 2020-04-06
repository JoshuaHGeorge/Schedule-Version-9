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
import android.view.inputmethod.InputMethodManager


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

    var c = Calendar.getInstance().getTime();

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
            hideKeyboard(this);
            databaseSubmission();
//            val newAlarm = Button(applicationContext) // make a new item
//
//            newAlarm.text = courseInput.text; // set the text of the new item
//            courseInput.setText(""); // clear the text of the input
//
//            alarmArange.addView(newAlarm); // put the item in the list

//            db.collection("schedules")
//                .get()
//                .addOnCompleteListener { task ->
//                    if (task.isSuccessful) {
//                        for (document in task.result!!) {
////                        Log.d(FragmentActivity.TAG, document.id + " => " + document.data)
//                            val newAlarm = Button(applicationContext) // make a new item
//
//                            newAlarm.text = "test"; // set the text of the new item
//                            courseInput.setText(""); // clear the text of the input
//
//                            alarmArange.addView(newAlarm); // put the item in the list
//                        }
//                    } else {
////                    Log.w(FragmentActivity.TAG, "Error getting documents.", task.exception)
//                    }
//                }

            changeToList(); // change to view the list
            databaseRead();

        }

        databaseRead();
    }

    private fun databaseSubmission()
    {
        val due = dayInput.text.toString().trim();

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

                        var padding = (alarmList.height - newAlarm.height)/ 14 ;
                        var due = (document.get("due"));


                        if(due == null )
                        {
                            padding =  newAlarm.paddingBottom; // padding size if no due date is found or is due/ past due
                        }
                        else {
                            due = due.toString();
                            due = due.toInt();

                            if(due == null )
                            {
                                padding =  newAlarm.paddingBottom; // max padding size if no due date is found
                            }
                            else if (due <= 0)
                            {
                                padding = padding * 14; // max padding size if is due/ past due
                            }
                            else if (due >= 14)
                            {
                                padding =  newAlarm.paddingBottom;
                            }
                            else
                            {
                                padding = padding * (14 - due); // padding set to corrospond with the number of days till due;
                            }
                        }


                        newAlarm.setPadding(newAlarm.paddingLeft, newAlarm.paddingTop, newAlarm.paddingRight, padding);
                        newAlarm.text = document.get("course").toString(); // set the text of the new item

                        courseInput.setText(""); // clear the text of the input

                        alarmArange.addView(newAlarm); // put the item in the list
                    }
                } else {
//                    Log.w(FragmentActivity.TAG, "Error getting documents.", task.exception)
                }
            }
    }

    fun changeToList()
    {
        alarmCreate.visibility = View.GONE;
        alarmList.visibility = View.VISIBLE;
    }
    fun changeToCreate()
    {
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
}
