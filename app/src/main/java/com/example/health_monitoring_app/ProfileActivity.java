package com.example.health_monitoring_app;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

public class ProfileActivity extends AppCompatActivity implements BackgroundWorker.BackgroundWorkerResponse {

    public static TextView usernameTv, fullnameTv, passwordTv, emailTv;
    public String loggedUsername;
    BackgroundWorker backgroundWorker = new BackgroundWorker(this);
    JSONObject jsonObject;
    public static final String LOG_TAG = "myLogs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_profile);

        usernameTv = (TextView) findViewById(R.id.profile_username);
        fullnameTv = (TextView) findViewById(R.id.profile_name);
        passwordTv = (TextView) findViewById(R.id.profile_password);
        emailTv = (TextView) findViewById(R.id.profile_email);

        loggedUsername = getIntent().getExtras().getString("arg");

        clientInfo();
    }



    private void clientInfo() {
        String type = "fetchProfile";
        backgroundWorker.delegate = this;
        backgroundWorker.execute(type, loggedUsername);
    }

    public void processFinish(String output) {
        try {
            Log.e(LOG_TAG,output);
            String fetchedUsername, fetchedFirstName, fetechedLastName, fetchedPwd, fetchedEmail;
            int fetchedID;
//            jsonObject = new JSONObject(output);
//            fetchedID = jsonObject.getInt("id");
//            fetchedUsername = jsonObject.getString("username");
//            fetchedFirstName = jsonObject.getString("first_name");
//            fetechedLastName = jsonObject.getString("last_name");
//            fetchedPwd = jsonObject.getString("password");
//            fetchedEmail = jsonObject.getString("email");

            JSONArray jsonArr = new JSONArray(output);
            JSONObject jsonObj = jsonArr.getJSONObject(0);
            fetchedID = jsonObj.getInt("id");
            fetchedUsername = jsonObj.getString("username");
            fetchedFirstName = jsonObj.getString("first_name");
            fetechedLastName = jsonObj.getString("last_name");
            fetchedPwd = jsonObj.getString("password");
            fetchedEmail = jsonObj.getString("email");
            Log.e(LOG_TAG,fetchedUsername);

            Client clientInfo = new Client(fetchedID, fetchedUsername, fetchedFirstName, fetechedLastName, fetchedPwd, fetchedEmail);

            Log.e(LOG_TAG,clientInfo.getUsername());
            usernameTv.setText(clientInfo.getUsername());
            fullnameTv.setText(clientInfo.getFullname());
            passwordTv.setText(clientInfo.getPassword());
            emailTv.setText(clientInfo.getEmail());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
