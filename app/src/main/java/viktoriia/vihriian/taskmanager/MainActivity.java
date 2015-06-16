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
        myFragmentManager.changeFragment(R.id.fragment_container, new LoginFragment(), true);
    }

    @Override
    public void onBackPressed() {
        String currentFragmentName = myFragmentManager.getLast().getClass().getName();

        if(currentFragmentName.contains("NotesList")) {
            showAlertDialog(R.string.logout, R.string.text_exit);

        } else if(currentFragmentName.contains("Login")) {
            showAlertDialog(R.string.title_exit, R.string.text_exit);
        } else {
            myFragmentManager.backToPreviousFragment(R.id.fragment_container);
            }
        }

    private void showAlertDialog(final int idTitle, final int idText) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(idTitle)
                .setMessage(idText)
                .setIcon(R.mipmap.ic_exit)
                .setCancelable(false)
                .setPositiveButton(R.string.confirm_button, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (idTitle == R.string.title_exit) {
                            finish();
                        } else if (idTitle == R.string.logout) {
                            myFragmentManager.backToPreviousFragment(R.id.fragment_container);
                        }
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
