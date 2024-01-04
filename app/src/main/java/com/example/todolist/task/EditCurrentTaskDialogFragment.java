package com.example.todolist.task;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.todolist.R;
import com.example.todolist.interfaces.Editable;
import com.example.todolist.task.AddNewTaskFragment;

public class EditCurrentTaskDialogFragment extends AddNewTaskFragment {
    private String name, description, date;
    private int pos;
    private Editable editable;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        editable = (Editable) context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder
                .setView(R.layout.add_task_dialog_layout)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        name = taskName.getText().toString();
                        description = taskDescription.getText().toString();
                        date = currentDateTime.getText().toString();
                        editable.edit(pos, name, description, date);
                    }
                })
                .setNegativeButton("Cancel", null);
        return builder.show();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initDialogViews();
        setDialogViews();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void setDialogViews() {
        name = getArguments().getString("name");
        description = getArguments().getString("description");
        date = getArguments().getString("date");
        pos = getArguments().getInt("pos");
        currentDateTime.setText(date);
        taskDescription.setText(description);
        taskName.setText(name);
    }

}