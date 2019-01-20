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
package ru.arvalon.chapter1;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ExecutorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.executor_activity_layout);

        TextView console = findViewById(R.id.title);
        console.setText("Chapter 1 - Executor");

        startWorking();
    }

    public void startWorking(){
        Executor executor = Executors.newFixedThreadPool(3);

        //Executor executor2 = Executors.newSingleThreadExecutor();

        for ( int i=0; i < 23; i++ ) {
            executor.execute(new MyRunnable());
        }
    }

    public class MyRunnable implements Runnable {

        public void run() {
            Log.d("Generic", "Running From Thread " + Thread.currentThread().getId());

            logOnConsole("Running From Thread " + Thread.currentThread().getId());

            SystemClock.sleep(1000);
        }
    }

    private void logOnConsole(final String message) {
        runOnUiThread(() -> {
            TextView console = findViewById(R.id.consoleTv);
            console.setText(console.getText() + "\n" + message);
        });
    }
}