package com.example.luca.firstprojectapp.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.luca.firstprojectapp.Interfaces.IOnActivityCallback;
import com.example.luca.firstprojectapp.R;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.login.widget.LoginButton;

/**
 * Created by Mary on 17/05/2015.
 */
public class ProfileFragment extends Fragment {
    private IOnActivityCallback listener;
    CallbackManager callbackManager;
    LoginButton loginButton;

//
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        container.removeAllViews();
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        View view = inflater.inflate(R.layout.profile_fragment_layout,container, false);
        Button edit = (Button) view.findViewById(R.id.editButton);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.manageUserProfile();
            }
        });
        loginButton = (LoginButton) view.findViewById(R.id.login_button);
        loginButton.setReadPermissions("user_friends");
        loginButton.setFragment(this);
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
}
