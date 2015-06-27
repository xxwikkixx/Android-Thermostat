package com.mawsom.mawsom.mawsomnobl;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;
import com.mawsom.mawsom.mawsomnobl.*;

/**
 * Created by Waqas on 6/11/2015.
 */

public class ScheduleActivity extends ActionBarActivity {

    private int scheduleIndex;
    private boolean showSettings=false;
    Fragment currentFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.schedule);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            scheduleIndex = extras.getInt("SCHEDULE_INDEX");
            showSettings = extras.getBoolean("SHOW_SETTINGS");
        }
        if (showSettings)
        {
            switchTab(new ScheduleDetailsFragment(scheduleIndex));
        } else {
            switchTab(new ScheduleDayFragment(scheduleIndex, 1));
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getSupportMenuInflater();
        inflater.inflate(R.menu.schedule, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menu) {

        switch (menu.getItemId())
        {
            case R.id.menu_sunday:
                switchTab(new ScheduleDayFragment(scheduleIndex, 1));
                break;
            case R.id.menu_monday:
                switchTab(new ScheduleDayFragment(scheduleIndex, 2));
                break;
            case R.id.menu_tuesday:
                switchTab(new ScheduleDayFragment(scheduleIndex, 3));
                break;
            case R.id.menu_wednesday:
                switchTab(new ScheduleDayFragment(scheduleIndex, 4));
                break;
            case R.id.menu_thursday:
                switchTab(new ScheduleDayFragment(scheduleIndex, 5));
                break;
            case R.id.menu_friday:
                switchTab(new ScheduleDayFragment(scheduleIndex, 6));
                break;
            case R.id.menu_saturday:
                switchTab(new ScheduleDayFragment(scheduleIndex, 7));
                break;
            case R.id.menu_settings:
                switchTab(new ScheduleDetailsFragment(scheduleIndex));
                break;
        }
        return super.onOptionsItemSelected(menu);
    }





    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //switchTab(currentFragment);
    }



    private void switchTab(Fragment fragment)
    {
        currentFragment = fragment;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentHolder, fragment);
        //transaction.addToBackStack(null);
        transaction.commit();

    }


}
