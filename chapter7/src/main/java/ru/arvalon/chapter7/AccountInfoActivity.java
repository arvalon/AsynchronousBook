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
package ru.arvalon.chapter7;

import static ru.arvalon.chapter7.MyJobService.LOGTAG;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.concurrent.TimeUnit;

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class AccountInfoActivity extends Activity {

    public static final int SYNC_JOB_ID = "SyncJobService".hashCode();
    public static final int SYNC_PER_JOB_ID = "SyncJobPerService".hashCode();

    private static final String SYNC_FILE = "account.json";
    private static final String SYNC_PATH = "account_sync";

    public static final String FIRST_NAME_KEY = "firstName";
    public static final String SURNAME_KEY = "surname";
    public static final String LANGUAGE_KEY = "language";
    public static final String EMAIL_KEY = "email";
    public static final String TELEPHONE_KEY = "telephone";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_info_activity);

        loadAccountInfo();

        Button saveBut = findViewById(R.id.saveBut);
        saveBut.setOnClickListener(v -> saveAccountInfo());

        Button syncBut = findViewById(R.id.syncBut);
        syncBut.setOnClickListener(v -> setupJob(false));

        Button syncPerBut = findViewById(R.id.syncPeriodically);
        syncPerBut.setOnClickListener(v -> setupJob(true));

        Button but = findViewById(R.id.jobListBut);

        but.setOnClickListener(v -> {

            Intent intent = new Intent(AccountInfoActivity.this, JobListActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
    }

    void loadAccountInfo() {

        EditText firstNameEv = findViewById(R.id.firstNameEv);
        EditText surnameEv = findViewById(R.id.surnameEv);
        EditText languageEv = findViewById(R.id.languageEv);
        EditText emailEv = findViewById(R.id.emailEv);
        EditText telephoneEv = findViewById(R.id.telephoneEv);

        try {
            JSONObject obj = new JSONObject(Util.loadJSONFromFile(this, "account.json"));
            firstNameEv.setText(obj.getString(FIRST_NAME_KEY));
            surnameEv.setText(obj.getString(SURNAME_KEY));
            languageEv.setText(obj.getString(LANGUAGE_KEY));
            emailEv.setText(obj.getString(EMAIL_KEY));
            telephoneEv.setText(obj.getString(TELEPHONE_KEY));

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    void setupJob(boolean isPeriodic) {
        PersistableBundle bundle = new PersistableBundle();
        bundle.putString(SyncTask.SYNC_FILE_KEY, SYNC_FILE);
        bundle.putString(SyncTask.SYNC_PATH_KEY, SYNC_PATH);

        ComponentName serviceName = new ComponentName(this, AccountBackupJobService.class);

        ComponentName serviceName2 = new ComponentName(this, MyJobService.class);

        JobInfo.Builder builder = null;

        if (isPeriodic) {
            builder = new JobInfo.Builder(SYNC_PER_JOB_ID, serviceName);
            builder.setPersisted(true);
            builder.setPeriodic(5000);

        } else {
            builder = new JobInfo.Builder(SYNC_JOB_ID, serviceName);
            builder.setOverrideDeadline(5000);
        }

        builder.setRequiresCharging(true)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                .setRequiresDeviceIdle(true)
                .setExtras(bundle);

        JobInfo.Builder builder2 = null;

        if (isPeriodic) {
            builder2 = new JobInfo.Builder(SYNC_PER_JOB_ID, serviceName2);
            builder2.setPersisted(true);
            builder2.setPeriodic(5000);

        } else {
            builder2 = new JobInfo.Builder(SYNC_JOB_ID, serviceName2);
            builder2.setOverrideDeadline(5000);
        }

        builder2.setRequiresCharging(true)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                .setRequiresDeviceIdle(true)
                .setExtras(bundle);

        JobScheduler jobScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        int result = jobScheduler.schedule(builder.build());

        if ( result != JobScheduler.RESULT_SUCCESS) {
            Log.i("SharedPref", "Failed to Setup a Job");
            Toast.makeText(AccountInfoActivity.this,
                    "Failed to Setup a SharedPrefBack job",
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(AccountInfoActivity.this,
                    "SharedPrefBack job successfully scheduled",
                    Toast.LENGTH_SHORT).show();
        }

        int result2 = jobScheduler.schedule(builder2.build());

        if ( result2 == JobScheduler.RESULT_SUCCESS) {
            Log.d(LOGTAG, "JobScheduler RESULT_SUCCESS");
            Toast.makeText(this, "JobScheduler RESULT_SUCCESS", Toast.LENGTH_SHORT).show();
        } else {
            Log.d(LOGTAG, "JobScheduler RESULT_FAILURE");
            Toast.makeText(this, "JobScheduler RESULT_FAILURE", Toast.LENGTH_SHORT).show();
        }
    }

    void saveAccountInfo() {

        EditText firstNameEv = findViewById(R.id.firstNameEv);
        EditText surnameEv = findViewById(R.id.surnameEv);
        EditText languageEv = findViewById(R.id.languageEv);
        EditText emailEv = findViewById(R.id.emailEv);
        EditText telephoneEv = findViewById(R.id.telephoneEv);

        try {
            JSONObject obj = new JSONObject();
            obj.put(FIRST_NAME_KEY, firstNameEv.getText().toString());
            obj.put(SURNAME_KEY, surnameEv.getText().toString());
            obj.put(LANGUAGE_KEY, languageEv.getText().toString());
            obj.put(EMAIL_KEY, emailEv.getText().toString());
            obj.put(TELEPHONE_KEY, telephoneEv.getText().toString());
            String content = obj.toString();
            FileWriter fw = new FileWriter(this.getFilesDir() + "/account.json");
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(content);
            bw.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}
