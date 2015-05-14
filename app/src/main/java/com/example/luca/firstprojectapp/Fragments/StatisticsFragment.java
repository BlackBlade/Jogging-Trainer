package com.example.luca.firstprojectapp.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;

import com.example.luca.firstprojectapp.DatabaseManagement.DatabaseManager;
import com.example.luca.firstprojectapp.Interfaces.IOnActivityCallback;
import com.example.luca.firstprojectapp.R;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
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

        container.removeAllViews();

        final View view = inflater.inflate(R.layout.statistics_layout,container,false); //TODO edit layout

        //setting graph bounds
        graphView = (GraphView) view.findViewById(R.id.weightGraph);
        initGraph(graphView);

        //funziona, bisogna fare tab.setup prima di aggiungere, senno nullpointer
        TabHost tab = (TabHost) view.findViewById(R.id.tabHost);
        tab.setup();
        TabHost.TabSpec ts = tab.newTabSpec("tag1"); //TODO modificare i tag in maniera sensata
        ts.setContent(R.id.weightLayout);
        ts.setIndicator("Weight Indicator");
        tab.addTab(ts);

        ts = tab.newTabSpec("tag2");
        ts.setContent(R.id.caloriesLayout);
        ts.setIndicator("Calories Indicator");
        tab.addTab(ts);

        ts = tab.newTabSpec("tag3");
        ts.setContent(R.id.performanceLayout);
        ts.setIndicator("Performance");
        tab.addTab(ts);

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

        //ora il grafo è preciso, basta scegliere dei valori equidistanti!
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


            //TODO add dynamic series
            LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[]{
                    new DataPoint(10.0,60.0),
                    new DataPoint(16.0,75.0),
                    new DataPoint(20.0,150.0)

            });

            graph.addSeries(series);

        }
    }
}
