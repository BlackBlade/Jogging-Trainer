package com.example.luca.firstprojectapp.Fragments.StatisticsTabFragment;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.luca.firstprojectapp.Interfaces.IOnActivityCallback;
import com.example.luca.firstprojectapp.R;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

/**
 * Created by luca on 15/05/15.
 */
public class StatisticsTabOne extends Fragment {

    private IOnActivityCallback listener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.statistictab_one_layout,container,false);

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
