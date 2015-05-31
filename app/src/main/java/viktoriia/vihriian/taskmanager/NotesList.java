package viktoriia.vihriian.taskmanager;

import java.util.ArrayList;

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
