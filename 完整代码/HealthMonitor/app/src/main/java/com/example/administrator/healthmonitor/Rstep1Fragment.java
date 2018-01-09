package com.example.administrator.healthmonitor;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

/**
 * Created by Administrator on 2017/12/18/018.
 */

public class Rstep1Fragment extends Fragment {
    private Button continbutton;
    private EditText phonetext;
    private TextView resultext;
    private String phoneno;
    private String FILEDIR="Account_Password";
    File account =new File(Environment.getExternalStorageDirectory() + File.separator + "Account_Password" + File.separator+"AccInfo.txt");
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
                    ((RegistActivity)getActivity()).setPhone(phoneno);
                    //WriteTxtFile(phoneno+" ","AccInfo.txt");
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

    public void WriteTxtFile(String strcontent, String filename) {
        String strContent = strcontent;
        BufferedWriter mBufferedWriter=null;
        try {
            File filedir = new File(Environment.getExternalStorageDirectory() + File.separator + FILEDIR +File.separator);
            if (!filedir.exists() && !filedir.isDirectory()) {
                Log.d("TestFile", "Create the file:" + FILEDIR + File.separator);
                filedir.mkdirs();
            }
            File file = new File(
                    Environment.getExternalStorageDirectory() + File.separator + FILEDIR + File.separator+filename);
            if (!file.exists()) {
                Log.d("TestFile", "Create the file:" + filename);
                file.createNewFile();
            }
            FileWriter fw=new FileWriter(file,true);
            mBufferedWriter=new BufferedWriter(fw);
            mBufferedWriter.write(strContent);
            mBufferedWriter.flush();
            mBufferedWriter.close();
        } catch (Exception e) {
            Log.e("TestFile", "Error on write File.");
            e.printStackTrace();
        }
    }

}
