package com.example.todolist.note;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.MainActivity;
import com.example.todolist.R;
import com.example.todolist.AskRemoveFragmentDialog;

public class NoteViewHolder extends RecyclerView.ViewHolder {

    private final TextView noteNameTextView;
    private final TextView idTextView;
    private final Button noteRemoveButton;
    public NoteViewHolder(@NonNull View itemView) {
        super(itemView);
        idTextView = (TextView) itemView.findViewById(R.id.idNoteTextView);
        noteNameTextView = (TextView) itemView.findViewById(R.id.noteNameTextView);
        noteRemoveButton = (Button) itemView.findViewById(R.id.removeNoteButton);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity mainActivity = (MainActivity) itemView.getContext();
                int index = getAbsoluteAdapterPosition();
                mainActivity.edit(index, "", "", "");
//                Bundle bundle = new Bundle();
//                String noteTitle = "";
//                int pos = getAbsoluteAdapterPosition();
//                noteTitle = noteNameTextView.getText().toString();
//                bundle.putString("note", noteTitle);
//                bundle.putInt("note_id", pos);
//                EditNoteFragment editNoteFragment = new EditNoteFragment();
//                editNoteFragment.setArguments(bundle);
//                MainActivity mainActivity = (MainActivity) itemView.getContext();
//                editNoteFragment.show(mainActivity.getSupportFragmentManager(), "EditNote");
            }
        });

        noteRemoveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int index = getAbsoluteAdapterPosition();
                Bundle bundle = new Bundle();
                bundle.putInt("index", index);
                AskRemoveFragmentDialog askRemoveFragmentDialog = new AskRemoveFragmentDialog();
                askRemoveFragmentDialog.setArguments(bundle);
                MainActivity activity =  (MainActivity) itemView.getContext();
                askRemoveFragmentDialog.show(activity.getSupportFragmentManager(), "AskRemove");
            }
        });
    }

    public TextView getNoteNameTextView() {
        return noteNameTextView;
    }

    public TextView getIdTextView() {
        return idTextView;
    }
}
