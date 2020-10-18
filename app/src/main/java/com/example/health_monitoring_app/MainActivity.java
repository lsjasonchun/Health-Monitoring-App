package com.example.health_monitoring_app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, BackgroundWorker.BackgroundWorkerResponse{


    BackgroundWorker backgroundWorker = new BackgroundWorker(this);
    public String fetchedUsername, fetchedFirstName, fetechedLastName, fetchedPwd, fetchedEmail;
    public int fetchedID, fetchedGpID;
    public String client;
    public Client clientInfo;
    public static final String LOG_TAG = "MAIN ACTIVITY";
    private DrawerLayout drawer;
    public String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Main Activity");

        drawer = findViewById(R.id.drawer_layout);

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

        if(getIntent().getExtras().getString("ActivityID").equals("FromLogin")){
            username = getIntent().getExtras().getString("arg");
            String gpUser = "";
            String gpCheck = "gp_";

            if(username.length() > 3) {
                gpUser = username.substring(0, 3);

                if(gpUser.equals(gpCheck)) {
                    navigationView.getMenu().findItem(R.id.nav_register).setVisible(true);
                }
            }

            clientInfo();
        }
    }

    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void clientInfo() {
        String type = "fetchProfile";
        backgroundWorker.delegate = this;
        backgroundWorker.execute(type, username);
    }

    public void processFinish(String output) {
        try {
            Log.e(LOG_TAG,output);

            JSONArray jsonArr = new JSONArray(output);
            JSONObject jsonObj = jsonArr.getJSONObject(0);
            fetchedID = jsonObj.getInt("id");
            fetchedUsername = jsonObj.getString("username");
            fetchedFirstName = jsonObj.getString("first_name");
            fetechedLastName = jsonObj.getString("last_name");
            fetchedPwd = jsonObj.getString("password");
            fetchedEmail = jsonObj.getString("email");
            fetchedGpID = jsonObj.getInt("gp_id");
            Log.e(LOG_TAG, "GP ID"+fetchedGpID);

            clientInfo = new Client(fetchedID, fetchedUsername, fetchedFirstName, fetechedLastName, fetchedPwd, fetchedEmail, fetchedGpID);

            Log.e(LOG_TAG,"USER AND GP ID"+clientInfo.getUsername() +clientInfo.getGpID());
        } catch (Exception e) {
            e.printStackTrace();
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
                Intent profileIntent = new Intent(this, ProfileActivity.class);
                profileIntent.putExtra("arg", clientInfo);
                startActivity(profileIntent);
                break;
            case R.id.nav_gp_profile:
                Intent gpIntent = new Intent(this, GpActivity.class);
                gpIntent.putExtra("arg", clientInfo);
                startActivity(gpIntent);
                break;
            case R.id.nav_BT_settings:
                Intent btSettinglIntent = new Intent(this, BtSettingActivity.class);
                startActivity(btSettinglIntent);
                break;
            case R.id.nav_logout:
                //Need to go back to Login Page.
                System.exit(0);
                break;
            case R.id.nav_register:
                Intent registerIntent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(registerIntent);
                break;
        }

        return false;
    }
}
