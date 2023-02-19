/*
 *  This code is part of "Asynchronous Android Programming".
 *
 *  Copyright (C) 2016 Helder Vasconcelos (heldervasc@bearstouch.com)
 *  Copyright (C) 2016 Packt Publishing
 *
 *  Permission is hereby granted, free of charge, to any person obtaining
 *  a copy of this software and associated documentation files (the
 *  "Software"), to deal in the Software without restriction, including
 *  without limitation the rights to use, copy, modify, merge, publish,
 *  distribute, sublicense, and/or sell copies of the Software, and to
 *  permit persons to whom the Software is furnished to do so, subject to
 *  the following conditions:
 *  
 */
package ru.arvalon.chapter11;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class MobileNetDetectionActivity extends Activity{

    public static final String TAG = "vga";

    BroadcastReceiver receiver = new MobileNetworkListener();

    IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wifi_detection_layout);

        Log.d(TAG,getClass().getSimpleName() +" onCreate");

        TextView console = findViewById(R.id.title);
        console.setText("Chapter 11 - Mobile Net Monitor");
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d(TAG,getClass().getSimpleName() +" onResume");

        registerReceiver(receiver,filter);
    }

    @Override
    protected void onPause() {
        super.onPause();

        unregisterReceiver(receiver);
    }

    @Subscribe
    public void onMobileNetDisconnectedEvent(MobileNetDisconnectedEvent event){

        String message = String.format("Mobile connection is not available ");
        appendToConsole(message);
    }

    @Subscribe
    public void onMobileNetConnectedEvent(MobileNetConnectedEvent event){

        String message = String.format(
                "%d Mobile connection is available State - %s\n",
                System.currentTimeMillis(),event.getDetailedState());

        appendToConsole(message);
    }

    void appendToConsole(String message){
        TextView console =  findViewById(R.id.console);
        console.setText( console.getText() + message);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);

    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();

    }

    public void statusCheck(View view) {

        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if(networkInfo != null)
        {
            if(networkInfo.isConnected())
            {
                switch (networkInfo.getType())
                {
                    case ConnectivityManager.TYPE_MOBILE:
                        Log.d(TAG, "Mobile");
                        break;
                    case ConnectivityManager.TYPE_WIFI:
                        Log.d(TAG, "Wi-Fi");
                        break;
                    default:
                        Log.d(TAG, "Other");
                        break;
                }

            } else {
                Log.d(TAG, "networkInfo.isConnected() false");
            }

        } else {
            Log.d(TAG, "networkInfo NULL");
        }
    }
}
