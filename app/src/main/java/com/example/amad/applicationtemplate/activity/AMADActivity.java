package com.example.amad.applicationtemplate.activity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.amad.applicationtemplate.app.AMADApplication;
import com.example.amad.applicationtemplate.dialog.AMADProgressDialogFragment;
import com.example.amad.applicationtemplate.service.AMADService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Vector;

import butterknife.ButterKnife;

/**
 * Created by btloc on 11/16/16.
 */

public abstract class AMADActivity extends AppCompatActivity{

    protected final Logger LOG = LoggerFactory.getLogger(this.getClass());

    @LayoutRes
    protected int getRootLayoutRes() {
        return 0;
    }

    protected View rootLayout;

    protected final boolean FORCE_SCREEN_ORIENTATION_PORTRAIT = true;

    protected final String TAG_PROGRESS_DIALOG = "progressDialog";

    protected AMADService amadService;

    protected List<ForegroundTaskDelegate> listOfForegroundTaskDelegates;

    public final AMADApplication getAMADApplication() {
        return (AMADApplication)getApplication();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (FORCE_SCREEN_ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        rootLayout = getLayoutInflater().inflate(getRootLayoutRes(), null);
        setContentView(rootLayout);

        ButterKnife.bind(this);

        amadService = getAMADApplication().getAmadService();
        listOfForegroundTaskDelegates = new Vector<>();
    }

    @Override
    protected void onDestroy() {
        for (ForegroundTaskDelegate delegate: listOfForegroundTaskDelegates) {
            if (delegate != null) {
                delegate.cancel();
            }
        }
        super.onDestroy();
    }

    public void showProgressDialog() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prevFrag = getSupportFragmentManager().findFragmentByTag(TAG_PROGRESS_DIALOG);
        if (prevFrag == null) {
            AMADProgressDialogFragment newFrag = AMADProgressDialogFragment.newInstance();
            ft.add(newFrag, TAG_PROGRESS_DIALOG);
        } else {
            ft.remove(prevFrag);
            getSupportFragmentManager().popBackStackImmediate();
        }
        ft.commitAllowingStateLoss();
    }

    public void dismissProgressDialog() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prevFrag = getSupportFragmentManager().findFragmentByTag(TAG_PROGRESS_DIALOG);
        if (prevFrag != null) {
            ft.remove(prevFrag);
            AMADProgressDialogFragment dlgFrag = (AMADProgressDialogFragment) prevFrag;
            if (dlgFrag.getDialog() != null && dlgFrag.getDialog().isShowing() && dlgFrag.isResumed()) {
                dlgFrag.dismissAllowingStateLoss();
                getSupportFragmentManager().popBackStackImmediate();
            }
        }
        ft.commitAllowingStateLoss();
    }

}
