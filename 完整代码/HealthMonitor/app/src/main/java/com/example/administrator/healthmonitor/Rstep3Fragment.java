package com.example.administrator.healthmonitor;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by Administrator on 2017/12/18/018.
 */

public class Rstep3Fragment extends Fragment {
    private Button loginbutton;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_rstep3, container,false);
        loginbutton=(Button)v.findViewById(R.id.login_button);
        return v;
    }
    public void onStart() {
        super.onStart();
        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), MainActivity.class));
                onStop();
            }
        });
    }
}
