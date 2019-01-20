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
package ru.arvalon.chapter5;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.util.LinkedList;
import java.util.Queue;

public class SaveMyLocationService extends Service {

    public static final String LOCATION_KEY = "location";

    boolean mShouldStop = false;

    Queue<String> mJobs = new LinkedList<>();

    Thread thread = new Thread() {
        @Override
        public void run() {
            super.run();
            while (!mShouldStop) {
                Log.i("MyStartedService", "Thread внутри блока while");
                String location = takeLocation(); // тут стоим из за wait'а коллекции
                if (location != null) {
                    Log.i("MyStartedService", "Thread location!=null");
                    consumeLocation(location);
                }
            }
        }
    };

    String takeLocation() {
        String location = null;
        synchronized (mJobs) {
            Log.i("MyStartedService", "takeLocation (внутри Thread'а) заблочили очередь");
            if (mJobs.isEmpty()) {
                try {
                    Log.i("MyStartedService", "takeLocation (внутри Thread'а) очередь пуста, ждём...");
                    mJobs.wait();
                    Log.i("MyStartedService", "takeLocation (внутри Thread'а) очередь стала не пуста, идём дальше");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            location = mJobs.poll();
        }
        return location;
    }

    void consumeLocation(String location){

        Log.i("MyStartedService", "Saving location : "
                                  + location
                                  + " on Thread"
                                  +  Thread.currentThread().getName() + " "
                                  + "[" + Thread.currentThread().getId() + "]");
        /*
         * The business logic of your service
         * will perform will be placed here
         */
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("MyStartedService", "Creating the Service ...");
        thread.start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        String location = intent.getStringExtra(LOCATION_KEY);
        synchronized (mJobs){
            Log.i("MyStartedService", "onStartCommand synchronized mJobs - добавили в коллекцию элемент");
            mJobs.add(location);
            mJobs.notify();
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.i("MyStartedService", "Destroying the Service ...");
        super.onDestroy();
        synchronized (mJobs){
            mShouldStop = true;
            mJobs.notify();
        }
        Log.i("MyStartedService", "Service stopped ...");

    }
}
