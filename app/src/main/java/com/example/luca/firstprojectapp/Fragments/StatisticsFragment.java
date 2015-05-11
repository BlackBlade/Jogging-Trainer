package com.example.luca.firstprojectapp.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.luca.firstprojectapp.DatabaseManager;
import com.example.luca.firstprojectapp.Interfaces.IOnActivityCallback;
import com.example.luca.firstprojectapp.R;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by luca on 11/05/15
 * Shows performance graphs to the user
 */
public class StatisticsFragment extends Fragment implements DatabaseManager.IOnCursorCallback {

    private IOnActivityCallback listener;
    private GraphView graphView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.statistics_layout,container,false); //TODO edit layout

        //setting graph bounds
        graphView = (GraphView) view.findViewById(R.id.graph);
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

    @Override
    public void fillView(Cursor cur) {
        //TODO implement method to fill graph
    }

    //TODO trovare un modo per plottare il grafo... La soluzione non sembra essere semplice
    /**
     * Inits the graph setting horizontal and vertical bounds
     * In this case vertical bounds are setted to initial weight graph
     * @param graph input graph to init
     */
    private void initGraph(GraphView graph){
        if(graph != null){
            //StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
            //staticLabelsFormatter.setHorizontalLabels(new String[]{"1","5","10",
            //"15","20","25","31"});
            //staticLabelsFormatter.setVerticalLabels(new String[]{"0","10","20","30","40","50","60","70",
              //      "80","90","100","110","120","130","140","150"});
            //graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);

            Calendar calendar = Calendar.getInstance();
            Date d1 = calendar.getTime();
            calendar.add(Calendar.DATE, 1);
            Date d2 = calendar.getTime();
            calendar.add(Calendar.DATE, 1);
            Date d3 = calendar.getTime();

            /*
            graph.getViewport().setXAxisBoundsManual(true);
            graph.getViewport().setMinX(1.0);
            graph.getViewport().setMaxX(31.0);

            graph.getViewport().setYAxisBoundsManual(true);
            graph.getViewport().setMinY(0.0);
            graph.getViewport().setMaxY(150.0);
            */
            //TODO add dynamic series
            LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[]{
                    new DataPoint(d1,38.0),
                    new DataPoint(d2,75.0),
                    new DataPoint(d3,75.0)

            });

            graph.addSeries(series);

            graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(listener.getContext()));
            graph.getGridLabelRenderer().setNumHorizontalLabels(3); // only 4 because of the space

            d1.setSeconds(0);
            d1.setDate(1);
            d1.setSeconds(0);
            d3.setDate(31);
            // set manual x bounds to have nice steps
            graph.getViewport().setMinX(d1.getTime());
            graph.getViewport().setMaxX(d3.getTime());
            graph.getViewport().setXAxisBoundsManual(true);

            graph.getViewport().setMinY(0.0);
            graph.getViewport().setMaxY(150.0);
            graph.getViewport().setYAxisBoundsManual(true);
        }
    }
}
