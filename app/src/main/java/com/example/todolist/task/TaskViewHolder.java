package com.example.todolist.task;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.MainActivity;
import com.example.todolist.R;
import com.example.todolist.interfaces.Removable;
import com.example.todolist.AskRemoveFragmentDialog;

public class TaskViewHolder extends RecyclerView.ViewHolder {
    private final TextView taskTextView;
    private final TextView stateTextView;
    private final TextView descriptionTextView;
    private final TextView dateTextView;
    private final Button deleteTaskButton;
    private final View view;
    private Removable removable;
    public TaskViewHolder(@NonNull View itemView) {
        super(itemView);

        //removable = (Removable)itemView.getContext();
        taskTextView = (TextView) itemView.findViewById(R.id.taskNameTextView);
        stateTextView = (TextView) itemView.findViewById(R.id.stateTextView);
        descriptionTextView = (TextView) itemView.findViewById(R.id.taskDescriptionTextView);
        dateTextView = (TextView) itemView.findViewById(R.id.dateTextView);
        deleteTaskButton = (Button) itemView.findViewById(R.id.removeTaskButton);
        view = itemView;
        deleteTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = getAbsoluteAdapterPosition();
                Bundle bundle = new Bundle();
                bundle.putInt("pos", pos);
                AskRemoveFragmentDialog askRemoveFragmentDialog = new AskRemoveFragmentDialog();
                askRemoveFragmentDialog.setArguments(bundle);
                MainActivity activity =  (MainActivity) itemView.getContext();
                askRemoveFragmentDialog.show(activity.getSupportFragmentManager(), "AskRemove");
            }
        });
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                String taskNameStr, taskDescriptionStr, taskDateStr;
                int pos = getAbsoluteAdapterPosition();
                taskNameStr = taskTextView.getText().toString();
                taskDescriptionStr = descriptionTextView.getText().toString();
                taskDateStr = dateTextView.getText().toString();
                bundle.putString("name", taskNameStr);
                bundle.putString("description", taskDescriptionStr);
                bundle.putString("date", taskDateStr);
                bundle.putInt("pos", pos);
                EditCurrentTaskDialogFragment editCurrentTaskDialogFragment =
                        new EditCurrentTaskDialogFragment();
                editCurrentTaskDialogFragment.setArguments(bundle);
                MainActivity activity =  (MainActivity) itemView.getContext();
                editCurrentTaskDialogFragment.show(activity.getSupportFragmentManager(), "EditTask");
            }
        });
    }

    public TextView getTaskTextView() {
        return taskTextView;
    }

    public TextView getDescriptionTextView() {
        return descriptionTextView;
    }

    public TextView getDateTextView() {
        return dateTextView;
    }

    public TextView getStateTextView() {
        return stateTextView;
    }
}
