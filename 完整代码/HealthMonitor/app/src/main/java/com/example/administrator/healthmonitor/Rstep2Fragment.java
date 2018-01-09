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
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by Administrator on 2017/12/18/018.
 */

public class Rstep2Fragment extends Fragment {
    private Button submitbutton;
    private EditText passwordtext;
    private EditText nicknametext;
    private EditText agetext;
    private TextView hint1,hint2;
    private String nickname,password,age;
    private String FILEDIR="Account_Password";
    private BufferedReader BR;
    private FileReader FR;
    private FileWriter FW;
    private BufferedWriter mBufferedWriter;
    File account =new File(Environment.getExternalStorageDirectory() + File.separator + "Account_Password" + File.separator+"AccInfo.txt");
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_rstep2, container,false);
        submitbutton=(Button)v.findViewById(R.id.submit_button);
        passwordtext=(EditText)v.findViewById(R.id.PasswordText);
        nicknametext=(EditText)v.findViewById(R.id.NickNameText);
        agetext=(EditText)v.findViewById(R.id.AgeText);
        hint1=(TextView)v.findViewById(R.id.hint1view);
        hint2=(TextView)v.findViewById(R.id.hint2view);
        return v;
    }
    public void onStart(){
        super.onStart();
        hint1.setText("");
        hint2.setText("");
        nickname="";
        password="";
        submitbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                password=passwordtext.getText().toString();
                nickname=nicknametext.getText().toString();
                age=agetext.getText().toString();
                if(password.length()<8){
                    passwordtext.setText("");
                    hint1.setText("Please follow the rules below.");
                }
                else{
                    String s,phone;
                    try{
                        FR=new FileReader(account);
                        BR=new BufferedReader(FR);
                        phone=((RegistActivity)getActivity()).getPhone();
                        s=phone+" "+nickname+" "+password+" "+age;
                        new NetworkThread(1,s).start();
                        WriteTxtFile(s+"\r\n","AccInfo.txt");
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.Rfragment, new Rstep3Fragment(), null).commit();
                        onStop();
                    }catch(IOException e){e.printStackTrace();}
                }
            }
        });
    }

    public void WriteTxtFile(String strcontent, String filename) {
        String strContent = strcontent;
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
