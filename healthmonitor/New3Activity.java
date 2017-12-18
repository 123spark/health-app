package com.example.administrator.healthmonitor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.BufferedReader;

public class New3Activity extends AppCompatActivity {
    private Button loginbutton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new3);
        loginbutton=(Button)findViewById(R.id.login_button);
    }

    protected void onStart(){
        super.onStart();
        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent();
                i.setClass(New3Activity.this,MainActivity.class);
                New3Activity.this.startActivity(i);
                onStop();
            }
        });
    }
}
