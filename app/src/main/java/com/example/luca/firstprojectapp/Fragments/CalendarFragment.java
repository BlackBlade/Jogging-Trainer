package com.example.luca.firstprojectapp.Fragments;

import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andexert.calendarlistview.library.DatePickerController;
import com.example.luca.firstprojectapp.DatabaseManagement.DatabaseManager;
import com.example.luca.firstprojectapp.Interfaces.IOnActivityCallback;
import com.example.luca.firstprojectapp.R;
import com.andexert.calendarlistview.library.DayPickerView;
import com.andexert.calendarlistview.library.SimpleMonthAdapter;


// need to include library for datepicker
/**
 * Created by MatteoOrzes on 18/05/2015.
 */
public class CalendarFragment extends Fragment implements DatabaseManager.IOnCursorCallback, DatePickerController{

    private IOnActivityCallback listener;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        container.removeAllViews();

        final View view = inflater.inflate(R.layout.statistics_layout,container,false);

        //calendarView = view.findViewById(R.id.pickerView);

        return view;

    }

    public void fillView(Cursor cur) {
        //implement that later
    }

    @Override
    public void fillView(Cursor cur, int position) {

    }

    @Override
    public int getMaxYear() {
        return 0;
    }

    @Override
    public void onDayOfMonthSelected(int i, int i2, int i3) {

    }

    @Override
    public void onDateRangeSelected(SimpleMonthAdapter.SelectedDays<SimpleMonthAdapter.CalendarDay> calendarDaySelectedDays) {

    }
}
