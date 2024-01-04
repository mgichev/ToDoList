package com.example.todolist.note;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.todolist.R;
import com.example.todolist.interfaces.Addable;

public class AddNewNoteFragment extends DialogFragment {

    protected EditText noteDescription;
    protected EditText noteTitle;
    protected Addable addable;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        addable = (Addable) context;
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
                .setView(R.layout.add_note_dialog_layout)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = noteTitle.getText().toString();
                        String description = noteDescription.getText().toString();
                        addable.add(name, description, "");
                    }
                })
                .setNegativeButton("Cancel", null);


        return builder.show();
    }

    protected void initDialogViews() {
        Dialog dialog = getDialog();
        noteTitle = dialog.findViewById(R.id.add_note_title_edit_text);
        noteDescription = dialog.findViewById(R.id.add_description_note_edit_text);
    }
}