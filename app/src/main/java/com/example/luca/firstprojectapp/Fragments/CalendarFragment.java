package com.example.luca.firstprojectapp.Fragments;

/**
 * Created by MatteoOrzes on 20/05/2015.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.luca.firstprojectapp.DatabaseManagement.DatabaseManager;
import com.example.luca.firstprojectapp.DatabaseManagement.SqlLiteHelper;
import com.example.luca.firstprojectapp.EditWeightnPlanActivity;
import com.example.luca.firstprojectapp.Interfaces.IOnActivityCallback;
import com.example.luca.firstprojectapp.R;
import com.squareup.timessquare.CalendarPickerView;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class CalendarFragment extends Fragment implements DatabaseManager.IOnCursorCallback {

    private IOnActivityCallback listener;
    private CalendarPickerView calendarView;
    private List<Date> selectedDates;
    private List<Date> highlitedDates;
    static final int EDIT_WEIGHT_PLAN = 1;
    private static final String QueryAllDates ="select * from " + SqlLiteHelper.TABLE_PLANNING;
    private static final String QueryHighlitedDates ="select * from " + SqlLiteHelper.TABLE_WEIGHT;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        container.removeAllViews();

        final View view = inflater.inflate(R.layout.calendar_layout,container,false);

        calendarView = (CalendarPickerView) view.findViewById(R.id.calendar_view);

        selectedDates = new ArrayList<Date>();
        highlitedDates = new ArrayList<Date>();

        Date today = new Date();

        //recupera selectedDates dal DB.
        listener.getDatabaseManager().syncQuerySelect(QueryAllDates, this, 1); //chiama metodo su db e poi fill view.

        //recupera highlitedDates dal DB
        listener.getDatabaseManager().syncQuerySelect(QueryHighlitedDates,this,2);

        this.initializeCalendar(today);

        calendarView.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener() {
            @Override
            public void onDateSelected(final Date date) { // chiamato quando una data viene selezionata dall'utente
                new AlertDialog.Builder(view.getContext())
                        .setCancelable(false)
                        .setTitle("JoggingTrainer")
                        .setMessage("Edit Weight or Plan Activity?")
                        .setPositiveButton("EditWeight", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                calendarView.selectDate(date); // la data non deve essere selezionata.
                                Intent intent = new Intent(view.getContext(), EditWeightnPlanActivity.class);
                                if (date != null){
                                    intent.putExtra("Date",date.getTime());
                                }
                                startActivityForResult(intent, EDIT_WEIGHT_PLAN);
                            }
                        })
                        .setNegativeButton("PlanActivity", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //inserire data nel db.
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }

            @Override
            public void onDateUnselected(final Date date) { // chiamato quando una data viene deselezionata dall'utente
                new AlertDialog.Builder(view.getContext())
                        .setCancelable(false)
                        .setTitle("JoggingTrainer")
                        .setMessage("Edit Weight or Cancel Activity?")
                        .setPositiveButton("EditWeight", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(view.getContext(), EditWeightnPlanActivity.class);
                                if (date != null){
                                    calendarView.selectDate(new Date(date.getTime())); //era gia selezionata e deve rimanerlo.
                                    intent.putExtra("Date",date.getTime());
                                    intent.putExtra("Code",2);
                                }
                                startActivityForResult(intent, EDIT_WEIGHT_PLAN);
                            }
                        })
                        .setNegativeButton("RemoveActivity", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // rimuovere l'attività dal db.
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });
        return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EDIT_WEIGHT_PLAN && resultCode == Activity.RESULT_OK){
            if(data.getIntExtra("Code", 0) == 3){
                highlitedDates.add(new Date(data.getLongExtra("Date",0)));
                calendarView.highlightDates(highlitedDates);
            }
        }
        if (requestCode == EDIT_WEIGHT_PLAN && resultCode == Activity.RESULT_OK){
            if(data.getIntExtra("Code", 0) == 4){
                highlitedDates.remove(new Date(data.getLongExtra("Date",0)));
                calendarView.highlightDates(highlitedDates);
            }
        }
    }


    /**
     * Used in order to initialize a new calendar.
     */
    private void initializeCalendar(Date today){
        Calendar nextYear = Calendar.getInstance();
        nextYear.add(Calendar.YEAR, 1);
        Calendar first = Calendar.getInstance();
        first.set(Calendar.MONTH,Calendar.JANUARY);
        first.set(Calendar.DAY_OF_MONTH,1);
        Date firstday = first.getTime();
        calendarView.init(firstday, nextYear.getTime()).inMode(CalendarPickerView.SelectionMode.MULTIPLE);
        if(selectedDates!=null){
            for (Date date:selectedDates){
                calendarView.selectDate(date);
            }
        }
        if(highlitedDates!=null){
            calendarView.highlightDates(highlitedDates);
        }
    }

    @Override
    public void fillView(Cursor cur, int position) {
        switch(position){
            case 1: // Query per selezionare tutte le date con attività pianificata.
                selectedDates.clear();
                while(cur.moveToNext()){
                    Date date = new Date(cur.getLong(0));
                    selectedDates.add(date);
                } break;
            case 2: // Query per selezionare tutte le date con peso inserito.
                highlitedDates.clear();
                while(cur.moveToNext()){
                    Date date = new Date(cur.getLong(0));
                    highlitedDates.add(date);
                } break;
            default:
                break;
        }
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
