package viktoriia.vihriian.taskmanager;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.internal.view.menu.ActionMenuItemView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.Calendar;
import java.util.GregorianCalendar;

import viktoriia.vihriian.taskmanager.core_classes.Note;
import viktoriia.vihriian.taskmanager.tools.MyAlarmManager;
import viktoriia.vihriian.taskmanager.tools.SharedPreferencesManager;

/**
 * Created by Администратор on 05.06.2015.
 */
public class NotesCreationFragment extends Fragment {

    public Toolbar toolbar;
    public Context myContext;
    public SharedPreferencesManager mPrefsManager;
    public Gson gson;
    public long time;
    private AlertDialog alertDialog;
    private EditText name;
    private EditText description;
    private int id;
    private boolean isAlarm;
    private MyFragmentManager myFragmentManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notes_creation, container, false);

        container.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
        container.setBackgroundColor(getResources().getColor(R.color.background_material_light));

        toolbar = (Toolbar) view.findViewById(R.id.tool_bar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        setHasOptionsMenu(true);

        myFragmentManager  = MyFragmentManager.getInstance();
        name = (EditText) view.findViewById(R.id.et_name);
        description = (EditText) view.findViewById(R.id.et_description);

        toolbar = (Toolbar) view.findViewById(R.id.tool_bar);
        toolbar.setTitle("Add task");

        myContext = getActivity();

        mPrefsManager = SharedPreferencesManager.getInstance(myContext.getApplicationContext());
        gson = new Gson();

        return view;

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getActivity().getMenuInflater().inflate(R.menu.menu_notes_creation, menu);
        super.onCreateOptionsMenu(menu, inflater);
        final View dialogView = View.inflate(myContext, R.layout.date_time_picker, null);
        alertDialog = new AlertDialog.Builder(myContext).create();

        final DatePicker datePicker = (DatePicker) dialogView.findViewById(R.id.date_picker);
        final TimePicker timePicker = (TimePicker) dialogView.findViewById(R.id.time_picker);
        timePicker.setIs24HourView(true);

        dialogView.findViewById(R.id.date_time_set).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar calendar = new GregorianCalendar(datePicker.getYear(),
                        datePicker.getMonth(),
                        datePicker.getDayOfMonth(),
                        timePicker.getCurrentHour(),
                        timePicker.getCurrentMinute());

                time = calendar.getTimeInMillis() / 1000;
                alertDialog.dismiss();
                ActionMenuItemView actionCalendar = (ActionMenuItemView) getView().findViewById(R.id.action_pick_date_time);
                actionCalendar.setIcon(ResourcesCompat.getDrawable(getResources(),
                        R.mipmap.ic_calendar_on, null));
            }
        });
        alertDialog.setView(dialogView);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_save:
                if(isFilledCorrectly()) {
                    saveNote();
                    myFragmentManager.changeFragment(R.id.fragment_container, new NotesListFragment());
                }
                return true;
            case R.id.action_pick_date_time:
                alertDialog.show();
                return true;
            case R.id.action_alarm:
                ActionMenuItemView actionAlarm = (ActionMenuItemView) getView().findViewById(R.id.action_alarm);
                if(isAlarm) {
                    isAlarm = false;
                    actionAlarm.setIcon(ResourcesCompat.getDrawable(getResources(),
                            R.mipmap.ic_alarm_off, null));
                    Toast.makeText(myContext, "Alarm is OFF", Toast.LENGTH_SHORT).show();
                } else {
                    isAlarm = true;
                    actionAlarm.setIcon(ResourcesCompat.getDrawable(getResources(),
                            R.mipmap.ic_alarm_on, null));
                    Toast.makeText(myContext, "Alarm is ON", Toast.LENGTH_SHORT).show();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setAlarm(Note note) {
        MyAlarmManager.setAlarm(myContext, MyAlarmReceiver.class, note);
    }

    private void cancelAlarm(Note note) {
        MyAlarmManager.cancelAlarm(myContext, MyAlarmReceiver.class, note);
    }

    private void saveNote() {
        id = SharedPreferencesManager.getNewNoteID();
        Note note = new Note(id, name.getText().toString(), description.getText().toString(),
                time, false, isAlarm);
        mPrefsManager.saveNote(note);
        /*intent.putExtra("note", gson.toJson(note));
        setResult(RESULT_OK, intent);*/
        if(isAlarm) {
            setAlarm(note);
        } else {
            cancelAlarm(note);
        }
    }

    private boolean isFilledCorrectly() {
        String message = null;
        if(name.getText().toString().equals("")) {
            message = "Name the note!";
        } else if(description.getText().toString().equals("")){
            message = "Describe the note!";
        } else if(time == 0) {
            message = "Pick up date and time!";
        }
        if(message == null) {
            return true;
        }
        Toast.makeText(myContext, message, Toast.LENGTH_SHORT).show();
        return false;
    }

}
