package com.calftracker.project.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.calftracker.project.models.Calf;
import com.calftracker.project.models.Farm;
import com.calftracker.project.calftracker.R;
import com.google.gson.Gson;


/**
 * Created by BrendonLapp on 10/31/17.
 */

public class CreateFarmActivity extends AppCompatActivity {


    private EditText farmName;
    private EditText farmLocation;
    private EditText farmOwner;

    private TextView farmNameRequired;
    private TextView farmLocationRequired;
    private TextView farmOwnerRequired;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_farm);

        // set UI to hide keyboard when user clicks anywhere off the keyboard
        setupUI(findViewById(R.id.createFarmParent));

        Button doneButton = (Button) findViewById(R.id.Create_Farm_Btn);

        farmName = (EditText) findViewById(R.id.Farm_Name_txt);
        farmLocation = (EditText) findViewById(R.id.Farm_Location_txt);
        farmOwner = (EditText) findViewById(R.id.Farm_Owner_txt);

        farmNameRequired = (TextView) findViewById(R.id.FarmName_Required_tv);
        farmLocationRequired = (TextView) findViewById(R.id.FarmLocation_Required_tv);
        farmOwnerRequired = (TextView) findViewById(R.id.FarmOwner_Required_tv);


        doneButton.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){

                boolean requirementsNotMet = false;

                if(farmName.getText().toString().equals("")){
                    requirementsNotMet = true;
                    farmNameRequired.setVisibility(View.VISIBLE);
                }else{
                    farmNameRequired.setVisibility(View.INVISIBLE);
                }
                if(farmOwner.getText().toString().equals("")){
                    requirementsNotMet = true;
                    farmOwnerRequired.setVisibility(View.VISIBLE);
                }else{
                    farmOwnerRequired.setVisibility(View.INVISIBLE);
                }
                if(farmLocation.getText().toString().equals("")){
                    requirementsNotMet = true;
                    farmLocationRequired.setVisibility(View.VISIBLE);
                }else{
                    farmLocationRequired.setVisibility(View.INVISIBLE);
                }



                //dialogBox();





                if(!requirementsNotMet) {
                    Farm farm = new Farm(farmName.getText().toString(), farmOwner.getText().toString(), farmLocation.getText().toString());
                    SharedPreferences mPrefs = getSharedPreferences("CalfTracker", Activity.MODE_PRIVATE);
                    SharedPreferences.Editor prefsEditor = mPrefs.edit();
                    Gson gson = new Gson();
                    String json = gson.toJson(farm);
                    prefsEditor.putString("Farm",json);
                    prefsEditor.apply();
                    callDashboard();
               }



            }
        });


    }

    public void setupUI(View view) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(CreateFarmActivity.this);
                    return false;
                }
            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView);
            }
        }
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

    private void callDashboard() {

        Intent intent = new Intent(this, DashboardActivity.class);
        finish();
        startActivity(intent);

    }

    /* EXAMPLE Dialog Box code for later
    private void dialogBox(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // set title
        alertDialogBuilder.setTitle(farmLocation.getText().toString());

        // set dialog message
        alertDialogBuilder
                .setMessage(farmLocation.getText().toString())
                .setCancelable(false)
                .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, close
                        // current activity
                        // MainActivity.this.finish();
                    }
                })
                .setNegativeButton("No",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.cancel();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();

    }
    */

}
