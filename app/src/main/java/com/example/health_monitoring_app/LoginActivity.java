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

//public class LoginActivity extends AppCompatActivity {
//    EditText login_Username, login_Password;
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_login);
//
//        login_Username = (EditText) findViewById(R.id.login_usernameInput);
//        login_Password = (EditText) findViewById(R.id.login_passwordInput);
//    }
//
//    public void OnLogin(View view) {
//        String username = login_Username.getText().toString();
//        String password = login_Password.getText().toString();
//        String operation = "login";
//        BackgroundWorker backgroundWorker = new BackgroundWorker(this);
//        backgroundWorker.execute(operation, username, password);
//    }
//}

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
        login_Button = (Button)findViewById(R.id.login_Button);
        login_Button.setOnClickListener(this);

        loginRegister_Button = (Button)findViewById(R.id.login_registerButton);
        loginRegister_Button.setOnClickListener(this);

        login_ShowPassword = (CheckBox) findViewById(R.id.login_showPassword);
        login_ShowPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b) {
                    login_Password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                else {
                    login_Password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

//        tvRegister = (TextView)findViewById(R.id.tvRegister);
//        tvRegister.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent in = new Intent(getApplicationContext(), RegisterActivity.class);
//                startActivity(in);
//            }
//        });
    }

    private boolean emptyValidate(EditText etEmail, EditText etPassword){
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();
        return (email.isEmpty() && password.isEmpty());
    }
    String password = "";
    @Override
    public void onClick(View v) {

        switch(v.getId()) {
            case R.id.login_Button:
                final String username = login_Username.getText().toString();
                password =  login_Password.getText().toString();

                HashMap<String, String> loginData = new HashMap<>();
                loginData.put("username", username);
                loginData.put("password", password);

                PostResponseAsyncTask loginTask = new PostResponseAsyncTask(this,
                        loginData, new AsyncResponse() {
                    @Override
                    public void processFinish(String s) {
                        Log.d(TAG, s);
                        if(s.contains("LoginSuccess")){
                            SharedPreferences pref = getSharedPreferences("loginData", MODE_PRIVATE);
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putString("username", username);
                            editor.putString("password", password);
                            editor.commit();
                            Toast.makeText(getApplicationContext(),
                                    "Login Success.", Toast.LENGTH_LONG).show();
                            Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
                            mainIntent.putExtra("arg", username);
                            startActivity(mainIntent);
                        }
                        else{
                            Toast.makeText(getApplicationContext(),
                                    "Something went wrong. Cannot login.", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                loginTask.setExceptionHandler(new ExceptionHandler() {
                    @Override
                    public void handleException(Exception e) {
                        if(e != null && e.getMessage() != null){
                            Log.d(TAG, e.getMessage());
                        }
                    }
                });
                loginTask.execute("http://192.168.1.65//client/login.php");
                break;
            case R.id.login_registerButton:
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(registerIntent);
                break;
        }
    }
}
