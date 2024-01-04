package com.example.todolist.task;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.DateTime;
import com.example.todolist.Note;
import com.example.todolist.NoteDataHolder;
import com.example.todolist.R;

import java.util.ArrayList;
import java.util.Date;

public class TaskRecyclerAdapter extends RecyclerView.Adapter<TaskViewHolder>{
    private ArrayList<Task> taskArrayList;


    public TaskRecyclerAdapter (ArrayList<Task> taskArrayList) {
        this.taskArrayList = taskArrayList;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_list_item, parent, false);

        TaskViewHolder taskViewHolder = new TaskViewHolder(v);
        return taskViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Date date = taskArrayList.get(position).getDate();
        int color = DateTime.calculateLeftTime(date);
        if (color == 0)
            holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.light_orange));
        else if (color == 1 || color == 2)
            holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.light_yellow));
        else
            holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.grey_white));
        holder.getTaskTextView().setText(taskArrayList.get(position).getTaskName());
        holder.getDescriptionTextView().setText(taskArrayList.get(position).getTaskDescription());
        String timeState = DateTime.HOW_MANY_TIME[DateTime.calculateLeftTime(date)];
        holder.getStateTextView().setText(timeState);
        holder.getDateTextView().setText(DateTime.fromDateToString(taskArrayList.get(position).getDate()));
    }
    @Override
    public int getItemCount() {
        return taskArrayList.size();
    }
}
