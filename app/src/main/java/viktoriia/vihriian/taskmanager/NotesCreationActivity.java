package viktoriia.vihriian.taskmanager;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
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
import java.util.UUID;

public class NotesCreationActivity extends AppCompatActivity {

    public Toolbar toolbar;
    public Context myContext;
    public SharedPreferences mPrefs;
    public Gson gson;
    public long time;
    private AlertDialog alertDialog;
    private int id;
    private EditText name;
    private EditText description;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_creation);

        name = (EditText) findViewById(R.id.et_name);
        description = (EditText) findViewById(R.id.et_description);

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        toolbar.setTitle("Add task");
        setSupportActionBar(toolbar);

        myContext = NotesCreationActivity.this;
        intent = getIntent();

        id = intent.getExtras().getInt("id");
        mPrefs = getSharedPreferences("myNotes", MODE_PRIVATE);
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
                    setAlarm();
                    finish();
                }
                return true;
            case R.id.action_pick_date_time:
                alertDialog.show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        int id = intent.getExtras().getInt("id");
    }

    private void setAlarm() {
        Intent myIntent = new Intent(NotesCreationActivity.this, MyAlarmReceiver.class);
        myIntent.putExtra("time", time);
        myIntent.putExtra("title", name.getText().toString());
        myIntent.putExtra("description", description.getText().toString());

        Calendar c = new GregorianCalendar();
        c.setTimeInMillis(time * 1000);
        long t = c.getTimeInMillis();
        PendingIntent pendingIntent = PendingIntent.getBroadcast(NotesCreationActivity.this, (int) time, myIntent, 0);
        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, t, pendingIntent);
    }

    private void saveNote() {
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        NotesList nArr = new NotesList();
        nArr.addAll(getFromPrefs().getAll());
        Note note = new Note(name.getText().toString(), description.getText().toString(),
                time, false);
        nArr.add(note);
        String json = gson.toJson(nArr);
        prefsEditor.putString("MyNotes", json);
        prefsEditor.commit();
        intent.putExtra("note", gson.toJson(note));
        setResult(RESULT_OK, intent);
    }

    private NotesList getFromPrefs() {
        String json = mPrefs.getString("MyNotes", "");
        return gson.fromJson(json, NotesList.class);
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
