package com.example.administrator.healthmonitor;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by Administrator on 2017/12/4/004.
 */

public class FinderFragment extends Fragment {
    private Button openBtn;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_finder, container,false);
        openBtn=(Button)v.findViewById(R.id.button_dakai);
        openBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent();
                i.setClass(getActivity(),ClassicBluetoothActivity.class);
                startActivity(i);
            }
        });
        return v;
    }
}
