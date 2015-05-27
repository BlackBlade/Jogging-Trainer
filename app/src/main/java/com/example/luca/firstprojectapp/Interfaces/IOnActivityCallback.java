package com.example.luca.firstprojectapp.Interfaces;

import android.location.LocationManager;
import android.support.v4.app.FragmentManager;
import android.content.Context;

import com.example.luca.firstprojectapp.DatabaseManagement.DatabaseManager;

/**
 * Created by luca on 11/05/15.
 * Interface that activities must implement to communicate with fragments
 */
public interface IOnActivityCallback {

    /**
     * Method called by fragment to retrieve the actual context, avoid using getActivity()
     * @return the actual context
     */
    public Context getContext();

    /**
     * Method called to access the Database manager in order to query the local database
     * @return the Database Manager
     */
    public DatabaseManager getDatabaseManager();

    /**
     * Method called to make a fragment swap in the fragment container
     * @param position the new fragment position
     */
    public void swapFragment(int position);

    /*

     */
    public LocationManager getSystemService();

    public FragmentManager getMySupportFragmentManager();

}
