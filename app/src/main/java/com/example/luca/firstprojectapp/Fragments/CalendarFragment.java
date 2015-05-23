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
import android.widget.Button;
import android.widget.Toast;

import com.example.luca.firstprojectapp.DatabaseManagement.DatabaseManager;
import com.example.luca.firstprojectapp.DatabaseManagement.SqlLiteHelper;
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
    static final int EDIT_WEIGHT_PLAN = 1;
    static final int DATE_SELECTED = 2;
    static final int DATE_UNSELECTED = 3;
    static final int DATE_STILL_SELECTED = 4;
    static final int DATE_NO_MORE_SELECTED = 5;
    private static final String Query ="select * from " + SqlLiteHelper.TABLE_PLANNING;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        container.removeAllViews();

        final View view = inflater.inflate(R.layout.calendar_layout,container,false);

        calendarView = (CalendarPickerView) view.findViewById(R.id.calendar_view);

        //recuperare selectedDates dal DB.
        listener.getDatabaseManager().querySelect(Query,this,1); //chiama metodo su db e poi fill view.

        this.initializeCalendar();

        calendarView.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener() {
            @Override
            public void onDateSelected(final Date date) {
                new AlertDialog.Builder(view.getContext())
                        .setTitle("JoggingTrainer")
                        .setMessage("Edit Weight or Cancel Activity?")
                        .setPositiveButton("EditWeight", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(view.getContext(), EditWeightnPlanActivity.class);
                                if (date != null){
                                    intent.putExtra("Date",date.getTime());
                                }
                                intent.putExtra("Code",DATE_SELECTED);
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
            public void onDateUnselected(final Date date) {
                new AlertDialog.Builder(view.getContext())
                        .setTitle("JoggingTrainer")
                        .setMessage("Edit Weight or Plan Activity?")
                        .setPositiveButton("EditWeight", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(view.getContext(), EditWeightnPlanActivity.class);
                                if (date != null){
                                    intent.putExtra("Date",date.getTime());
                                }
                                intent.putExtra("Code",DATE_UNSELECTED);
                                startActivityForResult(intent, EDIT_WEIGHT_PLAN);
                            }
                        })
                        .setNegativeButton("PlanActivity", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // inserire l'attività nel db.
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

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
            Toast.makeText(listener.getContext(),"culo",Toast.LENGTH_SHORT).show();
            //calendarView.selectDate(new Date(data.getLongExtra("Date",0)), true); // seleziona la data e scrolla i calendario fino a visualizzarla
        }
        if (requestCode == EDIT_WEIGHT_PLAN && resultCode == DATE_NO_MORE_SELECTED) {
            Toast.makeText(listener.getContext(),"culo",Toast.LENGTH_SHORT).show();
            this.initializeCalendar(); // in EditWeightnPlan mi assicurerò di rimuovere la data da selectedDates e quindi non apparirà una volta reinizializzato il calendario
        }
    }

    @Override
    public void fillView(Cursor cur, int position) {
        while(cur.moveToNext()){
            Date date = new Date(cur.getLong(0));
            selectedDates.add(date);
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
}
