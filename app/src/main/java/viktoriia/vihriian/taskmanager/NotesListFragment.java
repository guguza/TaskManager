package viktoriia.vihriian.taskmanager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hudomju.swipe.OnItemClickListener;
import com.hudomju.swipe.SwipeToDismissTouchListener;
import com.hudomju.swipe.SwipeableItemClickListener;
import com.hudomju.swipe.adapter.RecyclerViewAdapter;

import java.util.ArrayList;

import viktoriia.vihriian.taskmanager.core_classes.Note;
import viktoriia.vihriian.taskmanager.tools.DateFormatter;
import viktoriia.vihriian.taskmanager.tools.MyAlarmManager;
import viktoriia.vihriian.taskmanager.tools.SharedPreferencesManager;


public class NotesListFragment extends Fragment {

    private MyFragmentManager myFragmentManager;
    public ArrayList<Note> notesList;
    public RecyclerView rvNotes;
    public Toolbar toolbar;
    public Context myContext;
    public NotesListAdapter adapter;
    public static SharedPreferencesManager mPrefsManager;
    public Gson gson;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notes_list, container, false);

        container.setBackgroundColor(getResources().getColor(R.color.background_material_light));
        toolbar = (Toolbar) view.findViewById(R.id.tool_bar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        setHasOptionsMenu(true);

        myFragmentManager  = MyFragmentManager.getInstance();
        myContext = getActivity();
        notesList = new ArrayList<>();
        mPrefsManager = SharedPreferencesManager.getInstance(myContext.getApplicationContext());
        gson = new Gson();

        fillNotesFromPref();

        rvNotes = (RecyclerView) view.findViewById(R.id.rv);
        adapter = new NotesListAdapter(notesList);
        initRecyclerView();
        rvNotes.setAdapter(adapter);

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
        getActivity().getMenuInflater().inflate(R.menu.menu_notes_list, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_settings:
                return true;
            case R.id.action_add:
                /*Intent intent = new Intent(myContext, NotesCreationActivity.class);
                startActivityForResult(intent, 1);*/
                myFragmentManager.changeFragment(R.id.fragment_container, new NotesCreationFragment());
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {return;}
        Note note = gson.fromJson(data.getStringExtra("note"), Note.class);
        adapter.add(note);
    }

    public void fillNotesFromPref() {
        if(mPrefsManager.getNotesList() == null) {
            Toast.makeText(myContext, "Notes are not found!", Toast.LENGTH_SHORT).show();
        } else {
            notesList.addAll(mPrefsManager.getNotesList().getAll());
        }
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
        rvNotes.addOnItemTouchListener(new SwipeableItemClickListener(myContext,
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
                            if (!complite.isChecked()) {
                                adapter.notes.get(position).setComplite(true);
                            } else {
                                adapter.notes.get(position).setComplite(false);
                            }
                            adapter.updateNotes();
                        } else if (view.getId() == R.id.iv_alarm) {

                            ImageView alarm = (ImageView) view.findViewById(R.id.iv_alarm);

                            if (DateFormatter.isActual(adapter.notes.get(position).getDate())) {
                                if (adapter.notes.get(position).isAlarm()) {
                                    Toast.makeText(myContext, "Alarm is OFF!",
                                            Toast.LENGTH_SHORT).show();
                                    adapter.notes.get(position).setAlarm(false);
                                    alarm.setBackgroundResource(R.mipmap.ic_alarm_off_grey);
                                    MyAlarmManager.cancelAlarm(myContext,
                                            MyAlarmReceiver.class, adapter.notes.get(position));
                                } else {
                                    Toast.makeText(myContext, "Alarm is ON!",
                                            Toast.LENGTH_SHORT).show();
                                    adapter.notes.get(position).setAlarm(true);
                                    alarm.setBackgroundResource(R.mipmap.ic_alarm_on_black);
                                    MyAlarmManager.setAlarm(myContext,
                                            MyAlarmReceiver.class, adapter.notes.get(position));
                                }
                            } else {
                                if (adapter.notes.get(position).isAlarm()) {
                                    adapter.notes.get(position).setAlarm(false);
                                    alarm.setBackgroundResource(R.mipmap.ic_alarm_off_grey);
                                    MyAlarmManager.cancelAlarm(myContext,
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
}
