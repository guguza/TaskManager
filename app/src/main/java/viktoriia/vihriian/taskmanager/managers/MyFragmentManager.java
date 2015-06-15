package viktoriia.vihriian.taskmanager.managers;

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

    public void changeFragment(int containerID, Fragment newFragment, boolean isAdded) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(containerID, newFragment);
        transaction.commit();
        if(isAdded) {
            addFragmentToArray(newFragment);
        }
    }

    public void backToPreviousFragment(int containerID) {

        int position = fragments.size() - 1;
        removeFragmentFromArray(position);

        if(position < 0) { position = 0; }

        if(position != 0) {
            Fragment previousFragment = fragments.get(position - 1);
                changeFragment(containerID, previousFragment, false);
            }
        /*Fragment prePreviousFragment = getPreLast();
        if(prePreviousFragment != null) {
            if(getLast().getClass().getName().equals(prePreviousFragment.getClass().getName())) {
                removeFragmentFromArray(position - 2);
            }
        }*/

    }

    public Fragment getLast() {
        return fragments.get(fragments.size() - 1);
    }

    /*public Fragment getPreLast() {
        return (fragments.size() >= 2)? fragments.get(fragments.size() - 2): null;
    }*/

    public void addFragmentToArray(Fragment fragment) {
        fragments.add(fragment);
    }

    public Fragment removeFragmentFromArray(int position) {
        Fragment currentFragment = fragments.get(position);
        fragments.remove(position);
        return currentFragment;
    }

}
