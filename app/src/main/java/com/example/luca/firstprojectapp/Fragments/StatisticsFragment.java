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
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

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
        final View view = inflater.inflate(R.layout.message_layout,container,false); //TODO edit layout

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

    /**
     * Inits the graph setting horizontal and vertical bounds
     * In this case vertical bounds are setted to initial weight graph
     * @param graph input graph to init
     */
    private void initGraph(GraphView graph){
        if(graph != null){
            StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
            staticLabelsFormatter.setHorizontalLabels(new String[]{"1","2","3","4","5","6","7","8","9","10",
            "11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28",
            "29","30","31"});
            staticLabelsFormatter.setVerticalLabels(new String[]{"10","20","30","40","50","60","70",
                    "80","90","100","110","120","130","140","150"});
            graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);

            //TODO add dynamic series
            LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[]{
                    new DataPoint(0,1),
                    new DataPoint(1,5),
                    new DataPoint(31,3)

            });

            graph.addSeries(series);
        }
    }
}
