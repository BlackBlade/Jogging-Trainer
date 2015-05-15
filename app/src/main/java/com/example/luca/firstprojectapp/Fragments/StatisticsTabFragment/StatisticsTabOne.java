package com.example.luca.firstprojectapp.Fragments.StatisticsTabFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.luca.firstprojectapp.R;

/**
 * Created by luca on 15/05/15.
 */
public class StatisticsTabOne extends Fragment {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.statistictab_one_layout,container,false);

        return view;
    }
}
