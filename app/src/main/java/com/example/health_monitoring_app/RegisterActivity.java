package com.example.health_monitoring_app;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RegisterActivity extends AppCompatActivity implements AsyncResponse {

    public static final String LOG_TAG = "REGISTER LOG";
    EditText usernameEt, firstnameEt, lastnameEt, passwordEt, emailEt;
    EditText gpClinicEt, gpNameEt, gpEmailEt, gpContactEt;
    Button submitButton;
    List<GP> gpList;
    List<String> gpListString;
    Spinner gpSpinner;
    public String selectedGP;
    public String selectGpID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        usernameEt = (EditText) findViewById(R.id.reg_username);
        firstnameEt = (EditText) findViewById(R.id.reg_firstname);
        lastnameEt = (EditText) findViewById(R.id.reg_lastname);
        passwordEt = (EditText) findViewById(R.id.reg_password);
        emailEt = (EditText) findViewById(R.id.reg_email);

        submitButton = (Button) findViewById(R.id.reg_submit_button);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnRegister(v);
            }
        });

        PostResponseAsyncTask readData = new PostResponseAsyncTask(this, this);
        readData.execute("http://192.168.1.65//client/gp.php");
    }

    @Override
    public void processFinish(String output) {
        try {
            Log.e(LOG_TAG,output);

            gpList = new ArrayList<GP>();
            gpListString = new ArrayList<>();
            gpListString.add("Select your General Practictioner");

            JSONArray jsonArr = new JSONArray(output);

            for (int i = 0; i < jsonArr.length(); i++) {
                JSONObject jsonObj = jsonArr.getJSONObject(i);
                Log.e(LOG_TAG, Integer.toString(jsonArr.length()));

                int gp_id = Integer.parseInt(jsonObj.getString("gp_id"));
                String gp_name = jsonObj.getString("gp_name");
                String gp_clinic = jsonObj.getString("gp_clinic");
                int gp_clinc_num = Integer.parseInt(jsonObj.getString("gp_clinc_num"));
                String gp_email = jsonObj.getString("gp_email");

                gpList.add( new GP(gp_id, gp_clinc_num, gp_clinic, gp_name, gp_email));

                String gpDropdownString = gpList.get(i).getGpID() + " " + gpList.get(i).getGpName() + " " + gpList.get(i).getGpClinic();

                Log.e(LOG_TAG, gpDropdownString);
                gpListString.add(gpDropdownString);

                Log.e(LOG_TAG, gpListString.get(i));

            }

            gpSpinner = (Spinner) findViewById(R.id.gp_dropdown);

            Log.e(LOG_TAG, gpListString.get(0));

            ArrayAdapter<String> gpAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, gpListString);
            gpAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            gpSpinner.setAdapter(gpAdapter);
            gpSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    selectedGP = gpSpinner.getSelectedItem().toString();
                    Log.e(LOG_TAG, selectedGP);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void OnRegister(View view) {
        String username = usernameEt.getText().toString();
        String firstname = firstnameEt.getText().toString();
        String lastname = lastnameEt.getText().toString();
        String password = passwordEt.getText().toString();
        String email = emailEt.getText().toString();

        String type = "register";

        selectGpID = Character.toString(selectedGP.charAt(0));

        Log.e(LOG_TAG, "USER NAME: "+username);

        if(username.isEmpty() || firstname.isEmpty() || lastname.isEmpty() ||
                password.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "At least 1 input is empty", Toast.LENGTH_SHORT).show();
        }
        else if(Character.isDigit(selectedGP.charAt(0))) {
            BackgroundWorker backgroundWorker = new BackgroundWorker(this);
            backgroundWorker.execute(type, username, password, firstname, lastname, email, selectGpID);
        }
        else {
            Toast.makeText(this, "Select your GP in dropdown above", Toast.LENGTH_SHORT).show();
        }
    }
}
