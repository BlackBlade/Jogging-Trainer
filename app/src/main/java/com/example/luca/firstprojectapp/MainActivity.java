package com.example.luca.firstprojectapp;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

import com.example.luca.firstprojectapp.Fragments.StatisticsFragment;
import com.example.luca.firstprojectapp.Interfaces.IOnActivityCallback;

import java.sql.SQLException;

//ddiidididi di di
public class MainActivity extends Activity implements IOnActivityCallback {

    private DatabaseManager databaseManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        //adding slide fragment and main fragment to layout
        FragmentManager manager = getFragmentManager();
        FragmentTransaction trans = manager.beginTransaction();
        StatisticsFragment frag = new StatisticsFragment();
        trans.add(R.id.container,frag,"prova"); //TODO change fragment tags!!!
        SlideMenuFragment fragment = new SlideMenuFragment();
        trans.add(R.id.container,fragment,"fragment_slide");
        trans.commit();

        databaseManager = new DatabaseManager(this);
        try {
            databaseManager.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public DatabaseManager getDatabaseManager() {
        return this.databaseManager;
    }
}
