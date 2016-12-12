package com.example.amad.applicationtemplate.app;

import android.app.Application;

import com.example.amad.applicationtemplate.BuildConfig;
import com.example.amad.applicationtemplate.service.AMADService;
import com.example.amad.applicationtemplate.service.AMADServiceImpl;
import com.squareup.leakcanary.LeakCanary;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;

/**
 * Created by btloc on 11/16/16.
 */

public class AMADApplication extends Application {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    private AMADService amadService;

    @Override
    public void onCreate() {
        super.onCreate();

        // This process is dedicated to LeakCanary for heap analysis.
        // You should not init your app in this process.
        setupLeakCanary();

        // Normal app init code...
        setupLogLevel();
        setupMainSettings();
    }


    public static Boolean isInDebugMode() {
        return BuildConfig.DEBUG;
    }

    private void setupLeakCanary() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        LeakCanary.install(this);
    }

    private void setupLogLevel() {
        ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger) LoggerFactory
                .getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
        Level logLevel = Level.INFO;
        if (isInDebugMode()) {

            logLevel = Level.TRACE;
        }
        logger.setLevel(logLevel);
        LOG.info("Set log level to " + logLevel);
    }


    private void setupMainSettings() {
        amadService = new AMADServiceImpl(this);
    }

    public AMADService getAmadService() {
        return amadService;
    }

}
