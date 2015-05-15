package com.example.luca.firstprojectapp.Fragments;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.luca.firstprojectapp.DatabaseManagement.DatabaseManager;
import com.example.luca.firstprojectapp.Interfaces.IOnActivityCallback;
import com.example.luca.firstprojectapp.R;

/**
 * Created by luca on 05/05/15.
 * example fragment, we will create further fragment to adapt the view
 */
public class ShowMessageFragment extends Fragment implements DatabaseManager.IOnCursorCallback {

    private ListView list;
    private IOnActivityCallback listener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        container.removeAllViews();

        final View view = inflater.inflate(R.layout.message_layout,container,false);
        list = (ListView) view.findViewById(R.id.listView);
        //listener.getDatabaseManager().querySelect("select * from " + SqlLiteHelper.TABLE_PLANNING,this);

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(activity instanceof IOnActivityCallback)
            listener = (IOnActivityCallback) activity;
    }

    @Override
    public void fillView(Cursor cur,int position) { //TODO adapter da fare a mano, con il tutorial, evitando la classe deprecata
        /*
        list.setAdapter(new SimpleCursorAdapter(listener.getContext(),R.layout.list_item,cur,
                new String[]{SqlLiteHelper.COLUMN_STRING}, new int[]{R.id.textView}));
                */

    }

}
