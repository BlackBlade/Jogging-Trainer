package com.example.luca.firstprojectapp.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.luca.firstprojectapp.Interfaces.IOnActivityCallback;
import com.example.luca.firstprojectapp.R;

import java.util.ArrayList;

/**
 * Created by Mary on 10/05/2015.
 */
public class SlideMenuFragment extends Fragment {

    private DrawerLayout drawerLayout;
    private ArrayList<NavItem> list = new ArrayList<>();
    private ListView myList;
    private IOnActivityCallback listener;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.slide_menu_layout,container,false);

        list.add(new NavItem("Profile", R.drawable.abc_btn_check_material));
        list.add(new NavItem("Activity",R.drawable.abc_btn_check_material));
        list.add(new NavItem("Statistics",R.drawable.abc_btn_check_material));

        drawerLayout = (DrawerLayout) view.findViewById(R.id.drawerLayout);

        myList = (ListView) view.findViewById(R.id.navList);
        DrawerListAdapter adapter = new DrawerListAdapter(view.getContext(),list);
        myList.setAdapter(adapter);

        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               listener.swapFragment(position);
            }
        });
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(activity instanceof  IOnActivityCallback){
            listener = (IOnActivityCallback) activity;
        } else {
            throw new UnsupportedOperationException("Wrong input view during attach");
        }
    }

    //class for the item in the slider menu. This class represents a single item in the list
    class NavItem {
        String mTitle;
        int mIcon;

        public NavItem(String title,int icon) {
            mTitle = title;
            mIcon = icon;
        }
    }

    //adapter for populating the listview
    class DrawerListAdapter extends BaseAdapter {

        Context mContext;
        ArrayList<NavItem> mNavItems;

        public DrawerListAdapter(Context context, ArrayList<NavItem> navItems) {
            mContext = context;
            mNavItems = navItems;
        }

        @Override
        public int getCount() {
            return mNavItems.size();
        }

        @Override
        public Object getItem(int position) {
            return mNavItems.get(position);
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

            titleView.setText( mNavItems.get(position).mTitle );
            iconView.setImageResource(mNavItems.get(position).mIcon);

            return view;
        }
    }
}
