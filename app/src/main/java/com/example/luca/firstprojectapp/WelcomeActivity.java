package com.example.luca.firstprojectapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

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
 * Created by Mary on 28/05/2015.
 */
public class WelcomeActivity extends ActionBarActivity {

    private Boolean exit = false;
    private SharedPreferences myPreferences;
    private CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;
    private LoginButton loginButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.welcome_activity_layout);
        myPreferences = getSharedPreferences("pref", Context.MODE_PRIVATE);
        loginButton = (LoginButton) findViewById(R.id.first_login_button);
        loginButton.setReadPermissions("user_friends");
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
              //  callback.onSuccess(loginResult);
                SharedPreferences.Editor editor = myPreferences.edit();
                editor.putBoolean("logged", true);
                editor.apply();
                Intent intent = new Intent(WelcomeActivity.this,MainActivity.class);
                startActivity(intent);
                finish();

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

                if(newToken != null) {
                    Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                    SharedPreferences.Editor editor = myPreferences.edit();
                    editor.putBoolean("logged", true);
                    editor.apply();
                    startActivity(intent);
                   // finish();

                }

            }
        };

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {
                    if (newProfile != null) {
                        Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        SharedPreferences.Editor editor = myPreferences.edit();
                        editor.putBoolean("logged", true);
                        editor.apply();
                        startActivity(intent);
                    }

            }
        };

        accessTokenTracker.startTracking();
        profileTracker.startTracking();


    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        return super.onCreateView(parent, name, context, attrs);

    }

    @Override
        public void onBackPressed() {
            if (exit) {
                finish(); // finish activity
            } else {
                Toast.makeText(this, "Press Back again to Exit.",
                        Toast.LENGTH_SHORT).show();
                exit = true;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        exit = false;
                    }
                }, 3 * 1000);

            }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);//forward del risultato
    }

}
