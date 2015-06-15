package viktoriia.vihriian.taskmanager;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
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
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ViewAnimator;

import com.google.gson.Gson;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.GregorianCalendar;

import viktoriia.vihriian.taskmanager.core_classes.Note;
import viktoriia.vihriian.taskmanager.managers.DateFormatManager;
import viktoriia.vihriian.taskmanager.managers.MyAlarmManager;
import viktoriia.vihriian.taskmanager.managers.MyFragmentManager;
import viktoriia.vihriian.taskmanager.managers.SharedPreferencesManager;
import static android.content.Intent.ACTION_PICK;
        import static android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

public class NotesCreationFragment extends Fragment implements View.OnClickListener{

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
    private String image;
    ImageView imageView;
    private ViewAnimator animator;

    private static final int GALLERY_REQUEST = 9391;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_notes_creation, container, false);
        setContainerParams(container);
        setToolbar(view);

        name = (EditText) view.findViewById(R.id.et_name);
        description = (EditText) view.findViewById(R.id.et_description);
        animator = (ViewAnimator) view.findViewById(R.id.animator);
        imageView = (ImageView) view.findViewById(R.id.image);

        myContext = getActivity();
        mPrefsManager = SharedPreferencesManager.getInstance(myContext.getApplicationContext());
        myFragmentManager  = MyFragmentManager.getInstance();
        gson = new Gson();

        imageView.setOnClickListener(this);
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
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        getActivity().getMenuInflater().inflate(R.menu.menu_notes_creation, menu);

        setDialogWithDateAndTimePicker();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_save:
                if(isFilledCorrectly()) {
                    saveNote();
                    navigateTo(new NotesListFragment());
                }
                return true;
            case R.id.action_pick_date_time:
                alertDialog.show();
                return true;
            case R.id.action_alarm:
                defineAlarmValue();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GALLERY_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            image = data.getData().toString();
            loadImage();
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void loadImage() {
        // Index 1 is the progress bar. Show it while we're loading the image.
        animator.setDisplayedChild(1);

        Picasso.with(myContext)
                .load(image)
                .into(imageView, new Callback.EmptyCallback() {
            @Override public void onSuccess() {
                // Index 0 is the image view.
                animator.setDisplayedChild(0);
            }
        });
    }

    private void setContainerParams(ViewGroup container) {
        container.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
        container.setBackgroundColor(getResources().getColor(R.color.background_material_light));
    }

    private void setToolbar(View view) {
        toolbar = (Toolbar) view.findViewById(R.id.tool_bar);
        toolbar.setTitle(R.string.add);

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        setHasOptionsMenu(true);
    }

    private void openGallery() {
        Intent gallery = new Intent(ACTION_PICK, EXTERNAL_CONTENT_URI);
        startActivityForResult(gallery, GALLERY_REQUEST);
    }

    private void setDialogWithDateAndTimePicker() {

        final View dialogView = View.inflate(myContext, R.layout.date_time_picker, null);
        alertDialog = new AlertDialog.Builder(myContext).create();

        final DatePicker datePicker = (DatePicker) dialogView.findViewById(R.id.date_picker);
        final TimePicker timePicker = (TimePicker) dialogView.findViewById(R.id.time_picker);
        timePicker.setIs24HourView(true);

        //when user clicks the SET-button
        dialogView.findViewById(R.id.date_time_set).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar calendar = new GregorianCalendar(datePicker.getYear(),
                        datePicker.getMonth(),
                        datePicker.getDayOfMonth(),
                        timePicker.getCurrentHour(),
                        timePicker.getCurrentMinute());

                time = calendar.getTimeInMillis() / 1000;
                if (DateFormatManager.isActual(time)) {
                    alertDialog.dismiss();
                    ActionMenuItemView actionCalendar = (ActionMenuItemView) getView().findViewById(R.id.action_pick_date_time);
                    actionCalendar.setIcon(ResourcesCompat.getDrawable(getResources(),
                            R.mipmap.ic_calendar_on, null));
                } else {
                    Toast.makeText(myContext, R.string.date_error, Toast.LENGTH_SHORT).show();
                }

            }
        });
        alertDialog.setView(dialogView);
    }

    //switches on/off notification and changes alarm-drawable
    private void defineAlarmValue() {
        ActionMenuItemView actionAlarm = (ActionMenuItemView) getView().findViewById(R.id.action_alarm);
        if(isAlarm) {
            isAlarm = false;
            actionAlarm.setIcon(ResourcesCompat.getDrawable(getResources(),
                    R.mipmap.ic_alarm_off, null));
            Toast.makeText(myContext, R.string.off_alarm, Toast.LENGTH_SHORT).show();
        } else {
            isAlarm = true;
            actionAlarm.setIcon(ResourcesCompat.getDrawable(getResources(),
                    R.mipmap.ic_alarm_on, null));
            Toast.makeText(myContext, R.string.on_alarm, Toast.LENGTH_SHORT).show();
        }
    }

    //saves note in the preferences and starts the AlarmManager
    private void saveNote() {
        id = mPrefsManager.getNewNoteID();
        Note note = new Note(id, name.getText().toString(), description.getText().toString(),
                time, false, isAlarm, image);
        mPrefsManager.saveNote(note);
        if(isAlarm) {
            setAlarm(note);
        } else {
            cancelAlarm(note);
        }
    }

    private void setAlarm(Note note) {
        MyAlarmManager.setAlarm(myContext, MyAlarmReceiver.class, note);
    }

    private void cancelAlarm(Note note) {
        MyAlarmManager.cancelAlarm(myContext, MyAlarmReceiver.class, note);
    }

    private boolean isFilledCorrectly() {
        String message = null;
        if(name.getText().toString().equals("")) {
            message = getResources().getString(R.string.empty_name_error);
        } else if(description.getText().toString().equals("")){
            message = getResources().getString(R.string.empty_description_error);
        } else if(time == 0) {
            message = getResources().getString(R.string.empty_date_error);;
        }
        if(message == null) {
            return true;
        }
        Toast.makeText(myContext, message, Toast.LENGTH_SHORT).show();
        return false;
    }

    // Changes current fragment to the necessary one
    private void navigateTo(Fragment fragment) {
        myFragmentManager.changeFragment(R.id.fragment_container, fragment, false);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.image:
                openGallery();
                break;
        }
    }
}
