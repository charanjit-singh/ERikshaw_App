package com.example.vartikasharma.splashscreen;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Pattern;

public class register2 extends AppCompatActivity {

    EditText yournumberreg;
    Button  nextbutt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);
         yournumberreg = findViewById(R.id.numberreg);
         nextbutt = findViewById(R.id.next);
        nextbutt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkdataentered();
            }
        });
    }

    private boolean isValidPhone(String phone)
    {
        boolean check=false;
        if(!Pattern.matches("[a-zA-Z]+", phone))
        {
            if(phone.length() < 10)
            {
                check = false;

            }
            else
            {
                check = true;

            }
        }
        else
        {
            check=false;
        }
        return check;
    }

   void checkdataentered() {
       if (isValidPhone(yournumberreg.getText().toString())) {

           startActivity(new Intent(register2.this,register3.class));
       } else {

           yournumberreg.setError("Please Enter Valid Number");

       }

   }

}
