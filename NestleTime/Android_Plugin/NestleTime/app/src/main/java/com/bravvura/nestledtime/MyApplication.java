package com.bravvura.nestledtime;

import android.app.Application;

import com.bravvura.nestledtime.utils.CloudinaryManager;

/**
 * Created by Deepak Saini on 15-02-2018.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        initCloudinary();
    }

    private void initCloudinary() {
        CloudinaryManager.init(getApplicationContext());

    }
}
