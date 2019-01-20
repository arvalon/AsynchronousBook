package ru.arvalon.chapter10;

import android.os.AsyncTask;

public abstract class AsyncJob extends AsyncTask<Void,Void,Result<Void>> {

    @Override
    protected Result<Void> doInBackground(Void ...args) {

        Result<Void> result = new Result<>();
        try {
            runOnBackground();
        } catch (Throwable e) {
            result.error = e;
        }
        return result;
    }

    @Override
    protected void onPostExecute(Result<Void> result) {
        if ( result.error!=null ){
            onFailure(result.error);
        } else {
            onSuccess();
        }
    }

    abstract void runOnBackground() throws Exception;
    abstract void onFailure(Throwable e);
    abstract void onSuccess();
}
