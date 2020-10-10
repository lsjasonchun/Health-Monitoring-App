package com.example.health_monitoring_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawer;
    public String username;
    public TextView ewsValueTV, breathRateTV, tempTV, bloodPressureTV, oxSatTV, heartRateTV, consciousLvlTV;
    MainFragment mainFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Main Activity");
        getSupportActionBar().setTitle("Main Activity");

        drawer = findViewById(R.id.drawer_layout);

//        mainFragment = (MainFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
//        mainFragment.setMainValues("80");

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new MainFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }

        username = getIntent().getExtras().getString("arg");
    }

    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch(id) {
            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new MainFragment()).commit();
                break;
            case R.id.nav_profile:
                Intent profielIntent = new Intent(this, ProfileActivity.class);
                profielIntent.putExtra("arg", username);
                startActivity(profielIntent);
                break;
            case R.id.nav_gp_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new GPFragment()).commit();
                break;
            case R.id.nav_BT_settings:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new SettingsFragment()).commit();
                break;
            case R.id.nav_logout:
                //Need to go back to Login Page.
                System.exit(0);
                break;
        }

        return false;
    }

    public void setMainValues(){
        breathRateTV = findViewById(R.id.main_respiratory_value);
        tempTV = findViewById(R.id.main_temperature_value);
        bloodPressureTV = findViewById(R.id.main_blood_pressure_value);
        heartRateTV = findViewById(R.id.main_heart_rate_value);
        oxSatTV = findViewById(R.id.main_oxygen_sat_value);
        consciousLvlTV = findViewById(R.id.main_consciousness_value);

//        breathRateTV.setText((int) 80);
//        tempTV.setText("80");
//        bloodPressureTV.setText("80");
//        heartRateTV.setText("80");
//        oxSatTV.setText("80");
//        consciousLvlTV.setText("80");
    }

    public int calculateEWS(){
        int ewsValue = 1;
        int tempEws = 0;
        int breathRateEws = 0;
        int bloodPressureEws = 0;
        int heartRateEws = 0;
        int oxySatEws = 0;

        ewsValueTV = findViewById(R.id.main_ews_value);

//        switch ()

        return ewsValue;
    }
}
