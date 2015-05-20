package com.example.luca.firstprojectapp.Fragments.StatisticsTabFragment;

import android.app.Activity;
import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

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
public class StatisticsTabTwo extends Fragment implements DatabaseManager.IOnCursorCallback {

    private IOnActivityCallback listener;
    private GraphView graphView;
    private Calendar calendar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.statistictab_two_layout,container,false);

        //init calendar, cloning from the actual instance
        calendar = (Calendar) Calendar.getInstance().clone();

        //setting graph
        graphView = (GraphView) view.findViewById(R.id.graphCalories);
        initGraph(graphView);

        Button buttonPrevious = (Button) view.findViewById(R.id.buttonPreviousCalories);
        Button buttonNext = (Button) view.findViewById(R.id.buttonNextCalories);
        final TextView label = (TextView) view.findViewById(R.id.monthCalories);
        label.setText((calendar.get(Calendar.MONTH)+1) + " - " + calendar.get(Calendar.YEAR));

        buttonPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (calendar.get(Calendar.MONTH) != 0) {
                    graphView.removeAllSeries();
                    calendar.add(Calendar.MONTH, -1);
                    calendar.set(Calendar.DAY_OF_MONTH,calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
                    selectStatement(calendar);
                    label.setText((calendar.get(Calendar.MONTH)+1) + " - " + calendar.get(Calendar.YEAR));
                }
            }
        });

        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(calendar.get(Calendar.MONTH) != 11){
                    graphView.removeAllSeries();
                    calendar.add(Calendar.MONTH,1);
                    calendar.set(Calendar.DAY_OF_MONTH,calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
                    selectStatement(calendar);
                    label.setText((calendar.get(Calendar.MONTH)+1) + " - " + calendar.get(Calendar.YEAR));
                }
            }
        });

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
            graph.getGridLabelRenderer().setNumHorizontalLabels(2);
            graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);

            graph.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.HORIZONTAL);

            //style
            graph.getGridLabelRenderer().setGridColor(getResources().getColor(R.color.labelsColor));
            graph.getGridLabelRenderer().setHorizontalLabelsColor(getResources().getColor(R.color.labelsColor));
            graph.getGridLabelRenderer().setVerticalLabelsColor(getResources().getColor(R.color.labelsColor));
            graph.getViewport().setBackgroundColor(getResources().getColor(R.color.graphBackground));


            graph.getViewport().setXAxisBoundsManual(true);
            graph.getViewport().setMinX(1.0);
            graph.getViewport().setMaxX(31.0);

            selectStatement(calendar);

        }
    }

    private void selectStatement(Calendar cal){

        Calendar first = (Calendar) cal.clone();
        first.set(Calendar.DAY_OF_MONTH,1);
        first.set(Calendar.HOUR_OF_DAY,0);
        first.set(Calendar.MINUTE,0);
        first.set(Calendar.MILLISECOND,0);

        listener.getDatabaseManager().querySelect("select " + SqlLiteHelper.COLUMN_ID +
                "," + SqlLiteHelper.COLUMN_CALORIES + " from " + SqlLiteHelper.TABLE_STATS
                + " where " + SqlLiteHelper.COLUMN_ID + " between " + first.getTimeInMillis()
                + " and " + cal.getTimeInMillis()
                ,this,1);
    }

    @Override
    public void fillView(Cursor cur, int position) {
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>();
        LineGraphSeries<DataPoint> seriesAverage = new LineGraphSeries<>();

        Calendar cal = Calendar.getInstance();

        double sum = 0;

        while(cur.moveToNext()){

            cal.setTimeInMillis(cur.getLong(0));
            series.appendData(new DataPoint(cal.get(Calendar.DAY_OF_MONTH),cur.getLong(1)),false,1000000);
            sum += cur.getLong(1);

        }

        //average line, if data is present
        if(cur.getCount() != 0) {
            seriesAverage.appendData(new DataPoint(1, sum / cur.getCount()), false, 50);
            seriesAverage.appendData(new DataPoint(31, sum / cur.getCount()), false, 50);

        }
        cur.close();

        if(!series.isEmpty()) {
            series.setThickness(2);
            series.setColor(getResources().getColor(R.color.labelsColor));
            seriesAverage.setThickness(2);
            seriesAverage.setColor(getResources().getColor(R.color.averageColor));
            graphView.addSeries(series);
            graphView.addSeries(seriesAverage);
        }

    }
}
