package com.example.administrator.healthmonitor;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Administrator on 2017/12/18/018.
 */

public class Fstep1Fragment extends Fragment {
    private String code;
    private EditText codetext;
    private Button codebtn,confirmbtn;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_fstep1, container,false);
        codebtn=(Button)v.findViewById(R.id.button_code);
        codetext=(EditText)v.findViewById(R.id.edit_yanzheng) ;
        confirmbtn=(Button)v.findViewById(R.id.button_confirm1);
        return v;
    }
    public void onStart(){
        super.onStart();
        codebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "验证码已发送", Toast.LENGTH_LONG).show();
            }
        });
        confirmbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s="1234";
                code=codetext.getText().toString();
                if(code.equals(s)){
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.Ffragment, new Fstep2Fragment(), null)
                            .commit();
                    onStop();
                }
                else{
                    Toast.makeText(getActivity(), "验证码错误", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
