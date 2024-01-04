package com.example.todolist;

import java.util.ArrayList;

public class NoteDataHolder {
    private static ArrayList<Note> noteList = new ArrayList<Note>();
    private static int lastID = 0;

    public static void setNoteList(ArrayList<Note> noteList) {
        NoteDataHolder.noteList = noteList;
    }

    public static ArrayList<Note> getNoteList() {
        return noteList;
    }

    public static void addNote(Note note) {
        noteList.add(note);
    }

    public static Note getNote(int position) {
        return noteList.get(position);
    }

    public static void editNote(int position, Note note) {
        noteList.set(position, note);
    }

    public static void setLastID(int lastID) {
        NoteDataHolder.lastID = lastID;
    }

    public static int getLastID() {
        return lastID;
    }
}
