package com.example.health_monitoring_app;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity{

    EditText usernameEt, firstnameEt, lastnameEt, passwordEt, emailEt;
    EditText gpClinicEt, gpNameEt, gpEmailEt, gpContactEt;
    Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        usernameEt = (EditText) findViewById(R.id.reg_username);
        firstnameEt = (EditText) findViewById(R.id.reg_firstname);
        lastnameEt = (EditText) findViewById(R.id.reg_lastname);
        passwordEt = (EditText) findViewById(R.id.reg_password);
        emailEt = (EditText) findViewById(R.id.reg_email);

        gpClinicEt = (EditText) findViewById(R.id.reg_gp_clinic);
        gpNameEt = (EditText) findViewById(R.id.reg_gp_name);
        gpEmailEt = (EditText) findViewById(R.id.reg_gp_email);
        gpContactEt = (EditText) findViewById(R.id.reg_gp_contact);

        submitButton = (Button) findViewById(R.id.reg_submit_button);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnRegister(v);
            }
        });
    }

    public void OnRegister(View view) {
        String username = usernameEt.getText().toString();
        String firstname = firstnameEt.getText().toString();
        String lastname = lastnameEt.getText().toString();
        String password = passwordEt.getText().toString();
        String email = emailEt.getText().toString();

        String gpClinic = gpClinicEt.getText().toString();
        String gpName = gpNameEt.getText().toString();
        String gpEmail = gpEmailEt.getText().toString();
        String gpContact = gpContactEt.getText().toString();

        String type = "register";

        BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        backgroundWorker.execute(type, username, password, firstname, lastname, email);
    }
}
