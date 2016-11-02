package com.makasart.kpirozklad;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

/**
 * Created by Maxim on 02.11.2016.
 */

public class ScheduleActivity extends FragmentActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.schedule_activity);
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragmentContainers);
        if (fragment == null) {
            fragment = new ScheduleFragment();
            fm.beginTransaction().add(R.id.fragmentContainers, fragment).commit();
        }
    }
}
