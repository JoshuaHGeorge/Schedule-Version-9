package com.example.scheduleversion9

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.*

import androidx.recyclerview.widget.RecyclerView


import kotlinx.android.synthetic.main.activity_main.*

class ScheduleList : AppCompatActivity()
{
    var db = FirebaseFirestore.getInstance();

//    private var adapter: ScheduleAdapter? = null

    private var scheduleList = mutableListOf<Schedule>();

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        recyclerViewSchedules.setLayoutManager(LinearLayoutManager(this));

        val query = db.collection("schedules");


    }

//    private inner class ScheduleAdapter internal constructor(options: FirestoreRecyclerOptions<Schedule>)
}