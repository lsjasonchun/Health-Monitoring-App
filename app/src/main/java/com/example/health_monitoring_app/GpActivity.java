package com.example.health_monitoring_app;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.ExceptionHandler;
import com.kosalgeek.genasync12.PostResponseAsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

public class GpActivity extends AppCompatActivity implements AsyncResponse, BackgroundWorker.BackgroundWorkerResponse {

    public static TextView gpNameTv, gpClinicTv, gpContactTv, emailTv;
    private static final int REQUEST_CALL = 1;
    public Button gpEditBtn;
    public String usersGpID;
    BackgroundWorker backgroundWorker = new BackgroundWorker(this);
    JSONObject jsonObject;
    public Client clientInfo;
    public GP gpInfo;
    public String fetchedGpClinic, fetechedGpName, fetchedGpEmail;
    public int fetchedGpID, fetchedGpContact;
    public static final String LOG_TAG = "myLogs";
    public AlertDialog.Builder builder;

    private Button enableEditBtn, submitEditBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gp_profile);

        builder = new AlertDialog.Builder(this);

        builder.setMessage(R.string.dialog_message)
                .setTitle(R.string.dialog_title);

        // Add the buttons
        builder.setPositiveButton(R.string.callGp, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                makeEmergencyCall(Integer.toString(gpInfo.getGpContact()));
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        gpNameTv = (TextView) findViewById(R.id.profile_gpName);
        gpClinicTv = (TextView) findViewById(R.id.profile_gpClinic);
        gpContactTv = (TextView) findViewById(R.id.profile_gpContact);
        emailTv = (TextView) findViewById(R.id.profile_gpEmail);
        gpEditBtn = (Button) findViewById(R.id.gp_edit_button);
        gpEditBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        clientInfo = (Client) getIntent().getSerializableExtra("arg");

        usersGpID = Integer.toString(clientInfo.getGpID());

        gpInfo();
    }


    private void gpInfo() {
        String type = "fetchGpInfo";
        backgroundWorker.delegate = this;
        backgroundWorker.execute(type, usersGpID);
    }

    private void makeEmergencyCall(String gpContact){

        if(ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.CALL_PHONE}, REQUEST_CALL);
        } else {
            String dial = "tel:" + gpContact;
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
        }
    }

    public void processFinish(String output) {
        if (Character.isDigit(output.charAt(0))) {
            Toast.makeText(this, output, Toast.LENGTH_SHORT).show();
        } else {
            try {
                Log.e(LOG_TAG, output);

                JSONArray jsonArr = new JSONArray(output);
                JSONObject jsonObj = jsonArr.getJSONObject(0);
                fetchedGpID = jsonObj.getInt("gp_id");
                fetchedGpClinic = jsonObj.getString("gp_clinic");
                fetechedGpName = jsonObj.getString("gp_name");
                fetchedGpEmail = jsonObj.getString("gp_email");
                fetchedGpContact = jsonObj.getInt("gp_clinc_num");
                Log.e(LOG_TAG, fetechedGpName);

                gpInfo = new GP(fetchedGpID, fetchedGpContact, fetchedGpClinic, fetechedGpName, fetchedGpEmail);

                Log.e(LOG_TAG, gpInfo.getGpName());
                gpNameTv.setText(gpInfo.getGpName());
                gpClinicTv.setText(gpInfo.getGpClinic());
                emailTv.setText(gpInfo.getGpEmail());
                gpContactTv.setText(gpInfo.getGpContact());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
