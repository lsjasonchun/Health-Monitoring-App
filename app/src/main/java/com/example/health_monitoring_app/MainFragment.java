package com.example.health_monitoring_app;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONObject;

public class MainFragment extends Fragment implements BackgroundWorker.BackgroundWorkerResponse{

    public static final String LOG_TAG = "MAIN FRAGMENT";
    private static final int REQUEST_CALL = 1;
    BackgroundWorker backgroundWorker = new BackgroundWorker(getActivity());
    final String TAG = "MAINFRAG";
    ImageButton sosBtn;
    TextView  ewsTv, breathRateTV, tempTV, bloodPressureTV, oxSatTV, heartRateTV, consciousLvlTV;
    String breathRate, temp, bloodPressure, oxySat, heartRate;
    int ewsValue;
    public int ewsHighLvl= 7;
    public int ewsMediumLvl= 5;
    public String usersGpID, fetchedGpEmail, emailSubject, emailMessage, clientInfoString;
    public boolean sendEmail = false;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);

        usersGpID = ((MainActivity) getActivity()).getGpID();
        clientInfoString = ((MainActivity) getActivity()).getClientInfoString();
        Log.e(LOG_TAG, "USER GP ID "+usersGpID);
        Log.e(LOG_TAG,"Clinent info!!!" + clientInfoString);
        gpInfo();

        ewsTv = (TextView) v.findViewById(R.id.main_ews_value);
        breathRateTV = (TextView) v.findViewById(R.id.main_respiratory_value);
        tempTV = (TextView) v.findViewById(R.id.main_temperature_value);
        bloodPressureTV = (TextView) v.findViewById(R.id.main_blood_pressure_value);
        heartRateTV = (TextView) v.findViewById(R.id.main_heart_rate_value);
        oxSatTV = (TextView) v.findViewById(R.id.main_oxygen_sat_value);
        consciousLvlTV = (TextView) v.findViewById(R.id.main_consciousness_value);

        breathRate = "11";
        temp = "38.8";
        bloodPressure = "90";
        oxySat = "92.2";
        heartRate = "50";

        breathRateTV.setText(breathRate);
        tempTV.setText(temp);
        bloodPressureTV.setText(bloodPressure);
        heartRateTV.setText(heartRate);
        oxSatTV.setText(oxySat);
        consciousLvlTV.setText("Alert");

        getEwsValue(Float.parseFloat(breathRate), Float.parseFloat(temp), Float.parseFloat(bloodPressure),
                Float.parseFloat(heartRate), Float.parseFloat(oxySat));

        sosBtn = v.findViewById(R.id.main_help_button);
        sosBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeEmergencyCall();
            }
        });

        return v;
    }

    private void makeEmergencyCall(){
        String emergencyNum = "111";

        if(ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[] {Manifest.permission.CALL_PHONE}, REQUEST_CALL);
        } else {
            String dial = "tel:" + emergencyNum;
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
        }
    }

    // The result of when a permission was requested to user if permission status was not already granted
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQUEST_CALL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                makeEmergencyCall();
            } else {
                Toast.makeText(getActivity(), "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void getEwsValue(float bR, float tem, float bP, float hR, float oS){
        int ewsValue = 0;
        int highLvlCheck = 0;
        int dangerousLvl = 3;
        int mediumLvl = 2;
        int minorLvl = 1;
        Log.d(TAG, String.valueOf(tem));

        //Breathe Rate EWS Logic
        if((bR >= 25) || (bR <= 8)) {
            ewsValue = ewsValue + dangerousLvl;
            highLvlCheck = highLvlCheck + 1;
            breathRateTV.setBackgroundColor(Color.parseColor("#FF5555"));
        }
        else if((bR >= 21) && (bR <= 24)) {
            ewsValue = ewsValue + mediumLvl;
            breathRateTV.setBackgroundColor(Color.parseColor("#FFDF00"));
        }
        else if((bR >= 9) && (bR <= 11)) {
            ewsValue = ewsValue + minorLvl;
            breathRateTV.setBackgroundColor(Color.parseColor("#32CD32"));
        }

        //Temperature EWS Logic
        if(tem >= 39) {
            ewsValue = ewsValue + mediumLvl;
            tempTV.setBackgroundColor(Color.parseColor("#FFDF00"));
        }
        else if((tem >= 38) && (tem < 39) || (tem > 35) && (tem < 36)) {
            ewsValue = ewsValue + minorLvl;
            tempTV.setBackgroundColor(Color.parseColor("#32CD32"));
        }
        else if(tem <= 35) {
            ewsValue = ewsValue + dangerousLvl;
            highLvlCheck = highLvlCheck + 1;
            tempTV.setBackgroundColor(Color.parseColor("#FF5555"));
        }

        //Blood Pressure EWS Logic
        if((bP >= 220) || (bP <= 90)) {
            ewsValue = ewsValue + dangerousLvl;
            highLvlCheck = highLvlCheck + 1;
            bloodPressureTV.setBackgroundColor(Color.parseColor("#FF5555"));
        }
        else if((bP >= 100) && (bP < 100)) {
            ewsValue = ewsValue + minorLvl;
            bloodPressureTV.setBackgroundColor(Color.parseColor("#32CD32"));
        }
        else if((bP > 90) && (bP < 100)) {
            ewsValue = ewsValue + mediumLvl;
            bloodPressureTV.setBackgroundColor(Color.parseColor("#FFDF00"));
        }

        //Heart Rate EWS Logic
        if((hR >= 130) || (hR <= 40)) {
            ewsValue = ewsValue + dangerousLvl;
            highLvlCheck = highLvlCheck + 1;
            heartRateTV.setBackgroundColor(Color.parseColor("#FF5555"));
        }
        else if((hR >= 110) && (hR < 130)) {
            ewsValue = ewsValue + mediumLvl;
            heartRateTV.setBackgroundColor(Color.parseColor("#FFDF00"));
        }
        else if(((hR >= 90) && (hR < 110)) || ((hR > 40) && (hR <= 50))) {
            ewsValue = ewsValue + minorLvl;
            heartRateTV.setBackgroundColor(Color.parseColor("#32CD32"));
        }

        //Oxygen Saturation EWS Logic
        if(oS <= 91) {
            ewsValue = ewsValue + dangerousLvl;
            highLvlCheck = highLvlCheck + 1;
            oxSatTV.setBackgroundColor(Color.parseColor("#FF5555"));
        }
        else if((oS >= 92) && (oS <= 93)) {
            ewsValue = ewsValue + mediumLvl;
            oxSatTV.setBackgroundColor(Color.parseColor("#FFDF00"));
        }
        else if((oS >= 94) && (oS <= 95)) {
            ewsValue = ewsValue + minorLvl;
            oxSatTV.setBackgroundColor(Color.parseColor("#32CD32"));
        }

        if(ewsValue >= ewsHighLvl || highLvlCheck >= 1) {
            ewsTv.setBackgroundColor(Color.parseColor("#FF5555"));
            emailSubject = getString(R.string.email_highLvl_subject);
            emailMessage = getString(R.string.email_highLvl_message) + clientInfoString;
            Log.e(LOG_TAG, "EWS METHOD: HIGHLEVEL");
            sendEmail = true;
        }
        else if(ewsValue >= ewsMediumLvl && ewsValue < ewsHighLvl) {
            ewsTv.setBackgroundColor(Color.parseColor("#FFDF00"));
            emailSubject = getString(R.string.email_mediumLvl_subject);
            emailMessage = getString(R.string.email_mediumLvl_message) + clientInfoString;
            Log.e(LOG_TAG, "EWS METHOD: MEDIUM LEVEL");
            sendEmail = true;
        }

        ewsTv.setText(String.valueOf(ewsValue));
    }

    private void sendEmail(String fetchedGpEmail, String subject, String message) {
        JavaMailAPI javaMailAPI = new JavaMailAPI(getActivity(), this.fetchedGpEmail, subject, message);

        javaMailAPI.execute();
    }

    private void gpInfo() {
        String type = "fetchGpInfo";
        backgroundWorker.delegate = this;
        backgroundWorker.execute(type, usersGpID);
    }

    public void processFinish(String output) {
        try {
            Log.e(LOG_TAG, output);

            JSONArray jsonArr = new JSONArray(output);
            JSONObject jsonObj = jsonArr.getJSONObject(0);
            fetchedGpEmail = jsonObj.getString("gp_email");
            Log.e(LOG_TAG, fetchedGpEmail);


            if(sendEmail == true) {
                Log.e("SENDEMAIL TRUE: ", emailMessage + " " +emailSubject);
                sendEmail(fetchedGpEmail, emailSubject, emailMessage);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
