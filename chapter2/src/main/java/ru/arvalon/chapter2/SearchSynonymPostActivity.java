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

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class SearchSynonymPostActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scheduling_work_layout);

        // Handler bound to the main Thread
        final Handler handler = new Handler();
        final TextView text = findViewById(R.id.text);

        // Creates an assync line of execution
        Thread thread = new Thread(){
            public void run(){
                final String result = searchSynonym("build");
                // Using the view post capabilities
                text.post(() -> text.setText(result));
            }};

        // Start the background thread with a lower priority
        thread.setPriority(Thread.MIN_PRIORITY);
        thread.start();

    }

    String searchSynonym(String toLook){
        return "fake ";
    }
}
