package viktoriia.vihriian.taskmanager;

import android.app.Activity;
import android.content.Context;
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
import viktoriia.vihriian.taskmanager.managers.DateFormatManager;
import viktoriia.vihriian.taskmanager.managers.MyAlarmManager;
import viktoriia.vihriian.taskmanager.managers.MyFragmentManager;
import viktoriia.vihriian.taskmanager.managers.SharedPreferencesManager;


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
        setContainerParams(container);
        setToolbar(view);

        myContext = getActivity();
        myFragmentManager  = MyFragmentManager.getInstance();
        mPrefsManager = SharedPreferencesManager.getInstance(myContext.getApplicationContext());

        notesList = new ArrayList<>();
        gson = new Gson();

        fillNotesFromPrefs();

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
        getActivity().getMenuInflater().inflate(R.menu.menu_notes_list, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_settings:
                return true;
            case R.id.action_add:
                navigateTo(new NotesCreationFragment());
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setContainerParams(ViewGroup container) {
        container.setBackgroundColor(getResources().getColor(R.color.background_material_light));
    }
    private void setToolbar(View view) {
        toolbar = (Toolbar) view.findViewById(R.id.tool_bar);
        toolbar.setTitle(R.string.list);

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        setHasOptionsMenu(true);
    }

    //fills the ArrayList with values from *DB* - SharedPreferences
    public void fillNotesFromPrefs() {
        if(mPrefsManager.getNotesList() == null) {
            showInfo(getResources().getString(R.string.empty_note_list_error));
        } else {
            notesList.addAll(mPrefsManager.getNotesList().getAll());
        }
    }

    // Changes current fragment to necessary one
    private void navigateTo(Fragment fragment) {
        myFragmentManager.changeFragment(R.id.fragment_container, fragment);
    }

    //sets layoutmanager and listeners for the recycler view
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
                        } else if (view.getId() == R.id.tv_undo) {
                            touchListener.undoPendingDismiss();

                        } else if (view.getId() == R.id.check_box) {
                            setCheckboxValue(view, position);

                        } else if (view.getId() == R.id.iv_alarm) {
                            setAlarm(view, position);
                        }
                    }
                }));
    }

    //sets checkbox as checked or not (displays if the task is done)
    private void setCheckboxValue(View view, int position) {
        CheckBox complite = (CheckBox) view.findViewById(R.id.check_box);
        if (!complite.isChecked()) {
            adapter.notes.get(position).setComplite(true);
        } else {
            adapter.notes.get(position).setComplite(false);
        }
        adapter.updateNotes();
    }

    //switches on/off the notifications and accordingly changes the color of the alarm drawable
    private void setAlarm(View view, int position) {
        ImageView alarm = (ImageView) view.findViewById(R.id.iv_alarm);

        if (DateFormatManager.isActual(adapter.notes.get(position).getDate())) {

            if (adapter.notes.get(position).isAlarm()) {
                showInfo(getResources().getString(R.string.off_alarm));
                adapter.notes.get(position).setAlarm(false);
                alarm.setBackgroundResource(R.mipmap.ic_alarm_off_grey);
                MyAlarmManager.cancelAlarm(myContext,
                        MyAlarmReceiver.class, adapter.notes.get(position));

            } else {
                showInfo(getResources().getString(R.string.on_alarm));
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
            showInfo(getResources().getString(R.string.task_actuality_error));
        }
        adapter.updateNotes();
    }

    //shows toast-messages to the user
    private void showInfo(String text) {
        Toast.makeText(myContext, text, Toast.LENGTH_SHORT).show();
    }
}
