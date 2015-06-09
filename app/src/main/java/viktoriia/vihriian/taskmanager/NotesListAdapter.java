package viktoriia.vihriian.taskmanager;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;

import viktoriia.vihriian.taskmanager.core_classes.Note;
import viktoriia.vihriian.taskmanager.core_classes.NotesList;
import viktoriia.vihriian.taskmanager.managers.DateFormatManager;
import viktoriia.vihriian.taskmanager.managers.MyAlarmManager;
import viktoriia.vihriian.taskmanager.managers.SharedPreferencesManager;

public class NotesListAdapter extends RecyclerView.Adapter<NotesListAdapter.NotesListViewHolder> {

    private SharedPreferencesManager mPrefsManager;
    ArrayList<Note> notes;
    public Gson gson;
    private Context myContext;

    public NotesListAdapter(ArrayList<Note> p) {
        notes = new ArrayList<>();
        notes.addAll(p);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public NotesListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        myContext = parent.getContext();
        View v = LayoutInflater.from(myContext).inflate(R.layout.row, parent, false);

        NotesListViewHolder holder = new NotesListViewHolder(v);

        mPrefsManager = SharedPreferencesManager.getInstance(myContext);
        gson = new Gson();

        return holder;
    }

    @Override
    public void onBindViewHolder(NotesListViewHolder holder, int pos) {

        holder.name.setText(notes.get(pos).getName());
        holder.description.setText(notes.get(pos).getText());
        holder.date.setText(DateFormatManager
                .getStringFromFormattedLong(notes.get(pos).getDate()));
        holder.checkBox.setChecked(notes.get(pos).isComplite());
        if(notes.get(pos).isAlarm() && DateFormatManager
                .isActual(notes.get(pos).getDate())) {
            holder.alarm.setBackgroundResource(R.mipmap.ic_alarm_on_black);
        } else {
            holder.alarm.setBackgroundResource(R.mipmap.ic_alarm_off_grey);
        }
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public class NotesListViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        TextView name;
        TextView description;
        TextView date;
        CheckBox checkBox;
        ImageView alarm;

        public NotesListViewHolder(View v) {
            super(v);
            cv = (CardView) v.findViewById(R.id.cv);
            name = (TextView) v.findViewById(R.id.tv_name);
            description = (TextView) v.findViewById(R.id.tv_description);
            date = (TextView) v.findViewById(R.id.tv_date);
            checkBox = (CheckBox) v.findViewById(R.id.check_box);
            alarm = (ImageView) v.findViewById(R.id.iv_alarm);
        }
    }

   public void removeAt(int position) {
        cancelAlarm(position);
        notes.remove(position);
        this.notifyItemRemoved(position);
        this.notifyItemRangeChanged(position, notes.size());
        updateNotes();
    }

    public void updateNotes() {
        NotesList nArr = new NotesList();
        nArr.addAll(notes);
        mPrefsManager.saveNotesList(nArr);
    }

    private void cancelAlarm(int pos) {
        MyAlarmManager.cancelAlarm(myContext, MyAlarmReceiver.class, notes.get(pos).getId(),
                notes.get(pos).getDate(), notes.get(pos).getName(), notes.get(pos).getText());
    }
}
