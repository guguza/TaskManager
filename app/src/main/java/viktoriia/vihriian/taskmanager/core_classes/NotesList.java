package viktoriia.vihriian.taskmanager.core_classes;

import java.util.ArrayList;

import viktoriia.vihriian.taskmanager.core_classes.Note;

public class NotesList {
    private ArrayList<Note> myList;

    public NotesList() {
        myList = new ArrayList<>();
    }
    public NotesList(ArrayList<Note> list) {
        myList = new ArrayList<>();
        myList.addAll(list);
    }

    public ArrayList<Note> getAll() {
        return myList;
    }

    public void addAll(ArrayList<Note> list) {
        myList.addAll(list);
    }

    public void replaceAll(ArrayList<Note> list) {
        myList.clear();
        myList.addAll(list);
    }

    public Note get(int number) {
        return myList.get(number);
    }

    public void add(Note note) {
        myList.add(note);
    }
}
