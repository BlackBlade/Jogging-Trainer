package com.example.luca.firstprojectapp.Fragments;

import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.luca.firstprojectapp.DatabaseManagement.DatabaseManager;
import com.example.luca.firstprojectapp.Interfaces.IOnActivityCallback;
import com.example.luca.firstprojectapp.R;
// need to include library for datepicker
/**
 * Created by MatteoOrzes on 18/05/2015.
 */
public class CalendarFragment extends Fragment implements DatabaseManager.IOnCursorCallback/*,com.andexert.calendarlistview.library.DatePickerController*/{

    private IOnActivityCallback listener;
    //private CalendarListView calendarView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        container.removeAllViews();

        final View view = inflater.inflate(R.layout.statistics_layout,container,false);

        //calendarView = view.findViewById(R.id.calendar);

        return view;

    }
    @Override
    public void fillView(Cursor cur) {
        //implement that later
    }
}
