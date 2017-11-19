package com.calftracker.project.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.calftracker.project.adapters.IllnessAdapter;
import com.calftracker.project.models.Illness;
import com.calftracker.project.calftracker.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class ProtocolsIllnessActivity extends BaseActivity {
    private List<Illness> illnessList;
    private ListView lvIllness;
    private IllnessAdapter iAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_protocol_illness, frameLayout);
        mNavigationView.getMenu().findItem(R.id.nav_protocols).setChecked(true);

        // Custom title
        getSupportActionBar().setTitle(R.string.protocols_title);

        SharedPreferences mPreferences = getSharedPreferences("CalfTracker", Activity.MODE_PRIVATE);
        if (mPreferences.contains("IllnessList")) {
            SharedPreferences.Editor editor = mPreferences.edit();

            Gson gson = new Gson();
            String json = mPreferences.getString("IllnessList", "");
            illnessList = gson.fromJson(json, new TypeToken<ArrayList<Illness>>() {
            }.getType());
        } else { illnessList = new ArrayList<Illness>(); }

        lvIllness = (ListView) findViewById(R.id.listview_illness);
        iAdapter = new IllnessAdapter(getApplicationContext(), illnessList);
        lvIllness.setAdapter(iAdapter);

        lvIllness.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Do Something
            }
        });
    }

    public void onIllness_MedicineButtonClick(View view) {
        Intent intent = new Intent(ProtocolsIllnessActivity.this, ProtocolsMedicineActivity.class);
        startActivity(intent);
    }
    public void onIllness_VaccineButtonClick(View view) {
        Intent intent = new Intent(ProtocolsIllnessActivity.this, ProtocolsVaccineActivity.class);
        startActivity(intent);
    }
    public void onIllness_IllnessButtonClick(View view){
        Intent intent = new Intent(ProtocolsIllnessActivity.this, ProtocolsIllnessActivity.class);
        startActivity(intent);
    }
    public void onIllness_EditIllnessButtonClick(View view) {
        Intent intent = new Intent(ProtocolsIllnessActivity.this, ProtocolsEditIllnessActivity.class);
        startActivity(intent);
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Intent intent = new Intent(this, DashboardActivity.class);
            startActivity(intent);
        }
    }

}




