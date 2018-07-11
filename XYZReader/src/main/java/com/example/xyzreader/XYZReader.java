package com.example.xyzreader;

import com.example.xyzreader.di.DaggerAppComponent;

import dagger.android.AndroidInjector;
import dagger.android.support.DaggerApplication;
import timber.log.Timber;

public class XYZReader extends DaggerApplication {

    @Override
    public void onCreate() {
        super.onCreate();
//        deleteDatabase(ApplicationModule.DB_NAME); // uncomment to delete database on startup for testing purposes
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }


    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        return DaggerAppComponent.builder().create(this);
    }
}
