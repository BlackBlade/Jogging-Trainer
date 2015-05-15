package com.example.luca.firstprojectapp.Fragments;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;

import com.example.luca.firstprojectapp.DatabaseManagement.DatabaseManager;
import com.example.luca.firstprojectapp.Fragments.StatisticsTabFragment.ViewPagerAdapter;
import com.example.luca.firstprojectapp.Interfaces.IOnActivityCallback;
import com.example.luca.firstprojectapp.R;
import com.example.luca.firstprojectapp.SlidingTab.SlidingTabLayout;
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
    private ViewPager pager;
    private ViewPagerAdapter adapter;
    private SlidingTabLayout tabs;
    private static final CharSequence Titles[] = {"Weight","Calories","Perf."};
    private static final int Numboftabs = 3;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        container.removeAllViews();

        final View view = inflater.inflate(R.layout.statistics_layout,container,false);
        // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
        adapter =  new ViewPagerAdapter(listener.getMySupportFragmentManager(),Titles,Numboftabs);

        // Assigning ViewPager View and setting the adapter
        pager = (ViewPager) view.findViewById(R.id.pager);
        pager.setAdapter(adapter);

        // Assiging the Sliding Tab Layout View
        tabs = (SlidingTabLayout) view.findViewById(R.id.tabs);
        tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width

        // Setting Custom Color for the Scroll bar indicator of the Tab View
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.currTab);
            }
        });

        // Setting the ViewPager For the SlidingTabsLayout
        tabs.setViewPager(pager);

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
    public void fillView(Cursor cur, int position) {
        //TODO implement method to fill graph
    }

}
