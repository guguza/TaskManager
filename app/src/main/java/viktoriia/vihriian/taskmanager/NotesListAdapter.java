package viktoriia.vihriian.taskmanager;

import android.content.SharedPreferences;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;

import viktoriia.vihriian.taskmanager.Note;

/**
 * Created by Администратор on 21.05.2015.
 */
public class NotesListAdapter extends RecyclerView.Adapter<NotesListAdapter.NotesListViewHolder> {

    ArrayList<Note> notes;
    public Gson gson;

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
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row, parent, false);
        NotesListViewHolder holder = new NotesListViewHolder(v);
        gson = new Gson();
        return holder;
    }

    @Override
    public void onBindViewHolder(NotesListViewHolder holder, int pos) {

        holder.name.setText(notes.get(pos).getName());
        holder.description.setText(notes.get(pos).getText());
        holder.date.setText(DateFormatter
                .getStringFromFormattedLong(notes.get(pos).getDate()));
        holder.checkBox.setChecked(notes.get(pos).isComplite());
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public class NotesListViewHolder extends RecyclerView.ViewHolder{
        CardView cv;
        TextView name;
        TextView description;
        TextView date;
        CheckBox checkBox;

        public NotesListViewHolder(View v) {
            super(v);
            cv = (CardView) v.findViewById(R.id.cv);
            name = (TextView)v.findViewById(R.id.tv_name);
            description = (TextView)v.findViewById(R.id.tv_description);
            date = (TextView)v.findViewById(R.id.tv_date);
            checkBox = (CheckBox) v.findViewById(R.id.check_box);

            v.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    removeAt(getAdapterPosition());
                    return false;
                }
            });
        }
    }

    public void add(Note note) {
        notes.add(note);
        this.notifyDataSetChanged();
    }

    public void removeAt(int position) {
        notes.remove(position);
        this.notifyItemRemoved(position);
        this.notifyItemRangeChanged(position, notes.size());
        deleteNote();
    }

    private void deleteNote() {
        SharedPreferences.Editor prefsEditor = NotesListActivity.mPrefs.edit();
        NotesList nArr = new NotesList();
        nArr.addAll(notes);
        String json = gson.toJson(nArr);
        prefsEditor.putString("MyNotes", json);
        prefsEditor.commit();
    }

}
