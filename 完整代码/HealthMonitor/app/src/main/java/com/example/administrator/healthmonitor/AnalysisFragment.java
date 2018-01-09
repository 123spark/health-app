package com.example.administrator.healthmonitor;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

/**
 * Created by Administrator on 2017/12/4/004.
 */

public class AnalysisFragment extends Fragment {
    private ImageButton hrbtn;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_analysis, container,false);
        hrbtn=(ImageButton)v.findViewById(R.id.HRButton);
        hrbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), DataActivity.class));
            }
        });
        return v;
    }
}
