package com.example.todolist.note;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.Note;
import com.example.todolist.R;

import java.util.ArrayList;

public class NoteRecyclerAdapter extends RecyclerView.Adapter<NoteViewHolder>{

    private ArrayList<Note> noteArrayList;

    public NoteRecyclerAdapter(ArrayList<Note> noteArrayList) {
        this.noteArrayList = noteArrayList;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_list_item, parent, false);

        NoteViewHolder noteViewHolder = new NoteViewHolder(v);
        return noteViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        holder.getNoteNameTextView().setText(noteArrayList.get(position).getTitle());
        int id = position + 1;
        holder.getIdTextView().setText("" + id + ".");
    }

    @Override
    public int getItemCount() {
        return noteArrayList.size();
    }
}
