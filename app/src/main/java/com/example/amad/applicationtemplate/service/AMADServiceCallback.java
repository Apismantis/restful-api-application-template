package com.example.amad.applicationtemplate.service;

import android.os.AsyncTask;

import retrofit2.Call;

/**
 * Created by btloc on 11/25/16.
 */

public interface AMADServiceCallback <Result extends Object> {

    void setAsyncTask(AsyncTask task);

    void setCall(Call call);

    void cancel();

    void onPreExecute();

    void onPostExecute(Result result, Throwable throwable);

}