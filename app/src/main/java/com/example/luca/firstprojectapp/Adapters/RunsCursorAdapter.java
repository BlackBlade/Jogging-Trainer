package com.example.luca.firstprojectapp.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.luca.firstprojectapp.R;

import java.util.Calendar;

/**
 * Created by MatteoOrzes on 27/05/2015.
 */
public class RunsCursorAdapter extends CursorAdapter {
    public RunsCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater =  LayoutInflater.from(context);
        View v =inflater.inflate(R.layout.list_item_corsa, parent, false);
        this.bindView(v, context, cursor);
        return v;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
            TextView orario = (TextView) view.findViewById(R.id.orario);
            TextView calorie = (TextView) view.findViewById(R.id.calorie);
            TextView distanza = (TextView) view.findViewById(R.id.distanza);
            TextView durata = (TextView) view.findViewById(R.id.durata);

            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(cursor.getLong(0));
            orario.setText(cal.get(Calendar.DAY_OF_MONTH)+"/"+(cal.get(Calendar.MONTH)+1)+" "+cal.get(Calendar.HOUR_OF_DAY)+":"+cal.get(Calendar.MINUTE)+":"+cal.get(Calendar.SECOND));

            calorie.setText(cursor.getLong(1)+" cal");

            distanza.setText(((double) cursor.getLong(2))/1000+" KM");


            long timeInMillis = cursor.getLong(3);
            //cal.setTimeInMillis(cursor.getLong(3));
            durata.setText(timeInMillis/(1000*60*60)+" H "+timeInMillis/(1000*60)+" M "+timeInMillis/1000+" S");
    }
}
