package com.makasart.kpirozklad;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Created by Maxim on 02.11.2016.
 */

public class ScheduleActivity extends AppCompatActivity {

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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.start_menu, menu);
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
            Intent mIntent = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(mIntent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_in_right);
            return true;
        } else if (id == android.R.id.home) {
            Intent mIntent = new Intent(getApplicationContext(), StartMenuActivity.class);
            startActivity(mIntent);
            overridePendingTransition(R.anim.slide_from_up, R.anim.slide_from_down);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
