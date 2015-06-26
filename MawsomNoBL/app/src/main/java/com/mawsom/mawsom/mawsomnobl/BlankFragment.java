package com.mawsom.mawsom.mawsomnobl;

/**
 * Created by Waqas on 6/26/2015.
 */
import android.R;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class BlankFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.blank);
        return root;
    }

}
