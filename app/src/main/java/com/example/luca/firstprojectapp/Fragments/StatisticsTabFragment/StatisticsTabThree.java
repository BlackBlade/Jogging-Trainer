package com.example.luca.firstprojectapp.Fragments.StatisticsTabFragment;

import android.app.Activity;
import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
public class StatisticsTabThree extends Fragment implements DatabaseManager.IOnCursorCallback {

    private IOnActivityCallback listener;
    private GraphView graphView;
    private Calendar calendar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.statistictab_three_layout,container,false);

        //init calendar, cloning from the actual instance
        calendar = (Calendar) Calendar.getInstance().clone();

        //setting graph
        graphView = (GraphView) view.findViewById(R.id.graphPerformance);
        initGraph(graphView);

        Button buttonPrevious = (Button) view.findViewById(R.id.buttonPreviousPerformance);
        Button buttonNext = (Button) view.findViewById(R.id.buttonNextPerformance);
        final TextView label = (TextView) view.findViewById(R.id.monthPerformance);
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

    private void initGraph(GraphView graph){
        if(graph != null){

            StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
            staticLabelsFormatter.setHorizontalLabels(new String[]{"", "Days"});
            staticLabelsFormatter.setVerticalLabels(new String[]{"Low","Med","High"});
            graph.getGridLabelRenderer().setNumHorizontalLabels(2);
            graph.getGridLabelRenderer().setNumVerticalLabels(3);
            graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);

            graph.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.HORIZONTAL);

            //style
            graph.getGridLabelRenderer().setGridColor(getResources().getColor(R.color.gridColor));
            graph.getGridLabelRenderer().setHorizontalLabelsColor(getResources().getColor(R.color.labelsColor));
            graph.getGridLabelRenderer().setVerticalLabelsColor(getResources().getColor(R.color.labelsColor));
            graph.getViewport().setBackgroundColor(getResources().getColor(R.color.graphBackground));



            graph.getViewport().setXAxisBoundsManual(true);
            graph.getViewport().setMinX(1.0);
            graph.getViewport().setMaxX(31.0);

            graph.getViewport().setYAxisBoundsManual(true);
            graph.getViewport().setMinY(0.1);
            graph.getViewport().setMaxY(24.0);

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
                "," + SqlLiteHelper.COLUMN_TIME + "," + SqlLiteHelper.COLUMN_DISTANCE
                + " from " + SqlLiteHelper.TABLE_STATS
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

            /*
            first column returns time in milliseconds. We do /1000*60*60 to convert in hours.
            second column returns distance in meters. We do /1000 to convert in kilometers.
            Performance in calculated with (Distance in kilometers / Time in hours)
             */
            cal.setTimeInMillis(cur.getLong(0));
            series.appendData(new DataPoint(cal.get(Calendar.DAY_OF_MONTH),
                    ((double)cur.getLong(2) / 1000) / ((double)cur.getLong(1)/(1000 * 60 * 60))),false,1000000);

            sum +=  ((double)cur.getLong(2) / 1000) / ((double)cur.getLong(1)/(1000 * 60 * 60));
        }

        //average line, if data is present
        if(cur.getCount() != 0) {
            seriesAverage.appendData(new DataPoint(1, sum / cur.getCount()), false, 50);
            seriesAverage.appendData(new DataPoint(31, sum / cur.getCount()), false, 50);
        }
        cur.close();

        if(!series.isEmpty()) {
            series.setThickness(4);
            series.setColor(getResources().getColor(R.color.lineColor));
            seriesAverage.setThickness(2);
            seriesAverage.setColor(getResources().getColor(R.color.averageColor));
            graphView.addSeries(series);
            graphView.addSeries(seriesAverage);
        }

    }


}
