package com.makasart.kpirozklad;

import android.app.FragmentManager;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class StartMenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static int width;
    public static int height;
    public int widthPixels;
    public int heightPixels;
    public static int orientation;

    private TextView GeneralTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_menu);

        //Show display Size;
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        height = size.y;
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        widthPixels = metrics.widthPixels;
        heightPixels = metrics.heightPixels;
        orientation = getResources().getConfiguration().orientation;

        Log.d("NY", Integer.toString(width));
        Log.d("NY", Integer.toString(height));
        Log.d("NY", Integer.toString(widthPixels));
        Log.d("NY", Integer.toString(heightPixels));
        Log.d("NY", Integer.toString(orientation));

        //

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

   /*     FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        }); */

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        // FOR NAVIGATION VIEW ITEM TEXT COLOR
        int[][] state = new int[][] {
                new int[] {-android.R.attr.state_enabled}, // disabled
                new int[] {android.R.attr.state_enabled}, // enabled
                new int[] {-android.R.attr.state_checked}, // unchecked
                new int[] { android.R.attr.state_pressed}  // pressed

        };

        int[] color = new int[] {
                Color.WHITE,
                Color.WHITE,
                Color.WHITE,
                Color.WHITE
        };

        ColorStateList csl = new ColorStateList(state, color);


// FOR NAVIGATION VIEW ITEM ICON COLOR
        int[][] states = new int[][] {
                new int[] {-android.R.attr.state_enabled}, // disabled
                new int[] {android.R.attr.state_enabled}, // enabled
                new int[] {-android.R.attr.state_checked}, // unchecked
                new int[] { android.R.attr.state_pressed}  // pressed

        };

        int[] colors = new int[] {
                Color.WHITE,
                Color.WHITE,
                Color.WHITE,
                Color.WHITE
        };

        //added white color to "communicate" in menu
        Menu menu = navigationView.getMenu();
        MenuItem tools= menu.findItem(R.id.communicate);
        SpannableString s = new SpannableString(tools.getTitle());
        s.setSpan(new TextAppearanceSpan(this, R.style.TextAppearance44), 0, s.length(), 0);
        tools.setTitle(s);
        //

        ColorStateList csl2 = new ColorStateList(states, colors);
        navigationView.setItemTextColor(csl);
        navigationView.setItemIconTintList(csl2);
        navigationView.setNavigationItemSelectedListener(this);

        //Navigate to general form
        navigateToGeneral();

        View mhView =  navigationView.getHeaderView(0);
        GeneralTextView = (TextView)mhView.findViewById(R.id.from_drawer_to_general);
        GeneralTextView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),
                                R.anim.see_all_start);
                        GeneralTextView.startAnimation(animation);
                        return true;
                    case MotionEvent.ACTION_UP:
                        Animation animation2 = AnimationUtils.loadAnimation(getApplicationContext(),
                                R.anim.see_all_end);
                        navigateToGeneral();
                        GeneralTextView.startAnimation(animation2);
                        onBackPressed();
                }
                return false;
            }
        });
    }

    private void navigateToGeneral() {
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame,
                new StartMenuFragment())
                .commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        FragmentManager fragmentManager = getFragmentManager();

        if (id == R.id.nav_schedule) {
            Intent mIntent = new Intent(this, ScheduleActivity.class);
            startActivity(mIntent);
            onBackPressed();  //think, that be method to close drawer menu
            overridePendingTransition(R.anim.slide_in_up, R.anim.slide_in_down);
        } else if (id == R.id.nav_tasks) {
            fragmentManager.beginTransaction().replace(R.id.content_frame,
                    new TasksFragment())
                    .commit();
        } else if (id == R.id.nav_notes) {
            fragmentManager.beginTransaction().replace(R.id.content_frame,
                    new NotesFragment())
                    .commit();
        } else if (id == R.id.nav_teachers) {
            fragmentManager.beginTransaction().replace(R.id.content_frame,
                    new TeachersFragment())
                    .commit();
        } else if (id == R.id.nav_settings) {
            fragmentManager.beginTransaction().replace(R.id.content_frame,
                    new SettingsFragment())
                    .commit();
        } else if (id == R.id.nav_about) {
            fragmentManager.beginTransaction().replace(R.id.content_frame,
                    new AboutUsFragment())
                    .commit();
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
