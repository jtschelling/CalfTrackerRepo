package com.example.brett.calftracker;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class AddCalfActivity extends AppCompatActivity {
    private static final String TAG = "AddCalfActivity";

    private TextView mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    private TextView mGender;
    private Dialog mGenderListDialog;
    private String[] gender = {"Male","Female"};
    private AlertDialog alert;

    private int calfYear;
    private int calfMonth;
    private int calfDay;
    private Calendar calfDOB;

    private String calfGender;

    private int calfID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_calf);

        mDisplayDate = (TextView) findViewById(R.id.textViewDisplayDate);

        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        AddCalfActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        // this happens when the user has selected a date in the dialog and presses "OK"
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = month + "/" + day + "/" + year;
                mDisplayDate.setText(date);
                calfDay = day;
                calfMonth = month;
                calfYear = year;
            }
        };

        mGender = (TextView) findViewById(R.id.textViewSelectGender);

        mGender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert.show();
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Gender");
        builder.setItems(gender, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mGender.setText(gender[i]);
                calfGender = gender[i];
            }
        });
        alert = builder.create();
    }

    public void clickAddCalfButton(View view) {
        // GET USER INPUT FOR ID NUMBER FROM EDITTEXT
        EditText ID = (EditText) findViewById(R.id.editTextGetID);
        calfDOB = Calendar.getInstance();
        calfDOB.set(calfYear, calfMonth, calfDay);
        calfID = Integer.parseInt(ID.getText().toString());

        // MAKE NEW CALF OBJECT
        // TODO: FIX CALENDAR THING
        Calf newCalf = new Calf(calfID, calfGender, calfDOB);

        // turn that into json through gson and save to shared preferences
        SharedPreferences mPrefs = getSharedPreferences("test", Activity.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(newCalf);
        prefsEditor.putString("newCalf",json);
        prefsEditor.apply();

        // GO TO NEWLWY CREATED CALF PROFILE
        Intent intent = new Intent(this,CalfProfileActivity.class);
        startActivity(intent);
    }
}