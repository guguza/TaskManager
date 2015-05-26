package viktoriia.vihriian.taskmanager;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

import viktoriia.vihriian.taskmanager.Note;

/**
 * Created by Администратор on 21.05.2015.
 */
public class NotesListAdapter extends RecyclerView.Adapter<NotesListAdapter.NotesListViewHolder> {

    ArrayList<Note> notes;

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
        return holder;
    }

    @Override
    public void onBindViewHolder(NotesListViewHolder holder, int pos) {

        holder.date.setText(DateFormatter
                .getStringFromFormattedLong(notes.get(pos).getDate()));
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
        }
    }
}
