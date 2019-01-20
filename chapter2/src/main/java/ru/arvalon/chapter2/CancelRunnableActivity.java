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
package ru.arvalon.chapter2;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

public class CancelRunnableActivity  extends AppCompatActivity {

    final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cancel_work_layout);

        Button postBut = findViewById(R.id.post);
        Button cancelBut = findViewById(R.id.cancel);
        postBut.setOnClickListener(postListener);
        cancelBut.setOnClickListener(cancelListener);
    }

    final Runnable runnable = () -> {
        // ... do some work
        Log.i("CancelRunnableActivity","Running the job");
        logOnConsole("Running the job");
    };

    View.OnClickListener postListener = v -> {
        logOnConsole("Posting a new job");
        handler.postDelayed(runnable, TimeUnit.SECONDS.toMillis(5));
    };

    View.OnClickListener cancelListener = v -> {
        Button cancel = findViewById(R.id.cancel);
        cancel.setOnClickListener(v1 -> {
            logOnConsole("Cancelling the job");
            handler.removeCallbacks(runnable);
        });
    };



    private void logOnConsole(final String message) {
        runOnUiThread(() -> {
            TextView console = findViewById(R.id.consoleTv);
            console.setText(console.getText() + "\n" + message);
        });
    }
}
