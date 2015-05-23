package com.example.luca.firstprojectapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.example.luca.firstprojectapp.DatabaseManagement.DatabaseManager;
import com.example.luca.firstprojectapp.DatabaseManagement.SqlLiteHelper;
import com.example.luca.firstprojectapp.Interfaces.IOnActivityCallback;

import java.sql.SQLException;
import java.util.Date;

/**
 * Created by MatteoOrzes on 20/05/2015.
 */
public class EditWeightnPlanActivity extends ActionBarActivity implements IOnActivityCallback, DatabaseManager.IOnCursorCallback {

    private SharedPreferences pref;
    private DatabaseManager databaseManager;
    private EditText pesoEditText;
    private Button Conferma;
    private Date date;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editweight_layout);

        pref = getSharedPreferences("com.example.luca.firstprojectapp", Context.MODE_PRIVATE);

        pesoEditText = (EditText) findViewById(R.id.editPeso);
        Conferma = (Button) findViewById((R.id.confermaPeso));
        Conferma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salvaPeso();
            }
        });

        date = new Date(getIntent().getLongExtra("Date", 0));
        if(date == null){
            finish();
        }

        if(getIntent().getIntExtra("Code",0)==2){ // la data era gia stata selezionata in precedenza.
            // settare nei campi testuali le informazioni gia presenti nel database.
            String Query = new String("select " + SqlLiteHelper.COLUMN_WEIGHT + " from "
                    + SqlLiteHelper.TABLE_WEIGHT + " where " + SqlLiteHelper.COLUMN_ID + "=" + getIntent().getLongExtra("Date",0));
            //chiama query su db
        }
    }


    private void salvaPeso(){
        //inserire nel database le nuove informazioni o quelle opportunamente modificate
        // checkare che la data nel calendario resti selezionata. (effettuare in CalendarFragment)
        Intent intent = new Intent();
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
        while(cur.moveToNext()){
            pesoEditText.setText(cur.getLong(0)+"");
            //setta allo stesso modo il commento.
        }
    }
}
