package com.example.amad.applicationtemplate.activity;

import android.os.AsyncTask;
import android.widget.Toast;

import com.example.amad.applicationtemplate.service.AMADServiceCallback;

import java.lang.ref.WeakReference;

import retrofit2.Call;

/**
 * Created by btloc on 12/2/16.
 */

public class ForegroundTaskDelegate<Result extends Object> implements AMADServiceCallback<Result> {

    protected final WeakReference<AMADActivity> activityWeakReference;
    private AsyncTask task;
    private Call call;

    private ForegroundTaskDelegate() {
        // Don't allow default constructor outside
        activityWeakReference = new WeakReference<AMADActivity>(null);
    }

    public ForegroundTaskDelegate(AMADActivity activity) {
        activityWeakReference = new WeakReference<AMADActivity>(activity);
    }

    @Override
    public void setAsyncTask(AsyncTask task) {
        cancelAsyncTask();
        this.task = task;
    }

    @Override
    public void setCall(Call call) {
        cancelCall();
        this.call = call;
    }

    @Override
    public void cancel() {
        cancelAsyncTask();
        cancelCall();
    }

    protected void cancelAsyncTask() {
        if (task != null && !task.isCancelled()) {
            task.cancel(true);
        }
    }

    protected void cancelCall() {
        if (call != null && !call.isCanceled()) {
            call.cancel();
        }
    }

    protected boolean shouldHandleResultForActivity() {
        AMADActivity activity = activityWeakReference.get();
        if (activity != null && !activity.isDestroyed() && !activity.isFinishing())
            return true;
        return false;
    }

    protected void showProgress() {
        if (shouldHandleResultForActivity()) {
            AMADActivity activity = activityWeakReference.get();
            activity.showProgressDialog();
        }
    }

    protected void dismissProgress() {
        if (shouldHandleResultForActivity()) {
            AMADActivity activity = activityWeakReference.get();
            activity.dismissProgressDialog();
        }
    }

    @Override
    public void onPreExecute() {
        showProgress();
    }

    @Override
    public void onPostExecute(Result result, Throwable throwable) {
        dismissProgress();

        if (throwable != null) {
            if (shouldHandleResultForActivity()) {
                AMADActivity activity = activityWeakReference.get();
                Toast.makeText(activity, throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

}
