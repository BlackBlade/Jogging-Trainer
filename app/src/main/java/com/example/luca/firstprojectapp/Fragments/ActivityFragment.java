package com.example.luca.firstprojectapp.Fragments;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.luca.firstprojectapp.Interfaces.IOnActivityCallback;
import com.example.luca.firstprojectapp.R;
import com.example.luca.firstprojectapp.Utilities.MyLocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

/**
 * Created by luca on 21/05/15.
 */
public class ActivityFragment extends Fragment {

    private IOnActivityCallback listener;
    private Chronometer chronometer;
    private List<LatLng> coordinates = new ArrayList<>();

    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (container != null) {
            container.removeAllViews();
        }

        final View view = inflater.inflate(R.layout.activity_layout, container, false);

        SupportMapFragment mappa = new SupportMapFragment();
        FragmentTransaction transaction_map = getChildFragmentManager().beginTransaction();
        transaction_map.replace(R.id.mapContainer, mappa, "fragmentMap");
        transaction_map.commit();

        chronometer = (Chronometer) view.findViewById(R.id.chronometer);


        TextView MusicPlayer = (TextView) view.findViewById(R.id.textView2);
        MusicPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(listener.getContext(),coordinates.size()+"",Toast.LENGTH_SHORT).show();
            }
        });

        final Button buttonStop = (Button) view.findViewById(R.id.stopButton);
        buttonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //DB operation

                buttonStop.setClickable(false);

                //Azzerare a view

            }
        });

        final Button buttonStartPause = (Button) view.findViewById(R.id.start_pauseButton);
        buttonStartPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                buttonStop.setClickable(true);

                if(buttonStartPause.getText().toString().equals("Start")){
                    buttonStartPause.setText("Pause");
                    int stoppedMilliseconds = 0;

                    String chronoText = chronometer.getText().toString();
                    String array[] = chronoText.split(":");
                    if (array.length == 2) {
                        stoppedMilliseconds = Integer.parseInt(array[0]) * 60 * 1000
                                + Integer.parseInt(array[1]) * 1000;
                    } else if (array.length == 3) {
                        stoppedMilliseconds = Integer.parseInt(array[0]) * 60 * 60 * 1000
                                + Integer.parseInt(array[1]) * 60 * 1000
                                + Integer.parseInt(array[2]) * 1000;
                    }

                    chronometer.setBase(SystemClock.elapsedRealtime() - stoppedMilliseconds);
                    chronometer.start();
                } else {

                    chronometer.stop();
                    buttonStartPause.setText("Start");
                }
            }
        });

        view.findViewById(R.id.lockButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View b = view.findViewById(R.id.frameLayout);
                b.setClickable(true);
                b.setVisibility(View.VISIBLE);
            }
        });

        view.findViewById(R.id.lockButton1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View b = view.findViewById(R.id.frameLayout);
                b.setClickable(false);
                b.setVisibility(View.INVISIBLE);
            }
        });

        mappa.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                //setMarker(googleMap);
            }
        });

        LocationManager locationManager = listener.getSystemService();

        if ( !locationManager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            buildAlertMessageNoGps();
        }


        LocationListener locationListener = new MyLocationListener(coordinates);
        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, 5000, 10, locationListener);

        return view;
    }

    /**
     * Method used to request the user to activate the gps during the training
     */
    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(listener.getContext());
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
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
