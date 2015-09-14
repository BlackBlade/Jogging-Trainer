package com.example.luca.firstprojectapp.Services;

import android.app.Service;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Binder;
import android.os.IBinder;

import com.example.luca.firstprojectapp.Utilities.MyLocationListener;

public class MyRunService extends Service {

    private LocationManager locationManager;
    private MyLocationListener locationListener;


    private final IBinder mBinder = new LocalBinder();

    public MyRunService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    private void broarcastUpdate(String action, Intent i){
        sendBroadcast(i);
    }

    public class LocalBinder extends Binder {
        public MyRunService getService(){return MyRunService.this;}
    }

    public void setLocationManager(LocationManager manager){
        this.locationManager = manager;
    }

    public void start(){

    }

    public void stop(){
        stopSelf();
    }
}
