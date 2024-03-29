package com.mawsom.mawsom.mawsomnobl;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;

import com.mawsom.mawsom.mawsomnobl.data.Servers;
import com.mawsom.mawsom.mawsomnobl.data.Settings;

/**
 * Created by Waqas on 6/11/2015.
 */

public class GeneralSettingsFragment extends Fragment {

    EditText nameText;
    EditText locationText;
    EditText passwordText;
    EditText forecastUrlText;
    RadioButton scaleC;
    RadioButton scaleF;
    View root;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.general_settings, null);

        nameText = (EditText) root.findViewById(R.id.nameText);
        locationText = (EditText) root.findViewById(R.id.locationText);
        passwordText = (EditText) root.findViewById(R.id.passwordText);
        forecastUrlText = (EditText) root.findViewById(R.id.forecastUrlText);
        scaleC = (RadioButton) root.findViewById(R.id.scaleC);
        scaleF = (RadioButton) root.findViewById(R.id.scaleF);

        Settings s = Settings.getCurrent();

        nameText.setText( s.getName() );
        locationText.setText( s.getLocation() );
        passwordText.setText( String.valueOf(s.getPassword()) );
        forecastUrlText.setText( s.getForecastUrl() );

        if (s.getDisplayCelsius()) scaleC.setChecked(true); else scaleF.setChecked(true);

        return root;
    }


    @Override
    public void onPause()
    {
        new Thread(new Runnable() {
            public void run() {
                saveData();
            }
        }).start();

        super.onPause();
    }

    //onPause
    private void saveData()
    {
        Settings s = Settings.getCurrent();
        s.setName( nameText.getText().toString() );
        s.setLocation( locationText.getText().toString() );
        s.setForecastUrl ( forecastUrlText.getText().toString());
        s.setPassword(passwordText.getText().toString());
        s.setDisplayCelsius(scaleC.isChecked());
        s.save();

        Servers.getCurrent().getSelectedServer().setPassword(s.getPassword());

    }

}

