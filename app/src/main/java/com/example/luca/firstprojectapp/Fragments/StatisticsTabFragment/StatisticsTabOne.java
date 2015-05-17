package com.example.luca.firstprojectapp.Fragments.StatisticsTabFragment;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.luca.firstprojectapp.DatabaseManagement.DatabaseManager;
import com.example.luca.firstprojectapp.DatabaseManagement.SqlLiteHelper;
import com.example.luca.firstprojectapp.Interfaces.IOnActivityCallback;
import com.example.luca.firstprojectapp.R;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.Calendar;

/**
 * Created by luca on 15/05/15.
 */
public class StatisticsTabOne extends Fragment implements DatabaseManager.IOnCursorCallback{

    private IOnActivityCallback listener;
    private GraphView graphView;
    private Calendar calendar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.statistictab_one_layout,container,false);

        //init calendar, cloning from the actual instance
        calendar = (Calendar) Calendar.getInstance().clone();

        //setting graph
        graphView = (GraphView) view.findViewById(R.id.graphWeight);
        initGraph(graphView);

        return view;
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
     * Inits the graph setting horizontal and vertical bounds
     * In this case vertical bounds are setted to initial weight graph
     * @param graph input graph to init
     */
    private void initGraph(GraphView graph){
        if(graph != null){

            StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
            staticLabelsFormatter.setHorizontalLabels(new String[]{"", "Days"});
            staticLabelsFormatter.setVerticalLabels(new String[]{"30", "60", "90", "120", "150"});
            graph.getGridLabelRenderer().setNumHorizontalLabels(2);
            graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);

            graph.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.HORIZONTAL);

            graph.getViewport().setXAxisBoundsManual(true);
            graph.getViewport().setMinX(1.0);
            graph.getViewport().setMaxX(31.0);

            graph.getViewport().setYAxisBoundsManual(true);
            graph.getViewport().setMinY(30.0);
            graph.getViewport().setMaxY(150.0);

            selectStatement(calendar);

        }
    }

    private void selectStatement(Calendar cal){

        Calendar first = (Calendar) cal.clone();
        first.set(Calendar.DAY_OF_MONTH,0);
        first.set(Calendar.HOUR_OF_DAY,0);
        first.set(Calendar.MINUTE,0);
        first.set(Calendar.SECOND,0);
        first.set(Calendar.MILLISECOND,0);

        listener.getDatabaseManager().querySelect("select " + SqlLiteHelper.COLUMN_ID +
                "," + SqlLiteHelper.COLUMN_WEIGHT + " from " + SqlLiteHelper.TABLE_WEIGHT +
                " where " + SqlLiteHelper.COLUMN_ID + " between " + first.getTimeInMillis() +
                " and " + cal.getTimeInMillis()
                ,this,1);
    }

    @Override
    public void fillView(Cursor cur, int position) {
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>();


        while(cur.moveToNext()){
            series.appendData(new DataPoint(cur.getDouble(0),cur.getDouble(1)),false,1000000);

        }

        cur.close();

        if(!series.isEmpty()) {
            graphView.addSeries(series);
        }

    }
}
