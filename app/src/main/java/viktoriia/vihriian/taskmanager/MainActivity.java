package viktoriia.vihriian.taskmanager;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import viktoriia.vihriian.taskmanager.tools.MyFragmentManager;


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
            finish();
        } else {
            super.onBackPressed();
        }
    }
}
