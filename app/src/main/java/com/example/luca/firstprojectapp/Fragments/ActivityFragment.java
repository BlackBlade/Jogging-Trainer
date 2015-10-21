package com.example.luca.firstprojectapp.Fragments;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
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

import com.example.luca.firstprojectapp.Interfaces.IOnActivityCallback;
import com.example.luca.firstprojectapp.R;
import com.example.luca.firstprojectapp.Services.MyRunService;
import com.example.luca.firstprojectapp.Utilities.PhotoManager;
import com.example.luca.firstprojectapp.Utilities.PolylineManager;
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
    private boolean firstStart = true;
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
            if(intent.hasExtra("Latitude") && intent.hasExtra("Longitude")){
                double latitude = intent.getDoubleExtra("Latitude",0);
                double longitude = intent.getDoubleExtra("Longitude",0);
                buildPath(new LatLng(latitude,longitude));
            }
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

        SupportMapFragment mappa = new SupportMapFragment();
        FragmentTransaction transaction_map = getChildFragmentManager().beginTransaction();
        transaction_map.replace(R.id.mapContainer, mappa, "fragmentMap");
        transaction_map.commit();

        chronometer = (Chronometer) view.findViewById(R.id.chronometer);

        final Button buttonStop = (Button) view.findViewById(R.id.stopButton);
        buttonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                long distance = PolylineManager.getInstance().getDistance(); // service.getdistance
                //compute calories(assuming that the first factor is the average weight)
                double calories = 156*0.63*(distance/1000*1.60934);

                //operazioni sul db!
                listener.getDatabaseManager().insertStats(startTraining,calories
                        ,distance,Calendar.getInstance().getTimeInMillis() - startTraining.getTimeInMillis());

                listener.swapFragment(4
                ); // cosi quando ha startato l'activity per i dettagli
                //si ritrova gia nella sezione corrispondente

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

                        startTraining = Calendar.getInstance(); //inizio dell'allenamento
                        if ( !mService.isProviderEnabled(LocationManager.GPS_PROVIDER) ) {
                            buildAlertMessageNoGps();
                        }

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


        initialize();

        return view;
    }


    private static IntentFilter makeRunIntentFilter() {

        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MyRunService.BROADCAST_ACTION);
        return intentFilter;
    }


    private void initialize(){
        Intent i = new Intent(getActivity(),MyRunService.class);
        getActivity().startService(i);
        getActivity().bindService(i, mConnection, Context.BIND_AUTO_CREATE);
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
            map.addPolyline(PolylineManager.getInstance().getPolyline());
        }

    }

    @Override
    public void onResume()
    {
        super.onResume();
        mSimpleFacebook = SimpleFacebook.getInstance(getActivity());
        getActivity().registerReceiver(mReceiver,makeRunIntentFilter());
    }

    @Override
    public void onPause(){
        super.onPause();
        getActivity().unregisterReceiver(mReceiver);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        /**
         * Stopping service and clearing map resources
         */

        getActivity().unbindService(mConnection);
        mService.stopService();
        PolylineManager.getInstance().clear();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);//forward del risultato al login manager.
    }
}