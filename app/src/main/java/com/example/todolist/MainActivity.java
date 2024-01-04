package com.example.todolist;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.todolist.databinding.ActivityMainBinding;
import com.example.todolist.interfaces.Addable;
import com.example.todolist.interfaces.Editable;
import com.example.todolist.interfaces.Removable;
import com.example.todolist.note.AddNewNoteFragment;
import com.example.todolist.task.AddNewTaskFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity implements Removable, Addable, Editable {

    private ActivityMainBinding binding;

    private Removable removableListener;
    private Addable addableListener;

    private Editable editableListener;

    private NoteDatabaseHelper noteDatabaseHelper;
    private SQLiteDatabase database;
    private Cursor cursor;

    private static boolean isCurrentFragmentNotes = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        BottomNavigationView navView = findViewById(R.id.nav_view);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_today, R.id.navigation_all, R.id.navigation_projects)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
        openDatabase(this);
        initNoteDataHolder();
        closeDatabaseObjects();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();


        if (id == R.id.add_button_menu_item) {
            if (isCurrentFragmentNotes) {
                AddNewNoteFragment dialog = new AddNewNoteFragment();
                dialog.show(getSupportFragmentManager(), "AddNote");
            }
            else {
                AddNewTaskFragment dialog = new AddNewTaskFragment();
                dialog.show(getSupportFragmentManager(), "AddTask");
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void add(String task, String description, String dateTime) {
        addableListener.add(task, description, dateTime);
    }


    public void setListener(Removable removableListener, Addable addableListener, Editable editableListener) {
        this.removableListener = removableListener;
        this.addableListener = addableListener;
        this.editableListener = editableListener;
    }

    @Override
    public void remove(int position) {
        removableListener.remove(position);
    }

    @Override
    public void edit(int pos, String name, String description, String date) {
        editableListener.edit(pos, name, description, date);
    }

    protected void openDatabase(@NonNull Context context) {
        noteDatabaseHelper = new NoteDatabaseHelper(context);
        database = noteDatabaseHelper.getReadableDatabase();
    }

    protected void closeDatabaseObjects() {
        database.close();
        cursor.close();
    }

    private void initNoteDataHolder() {
        cursor = database.rawQuery("SELECT * from " + NoteDatabaseHelper.TABLE, null);
        String title, description;
        int id;
        NoteDataHolder.getNoteList().clear();
        while(cursor.moveToNext()) {
            title = cursor.getString(1);
            description = cursor.getString(2);
            Note note = new Note(title, description);
            NoteDataHolder.addNote(note);
        }
    }

    public static void setIsCurrentFragmentNotes(boolean flag) {
        isCurrentFragmentNotes = flag;
    }
}