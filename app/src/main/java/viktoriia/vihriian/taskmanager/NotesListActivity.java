package viktoriia.vihriian.taskmanager;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hudomju.swipe.OnItemClickListener;
import com.hudomju.swipe.SwipeToDismissTouchListener;
import com.hudomju.swipe.SwipeableItemClickListener;
import com.hudomju.swipe.adapter.RecyclerViewAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class NotesListActivity extends AppCompatActivity {

    public ArrayList<Note> notesList;
    public RecyclerView rvNotes;
    public Toolbar toolbar;
    public Context myContext;
    public NotesListAdapter adapter;
    public static SharedPreferencesManager mPrefsManager;
    public Gson gson;
    public static Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_list);

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        myContext = NotesListActivity.this;
        intent = getIntent();
        notesList = new ArrayList<>();
        mPrefsManager = SharedPreferencesManager.getInstance(getApplicationContext());
        gson = new Gson();

        fillNotesFromPref();

        rvNotes = (RecyclerView) findViewById(R.id.rv);
        adapter = new NotesListAdapter(notesList);
        initRecyclerView();
        rvNotes.setAdapter(adapter);
    }

    private void initRecyclerView() {
        LinearLayoutManager llm = new LinearLayoutManager(myContext);
        rvNotes.setLayoutManager(llm);
        final SwipeToDismissTouchListener<RecyclerViewAdapter> touchListener =
                new SwipeToDismissTouchListener<>(
                        new RecyclerViewAdapter(rvNotes),
                        new SwipeToDismissTouchListener.DismissCallbacks<RecyclerViewAdapter>() {
                            @Override
                            public boolean canDismiss(int position) {
                                return true;
                            }

                            @Override
                            public void onDismiss(RecyclerViewAdapter view, int position) {
                                adapter.removeAt(position);
                            }
                        });

        rvNotes.setOnTouchListener(touchListener);
        rvNotes.setOnScrollListener((RecyclerView.OnScrollListener) touchListener.makeScrollListener());
        rvNotes.addOnItemTouchListener(new SwipeableItemClickListener(this,
                new OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        if (view.getId() == R.id.tv_delete) {
                            touchListener.processPendingDismisses();
                            //adapter.deleteNote();
                        } else if (view.getId() == R.id.tv_undo) {
                            touchListener.undoPendingDismiss();
                        } else if (view.getId() == R.id.check_box) {
                            CheckBox complite = (CheckBox) view.findViewById(R.id.check_box);
                            if(!complite.isChecked()) {
                                adapter.notes.get(position).setComplite(true);
                            } else {
                                adapter.notes.get(position).setComplite(false);
                            }
                            adapter.updateNotes();
                        } else if (view.getId() == R.id.iv_alarm) {

                            ImageView alarm = (ImageView) view.findViewById(R.id.iv_alarm);

                            if(DateFormatter.isActual(adapter.notes.get(position).getDate())) {
                                if(adapter.notes.get(position).isAlarm()) {
                                    Toast.makeText(myContext, "Alarm is OFF!",
                                            Toast.LENGTH_SHORT).show();
                                    adapter.notes.get(position).setAlarm(false);
                                    alarm.setBackgroundResource(R.mipmap.ic_alarm_off_grey);
                                    MyAlarmManager.cancelAlarm(NotesListActivity.this,
                                            MyAlarmReceiver.class, adapter.notes.get(position));
                                } else {
                                    Toast.makeText(myContext, "Alarm is ON!",
                                            Toast.LENGTH_SHORT).show();
                                    adapter.notes.get(position).setAlarm(true);
                                    alarm.setBackgroundResource(R.mipmap.ic_alarm_on_black);
                                    MyAlarmManager.setAlarm(NotesListActivity.this,
                                            MyAlarmReceiver.class, adapter.notes.get(position));
                                }
                            } else {
                                if(adapter.notes.get(position).isAlarm()) {
                                    adapter.notes.get(position).setAlarm(false);
                                    alarm.setBackgroundResource(R.mipmap.ic_alarm_off_grey);
                                    MyAlarmManager.cancelAlarm(NotesListActivity.this,
                                            MyAlarmReceiver.class, adapter.notes.get(position));
                                }
                                Toast.makeText(myContext, "Task is not actual anymore!",
                                        Toast.LENGTH_SHORT).show();
                            }
                            adapter.updateNotes();
                        }
                    }
                }));
    }

    public void fillNotesFromPref() {
        if(mPrefsManager.getNotesList() == null) {
            Toast.makeText(myContext, "Notes are not found!", Toast.LENGTH_SHORT).show();
        } else {
            notesList.addAll(mPrefsManager.getNotesList().getAll());
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_notes_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch(id) {
            case R.id.action_settings:
                return true;
            case R.id.action_add:
                Intent intent = new Intent(NotesListActivity.this, NotesCreationActivity.class);
                intent.putExtra("id", adapter.getItemCount());
                startActivityForResult(intent, 1);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {return;}
        Note note = gson.fromJson(data.getStringExtra("note"), Note.class);
        adapter.add(note);
    }
}
