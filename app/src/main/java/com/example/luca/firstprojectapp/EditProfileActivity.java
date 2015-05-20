package com.example.luca.firstprojectapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;

import com.facebook.FacebookSdk;

/**
 * Created by Mary on 18/05/2015.
 */
public class EditProfileActivity extends ActionBarActivity {

    private SharedPreferences pref;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile_layout);
        FacebookSdk.sdkInitialize(getApplicationContext());//

        pref = getSharedPreferences("com.example.luca.firstprojectapp", Context.MODE_PRIVATE);
        Intent calledIntent = getIntent();
        if (calledIntent!=null) {
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("Name","Marina");
            editor.apply();
            TextView text = (TextView) findViewById(R.id.editWeight);
           text.setText(pref.getString("Name",""));

        }
    }
}
