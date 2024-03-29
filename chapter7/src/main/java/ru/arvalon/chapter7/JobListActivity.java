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

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.os.Build;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class JobListActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.job_list_activity);
        RecyclerView rv = findViewById(R.id.jobList);

        // Set the recycler view layout
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);

        initList();

        Button cancelAllBut = findViewById(R.id.cancelAllBut);

        cancelAllBut.setOnClickListener(v -> {
            JobScheduler jobScheduler = (JobScheduler)getSystemService(JOB_SCHEDULER_SERVICE);
            jobScheduler.cancelAll();

            initList();

            Toast.makeText(JobListActivity.this,
                    "Cancelling all the pending jobs",
                    Toast.LENGTH_LONG).show();
        });
    }

    void initList(){

        RecyclerView rv = findViewById(R.id.jobList);

        // Get the list of scheduled jobs
        JobScheduler jobScheduler = (JobScheduler)getSystemService(JOB_SCHEDULER_SERVICE);
        List<JobInfo> jobList = jobScheduler.getAllPendingJobs();

        // Initialize the adapter job list
        JobListRecyclerAdapter adapter = new JobListRecyclerAdapter(this,jobList);
        rv.setAdapter(adapter);

        TextView jobCountTv = findViewById(R.id.jobCount);
        jobCountTv.setText(Integer.toString(jobList.size()));
    }
}
