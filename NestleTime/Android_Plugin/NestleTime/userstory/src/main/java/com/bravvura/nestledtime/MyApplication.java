package com.bravvura.nestledtime;

import android.app.Application;
import android.content.Context;

import com.bravvura.nestledtime.utils.CloudinaryManager;

/**
 * Created by Deepak Saini on 15-02-2018.
 */

public class MyApplication extends Application {
    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        initCloudinary();
        this.context = getApplicationContext();
    }

    private void initCloudinary() {
        CloudinaryManager.init(getApplicationContext());
    }
}
