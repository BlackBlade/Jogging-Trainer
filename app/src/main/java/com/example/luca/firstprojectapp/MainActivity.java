package com.example.luca.firstprojectapp;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.luca.firstprojectapp.Fragments.StatisticsFragment;
import com.example.luca.firstprojectapp.Interfaces.IOnActivityCallback;

import java.sql.SQLException;

public class MainActivity extends ActionBarActivity implements IOnActivityCallback {

    private DatabaseManager databaseManager;
    private   Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("My Jogging Trainer");
        //getSupportActionBar().setTitle("My Jogging Trainer");


        //adding slide fragment and main fragment to layout
        FragmentManager manager = getFragmentManager();
        FragmentTransaction trans = manager.beginTransaction();
        StatisticsFragment frag = new StatisticsFragment();
        trans.add(R.id.container,frag,getString(R.string.Statistics)); //TODO change fragment tags!!!
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

    @Override
    public void swapFragment(int position) {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        switch (position){
            case 0:
                //esempio!!
                if(manager.findFragmentByTag(getString(R.string.ShowMessage)) == null) {
                    transaction.replace(R.id.container, new ShowMessageFragment(), getString(R.string.ShowMessage));
                    transaction.commit();
                }
                break;
            case 1:
                //altro esempio! questo metodo va implementato e modificato ad hoc durante l'aggiunta
                //di fragment nuovi!
                if(manager.findFragmentByTag(getString(R.string.Statistics)) == null) {
                    transaction.replace(R.id.container, new StatisticsFragment(), getString(R.string.Statistics));
                    transaction.commit();
                }
                break;
        }
    }
}
