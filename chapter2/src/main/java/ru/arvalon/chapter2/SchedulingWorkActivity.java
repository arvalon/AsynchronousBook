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
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SchedulingWorkActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scheduling_work_layout);

        Button postUIBut = findViewById(R.id.postUI);
        Button postBut = findViewById(R.id.post);
        Button postAtFrontBut = findViewById(R.id.postAtFront);

        postUIBut.setOnClickListener(postUIListener);
        postBut.setOnClickListener(postListener);
        postAtFrontBut.setOnClickListener(postAtFrontListener);

        TextView console = findViewById(R.id.title);
        console.setText("Chapter 2 - Scheduling Work");
    }

    View.OnClickListener postUIListener = v -> {
        final TextView myTextView = findViewById(R.id.myTv);
        Handler handler = new Handler(getMainLooper());

        handler.post(() -> {
            String result = processSomething();
            myTextView.setText(result);
            logOnConsole("Updating the UI from Runnable after processing...");
        });
    };

    View.OnClickListener postListener = v -> {
        Handler handler = new Handler(getMainLooper());
        handler.post(() -> {
            TextView text = findViewById(R.id.text);
            text.setText("Runnable updated the UI thread");
            logOnConsole("Updating the UI from Runnable");
        });
    };

    View.OnClickListener postAtFrontListener = v -> {
        Handler handler = new Handler(getMainLooper());
        handler.postAtFrontOfQueue(() -> {
            Log.i("SchedulingWorkActivity","Post at Front");
            logOnConsole("Posting Runnable at Queue front");
        });
    };

    private void logOnConsole(final String message) {
        runOnUiThread(() -> {
            TextView console = findViewById(R.id.consoleTv);
            console.setText(console.getText() + "\n" + message);
        });
    }



    String processSomething(){
        return "Hello World";
    }
}
