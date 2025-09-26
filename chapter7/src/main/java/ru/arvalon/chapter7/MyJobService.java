package ru.arvalon.chapter7;

import android.annotation.TargetApi;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.media.AudioManager;
import android.os.Build;
import android.text.format.Time;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class MyJobService extends JobService {

    static final String LOGTAG = "SyncJobService";

    static final String LOGFILENAME = "logfile.txt";

    AudioManager audioManager;

    @Override
    public boolean onStartJob(JobParameters params) {

        Log.i(LOGTAG,"onStartJob 1");

        audioManager = (AudioManager) getApplicationContext().getSystemService(AUDIO_SERVICE);
        audioManager.adjustVolume(AudioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND);

        File logFile = new File(getFilesDir(), LOGFILENAME);

        try (OutputStream oStream = openFileOutput(logFile.getName(), Context.MODE_APPEND);) {

            Time time = new Time();
            time.setToNow();
            oStream.write(time.toString().getBytes());
            oStream.write("\n".getBytes());
            oStream.flush();
            oStream.close();
            Log.i(LOGTAG, "Запись в файл закончена");
            return true;

        } catch (IOException e) {
            Log.i(LOGTAG, "Ошибка записи в файл: " + e);
        }

        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {

        audioManager = null;
        Log.i(LOGTAG,"onStopJob");
        return true;
    }
}
