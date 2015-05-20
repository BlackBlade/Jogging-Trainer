package com.example.luca.firstprojectapp.Fragments;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.Toast;

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
    private DayPickerView dayPickerView;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        container.removeAllViews();

        final View view = inflater.inflate(R.layout.calendar_layout,container,false);

        dayPickerView = (DayPickerView) view.findViewById(R.id.pickerView);

        dayPickerView.setController(this);

        return view;

    }


    @Override
    public void fillView(Cursor cur, int position) {
        //later
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(activity instanceof IOnActivityCallback){
            listener = (IOnActivityCallback) activity;

        } else {
            throw new UnsupportedOperationException("Wrong container, activity must implement" +
                    "IOnActivityCallback");
        }
    }

    @Override
    public int getMaxYear() {
        return 2016;
    }

    @Override
    public void onDayOfMonthSelected(int year, int month, int day)
    {
        Log.e("Day Selected", day + " / " + month + " / " + year);
    }

    @Override
    public void onDateRangeSelected(SimpleMonthAdapter.SelectedDays<SimpleMonthAdapter.CalendarDay> selectedDays)
    {

        Log.e("Date range selected", selectedDays.getFirst().toString() + " --> " + selectedDays.getLast().toString());
    }

}
