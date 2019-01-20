package ru.arvalon.chapter10;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;

public class AccountSettingsActivity extends AppCompatActivity {

    public static final String TAG = AccountSettingsActivity.class.getSimpleName();
    public static final String TASK_BACKUP = "backup";
    public static final String TASK_PERIODIC_BACKUP = "periodic_backup";

    TextView firstName;
    TextView lastName ;
    TextView age ;
    Button button;
    Button perButton;
    Button cancelButton;

    public static long ONE_HOUR = 60L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.backup_activity);

        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(AccountSettingsActivity.this);

        firstName = findViewById(R.id.firstName);
        firstName.setText(sharedPreferences.getString("firstName", ""));

        lastName = findViewById(R.id.lastName);
        lastName.setText(sharedPreferences.getString("lastName",""));

        age = findViewById(R.id.age);
        age.setText(sharedPreferences.getString("age",""));

        button = findViewById(R.id.save);
        button.setOnClickListener(listener);

        perButton = findViewById(R.id.periodic);
        perButton.setOnClickListener(startPeriodic);

        cancelButton = findViewById(R.id.cancel_periodic);
        cancelButton.setOnClickListener(cancelListener);
    }

    View.OnClickListener listener = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            SharedPreferences sharedPreferences =
                    PreferenceManager.getDefaultSharedPreferences(AccountSettingsActivity.this);

            sharedPreferences.edit().putString("firstName", firstName.getText().toString()).apply();
            sharedPreferences.edit().putString("lastName", lastName.getText().toString()).apply();
            sharedPreferences.edit().putString("age",age.getText().toString()).apply();

            FirebaseJobDispatcher dispatcher =
                    new FirebaseJobDispatcher(new GooglePlayDriver(getApplicationContext()));

            Job job = dispatcher.newJobBuilder()
                    .setService(MyBackupService.class)
                    .setTag(TASK_BACKUP)
                    .setConstraints(Constraint.DEVICE_CHARGING, Constraint.ON_UNMETERED_NETWORK)
                    .setTrigger(Trigger.executionWindow(0,60*60))
                    .setReplaceCurrent(true).build();

            dispatcher.mustSchedule(job);
        }
    };

    View.OnClickListener startPeriodic = v -> {

        FirebaseJobDispatcher dispatcher
                = new FirebaseJobDispatcher(new GooglePlayDriver(getApplicationContext()));

        Job periodicJob = dispatcher.newJobBuilder()
                .setService(MyBackupService.class)
                .setTag(TASK_PERIODIC_BACKUP)
                .setRetryStrategy(RetryStrategy.DEFAULT_LINEAR)
                .setLifetime(Lifetime.UNTIL_NEXT_BOOT).build();

        dispatcher.mustSchedule(periodicJob);
    };


    View.OnClickListener cancelListener = v -> {

        FirebaseJobDispatcher dispatcher =
                new FirebaseJobDispatcher(new GooglePlayDriver(getApplicationContext()));
        dispatcher.cancel(TASK_BACKUP);
    };
}
