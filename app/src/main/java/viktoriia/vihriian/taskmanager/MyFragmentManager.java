package viktoriia.vihriian.taskmanager;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.support.v4.app.FragmentActivity;

import java.util.ArrayList;

public class MyFragmentManager {
    private static volatile MyFragmentManager instance;
    private FragmentManager fragmentManager;
    private ArrayList<Fragment> fragments;
    private Context myContext;

    public static MyFragmentManager getInstance() {
        MyFragmentManager localInstance = instance;
        if (localInstance == null) {
            synchronized (MyFragmentManager.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new MyFragmentManager();
                }
            }
        }
        return localInstance;
    }

    public void init(Context context) {
        myContext = context;
        fragmentManager = ((FragmentActivity) myContext).getFragmentManager();
        fragments = new ArrayList<>();
    }

    public void changeFragment(int containerID, Fragment newFragment) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(containerID, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();
        addFragmentToArray(newFragment);
    }


//is it needed??
    /*public void backToPreviousFragment(int containerID) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        int position = fragments.size() - 2;
        if(position < 0) { position = 0; }
        Fragment previousFragment = fragments.get(position);
        transaction.replace(containerID, previousFragment);
        transaction.commit();
        if(position != 0) {
            removeFragmentFromArray(position);
        }
    }*/

    private void addFragmentToArray(Fragment fragment) {
        fragments.add(fragment);
    }

    private void removeFragmentFromArray(int position) { fragments.remove(position); }

}
