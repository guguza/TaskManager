package viktoriia.vihriian.taskmanager.tools;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import viktoriia.vihriian.taskmanager.core_classes.Note;
import viktoriia.vihriian.taskmanager.core_classes.NotesList;
import viktoriia.vihriian.taskmanager.core_classes.User;
import viktoriia.vihriian.taskmanager.core_classes.UsersList;

public class SharedPreferencesManager {
    private static volatile SharedPreferencesManager instance;
    private static SharedPreferences mPrefs;
    private static Context mContext;
    private static final User user = User.getInstance();

    private static final Gson gson = new Gson();

    private static final String APP_SETTINGS = "APP_SETTINGS";

    //fields
    private static final String NOTES = "NOTES";
    private static final String ID_COUNTER = "ID_COUNTER";
    private static final String USERS = "USERS";


    public static SharedPreferencesManager getInstance(Context context) {
        SharedPreferencesManager localInstance = instance;
        if (localInstance == null) {
            synchronized (SharedPreferencesManager.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new SharedPreferencesManager();
                    setSharedPrefs(context);
                    setDefaultIDCounter();
                }
            }
        }
        return localInstance;
    }

    private static void setSharedPrefs(Context context) {
        mContext = context;
        mPrefs = mContext.getSharedPreferences(APP_SETTINGS, Context.MODE_PRIVATE);
    }

    public static SharedPreferences getSharedPrefs() {
        return mPrefs;
    }

    private static void setDefaultIDCounter() {
        if(mPrefs.getInt(ID_COUNTER, 0) == 0) {
            SharedPreferences.Editor prefsEditor = mPrefs.edit();
            prefsEditor.putInt(ID_COUNTER, 1);
            prefsEditor.commit();
        }
    }

    private static void setIDCounter(int id) {
            SharedPreferences.Editor prefsEditor = mPrefs.edit();
            prefsEditor.putInt(ID_COUNTER, id);
            prefsEditor.commit();
    }

    public static int getNewNoteID() {
        int id = mPrefs.getInt(ID_COUNTER, 0);
        setIDCounter(id + 1);
        return id;
    }

    public static NotesList getNotesList() {
        if(mPrefs.getString(NOTES + user.getLogin(), "") == null) {
            return null;
        }
        String json = mPrefs.getString(NOTES + user.getLogin(), "");
        return gson.fromJson(json, NotesList.class);
    }

    public static void saveNotesList(NotesList list) {
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        String json = gson.toJson(list);
        prefsEditor.putString(NOTES + user.getLogin(), json);
        prefsEditor.commit();
    }

    public static void saveNote(Note note) {
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        NotesList nArr = new NotesList();
        if(getNotesList() != null) {
            nArr.addAll(getNotesList().getAll());
        }
        nArr.add(note);
        String json = gson.toJson(nArr);
        prefsEditor.putString(NOTES + user.getLogin(), json);
        prefsEditor.commit();
    }

    public static UsersList getUsersList() {
        if(mPrefs.getString(USERS, "") == null) {
            return null;
        }
        String json = mPrefs.getString(USERS, "");
        return gson.fromJson(json, UsersList.class);
    }

    public static void saveUsersList(UsersList list) {
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        String json = gson.toJson(list);
        prefsEditor.putString(USERS, json);
        prefsEditor.commit();
    }

    public static void saveUser(User user) {
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        UsersList uArr = new UsersList();
        if(getUsersList() != null) {
            uArr.addAll(getUsersList().getAll());
        }
        uArr.add(user);
        String json = gson.toJson(uArr);
        prefsEditor.putString(USERS, json);
        prefsEditor.commit();
    }

    //returns true if the User with the same name doesn't exist
    public static boolean checkUser(String login) {
        UsersList uList = new UsersList();
        if(getUsersList() != null) {
            uList.addAll(getUsersList().getAll());
            return !uList.isExists(login);
        }
        return true;
    }

    //returns true if the User with such login and password exists
    public static boolean checkUser(String login, String password) {
        UsersList uList = new UsersList();
        if(getUsersList() == null) {
            return false;
        }
        uList.addAll(getUsersList().getAll());
        return uList.isExists(login, password);
    }

}
