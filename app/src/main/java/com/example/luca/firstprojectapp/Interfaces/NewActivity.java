package com.example.luca.firstprojectapp.Interfaces;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

import com.example.luca.firstprojectapp.R;
import com.example.luca.firstprojectapp.SlideMenuFragment;


/**
 * Created by Mary on 13/05/2015.
 * This activity has been created just for checking if the activity is called correctly by the slider menu.
 */
public class NewActivity  extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_activity_layout);
        FragmentManager manager = getFragmentManager();
        FragmentTransaction trans = manager.beginTransaction();
        SlideMenuFragment fragment = new SlideMenuFragment();
        trans.add(R.id.newActivity,fragment,"fragment_slide");
        trans.commit();
    }
}
