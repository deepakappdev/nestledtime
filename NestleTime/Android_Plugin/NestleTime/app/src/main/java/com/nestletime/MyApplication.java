package com.nestletime;

import android.app.Application;

import com.cloudinary.Configuration;
import com.cloudinary.android.MediaManager;
import com.nestletime.utils.CloudinaryManager;

import java.util.HashMap;
import java.util.Map;

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
