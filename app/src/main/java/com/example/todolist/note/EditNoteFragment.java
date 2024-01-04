package com.example.todolist.note;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.todolist.note.AddNewNoteFragment;
import com.example.todolist.interfaces.Editable;
import com.example.todolist.R;

public class EditNoteFragment extends AddNewNoteFragment {
    private Editable editable;
    private String note, description;
    private int pos;

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
                .setView(R.layout.add_note_dialog_layout)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        note = noteTitle.getText().toString();
                        description = noteDescription.getText().toString();
                        editable.edit(pos, note, description, "");
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
        note = getArguments().getString("note");
        pos = getArguments().getInt("note_id");
        noteTitle.setText(note);
    }
}
