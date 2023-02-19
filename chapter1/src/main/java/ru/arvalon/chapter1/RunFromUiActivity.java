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
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class RunFromUiActivity extends AppCompatActivity {

    static int i=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.run_from_ui_activity_layout);

        final EditText word = findViewById(R.id.wordEt);

        Button search = findViewById(R.id.searchBut);
        search.setOnClickListener(v -> {

            Runnable searchTask = () -> {

                String result = searchSynomim(word.getText().toString());

                Log.d("AsyncAndroid", String.format("Searching for synonym for %s on %s",
                        word.getText(),Thread.currentThread().getName()));

                runOnUiThread(new SetSynonymResult(result));
            };

            Thread thread = new Thread(searchTask);
            thread.start();
        });
    }

     class SetSynonymResult implements Runnable {
        String synonym;

        SetSynonymResult(String synonym){
            this.synonym = synonym;
        }

        public void run() {
            Log.d("AsyncAndroid", String.format("Sending synonym result %s on %d",
                                    synonym,Thread.currentThread().getId()));

            TextView tv = findViewById(R.id.synonymTv);
            tv.setText(this.synonym);
        }
    };

    String searchSynomim(String word){
        return ++i % 2==0 ? "fake":"mock";
    }
}