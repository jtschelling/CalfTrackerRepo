package com.example.brett.calftracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by brett on 10/31/17.
 */
public class SplashScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        //final SharedPreferences sharedPref = getSharedPreferences("test",Activity.MODE_PRIVATE);
        //final SharedPreferences.Editor editor = sharedPref.edit();
        //editor.putBoolean("usedApp", false);
        //editor.commit();

        Thread timerThread = new Thread(){
            public void run(){
                try{
                    sleep(3000);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }finally{

                    SharedPreferences sharedPref = getSharedPreferences("test",Activity.MODE_PRIVATE);
                    boolean hasBeenUsed = sharedPref.getBoolean("usedApp", false);

                    if(!hasBeenUsed) {
                        Intent intent = new Intent(SplashScreen.this, CreateFarmActivity.class);

                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putBoolean("usedApp", true);
                        editor.commit();

                        startActivity(intent);
                    }else{
                        Intent intent = new Intent(SplashScreen.this, DashboardActivity.class);
                        startActivity(intent);

                    }
                }
            }
        };
        timerThread.start();
    }



    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        finish();
    }

}