package com.example.luca.firstprojectapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.luca.firstprojectapp.Adapters.NavItem;
import com.example.luca.firstprojectapp.Adapters.SlideListAdapter;
import com.example.luca.firstprojectapp.DatabaseManagement.DatabaseManager;
import com.example.luca.firstprojectapp.Fragments.ActivityFragment;
import com.example.luca.firstprojectapp.Fragments.CalendarFragment;
import com.example.luca.firstprojectapp.Fragments.HistoryFragment;
import com.example.luca.firstprojectapp.Fragments.ProfileFragment;
import com.example.luca.firstprojectapp.Fragments.StatisticsFragment;
import com.example.luca.firstprojectapp.Interfaces.IOnActivityCallback;
import com.sromku.simple.fb.SimpleFacebook;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;

public class MainActivity extends ActionBarActivity implements IOnActivityCallback {

    private DatabaseManager databaseManager;
    private Toolbar toolbar;
    private SlideListAdapter adapter; //the adapter for the listview
    private ArrayList<NavItem> list = new ArrayList<>();
    private SharedPreferences myPreferences;
    private SimpleFacebook mSimpleFacebook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        printHashKey();
        myPreferences = getSharedPreferences("pref",Context.MODE_PRIVATE);
        //if no one is logged
        if (!myPreferences.getBoolean("logged",false)){

            Intent intent = new Intent(MainActivity.this,WelcomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        }




        list.add(new NavItem("Profile", R.drawable.ic_launcher));
        //list.add(new NavItem("Activity",R.drawable.ic_launcher));
        list.add(new NavItem("Statistics",R.drawable.ic_launcher));
        list.add(new NavItem("Calendar",R.drawable.ic_launcher));
        list.add(new NavItem("Activity",R.drawable.ic_launcher));
        list.add(new NavItem("History",R.drawable.ic_launcher));
        ListView myList = (ListView) findViewById(R.id.navList);
        adapter = new SlideListAdapter(getContext(),list);
        myList.setAdapter(adapter);
        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DrawerLayout layout = (DrawerLayout) findViewById(R.id.DrawerLayout);
                layout.closeDrawers();
                //layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED); -->Remember: use this to lock it when the user logs out.
                swapFragment(position);


            }
        });

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("My Jogging Trainer");
        //adding slide fragment and main fragment to layout
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction trans = manager.beginTransaction();
        StatisticsFragment frag = new StatisticsFragment();
        trans.add(R.id.fragmentContainer,frag,getString(R.string.Statistics)); //TODO change fragment tags!!!
        trans.commit();

        //init manager
        databaseManager = new DatabaseManager(this);
        try {
            databaseManager.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void printHashKey(){
            // Add code to print out the key hash
            try {
                PackageInfo info = getPackageManager().getPackageInfo(
                        "com.example.luca.firstprojectapp",
                        PackageManager.GET_SIGNATURES);
                for (Signature signature : info.signatures) {
                    MessageDigest md = MessageDigest.getInstance("SHA");
                    md.update(signature.toByteArray());
                    //Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
                    //Toast.makeText(this,Base64.encodeToString(md.digest(), Base64.DEFAULT),Toast.LENGTH_LONG).show();
                }
            } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

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
        if (id ==R.id.action_profile) {
            swapFragment(0);
            return true;
        }
        else if (id == R.id.action_credits){
            Intent credits = new Intent(MainActivity.this, CreditsActivity.class);
            startActivity(credits);
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

    /**
     * This method swaps the fragment inside the fragment container.
     * @param position the new fragment position
     */
    @Override
    public void swapFragment(int position) {
        FragmentManager manager = getSupportFragmentManager();

        switch (position){

            case 0:
                if(manager.findFragmentByTag(getString(R.string.Profile))==null){
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.replace(R.id.fragmentContainer,new ProfileFragment(),getString(R.string.Profile));
                    transaction.commit();
                }
                break;

            case 1:
                if(manager.findFragmentByTag(getString(R.string.Statistics)) == null) {
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.replace(R.id.fragmentContainer, new StatisticsFragment(), getString(R.string.Statistics));
                    transaction.commit();
                }
                break;
           /* case 3:
                if(manager.findFragmentByTag(getString(R.string.ShowMessage)) == null) {
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.replace(R.id.fragmentContainer, new ShowMessageFragment(), getString(R.string.ShowMessage));
                    transaction.commit();
                }
                break;*/
            case 2:
                if(manager.findFragmentByTag(getString(R.string.Calendar)) == null) {
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.replace(R.id.fragmentContainer, new CalendarFragment(), getString(R.string.Calendar));
                    transaction.commit();
                }
                break;
            case 3:
                if(manager.findFragmentByTag(getString(R.string.Activity)) == null){
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.replace(R.id.fragmentContainer,new ActivityFragment(), getString(R.string.Activity));
                    transaction.commit();
                }
                break;
            case 4:
                if(manager.findFragmentByTag(getString(R.string.History)) == null){
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.replace(R.id.fragmentContainer,new HistoryFragment(), getString(R.string.History));
                    transaction.commit();
                }
                break;

        }
    }

    @Override
    public FragmentManager getMySupportFragmentManager(){
        return getSupportFragmentManager();
    }

    @Override
    public void endActivity() {

            Intent intent = new Intent(MainActivity.this,WelcomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
            finish();
    }

    @Override
    public LocationManager getSystemService(){
        return (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //mSimpleFacebook.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!myPreferences.getBoolean("logged",false)){

        Intent intent = new Intent(MainActivity.this,WelcomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }
    }
}
