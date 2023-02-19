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
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.util.PrintWriterPrinter;
import android.widget.Button;
import android.widget.TextView;

import java.io.PrintWriter;

public class HandlerExampleActivity extends AppCompatActivity {

    public static final String TAG = HandlerExampleActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.handler_example_layout);

        TextView console = findViewById(R.id.title);
        console.setText("Chapter 2 - Multithread Handler");

        final WeatherPresenter presHandler = new WeatherPresenter();

        HandlerThread handlerThread = new HandlerThread("background",
                Process.THREAD_PRIORITY_URGENT_DISPLAY);

        handlerThread.start();

        final WeatherRetriever retHandler = new WeatherRetriever(
                handlerThread.getLooper(),presHandler);

        Button todayBut = findViewById(R.id.todayBut);
        todayBut.setOnClickListener(v -> retHandler.sendEmptyMessage(WeatherRetriever.GET_TODAY_FORECAST));

        retHandler.removeCallbacksAndMessages(null);
        retHandler.hasMessages(WeatherRetriever.GET_TODAY_FORECAST);

        //  retHandler.sendEmptyMessage(WeatherRetriever.GET_TODAY_FORECAST);

        PrintWriterPrinter out= new PrintWriterPrinter(new PrintWriter(System.out,true));
        handlerThread.getLooper().setMessageLogging(out);
    }

    public class WeatherPresenter extends Handler {

        public static final int TODAY_FORECAST = 1;
        public static final int TOMORROW_FORECAST = 2;

        @Override
        public void handleMessage(Message msg) {
            switch(msg.what) {
                case TODAY_FORECAST:
                    Log.i(TAG,"Getting weather for today");
                    readTodayWeather((String) msg.obj);
                    break;

                case TOMORROW_FORECAST:
                    Log.i(TAG,"Getting weather for today");
                    readTomorrowWeather((String) msg.obj);
                    break;
            }
        }

        private void readTodayWeather(String word) {

            Log.i(TAG,"Reading Today Forecast at Thread["
                    +Thread.currentThread().getName()+","+Thread.currentThread().getId()+"]");

            String message ="The Weather for today is "+word;
            Log.i(TAG,message);
            logOnConsole(message);
        }

        private void readTomorrowWeather(String sentence) {
            String message ="The Weather for tomorrow  at Thread["
                    +Thread.currentThread().getName()+","+Thread.currentThread().getId()+"]";
            Log.i(TAG,message);
            logOnConsole(message);

        }
    };

    void logOnConsole(final String message) {
        runOnUiThread(() -> {
            TextView console = findViewById(R.id.consoleTv);
            console.setText(console.getText() + "\n" + message);
        });
    }

    public static class WeatherRetriever extends Handler {

        private final Handler mainHandler;

        public static final int GET_TODAY_FORECAST = 1;
        public static final int GET_TOMORROW_FORECAST = 2;

        public WeatherRetriever(Looper looper,Handler mainHandler){
            super(looper);
            this.mainHandler = mainHandler;
        }

        String getForecast(){
            return "The weather will provide most of England and western Europe with " +
                    "the best viewing conditions across the continent for Sunday " +
                    "night’s rare supermoon lunar eclipse.";
        }

        @Override
        public void handleMessage(Message msg) {
            switch(msg.what) {
                case GET_TODAY_FORECAST:

                    Log.i("Multithread Handler",
                            "Retrieving Today Weather Forecast at Thread  Thread["
                                    +Thread.currentThread().getName()
                                    +","+Thread.currentThread().getId()+"]");

                    final String sentence = getForecast();

                    Message resultMsg = mainHandler.obtainMessage(
                            WeatherPresenter.TODAY_FORECAST,sentence);

                    this.mainHandler.sendMessage(resultMsg);
                    break;
            }
        }
    };
}
