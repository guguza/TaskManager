package viktoriia.vihriian.taskmanager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;

import viktoriia.vihriian.taskmanager.core_classes.Note;

public class MyAlarmReceiver extends BroadcastReceiver {

    public MyAlarmReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        int id = intent.getExtras().getInt(Note.ID);
        long time = intent.getLongExtra(Note.DATE, 0L);
        String title = intent.getStringExtra(Note.TITLE);
        String text = intent.getStringExtra(Note.DESCRIPTION);

        Intent service1 = new Intent(context, MyAlarmService.class);

        service1.putExtra(Note.ID, id);
        service1.putExtra(Note.DATE, time);
        service1.putExtra(Note.TITLE, title);
        service1.putExtra(Note.DESCRIPTION, text);

        context.startService(service1);
    }
}
