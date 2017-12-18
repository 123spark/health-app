package com.example.administrator.healthmonitor;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Main1Activity extends AppCompatActivity {                   //注册登录界面
    private EditText accoText,passText;
    private TextView resultText;
    private Button signButton,forgetButton,newButton;
    private String accountid,password;
    private BufferedReader BR;
    private FileReader FR;
    File account =new File(Environment.getExternalStorageDirectory() + File.separator + "Account_Password" + File.separator+"AccInfo.txt");

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        accoText=(EditText)findViewById(R.id.AccountText);
        passText=(EditText)findViewById(R.id.PasswordText);
        resultText=(TextView)findViewById(R.id.ResultText);
        signButton=(Button)findViewById(R.id.signin_button);
        forgetButton=(Button)findViewById(R.id.forget_button);
        newButton=(Button)findViewById(R.id.new_button);
    }
    protected void onStart()
    {
        super.onStart();
        signButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s;
                String[] info=new String[2];
                accountid=accoText.getText().toString();
                password=passText.getText().toString();
                try {
                    FR = new FileReader(account);
                    BR=new BufferedReader(FR);
                    while((s=BR.readLine())!=null){
                        info=s.split(" ");
                        if(accountid.equals(info[0])){
                            if(password.equals(info[1])){
                                break;
                            }
                            else{
                                passText.setText("");
                                resultText.setText("密码错误！");
                                break;
                            }
                        }
                        else;
                    }
                    if(s!=null&&password.equals(info[1]))
                    {
                        resultText.setText("登录成功！");
                        Intent i=new Intent();
                        i.setClass(Main1Activity.this,Main2Activity.class);
                        Main1Activity.this.startActivity(i);
                    }
                    else if(s==null){
                        accoText.setText("");
                        passText.setText("");
                        resultText.setText("用户不存在，请注册！");
                    }
                }catch(IOException e){e.printStackTrace();}
            }
        });
        forgetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到找回密码页面
            }
        });
        newButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent();
                //i.setClass(MainActivity.this,New1Activity.class);
                i.setClass(Main1Activity.this,Main2Activity.class);
                Main1Activity.this.startActivity(i);
                onStop();
            }
        });
    }
}
