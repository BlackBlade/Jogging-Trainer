package com.example.luca.firstprojectapp.Interfaces;

import android.content.Context;

import com.example.luca.firstprojectapp.DatabaseManager;

/**
 * Created by luca on 11/05/15.
 * Interface that activities must implement to communicate with fragments
 */
public interface IOnActivityCallback {

    public Context getContext();

    public DatabaseManager getDatabaseManager();

    public void swapFragment(int position);
}
