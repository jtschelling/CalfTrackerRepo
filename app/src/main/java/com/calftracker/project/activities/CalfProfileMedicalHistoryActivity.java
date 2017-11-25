package com.calftracker.project.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.calftracker.project.adapters.MedicalHistoryAdministeredVaccineAdapter;
import com.calftracker.project.adapters.MedicalHistoryNeededVaccineAdapter;
import com.calftracker.project.calftracker.R;
import com.calftracker.project.interfaces.MedicalHistoryVaccineMethods;
import com.calftracker.project.models.Calf;
import com.calftracker.project.models.Task;
import com.calftracker.project.models.Vaccine;
import com.calftracker.project.models.Vaccine_With_Date;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class CalfProfileMedicalHistoryActivity extends AppCompatActivity implements MedicalHistoryVaccineMethods {

    String calfID;
    ArrayList<Calf> calfList;
    Calf calf;

    MedicalHistoryNeededVaccineAdapter neededVacAdapter;
    MedicalHistoryAdministeredVaccineAdapter administeredVacAdapter;

    Task task;


    ListView mlistview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calf_profile_medical_history);

        // Stylize action bar to use back button and custom title
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Medical History");

        // try and get calf object made by main activity
        SharedPreferences mPreferences = getSharedPreferences("CalfTracker", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = mPreferences.edit();

        Gson gson = new Gson();
        String json = mPreferences.getString("CalfList","");
        calfList = gson.fromJson(json, new TypeToken<ArrayList<Calf>>(){}.getType());

        json = mPreferences.getString("calfToViewInProfile","");
        calfID = gson.fromJson(json, String.class);

        // Load in the Task
        json = mPreferences.getString("Task", "");
        task = gson.fromJson(json, new TypeToken<Task>() {}.getType());

        // Search through the calfList to find the correct calf by ID
        for (int i = 0; i < calfList.size(); i++) {
            if (calfList.get(i).getFarmId().equals(calfID)) {
                calf = calfList.get(i);
                break;
            }
        }

        mlistview = (ListView) findViewById(R.id.listViewMedicalHistory);

        neededVacAdapter = new MedicalHistoryNeededVaccineAdapter(getApplicationContext(),calf.getNeededVaccines(),CalfProfileMedicalHistoryActivity.this);

        mlistview.setAdapter(neededVacAdapter);
    }

    public void clickAdministeredButton(View view) {
        administeredVacAdapter = new MedicalHistoryAdministeredVaccineAdapter(getApplicationContext(),calf.getAdministeredVaccines(), CalfProfileMedicalHistoryActivity.this);
        mlistview.setAdapter(administeredVacAdapter);
    }

    public void clickNeededButton(View view) {
        neededVacAdapter = new MedicalHistoryNeededVaccineAdapter(getApplicationContext(),calf.getNeededVaccines(),CalfProfileMedicalHistoryActivity.this);
        mlistview.setAdapter(neededVacAdapter);
    }

    @Override
    public void administerVaccine(Vaccine vaccine) {
        for(int i = 0; i < calf.getNeededVaccines().size(); i++) {
            if (calf.getNeededVaccines().get(i).equals(vaccine)) {
                calf.getNeededVaccines().remove(i);

                Calendar today = Calendar.getInstance();
                int todayYear = today.get(Calendar.YEAR);
                int todayMonth = today.get(Calendar.MONTH);
                int todayDay = today.get(Calendar.DATE);

                calf.addAdministeredVaccine(vaccine,new GregorianCalendar(todayYear,todayMonth,todayDay));
                break;
            }
        }

        SharedPreferences mPrefs = getSharedPreferences("CalfTracker", Activity.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(calfList);
        prefsEditor.putString("CalfList",json);
        prefsEditor.apply();

        neededVacAdapter.notifyDataSetChanged();
    }

    public void returnToNeeded(Vaccine_With_Date vaccine) {
        for(int i = 0; i < calf.getAdministeredVaccines().size(); i++) {
            if (calf.getAdministeredVaccines().get(i).equals(vaccine)) {
                calf.getAdministeredVaccines().remove(i);

                task.placeVaccineInTasks(vaccine.getVaccine(), calf);

                calf.getNeededVaccines().add(vaccine.getVaccine());
                break;
            }
        }

        SharedPreferences mPrefs = getSharedPreferences("CalfTracker", Activity.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(calfList);
        prefsEditor.putString("CalfList",json);
        prefsEditor.apply();


        json = gson.toJson(task);
        prefsEditor.putString("Task",json);
        prefsEditor.apply();

        administeredVacAdapter.notifyDataSetChanged();
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
