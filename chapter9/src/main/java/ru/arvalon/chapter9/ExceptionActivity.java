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
package ru.arvalon.chapter9;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.ByteBuffer;

public class ExceptionActivity extends Activity {

    static {
        //System.loadLibrary("c++_shared");
        //System.loadLibrary("mylib");
        System.loadLibrary("exception");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exception_layout);

        Button button = findViewById(R.id.genException);
        button.setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = v -> {
        try {
            ByteBuffer byteBuffer = ByteBuffer.allocate(8);
            genException(byteBuffer);
        } catch (RuntimeException e) {
            TextView console = findViewById(R.id.console);
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            console.setText(sw.toString());
        }
    };

    private native void genException(ByteBuffer buffer);
}
