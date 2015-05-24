package com.example.luca.firstprojectapp;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.luca.firstprojectapp.DatabaseManagement.DatabaseManager;
import com.example.luca.firstprojectapp.Interfaces.IOnActivityCallback;
import java.util.Date;

/**
 * Created by MatteoOrzes on 20/05/2015.
 */
public class EditWeightnPlanActivity extends ActionBarActivity implements IOnActivityCallback, DatabaseManager.IOnCursorCallback {

    private DatabaseManager databaseManager;
    private EditText pesoEditText;
    private Date date;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editweight_layout);

        databaseManager = new DatabaseManager(this);

        pesoEditText = (EditText) findViewById(R.id.editPeso);
        Button confirm = (Button) findViewById((R.id.confermaPeso));
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salvaPeso();
            }
        });

        Button remove = (Button) findViewById((R.id.removeWeight));
        confirm.setOnClickListener(new View.OnClickListener() {
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

        /*
        String QueryPeso = new String("select " + SqlLiteHelper.COLUMN_WEIGHT + " from "
                           + SqlLiteHelper.TABLE_WEIGHT + " where " + SqlLiteHelper.COLUMN_ID + "="
                           + getIntent().getLongExtra("Date",0));

        databaseManager.syncQuerySelect(QueryPeso,this,1);
        */
        if(pesoEditText.getText().toString().equals("")){
            remove.setActivated(false);   //disabilita il bottone remove se a questa data non era associato alcun peso.
        }
    }


    private void salvaPeso(){
        Intent intent = new Intent();
        if (!pesoEditText.getText().toString().equals("")){
            //String QuerySalvaPeso = new String();   //TO DO
            //databaseManager.syncQuerySelect(QuerySalvaPeso,this,1);
            intent.putExtra("Date",date.getTime());
            intent.putExtra("Code",3); //un peso è stato effettivamente inizializzato.
        }
        setResult(RESULT_OK, intent);
        finish();
    }

    private void rimuoviPeso(){
        //String QueryRimuoviPeso = new String();   //TO DO
        //databaseManager.syncQuerySelect(QueryRimuoviPeso,this,2);
        Intent intent = new Intent();
        intent.putExtra("Date",date.getTime());
        intent.putExtra("Code",4); //un peso è stato effettivamente tolto.
        setResult(RESULT_OK, intent);
        finish();
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
    public void manageUserProfile() {

    }

    @Override
    public void fillView(Cursor cur, int position) {
        switch(position){
            case 1:
                while(cur.moveToNext()){
                    pesoEditText.setText(cur.getLong(0)+"");
                }break;
            case 2:
                //nothing
                break;
            default:break;
        }
    }
}
