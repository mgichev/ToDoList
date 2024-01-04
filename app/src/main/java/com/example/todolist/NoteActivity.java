package com.example.todolist;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

import com.example.todolist.note.AddNewNoteFragment;
import com.example.todolist.task.AddNewTaskFragment;

import java.util.ArrayList;

public class NoteActivity extends AppCompatActivity {

    EditText noteTitleTextView;
    EditText noteDescriptionTextView;
    private int id = 0;
    ArrayList<Note> noteList = new ArrayList<Note>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        noteTitleTextView = findViewById(R.id.note_title_edit_text);
        noteDescriptionTextView = findViewById(R.id.note_description_edit_text);
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                String title, description;
                title = noteTitleTextView.getText().toString();
                description = noteDescriptionTextView.getText().toString();
                Note note = new Note(title, description);
                NoteDataHolder.getNoteList().set(id, note);
               finish();
            }
        });
        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);
        noteList = NoteDataHolder.getNoteList();
        noteTitleTextView.setText(noteList.get(id).getTitle());
        noteDescriptionTextView.setText(noteList.get(id).getDescription());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar_note_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();


        if (id == R.id.back_button_menu_item) {
            getOnBackPressedDispatcher().onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}

