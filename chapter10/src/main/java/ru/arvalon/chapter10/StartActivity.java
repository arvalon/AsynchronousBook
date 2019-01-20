package ru.arvalon.chapter10;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

/**
 * https://firebase.google.com/docs/cloud-messaging/
 */

public class StartActivity extends AppCompatActivity {

    public static final String TAG = "vga";

    public static final String TOPIC_NAME = "/topics/forum";
    public static final String MSG_DELIVERY = "asyncforum";

    public static final String USERNAME_KEY = "username";
    public static final String TEXT_KEY = "text";

    private TextView tvChat;
    private Button btnSend;
    private EditText edtSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        Log.d(TAG, getClass().getSimpleName()+" onCreate");

        StatFs statFs = new StatFs(Environment.getRootDirectory().getPath());
        Log.d(TAG, "Free space is " + (statFs.getAvailableBlocksLong() * statFs.getBlockSizeLong() / 1024) + " Kb");

        tvChat=findViewById(R.id.tv_chat);
        btnSend=findViewById(R.id.sendButton);
        edtSend=findViewById(R.id.msg);

        btnSend.setOnClickListener(v -> {
            String message = edtSend.getText().toString();
            Log.d(TAG, getClass().getSimpleName()+" send "+message);
            RemoteMessage.Builder rmBuilder = new RemoteMessage.Builder("1080883751253");
            rmBuilder.addData("my_key","my_value").setMessageId("messID").setMessageType("mes_type");

            FirebaseMessaging.getInstance().send(rmBuilder.build());
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, getClass().getSimpleName()+" onResume");
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int rc = apiAvailability.isGooglePlayServicesAvailable(getApplicationContext());

        if (rc== ConnectionResult.SUCCESS){
            Log.d(TAG, "Google Play Api Avaliable");
            FirebaseMessaging.getInstance().subscribeToTopic(TOPIC_NAME);

            IntentFilter filter = new IntentFilter(MSG_DELIVERY);

            LocalBroadcastManager.getInstance(getApplicationContext())
                    .registerReceiver(onMessageReceiver,filter);

        }else {
            Log.d(TAG, "Google Play Api NO Avaliable");
        }
    }

    public void printToken(View view) {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, getClass().getSimpleName()+" onPause");

        FirebaseMessaging.getInstance().unsubscribeFromTopic(TOPIC_NAME);

        LocalBroadcastManager.getInstance(getApplicationContext())
                .unregisterReceiver(onMessageReceiver);
    }

    BroadcastReceiver onMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String name = intent.getStringExtra(USERNAME_KEY);
            String text = intent.getStringExtra(TEXT_KEY);
            String line = name+" : "+text+"\n";

            tvChat.setText(line+" "+tvChat.getText().toString());
        }
    };
}