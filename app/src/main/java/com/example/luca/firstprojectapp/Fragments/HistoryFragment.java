package com.example.luca.firstprojectapp.Fragments;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.example.luca.firstprojectapp.Adapters.RunsCursorAdapter;
import com.example.luca.firstprojectapp.DatabaseManagement.DatabaseManager;
import com.example.luca.firstprojectapp.DatabaseManagement.SqlLiteHelper;
import com.example.luca.firstprojectapp.Interfaces.IOnActivityCallback;
import com.example.luca.firstprojectapp.R;
import com.example.luca.firstprojectapp.Utilities.OnSwipeTouchListener;

import java.util.Calendar;

/**
 * Created by MatteoOrzes on 24/05/2015.
 */
public class HistoryFragment extends Fragment implements DatabaseManager.IOnCursorCallback{

    private IOnActivityCallback listener;
    private ListView listView;
    private TextView textView;
    private Calendar calendar;
    private RunsCursorAdapter adapter;
    //private final static String[] columnNames = new String[]{SqlLiteHelper.COLUMN_ID,SqlLiteHelper.COLUMN_CALORIES,SqlLiteHelper.COLUMN_DISTANCE,SqlLiteHelper.COLUMN_TIME};
    //private final static int[] viewsId = new int[]{R.id.orario,R.id.calorie,R.id.distanza, R.id.durata};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        container.removeAllViews();
        final View view = inflater.inflate(R.layout.history_layout,container,false);

        calendar = (Calendar) Calendar.getInstance();

        listView = (ListView) view.findViewById(R.id.corse_listview);

        textView = (TextView) view.findViewById(R.id.textView2);

        textView.setText(""+calendar.get(Calendar.MONTH)+" / "+calendar.get(Calendar.YEAR));

        selectStatement(calendar);

        //listener.getDatabaseManager().querySelect("select * from " + SqlLiteHelper.TABLE_STATS + "where " + , this, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

        view.setOnTouchListener(new OnSwipeTouchListener(listener.getContext()){
            @Override
            public void onSwipeLeft() {
                if (calendar.get(Calendar.MONTH) != 0) {
                    calendar.add(Calendar.MONTH, -1);
                    calendar.set(Calendar.DAY_OF_MONTH,calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
                    selectStatement(calendar);
                    textView.setText((calendar.get(Calendar.MONTH)+1) + " - " + calendar.get(Calendar.YEAR));
                }
            }
            @Override
            public void onSwipeRight() {
                if (calendar.get(Calendar.MONTH) != 0) {
                    calendar.add(Calendar.MONTH, +1);
                    calendar.set(Calendar.DAY_OF_MONTH,calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
                    selectStatement(calendar);
                    textView.setText((calendar.get(Calendar.MONTH)-1) + " - " + calendar.get(Calendar.YEAR));
                }
            }
        });

        return view;
    }


    private void selectStatement(Calendar cal){

        Calendar first = (Calendar) cal.clone();
        first.set(Calendar.DAY_OF_MONTH,1);
        first.set(Calendar.HOUR_OF_DAY,0);
        first.set(Calendar.MINUTE,0);
        first.set(Calendar.MILLISECOND,0);

        listener.getDatabaseManager().querySelect("select * from " + SqlLiteHelper.TABLE_WEIGHT
                + " where " + SqlLiteHelper.COLUMN_ID + " between " + first.getTimeInMillis()
                + " and " + cal.getTimeInMillis()
                ,this,1);
    }

    @Override
    public void fillView(Cursor cur, int position) {
        adapter = new RunsCursorAdapter(listener.getContext(),cur,0);
        listView.setAdapter(adapter);
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
