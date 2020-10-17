package com.example.health_monitoring_app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ProfileFragment extends Fragment {
    private ProfileFragmentListener profileListener;
    private Button editProfileBtn;

    public interface ProfileFragmentListener {
        public String getDataFromDB(String username);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.activity_profile, container, false);


        return v;
    }
}
