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
import android.widget.Button;

import com.example.luca.firstprojectapp.DatabaseManagement.DatabaseManager;
import com.example.luca.firstprojectapp.EditWeightnPlanActivity;
import com.example.luca.firstprojectapp.Interfaces.IOnActivityCallback;
import com.example.luca.firstprojectapp.R;
import com.squareup.timessquare.CalendarPickerView;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class CalendarFragment extends Fragment implements DatabaseManager.IOnCursorCallback {

    private IOnActivityCallback listener;
    private CalendarPickerView calendarView;
    private List<Date> selectedDates;
    private Button confermaDate;
    static final int EDIT_WEIGHT_PLAN = 1;
    static final int DATE_SELECTED = 2;
    static final int DATE_UNSELECTED = 3;
    static final int DATE_STILL_SELECTED = 4;
    static final int DATE_NO_MORE_SELECTED = 5;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        container.removeAllViews();

        final View view = inflater.inflate(R.layout.calendar_layout,container,false);

        calendarView = (CalendarPickerView) view.findViewById(R.id.calendar_view);

        //recuperare selectedDates dal DB.

        this.initializeCalendar();

        confermaDate = (Button) view.findViewById(R.id.confirmDates);

        confermaDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedDates = calendarView.getSelectedDates();
            }
        });

        calendarView.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener() {
            @Override
            public void onDateSelected(Date date) {
                Intent intent = new Intent(view.getContext(), EditWeightnPlanActivity.class);
                if (date != null){
                    intent.putExtra("Date",date.getTime());
                }
                intent.putExtra("Code",DATE_SELECTED);
                startActivityForResult(intent, EDIT_WEIGHT_PLAN);
            }

            @Override
            public void onDateUnselected(Date date) {
                Intent intent = new Intent(view.getContext(), EditWeightnPlanActivity.class);
                if (date != null){
                    intent.putExtra("Date",date.getTime());
                }
                intent.putExtra("Code",DATE_UNSELECTED);
                startActivityForResult(intent, EDIT_WEIGHT_PLAN);
            }
        });


        return view;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EDIT_WEIGHT_PLAN && resultCode == DATE_STILL_SELECTED) {
            calendarView.selectDate(new Date(data.getLongExtra("Date",0)), true); // seleziona la data e scrolla i calendario fino a visualizzarla
        }
        if (requestCode == EDIT_WEIGHT_PLAN && resultCode == DATE_NO_MORE_SELECTED) {
            this.initializeCalendar(); // in EditWeightnPlan mi assicurerò di rimuovere la data da selectedDates e quindi non apparirà una volta reinizializzato il calendario
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

    /**
     * Used in order to initialize a new calendar.
     */
    private void initializeCalendar(){
        Calendar nextYear = Calendar.getInstance();
        nextYear.add(Calendar.YEAR, 1);
        Date today = new Date();
        calendarView.init(today, nextYear.getTime()).inMode(CalendarPickerView.SelectionMode.MULTIPLE);
        if(selectedDates!=null){
            for (Date date:selectedDates){
                calendarView.selectDate(date);
            }
        }
    }



    // Ogni volta che si abbandona il fragment bisogna salvare nel db o nelle shared preferences il risutato di
    // selectedDates, ovvero calendar.getSelectedDates();
    // questo fornisce una lista contenente tutte le date selezionate/accese, sarà quindi possibile alla riapertura di tale
    // fragment riselezionare tutte le date presenti in tale lista. (effettuato da initializeCalendar).
}
