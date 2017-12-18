package com.example.administrator.healthmonitor;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by Administrator on 2017/12/4/004.
 */

public class AnalysisFragment extends Fragment {
    private ImageButton hrbtn,stepbtn;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_analysis, container,false);
        //hrbtn=(ImageButton)v.findViewById(R.id.HRButton);
        stepbtn=(ImageButton)v.findViewById(R.id.StepButton);
        /*hrbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.viewpager, new HeartRateFragment()).commit();
            }
        });*/
        return v;
    }
}

