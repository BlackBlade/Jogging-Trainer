package com.example.luca.firstprojectapp.Fragments;

/**
 * Created by MatteoOrzes on 20/05/2015.
 */

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.luca.firstprojectapp.DatabaseManagement.DatabaseManager;
import com.example.luca.firstprojectapp.EditWeightnPlanActivity;
import com.example.luca.firstprojectapp.Interfaces.IOnActivityCallback;
import com.example.luca.firstprojectapp.R;
import com.squareup.timessquare.CalendarPickerView;

import java.util.Calendar;
import java.util.Date;


public class Calendar2Fragment extends Fragment implements DatabaseManager.IOnCursorCallback {

    private IOnActivityCallback listener;
    private CalendarPickerView calendarView;
    static final int EDIT_WEIGHT_PLAN = 1;
    static final int DATE_SELECTED = 2;
    static final int DATE_UNSELECTED = 3;
    static final int DATE_STILL_SELECTED = 4;
    static final int DATE_NO_MORE_SELECTED = 5;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        container.removeAllViews();

        Calendar nextYear = Calendar.getInstance();
        nextYear.add(Calendar.YEAR, 1);

        final View view = inflater.inflate(R.layout.calendar2_layout,container,false);

        calendarView = (CalendarPickerView) view.findViewById(R.id.calendar_view);

        Date today = new Date();
        calendarView.init(today, nextYear.getTime()).inMode(CalendarPickerView.SelectionMode.MULTIPLE);

        //recuperare da db lista delle date selezionate in precedenza e precedere a riselezionarle.

        calendarView.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener() {
            @Override
            public void onDateSelected(Date date) {
                /*
                Intent intent = new Intent(view.getContext(), EditWeightnPlanActivity.class);
                if (date != null){
                    intent.putExtra("Date",date.getTime());
                }
                intent.putExtra("Code",DATE_SELECTED);
                startActivityForResult(intent, EDIT_WEIGHT_PLAN);
                */
            }

            @Override
            public void onDateUnselected(Date date) {
                /*
                Intent intent = new Intent(view.getContext(), EditWeightnPlanActivity.class);
                if (date != null){
                    intent.putExtra("Date",date.getTime());
                }
                intent.putExtra("Code",DATE_UNSELECTED);
                startActivityForResult(intent, EDIT_WEIGHT_PLAN);
                */
            }
        });


        return view;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EDIT_WEIGHT_PLAN && resultCode == DATE_STILL_SELECTED) {
            //calendarView.selectDate(new Date(/*inserire il tempo in millisecondi*/));
        }
        if (requestCode == EDIT_WEIGHT_PLAN && resultCode == DATE_NO_MORE_SELECTED) {
            // la data non è piu selezionata.
        }
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
}
