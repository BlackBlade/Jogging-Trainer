package com.example.luca.firstprojectapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.luca.firstprojectapp.DatabaseManagement.DatabaseManager;
import com.example.luca.firstprojectapp.DatabaseManagement.SqlLiteHelper;
import com.example.luca.firstprojectapp.Interfaces.IOnActivityCallback;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by MatteoOrzes on 20/05/2015.
 */
public class EditWeightnPlanActivity extends ActionBarActivity implements IOnActivityCallback, DatabaseManager.IOnCursorCallback {

    private DatabaseManager databaseManager;
    private EditText pesoEditText;
    private Date date;
    private Boolean previouslySetted;
    private final static double MIN_PESO = 30;
    private final static double MAX_PESO = 150;
    private SharedPreferences myPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editweight_layout);
        myPreferences = getSharedPreferences("pref", Context.MODE_PRIVATE);
        databaseManager = new DatabaseManager(this);
        try{
            databaseManager.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("My Jogging Trainer");

        pesoEditText = (EditText) findViewById(R.id.editPeso);

        Button confirm = (Button) findViewById((R.id.confermaPeso));
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salvaPeso();
            }
        });

        Button remove = (Button) findViewById((R.id.rimuoviPeso));
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rimuoviPeso();
            }
        });

        date = new Date(getIntent().getLongExtra("Date", 0));
        if(getIntent().getLongExtra("Date", 0) == 0){
            Toast.makeText(this,"data non valida",Toast.LENGTH_LONG).show();
            finish();
        }


        String QueryPeso = new String("select " + SqlLiteHelper.COLUMN_WEIGHT + " from "
                           + SqlLiteHelper.TABLE_WEIGHT + " where " + SqlLiteHelper.COLUMN_ID + "="
                           + getIntent().getLongExtra("Date",0));

        Cursor cur = databaseManager.syncQuerySelect(QueryPeso,this,1);  //NON funziona
        this.fillView(cur, 1);



        previouslySetted = true;
        if(pesoEditText.getText().toString().isEmpty() || pesoEditText.getText().toString() == null){
            Toast.makeText(this,"Inserire nuovo Peso",Toast.LENGTH_SHORT).show();
            previouslySetted=false;
        } else {
            Toast.makeText(this,"Modificare Peso",Toast.LENGTH_SHORT).show();
        }
    }


    private void salvaPeso(){
        if (!(pesoEditText.getText().toString().isEmpty() || pesoEditText.getText().toString() == null)){
            Intent intent = new Intent();
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(date.getTime());
            double peso = Double.parseDouble(pesoEditText.getText().toString());
            if ((peso>=MIN_PESO)&&(peso<=MAX_PESO)){
                if(previouslySetted){
                    databaseManager.updateWeightChange(cal, peso);
                    intent.putExtra("Code",5);   // un peso è stato modificato.
                }else{
                    databaseManager.insertWeightChange(cal, peso);
                    intent.putExtra("Code",3);   //un peso è stato effettivamente inizializzato.
                }
                intent.putExtra("Date",date.getTime());
                setResult(RESULT_OK, intent);
                SharedPreferences.Editor editor = myPreferences.edit();
                editor.putFloat("weight",(float)peso);
                editor.apply();
                finish();
            } else{
                Toast.makeText(this,"Peso inserito non valido. (Peso deve essere compreso fra 30 e 150)",Toast.LENGTH_SHORT).show();
            }
        } else{
            Toast.makeText(this,"Nessun Peso è stato inserito.",Toast.LENGTH_SHORT).show();
        }
    }

    private void rimuoviPeso(){
        if (previouslySetted) {
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(date.getTime());
            databaseManager.deleteWeightChange(cal);
            Intent intent = new Intent();
            intent.putExtra("Date", date.getTime());
            intent.putExtra("Code", 4); //un peso è stato effettivamente tolto.
            setResult(RESULT_OK, intent);
            finish();
        }
    }


    @Override
    public Context getContext() {
        return this;
    }

    public DatabaseManager getDatabaseManager() {
        return this.databaseManager;
    }

    @Override
    public void swapFragment(int position) {
        // NOPE.
    }

    @Override
    public FragmentManager getMySupportFragmentManager() {
        return null;
    }

    @Override
    public void endActivity() {

    }

    @Override
    public LocationManager getSystemService(){
        return null;
    }


    @Override
    public void fillView(Cursor cur, int position) {
        switch(position){
            case 1:
                while(cur.moveToNext()){
                    pesoEditText.setText(cur.getLong(0)+"");
                }break;
            default:break;
        }
    }
}
