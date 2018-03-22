package com.bravvura.nestledtime;

import android.app.Application;
import android.content.Context;

import com.bravvura.nestledtime.utils.CloudinaryManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.MemoryCategory;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;

/**
 * Created by Deepak Saini on 15-02-2018.
 */

public class MyApplication extends Application {
    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        initCloudinary(getApplicationContext());
        initGlide(getApplicationContext());
        this.context = getApplicationContext();
    }

    private void initGlide(Context context) {
        GlideBuilder builder = new GlideBuilder(context);
        int maxSize50MB = 1024*1024*50;
        builder.setDiskCache(new InternalCacheDiskCacheFactory(context, maxSize50MB));
    }

    private void initCloudinary(Context context) {
        CloudinaryManager.init(context);
    }
}
