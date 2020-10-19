package com.example.health_monitoring_app;

import android.os.Bundle;
import android.widget.ExpandableListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class InfoActivity extends AppCompatActivity {
    private ExpandableListView listView;
    private ExpandableListAdapter listAdapter;
    private List<String> listDataHeader;
    private HashMap<String, List<String>> listHash;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_info);

        listView = (ExpandableListView) findViewById(R.id.lvExp);
        initData();
        listAdapter = new ExpandableListAdapter(this, listDataHeader, listHash);
        listView.setAdapter(listAdapter);
    }

    private void initData() {
        listDataHeader = new ArrayList<>();
        listHash = new HashMap<>();

        listDataHeader.add("App Info");
        listDataHeader.add("Early Warning Score (EWS)");
        listDataHeader.add("Data From Health Device");
        listDataHeader.add("Contact");

        List<String> appInfo = new ArrayList<>();
        appInfo.add(getString(R.string.info_appExpand));

        List<String> ewsInfo = new ArrayList<>();
        ewsInfo.add(getString(R.string.info_ewsExpand));

        List<String> dataInfo = new ArrayList<>();
        dataInfo.add(getString(R.string.info_dataExpand));

        List<String> contactInfo = new ArrayList<>();
        contactInfo.add(getString(R.string.info_contactExpand));

        listHash.put(listDataHeader.get(0), appInfo);
        listHash.put(listDataHeader.get(1), ewsInfo);
        listHash.put(listDataHeader.get(2), dataInfo);
        listHash.put(listDataHeader.get(3), contactInfo);
    }
}
