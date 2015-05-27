package com.example.luca.firstprojectapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.luca.firstprojectapp.R;

import java.util.List;

/**
 * Created by MatteoOrzes on 27/05/2015.
 */
public class CorseAdapter extends ArrayAdapter<Corsa> {

    private class ViewHolder {
        ImageView imageView;
        TextView distanzaPercorsa;
        TextView tempoImpiegato;
        TextView calorieConsumate;
        TextView orarioInizio;
    }

    public CorseAdapter (Context context, List<Corsa> Corse){
        super(context,0,Corse);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_corsa, parent, false);
            holder = new ViewHolder();
            /*
            holder.imageView = (ImageView) convertView.findViewById(R.id.user_image);
            holder.distanzaPercorsa = (TextView) convertView.findViewById(R.id.distanza);
            holder.tempoImpiegato = (TextView) convertView.findViewById(R.id.tempo);
            holder.calorieConsumate = (TextView) convertView.findViewById(R.id.calorie);
            holder.orarioInizio = (TextView) convertView.findViewById(R.id.orario);
            */

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Corsa corsa = (Corsa) getItem(position);


        // recupero informazioni dal db e le setto nell'holder.
        return null;
    }
}
