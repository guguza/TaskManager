package viktoriia.vihriian.taskmanager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;

public class MyAlarmReceiver extends BroadcastReceiver {
    public MyAlarmReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        int id = intent.getExtras().getInt("id");
        long time = intent.getLongExtra("time", 0L);
        String title = intent.getStringExtra("title");
        String text = intent.getStringExtra("description");
        Intent service1 = new Intent(context, MyAlarmService.class);
        service1.putExtra("id", id);
        service1.putExtra("time", time);
        service1.putExtra("title", title);
        service1.putExtra("description", text);
        context.startService(service1);
    }
}
