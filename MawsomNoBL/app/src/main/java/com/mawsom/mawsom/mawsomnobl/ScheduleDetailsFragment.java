package com.mawsom.mawsom.mawsomnobl;

import android.R;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.mawsom.mawsom.mawsomnobl.data.Schedules;

/**
 * Created by Waqas on 6/11/2015.
 */

public class ScheduleDetailsFragment extends Fragment {

    private int scheduleIndex=0;

    EditText scheduleNameText;
    Button deleteButton;


    public ScheduleDetailsFragment()
    {

    }

    public ScheduleDetailsFragment(int scheduleIndex)
    {
        this.scheduleIndex = scheduleIndex;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.schedule_details_tab, null);



        scheduleNameText = (EditText) root.findViewById(R.id.scheduleNameText);
        deleteButton = (Button) root.findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {public void onClick(View view) {delete();}});


        scheduleNameText.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                Schedules.getCurrent().get(scheduleIndex).setName(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

        });

        scheduleNameText.setText(Schedules.getCurrent().get(scheduleIndex).getName());



        return root;
    }

    private void delete()
    {
        Builder d = new AlertDialog.Builder(getActivity());
        d.setIcon(android.R.drawable.ic_dialog_alert);
        d.setTitle("Delete Schedule");
        d.setMessage("Are you sure you wish to delete this schedule?  It can not be undone.");
        d.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Schedules.getCurrent().remove(scheduleIndex);
                getActivity().finish();
            }

        });
        d.setNegativeButton("No", null);
        d.show();
    }


}
