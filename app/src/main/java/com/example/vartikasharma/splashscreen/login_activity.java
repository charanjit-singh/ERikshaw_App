package com.example.vartikasharma.splashscreen;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vartikasharma.splashscreen.rest.ApiClient;
import com.example.vartikasharma.splashscreen.rest.ApiInterface;
import com.google.gson.JsonObject;
import com.example.vartikasharma.splashscreen.ApplicationState;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class login_activity extends AppCompatActivity {
    // Login page Activity: For Driver Application, Only Login is required
    EditText mEditUsername, mEditPassword;
    TextView mLoginButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_login);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("SharedData", 0); // 0 - for private mode
        final SharedPreferences.Editor editor = pref.edit();
        if (!pref.getString("token","NA").equals("NA"))
        {
            // note not
            Toast.makeText(this, "Already Logged in", Toast.LENGTH_SHORT).show();
        }
        mLoginButton = (TextView) findViewById(R.id.btn_login);
        mEditPassword = (EditText) findViewById(R.id.edit_password);
        mEditUsername = (EditText) findViewById(R.id.edit_username);
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = mEditUsername.getText().toString();
                String password = mEditPassword.getText().toString();
                ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
                Call<JsonObject> call = apiInterface.login(username, password);
                call.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        Boolean invalidLogin = true;

                        try {
                            String status = response.body().get("status").getAsString();

                            if (status.equals("success")) {
                                String token = response.body().get("token").getAsString();
                                editor.putString("token", token);
                                editor.commit();
                                Toast.makeText(login_activity.this, "Token: " + token, Toast.LENGTH_SHORT).show();
                                invalidLogin = false;

                            }
                        }
                        catch (Exception e){
                                e.printStackTrace();
                        }
                        if(invalidLogin)
                        {
                            Toast.makeText(login_activity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            startActivity(new Intent(login_activity.this, mainpage_activity.class));
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        Toast.makeText(login_activity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });
    }


}
