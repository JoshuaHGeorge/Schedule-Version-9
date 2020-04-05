package com.example.scheduleversion9

class Schedule {
    var scheduleId: String? = null;
    var course: String? = null;

    constructor(){}

    constructor(scheduleId: String, course: String)
    {
        this.scheduleId = scheduleId;
        this.course = course;
    }
}