package com.example.health_monitoring_app;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.health_monitoring_app.POJO.Client;
import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.ExceptionHandler;
import com.kosalgeek.genasync12.PostResponseAsyncTask;

import org.json.JSONObject;

import java.util.HashMap;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener{

    public static EditText usernameEt, firstNameEt, lastNameEt, passwordEt, emailEt;
    public String loggedUsername;
    BackgroundWorker backgroundWorker = new BackgroundWorker(this);
    JSONObject jsonObject;
    public Client clientInfo;
    String updateUsername, updatePwd, updateFirstName, updateLastName, updateEmail;
    public int fetchedID, fetchedGpID;
    public static final String LOG_TAG = "myLogs";

    private Button enableEditBtn, submitEditBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        usernameEt = (EditText) findViewById(R.id.profile_username);
        firstNameEt = (EditText) findViewById(R.id.profile_firstName);
        lastNameEt = (EditText) findViewById(R.id.profile_lastName);
        passwordEt = (EditText) findViewById(R.id.profile_password);
        emailEt = (EditText) findViewById(R.id.profile_email);

        enableEditBtn = (Button) findViewById(R.id.profile_enableEdit_button);
        enableEditBtn.setOnClickListener(this);
        submitEditBtn = (Button) findViewById(R.id.profile_submitEdit_button);
        submitEditBtn.setOnClickListener(this);

        usernameEt.setFocusable(false);
        firstNameEt.setFocusable(false);
        lastNameEt.setFocusable(false);
        passwordEt.setFocusable(false);
        emailEt.setFocusable(false);

        clientInfo = (Client) getIntent().getSerializableExtra("arg");

        Log.e(LOG_TAG,"PROFILE" +clientInfo.getFirstName() + clientInfo.getLastName() +clientInfo.getGpID()+ clientInfo.getClientID());

        Log.e(LOG_TAG,clientInfo.getUsername());
        usernameEt.setText(clientInfo.getUsername());
        firstNameEt.setText(clientInfo.getLastName());
        lastNameEt.setText(clientInfo.getLastName());
        passwordEt.setText(clientInfo.getPassword());
        emailEt.setText(clientInfo.getEmail());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.profile_enableEdit_button:
                usernameEt.setFocusableInTouchMode(true);
                usernameEt.setBackgroundResource(R.drawable.border_tint);
                firstNameEt.setFocusableInTouchMode(true);
                firstNameEt.setBackgroundResource(R.drawable.border_tint);
                lastNameEt.setFocusableInTouchMode(true);
                lastNameEt.setBackgroundResource(R.drawable.border_tint);
                passwordEt.setFocusableInTouchMode(true);
                passwordEt.setBackgroundResource(R.drawable.border_tint);
                emailEt.setFocusableInTouchMode(true);
                emailEt.setBackgroundResource(R.drawable.border_tint);
                Log.e(LOG_TAG,"HELLO EDIT BUTTON CLICKED");
                break;
            case R.id.profile_submitEdit_button:
                final String id = Integer.toString(fetchedID);
                updateUsername = usernameEt.getText().toString();
                updatePwd = passwordEt.getText().toString();
                updateFirstName = firstNameEt.getText().toString();
                updateLastName = lastNameEt.getText().toString();
                updateEmail = emailEt.getText().toString();

                HashMap<String, String> updateData = new HashMap<>();
                updateData.put("id", id);
                updateData.put("username", updateUsername);
                updateData.put("password", updatePwd);
                updateData.put("first_name", updateFirstName);
                updateData.put("last_name", updateLastName);
                updateData.put("email", updateEmail);

                PostResponseAsyncTask updateTask = new PostResponseAsyncTask(this,
                        updateData, new AsyncResponse() {
                    @Override
                    public void processFinish(String s) {
                        Log.d(LOG_TAG, s);
                        if(s.contains("Record Updated Successfully")){
                            SharedPreferences pref = getSharedPreferences("updateData", MODE_PRIVATE);
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putString("id", id);
                            editor.putString("username", updateUsername);
                            editor.putString("password", updatePwd);
                            editor.putString("first_name", updateFirstName);
                            editor.putString("last_name", updateLastName);
                            editor.putString("email", updateEmail);
                            editor.commit();
                            Toast.makeText(getApplicationContext(),
                                    "Record Updated Successfully", Toast.LENGTH_LONG).show();
                        }
                        else{
                            Toast.makeText(getApplicationContext(),
                                    "Something went wrong. Cannot login.", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                updateTask.setExceptionHandler(new ExceptionHandler() {
                    @Override
                    public void handleException(Exception e) {
                        if(e != null && e.getMessage() != null){
                            Log.d(LOG_TAG, e.getMessage());
                        }
                    }
                });
                updateTask.execute("http://192.168.1.65//client/updateUser.php");
                break;
        }
    }
}
