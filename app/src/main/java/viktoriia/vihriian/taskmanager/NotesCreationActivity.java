package viktoriia.vihriian.taskmanager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.internal.view.menu.ActionMenuItemView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

public class NotesCreationActivity extends AppCompatActivity {

    public Toolbar toolbar;
    public Context myContext;
    public SharedPreferencesManager mPrefsManager;
    public Gson gson;
    public long time;
    private AlertDialog alertDialog;
    private EditText name;
    private EditText description;
    private Intent intent;
    private int id;
    private boolean isAlarm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_notes_creation);

        name = (EditText) findViewById(R.id.et_name);
        description = (EditText) findViewById(R.id.et_description);

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        toolbar.setTitle("Add task");
        setSupportActionBar(toolbar);

        myContext = NotesCreationActivity.this;
        intent = getIntent();

        mPrefsManager = SharedPreferencesManager.getInstance(getApplicationContext());
        gson = new Gson();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_notes_creation, menu);
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
                ActionMenuItemView actionCalendar = (ActionMenuItemView) findViewById(R.id.action_pick_date_time);
                actionCalendar.setIcon(ResourcesCompat.getDrawable(getResources(),
                        R.mipmap.ic_calendar_on, null));
            }
        });
        alertDialog.setView(dialogView);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch(id) {
            case R.id.action_save:
                if(isFilledCorrectly()) {
                    saveNote();
                    finish();
                }
                return true;
            case R.id.action_pick_date_time:
                alertDialog.show();
                return true;
            case R.id.action_alarm:
                ActionMenuItemView actionAlarm = (ActionMenuItemView) findViewById(R.id.action_alarm);
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

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void setAlarm(Note note) {
        MyAlarmManager.setAlarm(NotesCreationActivity.this, MyAlarmReceiver.class, note);
    }

    private void cancelAlarm(Note note) {
        MyAlarmManager.cancelAlarm(NotesCreationActivity.this, MyAlarmReceiver.class, note);
    }

    private void saveNote() {
        id = mPrefsManager.getNewNoteID();
        Note note = new Note(id, name.getText().toString(), description.getText().toString(),
                time, false, isAlarm);
        mPrefsManager.saveNote(note);
        intent.putExtra("note", gson.toJson(note));
        setResult(RESULT_OK, intent);
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
