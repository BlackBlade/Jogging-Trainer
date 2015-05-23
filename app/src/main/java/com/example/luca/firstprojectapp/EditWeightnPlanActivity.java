package com.example.luca.firstprojectapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.luca.firstprojectapp.DatabaseManagement.DatabaseManager;

import java.sql.SQLException;
import java.util.Date;

/**
 * Created by MatteoOrzes on 20/05/2015.
 */
public class EditWeightnPlanActivity extends ActionBarActivity {

    private SharedPreferences pref;
    private DatabaseManager databaseManager;
    private EditText pesoEditText, commentoEditText;
    private Button confermaData, rimuoviData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editweight_layout);

        pref = getSharedPreferences("com.example.luca.firstprojectapp", Context.MODE_PRIVATE);
        databaseManager = new DatabaseManager(this);
        try {
            databaseManager.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        pesoEditText = (EditText) findViewById(R.id.editPeso);
        commentoEditText = (EditText) findViewById(R.id.editCommento);
        confermaData = (Button) findViewById(R.id.confirmDate);
        rimuoviData = (Button) findViewById(R.id.removeDate);

        Date date = new Date(getIntent().getLongExtra("Date", 0));
        if(date == null){
            finish();
        }

        confermaData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salvaData();
            }
        });

        rimuoviData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eliminaData();
            }
        });
    }


    private void salvaData(){

    }

    private void eliminaData(){

    }

    public DatabaseManager getDatabaseManager() {
        return this.databaseManager;
    }

}
