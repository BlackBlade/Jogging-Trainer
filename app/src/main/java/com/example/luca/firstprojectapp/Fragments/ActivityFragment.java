package com.example.luca.firstprojectapp.Fragments;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import com.example.luca.firstprojectapp.Interfaces.IOnActivityCallback;
import com.example.luca.firstprojectapp.MyRunService;
import com.example.luca.firstprojectapp.R;
import com.example.luca.firstprojectapp.Utilities.MyLocationListener;
import com.example.luca.firstprojectapp.Utilities.PhotoManager;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.share.ShareApi;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareButton;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.sromku.simple.fb.Permission;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.SimpleFacebookConfiguration;
import com.sromku.simple.fb.listeners.OnPublishListener;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

/**
 * Created by luca on 21/05/15.
 */
public class ActivityFragment extends Fragment {

    private IOnActivityCallback listener;
    private Chronometer chronometer;
    private PolylineOptions coordinates = new PolylineOptions();
    private boolean firstStart = true;
    private LocationManager locationManager;
    private  MyLocationListener locationListener;
    private GoogleMap map;
    private CallbackManager callbackManager;
    private String photoPath = null;
    private SimpleFacebook mSimpleFacebook;
    private ShareDialog shareDialog;
    private  File f;
    private boolean ok = false;
    private Button buttonStartPause;

    private MyRunService mService;

    private final ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = ((MyRunService.LocalBinder) service).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;

        }
    };

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //TODO implement on receive
        }
    };

    private Calendar startTraining;

    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (container != null) {
            container.removeAllViews();
        }


        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);
        final View view = inflater.inflate(R.layout.activity_layout, container, false);
        mSimpleFacebook = SimpleFacebook.getInstance(getActivity());

        final OnPublishListener onPublishListener = new OnPublishListener() {
            @Override
            public void onComplete(String id) {
                Log.i("Tag","Succesfully published");
            }

        };

        Permission[] permissions = new Permission[] {
                Permission.USER_PHOTOS,
                Permission.EMAIL,
                Permission.PUBLISH_ACTION
        };

        SimpleFacebookConfiguration configuration = new SimpleFacebookConfiguration.Builder()
                .setAppId("1653608934858178")
                .setNamespace("My Jogging Trainer")
                .setPermissions(permissions)
                .build();

        SimpleFacebook.setConfiguration(configuration);

        coordinates.width(5);
        coordinates.color(Color.BLUE);
        SupportMapFragment mappa = new SupportMapFragment();
        FragmentTransaction transaction_map = getChildFragmentManager().beginTransaction();
        transaction_map.replace(R.id.mapContainer, mappa, "fragmentMap");
        transaction_map.commit();

        chronometer = (Chronometer) view.findViewById(R.id.chronometer);

        TextView MusicPlayer = (TextView) view.findViewById(R.id.takePhotoButton);
        MusicPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(listener.getContext(),coordinates.getPoints().size()+"",Toast.LENGTH_SHORT).show();
            }
        });

        final Button buttonStop = (Button) view.findViewById(R.id.stopButton);
        buttonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                long distance = locationListener.getTotalDistance();
                //compute calories(assuming that the first factor is the average weight)
                double calories = 156*0.63*(distance/1000*1.60934);

                //operazioni sul db!
                listener.getDatabaseManager().insertStats(startTraining,calories
                        ,distance,Calendar.getInstance().getTimeInMillis() - startTraining.getTimeInMillis());

                listener.swapFragment(4
                ); // cosi quando ha startato l'activity per i dettagli
                                          //si ritrova gia nella sezione corrispondente

                //startare una activity per visualizzare il risultato!

            }
        });

        buttonStartPause = (Button) view.findViewById(R.id.start_pauseButton);
        buttonStartPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                buttonStop.setClickable(true);



                if(buttonStartPause.getText().toString().equals("Start")){
                    buttonStartPause.setText("Pause");
                    if(firstStart){
                        firstStart = false;
                        locationManager = listener.getSystemService();
                        startTraining = Calendar.getInstance(); //inizio dell'allenamento

                        if ( !locationManager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
                            buildAlertMessageNoGps();
                        }


                        locationListener = new MyLocationListener(coordinates,ActivityFragment.this);
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER, 50, 5, locationListener);

                    }else{
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER, 50, 5, locationListener);
                    }

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
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,9999999,
                            9999999,locationListener);
                    buttonStartPause.setText("Start");
                }
            }
        });

        /**
         * Created by Marina Londei.
         * After checking if the Camera is available, it creates an image file using PhotoManager methods.
         * The picture can be then posted on Facebook (posting done using facebook graph api).
         */
        final ShareButton takePhoto = (ShareButton) view.findViewById(R.id.takePhotoButton);
        takePhoto.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                       Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (PhotoManager.getInstance().isIntentAvaliable(getActivity(), MediaStore.ACTION_IMAGE_CAPTURE)){
                            try {
                                f = PhotoManager.getInstance().createImageFile();
                                photoPath = f.getAbsolutePath();
                                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                                startActivityForResult(takePictureIntent,1);
                                ok=true;
                            }catch(IOException e){
                                e.printStackTrace();

                            }
                            if (ok) {
                                new AlertDialog.Builder(getActivity())
                                        .setTitle("Publish photo")
                                        .setMessage("Do you want to publish this photo on Facebook?")
                                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                SharePhoto photo = new SharePhoto.Builder()
                                                        .setImageUrl(Uri.fromFile(f))
                                                        .setCaption("Hey! Look where I'm at! " +
                                                        "Running with My Jogging Trainer")
                                                        .build();
                                                SharePhotoContent content = new SharePhotoContent.Builder()
                                                        .addPhoto(photo)
                                                        .build();
                                                ShareApi.share(content, null);
                                            }
                                        })
                                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        })
                                        .show();
                            }

                        }

                }
            }
        );


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
                map = googleMap;
            }
        });

        return view;
    }

    private void initialize(){
        Intent i = new Intent(getActivity(),MyRunService.class);
        getActivity().startService(i);
        getActivity().bindService(i,mConnection,Context.BIND_AUTO_CREATE);
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

    public void buildPath(LatLng lastPosition){
        map.clear();
        if(lastPosition != null) {
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(lastPosition);
            map.addMarker(markerOptions);

            map.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(lastPosition,
                    15, 0, 0)));
            map.addPolyline(coordinates);
        }

    }

    @Override
    public void onResume()
    {
        super.onResume();
        mSimpleFacebook = SimpleFacebook.getInstance(getActivity());
    }

    @Override
    public void onPause(){
        super.onPause();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);//forward del risultato al login manager.
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
