package com.example.todolist.task;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.TaskDatabaseHelper;
import com.example.todolist.DateTime;
import com.example.todolist.R;
import com.example.todolist.interfaces.Addable;
import com.example.todolist.interfaces.Editable;
import com.example.todolist.interfaces.Removable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;

public class TaskListFragment extends Fragment implements Removable, Addable, Editable {

    protected RecyclerView myRecyclerView;
    protected RecyclerView.LayoutManager myLayoutManager;

    protected ArrayList<Task> todayThingsList = new ArrayList<Task>();
    protected TaskRecyclerAdapter mAdapter;

    protected TaskDatabaseHelper taskDatabaseHelper;
    protected SQLiteDatabase database;
    protected Cursor cursor;

    protected void initListFromDatabase() {
        cursor = database.rawQuery("SELECT * from " + TaskDatabaseHelper.TABLE, null);
        String name, description, date;
        int id;
        todayThingsList.clear();
        while (cursor.moveToNext()) {
            id = cursor.getInt(0);
            name = cursor.getString(1);
            description = cursor.getString(2);
            date = cursor.getString(3);
            Task task = new Task(id, name, description, DateTime.fromStringToDate(date));
            todayThingsList.add(task);
        }
        todayThingsList.sort(new Comparator<Task>() {
            @Override
            public int compare(Task o1, Task o2) {
                return Long.compare(o1.getDate().getTime(), o2.getDate().getTime());
            }
        });
    }

    protected void openDatabase(@NonNull Context context) {
        taskDatabaseHelper = new TaskDatabaseHelper(context);
        database = taskDatabaseHelper.getReadableDatabase();
    }

    protected void closeDatabaseObjects() {
        database.close();
        cursor.close();
    }


    protected void setRecyclerView(View root) {
        myRecyclerView.setHasFixedSize(true);
        myLayoutManager = new LinearLayoutManager(root.getContext());
        myRecyclerView.setLayoutManager(myLayoutManager);
    }

    protected void setAdapter() {
        DividerItemDecoration myDivider = new DividerItemDecoration(myRecyclerView.getContext(), RecyclerView.VERTICAL);
        myDivider.setDrawable(AppCompatResources.getDrawable(this.getContext(), R.drawable.divider_drawable));
        myRecyclerView.addItemDecoration(myDivider);
        myRecyclerView.setAdapter(mAdapter);
    }

    protected void setDefaultValues() {
        Date date = new Date(Calendar.getInstance().getTimeInMillis());
        this.add("Помыть посуду", "Нужно помыть посуду", DateTime.fromDateToString(date));
        this.add("Вынести мусор", "Нужно вынести мусор", DateTime.fromDateToString(date));
        this.add("Сходить в бассейн", "Пора идти в бассейн", DateTime.fromDateToString(date));
    }


    @Override
    public void remove(int position) {
        Task removableTask = todayThingsList.get(position);
        int id = removableTask.getId();
        todayThingsList.remove(position);
        mAdapter.notifyItemRemoved(position);
        database.delete(TaskDatabaseHelper.TABLE, "id = ?", new String[]{String.valueOf(id)});
    }

    @Override
    public void add(String task, String description, String dateTime) {
        ContentValues cv = new ContentValues();
        cv.put(TaskDatabaseHelper.COLUMN_TASK_NAME, task);
        cv.put(TaskDatabaseHelper.COLUMN_DESCRIPTION, description);
        cv.put(TaskDatabaseHelper.COLUMN_DATE, dateTime);
        database.insert(TaskDatabaseHelper.TABLE, null, cv);
        cursor = database.rawQuery("SELECT * from " + TaskDatabaseHelper.TABLE, null);
        cursor.moveToLast();
        Task newTask = new Task(cursor.getInt(0), task, description, DateTime.fromStringToDate(dateTime));
        todayThingsList.add(newTask);
        if(mAdapter!=null) {
            mAdapter.notifyItemInserted(todayThingsList.size() - 1);
        }
    }

    @Override
    public void edit(int pos, String name, String description, String date) {
        Task oldTask  = todayThingsList.get(pos);
        int id = oldTask.getId();
        Task task = new Task(id, name, description, DateTime.fromStringToDate(date));
        todayThingsList.set(pos, task);
        mAdapter.notifyItemChanged(pos);
        editDatabaseValue(name, description, date, id);
    }

    protected void editDatabaseValue(String name, String description, String date, int id) {
        ContentValues cv = new ContentValues();
        cv.put(TaskDatabaseHelper.COLUMN_TASK_NAME, name);
        cv.put(TaskDatabaseHelper.COLUMN_DESCRIPTION, description);
        cv.put(TaskDatabaseHelper.COLUMN_DATE, date);
        database.update(TaskDatabaseHelper.TABLE, cv, TaskDatabaseHelper.COLUMN_ID + "=" + id, null);
    }


}