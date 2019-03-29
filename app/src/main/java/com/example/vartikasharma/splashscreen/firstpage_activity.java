package com.example.vartikasharma.splashscreen;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class firstpage_activity extends AppCompatActivity {
    TextView signup;
    TextView signin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        // check if already logged in

        signin=(TextView)findViewById(R.id.firstpagesignin);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(firstpage_activity.this, login_activity.class));
            }
        });

    }
}
