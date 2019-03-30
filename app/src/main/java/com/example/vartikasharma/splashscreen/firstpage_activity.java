package com.example.vartikasharma.splashscreen;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class firstpage_activity extends AppCompatActivity {

    TextView signin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        // check if already logged in
        SharedPreferences pref = getApplicationContext().getSharedPreferences("SharedData", 0); // 0 - for private mode
        if (!pref.getString("token","NA").equals("NA"))
        {
            // note not
//            Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
            Intent main_activity = new Intent(firstpage_activity.this, mainpage_activity.class);
            startActivity(main_activity);
            return;
        }

        signin=(TextView)findViewById(R.id.firstpagesignin);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(firstpage_activity.this, login_activity.class));
            }
        });

    }
}
