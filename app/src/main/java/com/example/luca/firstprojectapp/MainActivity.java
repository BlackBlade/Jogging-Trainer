package com.example.luca.firstprojectapp;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

import java.sql.SQLException;

//ddiidididi di di
public class MainActivity extends Activity implements ShowMessageFragment.IOnActivityCallback {

    private DatabaseManager databaseManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        //SlideMenuFragment fragment = new SlideMenuFragment();
        FragmentManager manager = getFragmentManager();
        FragmentTransaction trans = manager.beginTransaction();
        //trans.add(R.id.container,fragment,"fragment_slide");


        databaseManager = new DatabaseManager(this);
        try {
            databaseManager.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }


        ShowMessageFragment frag = new ShowMessageFragment();
        //FragmentManager manager = getFragmentManager();
       // FragmentTransaction trans = manager.beginTransaction();
        trans.add(R.id.container,frag,"prova");
        SlideMenuFragment fragment = new SlideMenuFragment();
        trans.add(R.id.container,fragment,"fragment_slide");
        trans.commit();

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
