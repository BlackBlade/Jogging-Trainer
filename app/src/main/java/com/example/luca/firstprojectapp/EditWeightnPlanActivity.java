package com.example.luca.firstprojectapp;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;

import com.example.luca.firstprojectapp.DatabaseManagement.DatabaseManager;
import com.example.luca.firstprojectapp.Interfaces.IOnActivityCallback;

/**
 * Created by MatteoOrzes on 20/05/2015.
 */
public class EditWeightnPlanActivity extends ActionBarActivity implements IOnActivityCallback {
    @Override
    public Context getContext() {
        return null;
    }

    @Override
    public DatabaseManager getDatabaseManager() {
        return null;
    }

    @Override
    public void swapFragment(int position) {

    }

    @Override
    public FragmentManager getMySupportFragmentManager() {
        return null;
    }

    @Override
    public void manageUserProfile() {

    }
}
