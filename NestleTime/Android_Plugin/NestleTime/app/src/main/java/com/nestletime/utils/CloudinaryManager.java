package com.nestletime.utils;

import android.content.Context;

import com.cloudinary.Configuration;
import com.cloudinary.android.MediaManager;

/**
 * Created by Deepak Saini on 15-02-2018.
 */

public class CloudinaryManager {
    public static void init(Context context) {
        Configuration config = new Configuration();
        config.cloudName = "nestled-time-alpha";
        config.apiKey = "752819916288738";
        config.secure = true;
        config.cname = "https://api.cloudinary.com/v1_1/{media_bucket}/upload";
        config.apiSecret = "bQjZWZavtRdSxhIHGsDjrId5SeE";
        config.uploadPrefix = "https://api.cloudinary.com";
        MediaManager.init(context, config);
    }

    public void uploadMedia() {

    }

    public static String uploadFile(String fileToUpload) {
        return MediaManager.get().upload(fileToUpload)
                .unsigned("z4vocg0c")
                .dispatch();
    }
}
