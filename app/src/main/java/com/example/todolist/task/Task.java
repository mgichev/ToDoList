package com.example.todolist.task;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Task {

    private int id;
    private String taskName;
    private String taskDescription;
    private Date dateTime;

    public Task(int id, String taskName, String taskDescription, Date date) {
        this.id = id;
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.dateTime = date;
    }


    public String getTaskDescription() {
        return taskDescription;
    }

    public String getTaskName() {
        return taskName;
    }

    public Date getDate() { return dateTime; }

    public int getId() {
        return id;
    }
}
