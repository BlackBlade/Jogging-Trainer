package com.example.luca.firstprojectapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;

import com.example.luca.firstprojectapp.DatabaseManagement.DatabaseManager;
import com.example.luca.firstprojectapp.Interfaces.IOnActivityCallback;

import java.sql.SQLException;

/**
 * Created by MatteoOrzes on 20/05/2015.
 */
public class EditWeightnPlanActivity extends ActionBarActivity {

    private SharedPreferences pref;
    private DatabaseManager databaseManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editweight_layout);

        databaseManager = new DatabaseManager(this);
        try {
            databaseManager.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }


        pref = getSharedPreferences("com.example.luca.firstprojectapp", Context.MODE_PRIVATE);
    }


    public DatabaseManager getDatabaseManager() {
        return this.databaseManager;
    }

}
