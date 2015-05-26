package viktoriia.vihriian.taskmanager;

import android.app.Activity;
import android.content.Context;
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
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class NotesListActivity extends AppCompatActivity {

    public ArrayList<Note> notesList;
    public RecyclerView rvNotes;
    public Toolbar toolbar;
    public Context myContext;
    public NotesListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_list);

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        myContext = NotesListActivity.this;
        notesList = new ArrayList<>();

        fillNotesList();
        rvNotes = (RecyclerView) findViewById(R.id.rv);
        adapter = new NotesListAdapter(notesList);

        LinearLayoutManager llm = new LinearLayoutManager(myContext);
        rvNotes.setLayoutManager(llm);
        /*rvNotes.addOnItemTouchListener(
                 new RecyclerItemClickListener(myContext, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int pos) {
                        Animation fadeIn = new AlphaAnimation(0, 1);
                        fadeIn.setDuration(300);
                        view.startAnimation(fadeIn);
                        startIntentWithExtras(pos);

                    }
                })
        );*/
        rvNotes.setAdapter(adapter);


    }

    public void fillNotesList() {
        Calendar calendar;
        for(int i = 0; i < 5; i++) {
            calendar = Calendar.getInstance();
            notesList.add(new Note("Name" + i + 1, "Description " + i + 1,
                    DateFormatter.formatDateAsLong(calendar), false));
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
