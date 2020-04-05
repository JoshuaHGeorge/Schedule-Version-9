package com.example.scheduleversion9

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.firestore.FirebaseFirestore

import android.icu.util.Calendar
import android.view.View
import android.view.View.VISIBLE
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
//import jdk.nashorn.internal.runtime.ECMAException.getException
//import androidx.test.orchestrator.junit.BundleJUnitUtils.getResult
import com.google.firebase.firestore.QueryDocumentSnapshot
//import org.junit.experimental.results.ResultMatchers.isSuccessful
import com.google.firebase.firestore.QuerySnapshot
import com.google.android.gms.tasks.Task
import androidx.annotation.NonNull
import com.google.android.gms.tasks.OnCompleteListener




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

    var c = Calendar.getInstance().getTime()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navList = findViewById(R.id.nav_list) as Button;
        navCreate = findViewById(R.id.nav_create) as Button;

        alarmList = findViewById(R.id.alarm_list) as ConstraintLayout
        alarmArange = findViewById(R.id.alarm_arange) as LinearLayout
        alarmCreate = findViewById(R.id.alarm_create) as ConstraintLayout

        saveAlarm = findViewById(R.id.save_alarm) as Button;
        courseInput = findViewById(R.id.course_input) as EditText;

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

            databaseRead();

            changeToList(); // change to view the list
        }
    }

    private fun databaseSubmission()
    {
        val newSchedule = HashMap<String, Any>();
        newSchedule.put("course", courseInput.text.toString());

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
}
