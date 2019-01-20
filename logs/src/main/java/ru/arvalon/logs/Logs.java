package ru.arvalon.logs;

import android.util.Log;

/**
 * Логгирование в Logcat
 * @author Artem Vaganov
 */
public class Logs {

    public static final String TAG = "asyncbook";
    private static final String ERROR = "ERROR: ";
    private static final String SEPARATOR = ": ";
    private static final String THREAD = "Thread: ";

    public static void info(Object obj, String message) {

        if (BuildConfig.DEBUG){

            StringBuilder str = new StringBuilder();

            if (obj != null) str.append(obj.getClass().getSimpleName()).append(SEPARATOR);

            str.append(THREAD+ Thread.currentThread().getName()).append(SEPARATOR);

            str.append(message);

            Log.i(TAG, str.toString());
        }
    }

    public static void error(Object obj, String message, Exception ex) {

        if (BuildConfig.DEBUG){
            StringBuilder str = new StringBuilder();

            if (obj != null) str.append(obj.getClass().getName()).append(SEPARATOR);

            str.append(THREAD+ Thread.currentThread().getName()).append(SEPARATOR);

            str.append(ERROR).append(message);

            Log.e(TAG, str.toString());
            Log.d(TAG,str.toString());

            ex.printStackTrace();
        }
    }
}