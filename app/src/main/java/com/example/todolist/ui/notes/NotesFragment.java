package com.example.todolist.ui.notes;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.Note;
import com.example.todolist.NoteActivity;
import com.example.todolist.NoteDataHolder;
import com.example.todolist.NoteDatabaseHelper;
import com.example.todolist.interfaces.Addable;
import com.example.todolist.interfaces.Editable;
import com.example.todolist.MainActivity;
import com.example.todolist.note.NoteRecyclerAdapter;
import com.example.todolist.R;
import com.example.todolist.interfaces.Removable;
import com.example.todolist.databinding.FragmentNotesBinding;

import java.util.ArrayList;

public class NotesFragment extends Fragment implements Addable, Editable, Removable {

    private RecyclerView notesRecyclerView;
    private RecyclerView.LayoutManager myLayoutManager;
    private NoteRecyclerAdapter myAdapter;
    private ArrayList<Note> noteArrayList = new ArrayList<Note>();

    private Addable addable;
    private Editable editable;
    private Removable removable;


    private FragmentNotesBinding binding;
    private NoteDatabaseHelper noteDatabaseHelper;
    private SQLiteDatabase database;
    private Cursor cursor;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        addable = (MainActivity) context;
        editable = (MainActivity) context;
        removable = (MainActivity) context;
        MainActivity.setIsCurrentFragmentNotes(true);
        openDatabase(context);
        initNoteDataHolder();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        MainActivity.setIsCurrentFragmentNotes(false);
        closeDatabaseObjects();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NotesViewModel notesViewModel =
                new ViewModelProvider(this).get(NotesViewModel.class);

        binding = FragmentNotesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        MainActivity mainActivity = (MainActivity) root.getContext();
        mainActivity.setListener(this, this, this);
        setRecyclerView(root);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void setRecyclerView(View root) {
        notesRecyclerView = (RecyclerView) root.findViewById(R.id.notesRecyclerView);
        notesRecyclerView.setHasFixedSize(true);
        myLayoutManager = new LinearLayoutManager(root.getContext());
        notesRecyclerView.setLayoutManager(myLayoutManager);
    }

    @Override
    public void onResume() {
        super.onResume();
        setAdapter();
        editDB();
    }

    private void editDB() {
        int id = NoteDataHolder.getLastID();
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            for (int i = 0; i < id; i++) {
                cursor.moveToNext();
            }
            int realId = cursor.getInt(0);
            String name = cursor.getString(1);

            Note note = NoteDataHolder.getNote(id);

            ContentValues cv = new ContentValues();
            cv.put(NoteDatabaseHelper.COLUMN_NOTE_TITLE, note.getTitle());
            cv.put(NoteDatabaseHelper.COLUMN_DESCRIPTION, note.getDescription());
            database.update(NoteDatabaseHelper.TABLE, cv, NoteDatabaseHelper.COLUMN_ID + "=" + realId, null);
        }
    }

    private void setAdapter() {
        myAdapter = new NoteRecyclerAdapter(NoteDataHolder.getNoteList());
        DividerItemDecoration myDivider = new DividerItemDecoration(notesRecyclerView.getContext(), RecyclerView.VERTICAL);
        myDivider.setDrawable(AppCompatResources.getDrawable(this.getContext(), R.drawable.divider_drawable));
        notesRecyclerView.addItemDecoration(myDivider);
        notesRecyclerView.setAdapter(myAdapter);
    }

    @Override
    public void add(String title, String description, String dateTime) {
        Note note = new Note(title, description);
        NoteDataHolder.addNote(note);
        myAdapter.notifyItemInserted(NoteDataHolder.getNoteList().size() - 1);
        ContentValues cv = new ContentValues();
        cv.put(NoteDatabaseHelper.COLUMN_NOTE_TITLE, title);
        cv.put(NoteDatabaseHelper.COLUMN_DESCRIPTION, description);
        database.insert(NoteDatabaseHelper.TABLE, null, cv);
        cursor = database.rawQuery("SELECT * from " + NoteDatabaseHelper.TABLE, null);
    }

    @Override
    public void edit(int pos, String name, String description, String date) {
        Intent intent = new Intent(requireActivity(), NoteActivity.class);
        intent.putExtra("id", pos);
        NoteDataHolder.setLastID(pos);
        requireActivity().startActivity(intent);
    }

    @Override
    public void remove(int position) {
        cursor.moveToFirst();
        for (int i = 0; i < position; i++) {
            cursor.moveToNext();
        }
        int realId = cursor.getInt(0);
        NoteDataHolder.getNoteList().remove(position);
        myAdapter.notifyItemRemoved(position);
        database.delete(NoteDatabaseHelper.TABLE, "id = ?", new String[]{String.valueOf(realId)});
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
}