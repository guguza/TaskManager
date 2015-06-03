package viktoriia.vihriian.taskmanager;

import android.util.Log;

import java.util.ArrayList;

public class UsersList {
    private ArrayList<User> myList;

    public UsersList() {
        myList = new ArrayList<>();
    }

    public UsersList(ArrayList<User> list) {
        myList = new ArrayList<>();
        myList.addAll(list);
    }

    public ArrayList<User> getAll() {
        return myList;
    }

    public void addAll(ArrayList<User> list) {
        myList.addAll(list);
    }

    public void replaceAll(ArrayList<User> list) {
        myList.clear();
        myList.addAll(list);
    }

    public boolean isExists(String login) {
        for(User user: myList) {
            if(user.getLogin().equals(login)) {
                return true;
            }
        }
        return false;
    }

    public boolean isExists(String login, String password) {
        for(User user: myList) {
            if(user.getLogin().equals(login)
                    && user.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }

    public User get(int number) {
        return myList.get(number);
    }

    public void add(User user) {
        myList.add(user);
    }

    public void print() {
        for(User user: myList) {
            Log.d("lol", "Login: " + user.getLogin() + ", " + "password: " + user.getPassword());
        }
    }
}
