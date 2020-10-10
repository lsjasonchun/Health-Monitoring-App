package com.example.health_monitoring_app;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class MainFragment extends Fragment {
    final String TAG = "MAINFRAG";
    ImageButton sosBtn;
    TextView  ewsTv, breathRateTV, tempTV, bloodPressureTV, oxSatTV, heartRateTV, consciousLvlTV;
    String breathRate, temp, bloodPressure, oxySat, heartRate;
    int ewsValue;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);

        ewsTv = (TextView) v.findViewById(R.id.main_ews_value);
        breathRateTV = (TextView) v.findViewById(R.id.main_respiratory_value);
        tempTV = (TextView) v.findViewById(R.id.main_temperature_value);
        bloodPressureTV = (TextView) v.findViewById(R.id.main_blood_pressure_value);
        heartRateTV = (TextView) v.findViewById(R.id.main_heart_rate_value);
        oxSatTV = (TextView) v.findViewById(R.id.main_oxygen_sat_value);
        consciousLvlTV = (TextView) v.findViewById(R.id.main_consciousness_value);

        breathRate = "12";
        temp = "38.8";
        bloodPressure = "250";
        oxySat = "92.2";
        heartRate = "50";

        breathRateTV.setText(breathRate);
        tempTV.setText(temp);
        bloodPressureTV.setText(bloodPressure);
        heartRateTV.setText(heartRate);
        oxSatTV.setText(oxySat);
        consciousLvlTV.setText("Alert");

        ewsValue = getEwsValue(Float.parseFloat(breathRate), Float.parseFloat(temp), Float.parseFloat(bloodPressure),
                Float.parseFloat(heartRate), Float.parseFloat(oxySat));

        Log.d(TAG, String.valueOf(ewsValue));
        ewsTv.setText(String.valueOf(ewsValue));

        sosBtn = v.findViewById(R.id.main_help_button);
        sosBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return v;
    }

    public int getEwsValue(float bR, float tem, float bP, float hR, float oS){
        int ewsValue = 0;
        int dangerousLvl = 3;
        int mediumLvl = 2;
        int minorLvl = 1;
        Log.d(TAG, String.valueOf(tem));

        //Breathe Rate EWS Logic
        if((bR >= 25) || (bR <= 9)) {
            ewsValue = ewsValue + dangerousLvl;
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
            tempTV.setBackgroundColor(Color.parseColor("#FF5555"));
        }

        //Blood Pressure EWS Logic
        if((bP >= 220) || (bP <= 90)) {
            ewsValue = ewsValue + dangerousLvl;
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

        return ewsValue;
    }
}
