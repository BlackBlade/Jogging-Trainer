package com.example.luca.firstprojectapp.Fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.luca.firstprojectapp.DatabaseManagement.DatabaseManager;
import com.example.luca.firstprojectapp.Interfaces.IOnActivityCallback;
import com.example.luca.firstprojectapp.R;

/**
 * Created by MatteoOrzes on 24/05/2015.
 */
public class HistoryFragment extends Fragment implements DatabaseManager.IOnCursorCallback{

    private IOnActivityCallback listener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        container.removeAllViews();
        final View view = inflater.inflate(R.layout.history_layout,container,false);
        return view;
    }
    @Override
    public void fillView(Cursor cur, int position) {

    }
}
