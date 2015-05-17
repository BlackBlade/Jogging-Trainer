package com.example.luca.firstprojectapp.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.luca.firstprojectapp.R;

/**
 * Created by Mary on 17/05/2015.
 */
public class ProfileFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        container.removeAllViews();

        View view = inflater.inflate(R.layout.profile_fragment_layout,container, false);
        return view;
    }
}
