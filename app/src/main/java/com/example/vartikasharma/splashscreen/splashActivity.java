package com.example.vartikasharma.splashscreen;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

public class splashActivity extends AppCompatActivity {
    TextView signup;
    TextView signin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        signin=(TextView)findViewById(R.id.firstpagesignin);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(splashActivity.this,MainActivity.class));
            }
        });
        signup=(TextView)findViewById(R.id.firstpagesignup);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(splashActivity.this,MainActivity.class));
            }
        });




    }
}
