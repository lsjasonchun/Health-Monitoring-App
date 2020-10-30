package com.example.health_monitoring_app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.ExceptionHandler;
import com.kosalgeek.genasync12.PostResponseAsyncTask;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    final String TAG = "LoginActivity";
    EditText login_Username, login_Password;
    Button login_Button, loginRegister_Button;
    CheckBox login_ShowPassword;
    TextView tvRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login_Username = (EditText) findViewById(R.id.login_usernameInput);
        login_Password = (EditText) findViewById(R.id.login_passwordInput);
        login_Button = (Button) findViewById(R.id.login_Button);
        login_Button.setOnClickListener(this);

        login_ShowPassword = (CheckBox) findViewById(R.id.login_showPassword);
        login_ShowPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    login_Password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    login_Password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
    }

    String password = "";

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.login_Button:
                final String username = login_Username.getText().toString();
                password = login_Password.getText().toString();

                HashMap<String, String> loginData = new HashMap<>();
                loginData.put("username", username);
                loginData.put("password", password);

                PostResponseAsyncTask loginTask = new PostResponseAsyncTask(this,
                        loginData, new AsyncResponse() {
                    @Override
                    public void processFinish(String s) {
                        Log.d(TAG, s);
                        if (s.contains("LoginSuccess")) {
                            SharedPreferences pref = getSharedPreferences("loginData", MODE_PRIVATE);
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putString("username", username);
                            editor.putString("password", password);
                            editor.commit();
                            Toast.makeText(getApplicationContext(),
                                    "Login Success.", Toast.LENGTH_LONG).show();
                            Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
                            mainIntent.putExtra("arg", username);
                            mainIntent.putExtra("ActivityID", "FromLogin");
                            startActivity(mainIntent);
                        } else {
                            Toast.makeText(getApplicationContext(),
                                    "Something went wrong. Cannot login.", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                loginTask.setExceptionHandler(new ExceptionHandler() {
                    @Override
                    public void handleException(Exception e) {
                        if (e != null && e.getMessage() != null) {
                            Log.d(TAG, e.getMessage());
                        }
                    }
                });
                loginTask.execute("http://192.168.1.66//client/login.php");

                break;
        }
    }
}
