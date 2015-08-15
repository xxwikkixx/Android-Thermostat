package com.mawsom.mawsom.mawsomnobl;


import android.app.ActionBar;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

/**
 * Created by Waqas on 6/26/2015.
 */

public class SettingsActivity extends FragmentActivity{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.settings);

        Bundle extras = getIntent().getExtras();
        //setCurrentTab(new GeneralSettingsFragment());
        //switchTab(new GeneralSettingsFragment());

        //need to  setup tabs for the fragment activities

    }

}
    /*
        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.settings, menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem menu) {

            switch (menu.getItemId())
            {
                case R.id.menu_general:
                    switchTab(new GeneralSettingsFragment());
                    break;
                case R.id.menu_usage:
                    switchTab(new UsageSettingsFragment());
                    break;
                case R.id.menu_furnace:
                    switchTab(new FurnaceSettingsFragment());
                    break;
            }
            return super.onOptionsItemSelected(menu);
        }

        //auto switches the tabs when starting up the settings activity
        public void switchTab(Fragment fragment)
        {
            currentFragment = fragment;
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragmentHolder, fragment);
            transaction.commit();
        }
    */


//}
