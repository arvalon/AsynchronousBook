package ru.arvalon.chapter10;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.preference.PreferenceManager;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;

public class MyBackupService extends JobService {

    private  static String FIRST_NAME="firstName";
    private  static String LAST_NAME="lastName";
    private  static String AGE="age";
    private  static String RESOURCE="/account";
    private  static String OPERATION="update";

    @Override
    public boolean onStartJob(JobParameters job) {

        final SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(this);

        Bundle data = new Bundle();
        data.putString(FIRST_NAME, sharedPreferences.getString(FIRST_NAME,""));
        data.putString(LAST_NAME, sharedPreferences.getString(LAST_NAME,""));
        data.putString(AGE, sharedPreferences.getString(AGE,""));
        data.putString("resource", RESOURCE);
        data.putString("operation", sharedPreferences.getString(OPERATION,""));
        String id = Integer.toString(new Random().nextInt());

        //RemoteMessage.Builder builder = new RemoteMessage.Builder("tag").
        //FirebaseMessaging.getInstance().send();

        return false;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        return false;
    }
}
