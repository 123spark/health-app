package com.example.administrator.healthmonitor;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

public class New2Activity extends AppCompatActivity {
    private Button submitbutton;
    private EditText passwordtext;
    private EditText nicknametext;
    private TextView hint1,hint2;
    private String nickname,password;
    private String FILEDIR="Account_Password";
    private BufferedReader BR;
    private FileReader FR;
    private FileWriter FW;
    private BufferedWriter mBufferedWriter;
    File account =new File(Environment.getExternalStorageDirectory() + File.separator + "Account_Password" + File.separator+"AccInfo.txt");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new2);
        submitbutton=(Button)findViewById(R.id.submit_button);
        passwordtext=(EditText)findViewById(R.id.PasswordText);
        nicknametext=(EditText)findViewById(R.id.NickNameText);
        hint1=(TextView)findViewById(R.id.hint1view);
        hint2=(TextView)findViewById(R.id.hint2view);
    }
        protected void onStart(){
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
                    if(password.length()<8){
                        passwordtext.setText("");
                        hint1.setText("Please follow the rules below.");
                    }
                    else{
                        String s,namexist;
                        try{
                            FR=new FileReader(account);
                            BR=new BufferedReader(FR);
                            while((s=BR.readLine())!=null){
                                namexist=s.split(" ")[0];
                                if(nickname.equals(namexist))
                                    break;
                            }
                            if(s!=null){
                                nicknametext.setText("");
                                hint2.setText("NickName already exits!");
                            }
                            else{
                                s=nickname+" "+password+"\r\n";
                                WriteTxtFile(s,"AccInfo.txt");
                                Intent i=new Intent();
                                i.setClass(New2Activity.this,New3Activity.class);
                                New2Activity.this.startActivity(i);
                                onStop();
                            }
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
            Toast.makeText(this,"存储成功！",Toast.LENGTH_SHORT).show();
            mBufferedWriter.flush();
            mBufferedWriter.close();
        } catch (Exception e) {
            Log.e("TestFile", "Error on write File.");
            Toast.makeText(this,"存储失败！",Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}
