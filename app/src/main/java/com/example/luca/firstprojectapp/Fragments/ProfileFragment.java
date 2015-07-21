package com.example.luca.firstprojectapp.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.luca.firstprojectapp.DownloadImageTask;
import com.example.luca.firstprojectapp.Interfaces.IOnActivityCallback;
import com.example.luca.firstprojectapp.R;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

/**
 * Created by Marina Londei on 17/05/2015.
 */
public class ProfileFragment extends Fragment{

    private IOnActivityCallback listener;
    private CallbackManager callbackManager;
    private LoginButton loginButton;
    private AccessTokenTracker accessTokenTracker; //per tenere traccia del profilo e ottenere informazioni
    private ProfileTracker profileTracker;
    private TextView nameText;
    private TextView surnameText;
    private ImageView profilePic;
    private DownloadImageTask download;
    private View view;
    private SharedPreferences myPreferences;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        container.removeAllViews();
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        view = inflater.inflate(R.layout.profile_fragment_layout, container, false);
        myPreferences = getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE);
        nameText = (TextView) view.findViewById(R.id.name); //setting della textView
        surnameText = (TextView) view.findViewById(R.id.surname);
        profilePic = (ImageView) view.findViewById(R.id.imageView);

        loginButton = (LoginButton) view.findViewById(R.id.login_button);
        loginButton.setReadPermissions("user_friends");
        loginButton.setFragment(this);
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
            public void onSuccess(LoginResult loginResult) {

        }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException e) {
            }
        });

        accessTokenTracker= new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldToken, AccessToken newToken) {
                if(newToken==null) {
                    nameText.setText("");
                    surnameText.setText("");
                    profilePic.setImageBitmap(null);
                    SharedPreferences.Editor editor = myPreferences.edit();
                    editor.putBoolean("logged",false);
                    editor.apply();
                    listener.endActivity();

                }

            }
        };

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {
                if (myPreferences.getBoolean("logged",false)){
                    displayMessage(newProfile);
                }

            }
        };

        accessTokenTracker.startTracking();
        profileTracker.startTracking();

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);//forward del risultato al login manager.
    }


    private void displayMessage(Profile profile){
        if(profile != null){

            nameText.setText(profile.getFirstName());
            surnameText.setText(profile.getLastName());
            download = new DownloadImageTask(profilePic);
            download.execute(""+profile.getProfilePictureUri(100,100));
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        accessTokenTracker.stopTracking();
        profileTracker.stopTracking();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        accessTokenTracker.stopTracking();
        profileTracker.stopTracking();
    }

    @Override
    public void onResume() {
        super.onResume();
        Profile profile = Profile.getCurrentProfile();
        accessTokenTracker.startTracking();
        profileTracker.startTracking();
        displayMessage(profile);
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



}
