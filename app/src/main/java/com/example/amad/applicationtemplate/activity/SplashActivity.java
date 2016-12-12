package com.example.amad.applicationtemplate.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.amad.applicationtemplate.R;

public class SplashActivity extends AMADActivity {

    @Override
    protected int getRootLayoutRes() {
        return R.layout.activity_splash;
    }

    private LoadDataDelegate loadDataDelegate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loadDataDelegate = new LoadDataDelegate(this);
        listOfForegroundTaskDelegates.add(loadDataDelegate);

        amadService.loadData(loadDataDelegate);
    }

    private void gotoMainScreen() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private static class LoadDataDelegate extends ForegroundTaskDelegate<Integer> {

        public LoadDataDelegate(AMADActivity activity) {
            super(activity);
        }

        @Override
        public void onPostExecute(Integer result, Throwable throwable) {
            super.onPostExecute(result, throwable);

            if (throwable == null && shouldHandleResultForActivity()) {
                if (result == 1) {
                    SplashActivity activity = (SplashActivity)activityWeakReference.get();
                    activity.gotoMainScreen();
                }
            }
        }
    }


}
