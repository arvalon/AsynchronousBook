package ru.arvalon.asynchronousbook;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ru.arvalon.chapter1.ExecutorActivity;
import ru.arvalon.chapter1.RunFromUiActivity;
import ru.arvalon.chapter11.LocationActivity;
import ru.arvalon.chapter11.MobileNetDetectionActivity;
import ru.arvalon.chapter11.PaginatedActivity;
import ru.arvalon.chapter12.HelloRxJava;
import ru.arvalon.chapter12.RxListActivity;
import ru.arvalon.chapter12.RxSchedulerActivity;
import ru.arvalon.chapter12.ZipActivity;
import ru.arvalon.chapter2.CancelRunnableActivity;
import ru.arvalon.chapter2.HandlerExampleActivity;
import ru.arvalon.chapter2.SchedulingWorkActivity;
import ru.arvalon.chapter2.SpeakActivity;
import ru.arvalon.chapter3.MyPuppyAlbumActivity;
import ru.arvalon.chapter3.ShowMyPuppyActivity;
import ru.arvalon.chapter3.ShowMyPuppyHeadlessActivity;
import ru.arvalon.chapter4.AlbumListActivity;
import ru.arvalon.chapter4.AlbumListActivitySimple;
import ru.arvalon.chapter4.BitcoinExchangeRateActivity;
import ru.arvalon.chapter4.WhoIsOnlineActivity;
import ru.arvalon.chapter5.CountMsgsFromActivity;
import ru.arvalon.chapter5.GrepActivity;
import ru.arvalon.chapter5.SaveMyLocationActivity;
import ru.arvalon.chapter5.Sha1Activity;
import ru.arvalon.chapter5.Sha1BroacastActivity;
import ru.arvalon.chapter5.Sha1BroadcastUnhActivity;
import ru.arvalon.chapter5.UploadArtworkActivity;
import ru.arvalon.chapter6.AlarmActivity;
import ru.arvalon.chapter6.AlarmClockActivity;
import ru.arvalon.chapter6.RepeatingAlarmActivity;
import ru.arvalon.chapter6.SMSDispatchActivity;
import ru.arvalon.chapter7.AccountInfoActivity;
import ru.arvalon.chapter7.JobListActivity;
import ru.arvalon.chapter8.GreetingsActivity;
import ru.arvalon.chapter8.HelloSSLActivity;
import ru.arvalon.chapter8.UserInfoActivity;
import ru.arvalon.chapter8.UserListActivity;
import ru.arvalon.chapter9.ConvertGrayScaleActivity;
import ru.arvalon.chapter9.ExceptionActivity;
import ru.arvalon.chapter9.MyNativeActivity;
import ru.arvalon.chapter9.NativeThreadsActivity;
import ru.arvalon.chapter9.StatsActivity;

public class MainActivity extends AppCompatActivity {

    List<Example> examples = new ArrayList<>();

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Android Asynchronous Programming");
        }

        populateList();

        mRecyclerView = findViewById(R.id.exampleList);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new ExampleAdapter(this,examples);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void populateList() {
        examples.add(new Example("Chap.1 - Executor", ExecutorActivity.class));
        examples.add(new Example("Chap.1 - Run from UI", RunFromUiActivity.class));

        examples.add(new Example("Chap.2 - Scheduling Work", SchedulingWorkActivity.class));
        examples.add(new Example("Chap.2 - Cancelling Work", CancelRunnableActivity.class));
        examples.add(new Example("Chap.2 - Handler Example", SpeakActivity.class));
        examples.add(new Example("Chap.2 - Multi-thread Handler", HandlerExampleActivity.class));

        examples.add(new Example("Chap.3 - Download AsyncTask (one)", ShowMyPuppyActivity.class));
        examples.add(new Example("Chap.3 - Download AsyncTask", MyPuppyAlbumActivity.class));
        examples.add(new Example("Chap.3 - Headless Fragment", ShowMyPuppyHeadlessActivity.class));

        examples.add(new Example("Chap.4 - Loading Data with Loader", WhoIsOnlineActivity.class));
        examples.add(new Example("Chap.4 - AsyncTaskLoader", BitcoinExchangeRateActivity.class));
        examples.add(new Example("Chap.4 - CursorLoader", AlbumListActivitySimple.class));
        examples.add(new Example("Chap.4 - CursorLoader (no simple)", AlbumListActivity.class));

        examples.add(new Example("Chap.5 - Started Service", SaveMyLocationActivity.class));
        examples.add(new Example("Chap.5 - Intent Service", CountMsgsFromActivity.class));
        examples.add(new Example("Chap.5 - Upload Image Service", UploadArtworkActivity.class));
        examples.add(new Example("Chap.5 - Bound Service", Sha1Activity.class));
        examples.add(new Example("Chap.5 - Bound Service (Broacast)", Sha1BroacastActivity.class));
        examples.add(new Example("Chap.5 - Bound Service (Broadcast Unh)", Sha1BroadcastUnhActivity.class));
        examples.add(new Example("Chap.5 - Remote Service", GrepActivity.class));

        examples.add(new Example("Chap.6 - Scheduling Alarm", AlarmActivity.class));
        examples.add(new Example("Chap.6 - Alarm Clock", AlarmClockActivity.class));
        examples.add(new Example("Chap.6 - Repeating Alarm", RepeatingAlarmActivity.class));
        examples.add(new Example("Chap.6 - Wake up Alarm", SMSDispatchActivity.class));

        examples.add(new Example("Chap.7 - Schedule Job", AccountInfoActivity.class));
        examples.add(new Example("Chap.7 - Cancel Jobs", JobListActivity.class));

        examples.add(new Example("Chap.8 - Retrieve Remote Text", GreetingsActivity.class));
        examples.add(new Example("Chap.8 - Retrieve JSON", UserListActivity.class));
        examples.add(new Example("Chap.8 - Retrieve XML", UserInfoActivity.class));
        examples.add(new Example("Chap.8 - Connect using SSL", HelloSSLActivity.class));

        examples.add(new Example("Chap.9 - Calling Native Functions", MyNativeActivity.class));
        examples.add(new Example("Chap.9 - Background Work on Native", ConvertGrayScaleActivity.class));
        examples.add(new Example("Chap.9 - Native Threads", NativeThreadsActivity.class));
        examples.add(new Example("Chap.9 - Wrapping Native Objects", StatsActivity.class));
        examples.add(new Example("Chap.9 - Exception Handling", ExceptionActivity.class));

        examples.add(new Example("Chap.11 - Post Event", MobileNetDetectionActivity.class));
        examples.add(new Example("Chap.11 - Post Sticky Event", LocationActivity.class));
        examples.add(new Example("Chap.11 - Delivery Mode", PaginatedActivity.class));

        examples.add(new Example("Chap.12 - Hello RxJava", HelloRxJava.class));
        examples.add(new Example("Chap.12 - Debounce Operator", RxListActivity.class));
        examples.add(new Example("Chap.12 - Combining Operator", ZipActivity.class));
        examples.add(new Example("Chap.12 - Rx Scheduler", RxSchedulerActivity.class));
    }
}