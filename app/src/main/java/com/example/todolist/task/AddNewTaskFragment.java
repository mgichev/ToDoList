package com.example.todolist.task;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.todolist.DateTime;
import com.example.todolist.Note;
import com.example.todolist.NoteDataHolder;
import com.example.todolist.R;
import com.example.todolist.interfaces.Addable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AddNewTaskFragment extends DialogFragment {

    protected TextView currentDateTime;
    protected EditText taskDescription;
    protected EditText taskName;
    private Spinner spinner;
    private Button insertButton;
    private Addable addable;
    private final String GET_STRING = "/get";
    private String newText = "";
    protected Calendar dateAndTime = Calendar.getInstance();
    protected Date date = new Date();


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        addable = (Addable) context;
        if(date.getTime() != dateAndTime.getTimeInMillis()) {
            long temp = date.getTime() -  dateAndTime.getTimeInMillis();
            int dif = (int) temp;
            dateAndTime.add(Calendar.MILLISECOND, dif);
        }
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initDialogViews();
        return super.onCreateView(inflater, container, savedInstanceState);
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder
                .setView(R.layout.add_task_dialog_layout)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = taskName.getText().toString();
                        String description = taskDescription.getText().toString();
                        String dateTime = DateTime.fromDateToString(date);
                        addable.add(name, description, dateTime);
                    }
                })
                .setNegativeButton("Cancel", null);


        return builder.show();
    }

    protected void initDialogViews() {
        Dialog dialog = getDialog();
        Context context = dialog.getContext();
        currentDateTime = dialog.findViewById(R.id.task_date_time_text_view);
        taskDescription = dialog.findViewById(R.id.add_description_edit_text);
        spinner = dialog.findViewById(R.id.notes_spinner);
        insertButton = dialog.findViewById(R.id.insert_button);
        ArrayList<String> list = new ArrayList<String>();
        int id = 0;
        list.add(0 + ". None");
        id++;
        for (Note n : NoteDataHolder.getNoteList()) {
            String addStr = n.getTitle();
            int maxLength = Math.min(15, addStr.length());
            String outputStr = addStr.substring(0, maxLength);
            if (addStr.length() > outputStr.length()) {
                outputStr += "...";
            }
            outputStr = id + ". " + outputStr;
            list.add(outputStr);
            id++;
        }
        ArrayAdapter<String> adapter = new ArrayAdapter(context,
                android.R.layout.simple_spinner_item, list);
        spinner.setAdapter(adapter);

        AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    try {
                        int realPosition = position - 1;
                        newText = NoteDataHolder.getNote(realPosition).getDescription();
                        if (newText.length() != 0) {
                            newText += "\n";
                        }
                    }
                    catch (Exception e) {
                        newText = "";
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
        spinner.setOnItemSelectedListener(itemSelectedListener);
        insertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentText = taskDescription.getText().toString();
                currentText += newText;
                newText = "";
                taskDescription.setText(currentText);
            }
        });
        taskName = dialog.findViewById(R.id.add_task_name_edit_text);

        Button timeButton, calendarButton;

        timeButton = dialog.findViewById(R.id.open_time_button);
        calendarButton = dialog.findViewById(R.id.open_calendar_button);
        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTime(context);
            }
        });

        calendarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate(context);
            }
        });
    }
    protected void changeDateTime() {
        String outputTime = DateTime.fromLongToString(dateAndTime.getTimeInMillis());
        currentDateTime.setText(outputTime);
        date = new Date(dateAndTime.getTimeInMillis());
    }

    protected void setDate(Context context){
        int a = 0;
        new DatePickerDialog(context, onDateSetListener,
                dateAndTime.get(Calendar.YEAR),
                dateAndTime.get(Calendar.MONTH),
                dateAndTime.get(Calendar.DAY_OF_MONTH))
                .show();

    }
    protected void setTime(Context context) {
        new TimePickerDialog(context, onTimeSetListener,
                dateAndTime.get(Calendar.HOUR_OF_DAY),
                dateAndTime.get(Calendar.MINUTE), true)
                .show();
    }
    protected TimePickerDialog.OnTimeSetListener onTimeSetListener=new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            dateAndTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
            dateAndTime.set(Calendar.MINUTE, minute);
            changeDateTime();
        }
    };
    protected DatePickerDialog.OnDateSetListener onDateSetListener =new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            dateAndTime.set(Calendar.YEAR, year);
            dateAndTime.set(Calendar.MONTH, month);
            dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            changeDateTime();
        }
    };
}