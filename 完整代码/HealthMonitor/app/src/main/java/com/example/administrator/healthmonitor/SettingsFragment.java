package com.example.administrator.healthmonitor;

import android.os.Environment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by Administrator on 2017/12/4/004.
 */

public class SettingsFragment extends Fragment {
    private BufferedReader BR;
    private FileReader FR;
    private String info;
    private String[] splitinfo=new String[4];
    private TextView name_show,gender_show,age_show;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_settings, container,false);
        name_show=(TextView)v.findViewById(R.id.name_show);
        gender_show=(TextView)v.findViewById(R.id.gender_show);
        age_show=(TextView)v.findViewById(R.id.age_show);
        File account =new File(Environment.getExternalStorageDirectory() + File.separator + "Account_Password" + File.separator+"AccInfo.txt");
        try {
            FR = new FileReader(account);
            BR = new BufferedReader(FR);
            info=BR.readLine();
            splitinfo=info.split(" ");
            name_show.setText(splitinfo[1]);
            age_show.setText(splitinfo[3]);
        }catch(IOException e){e.printStackTrace();}
        return v;
    }
}
