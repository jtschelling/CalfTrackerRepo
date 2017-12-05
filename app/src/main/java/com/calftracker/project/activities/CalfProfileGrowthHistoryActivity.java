package com.calftracker.project.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.calftracker.project.adapters.calfprofile.GrowthHistoryHeightAdapter;
import com.calftracker.project.adapters.calfprofile.GrowthHistoryWeightAdapter;
import com.calftracker.project.calftracker.R;
import com.calftracker.project.models.Calf;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class CalfProfileGrowthHistoryActivity extends AppCompatActivity {

    String calfID;
    ArrayList<Calf> calfList;
    private Calf calf;

    GrowthHistoryWeightAdapter weightAdapter;
    GrowthHistoryHeightAdapter heightAdapter;
    private boolean noWeights = true;
    private boolean noHeights = true;

    ListView listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calf_profile_growth_history);

        // Stylize action bar to use back button and custom title
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Growth History");

        retrieveData();

        // try and get calf object made by main activity
        SharedPreferences mPreferences = getSharedPreferences("CalfTracker", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = mPreferences.edit();
        Gson gson = new Gson();
        String json = mPreferences.getString("CalfList","");
        json = mPreferences.getString("calfToViewInProfile","");
        calfID = gson.fromJson(json, String.class);

        // Search through the calfList to find the correct calf by ID
        for (int i = 0; i < calfList.size(); i++) {
            if (calfList.get(i).getFarmId().equals(calfID)) {
                calf = calfList.get(i);
                break;
            }
        }
        noRecordingsCheck();

        listview = (ListView) findViewById(R.id.listViewGrowthHistory);
        if (calf.getPhysicalHistory().isEmpty() || noWeights) {
            findViewById(R.id.textViewNoWeightRecorded).setVisibility(View.VISIBLE);
            findViewById(R.id.textViewNoHeightRecorded).setVisibility(View.GONE);
        } else {
            findViewById(R.id.textViewNoWeightRecorded).setVisibility(View.GONE);
            findViewById(R.id.textViewNoHeightRecorded).setVisibility(View.GONE);
        }

        weightAdapter = new GrowthHistoryWeightAdapter(getApplicationContext(),calf.getPhysicalHistory());
        listview.setAdapter(weightAdapter);

    }

    // TODO
    public void saveData() {
        // EMPTY METHOD TO KEEP CONSISTENCY
        // NO DATA IS SAVED IN THIS ACTIVITY
    }

    public void retrieveData() {
        SharedPreferences mPreferences = getSharedPreferences("CalfTracker", Activity.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPreferences.getString("CalfList","");
        calfList = gson.fromJson(json, new TypeToken<ArrayList<Calf>>(){}.getType());
    }

    public void onClickWeightButton(View view) {
        noRecordingsCheck();
        if (calf.getPhysicalHistory().isEmpty() || noWeights) {
            findViewById(R.id.textViewNoWeightRecorded).setVisibility(View.VISIBLE);
            findViewById(R.id.textViewNoHeightRecorded).setVisibility(View.GONE);
        } else {
            findViewById(R.id.textViewNoWeightRecorded).setVisibility(View.GONE);
            findViewById(R.id.textViewNoHeightRecorded).setVisibility(View.GONE);
        }
        weightAdapter = new GrowthHistoryWeightAdapter(getApplicationContext(),calf.getPhysicalHistory());
        listview.setAdapter(weightAdapter);
    }

    public void onClickHeightButton(View view) {
        noRecordingsCheck();
        if (calf.getPhysicalHistory().isEmpty() || noHeights) {
            findViewById(R.id.textViewNoHeightRecorded).setVisibility(View.VISIBLE);
            findViewById(R.id.textViewNoWeightRecorded).setVisibility(View.GONE);
        } else {
            findViewById(R.id.textViewNoWeightRecorded).setVisibility(View.GONE);
            findViewById(R.id.textViewNoHeightRecorded).setVisibility(View.GONE);
        }
        heightAdapter = new GrowthHistoryHeightAdapter(getApplicationContext(),calf.getPhysicalHistory());
        listview.setAdapter(heightAdapter);
    }

    public void noRecordingsCheck() {
        for (int i = 0; i < calf.getPhysicalHistory().size(); i++) {
            if (calf.getPhysicalHistory().get(i).getWeight() != -1) {
                noWeights = false;
            }
            if (calf.getPhysicalHistory().get(i).getHeight() != -1) {
                noHeights = false;
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, CalfProfileActivity.class);
        startActivity(intent);
    }

    public boolean onOptionsItemSelected(MenuItem item){
        Intent intent = new Intent(getApplicationContext(), CalfProfileActivity.class);
        startActivity(intent);
        return true;
    }
}
