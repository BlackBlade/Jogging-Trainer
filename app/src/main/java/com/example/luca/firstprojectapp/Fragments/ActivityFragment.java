package com.example.luca.firstprojectapp.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.luca.firstprojectapp.R;

/**
 * Created by luca on 21/05/15.
 */
public class ActivityFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        container.removeAllViews();

        final View view = inflater.inflate(R.layout.activity_layout,container,false);

        return view;
    }
}
