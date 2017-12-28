package com.example.administrator.healthmonitor;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Administrator on 2017/12/18/018.
 */

public class Rstep1Fragment extends Fragment {
    private Button continbutton;
    private EditText phonetext;
    private TextView resultext;
    private String phoneno;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_rstep1, container,false);
        continbutton=(Button)v.findViewById(R.id.conti_button);
        phonetext=(EditText)v.findViewById(R.id.PhoneText);
        resultext=(TextView)v.findViewById(R.id.R2View);
        return v;
    }
    public void onStart(){
        super.onStart();
        resultext.setText("");
        continbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneno = phonetext.getText().toString();
                if(phoneno.length()==11){
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.Rfragment, new Rstep2Fragment(), null)
                            .commit();
                    onStop();
                }
                else{
                    resultext.setText("请重新输入手机号码！");
                    phonetext.setText("");
                }
            }
        });
    }
}
