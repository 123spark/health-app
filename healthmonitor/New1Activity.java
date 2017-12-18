package com.example.administrator.healthmonitor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class New1Activity extends AppCompatActivity {
    private Button continbutton;
    private EditText phonetext;
    private TextView resultext;
    private String phoneno;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new1);
        continbutton=(Button)findViewById(R.id.conti_button);
        phonetext=(EditText)findViewById(R.id.PhoneText);
        resultext=(TextView)findViewById(R.id.R2View);
    }

    protected void onStart(){
        super.onStart();
        resultext.setText("");
        continbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneno = phonetext.getText().toString();
                if(phoneno.length()==11){
                    Intent i=new Intent();
                    i.setClass(New1Activity.this,New2Activity.class);
                    New1Activity.this.startActivity(i);
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
