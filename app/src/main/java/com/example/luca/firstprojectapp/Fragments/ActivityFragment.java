package com.example.luca.firstprojectapp.Fragments;


import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import com.example.luca.firstprojectapp.Interfaces.IOnActivityCallback;
import com.example.luca.firstprojectapp.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

/**
 * Created by luca on 21/05/15.
 */
public class ActivityFragment extends Fragment {

    private IOnActivityCallback listener;
    private LatLng actualLatLng;
    private Chronometer chronometer;

    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        container.removeAllViews();

        final View view = inflater.inflate(R.layout.activity_layout, container, false);

        SupportMapFragment mappa = new SupportMapFragment();
        FragmentTransaction transaction_map = getChildFragmentManager().beginTransaction();
        transaction_map.replace(R.id.mapContainer, mappa, "fragmentMap");
        transaction_map.commit();

        chronometer = (Chronometer) view.findViewById(R.id.chronometer);
        chronometer.start();

        TextView culo = (TextView) view.findViewById(R.id.textView2);
        culo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(listener.getContext(),"culissimo",Toast.LENGTH_SHORT).show();
            }
        });
        mappa.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                //setMarker(googleMap);
            }
        });

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

    /*
    public void setMarker(GoogleMap marker) {
        this.map = marker;
        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                actualLatLng = latLng;
            }
        });

        new GetPersonaLocation().execute();

    }


    protected class GetPersonaLocation extends AsyncTask<Void, Void, Address> {

        @Override
        protected Address doInBackground(Void... params) {
            List<Address> addresses = null;
            try {
                Geocoder geocoder = new Geocoder(listener.getContext(), Locale.getDefault());
                if(listener.getCoordinates() != null){
                    addresses = new LinkedList<>();
                    Locale loc = Locale.getDefault();
                    Address add = new Address(loc);
                    add.setLatitude(listener.getCoordinates().latitude);
                    add.setLongitude(listener.getCoordinates().longitude);
                    addresses.add(add);
                }else {
                    addresses = geocoder.getFromLocationName("Via Busseto 15, Riccione", 1);
                }

            } catch (Exception e1) {
                e1.printStackTrace();
            }

            return addresses != null && addresses.size() > 0 ? addresses.get(0) : null;
        }

        @Override
        protected void onPostExecute(Address address) {
            super.onPostExecute(address);
            if (address != null && map != null) {
                LatLng pos = new LatLng(address.getLatitude(), address.getLongitude());
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(pos);
                map.addMarker(markerOptions);

                map.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(pos,
                        15, 0, 0)));
            }
        }
    }
    */
}
