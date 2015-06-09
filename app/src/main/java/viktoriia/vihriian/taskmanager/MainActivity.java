package viktoriia.vihriian.taskmanager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import viktoriia.vihriian.taskmanager.managers.MyFragmentManager;


public class MainActivity extends AppCompatActivity {

    private MyFragmentManager myFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myFragmentManager = MyFragmentManager.getInstance();
        myFragmentManager.init(this);
        myFragmentManager.changeFragment(R.id.fragment_container, new LoginFragment());
    }

    @Override
    public void onBackPressed() {
        //myFragmentManager.backToPreviousFragment(R.id.fragment_container);
        if (getFragmentManager().getBackStackEntryCount() > 1 ){
            getFragmentManager().popBackStack();
        } else if(getFragmentManager().getBackStackEntryCount() == 1 ) {
            showAlertDialog();
        } else {
            super.onBackPressed();
        }
    }

    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(R.string.title_exit)
                .setMessage(R.string.text_exit)
                .setIcon(R.mipmap.ic_exit)
                .setCancelable(false)
                .setPositiveButton(R.string.confirm_button, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                })
                .setNegativeButton(R.string.declain_button, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
