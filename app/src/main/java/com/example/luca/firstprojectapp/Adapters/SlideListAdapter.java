package com.example.luca.firstprojectapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.luca.firstprojectapp.R;

import java.util.ArrayList;

/**
 * Created by Mary on 15/05/2015.
 */
public class SlideListAdapter extends BaseAdapter {

    Context mContext;
    ArrayList<NavItem> navItems;

    public SlideListAdapter(Context context, ArrayList<NavItem> navItems) {

        mContext = context;
        this.navItems = navItems;
    }

    @Override
    public int getCount() {
        return navItems.size();
    }

    @Override
    public Object getItem(int position) {
        return navItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.slide_menu_item, null);
        }
        else {
            view = convertView;
        }

        TextView titleView = (TextView) view.findViewById(R.id.Titolo);
        ImageView iconView = (ImageView) view.findViewById(R.id.icon);

        titleView.setText(navItems.get(position).mTitle );
        iconView.setImageResource(navItems.get(position).mIcon);

        return view;
    }



}
