package com.cubes.android.komentar.di;

import android.app.Application;


public class MyApplication extends Application {

    public AppContainer appContainer;

    @Override
    public void onCreate() {
        super.onCreate();

        appContainer = new AppContainer(getApplicationContext());

    }


}


