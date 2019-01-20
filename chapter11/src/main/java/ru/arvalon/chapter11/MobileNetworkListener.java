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

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;

public class MobileNetworkListener extends BroadcastReceiver {

    public MobileNetworkListener() {
        Log.d(MobileNetDetectionActivity.TAG,getClass().getSimpleName() +" constructor");
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d(MobileNetDetectionActivity.TAG, "Connectivity changed ");

        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if ( isMobileNetwork(context,networkInfo) && !networkInfo.isConnected()) {

            Log.d(MobileNetDetectionActivity.TAG, "No Mobile Network Available "+intent.toString());
            EventBus.getDefault().post(new MobileNetDisconnectedEvent());

        } else if ( isMobileNetwork(context,networkInfo) && networkInfo.isConnected()) {

            Log.d(MobileNetDetectionActivity.TAG," Mobile Network Available");
            EventBus.getDefault().post(new MobileNetConnectedEvent(networkInfo.getState().toString()));
        }
    }

    public boolean isMobileNetwork(Context context, NetworkInfo info) {
        return info!=null && (info.getType() == ConnectivityManager.TYPE_MOBILE);
    }
}
