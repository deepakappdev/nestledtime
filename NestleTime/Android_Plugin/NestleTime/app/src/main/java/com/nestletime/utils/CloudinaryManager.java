package com.nestletime.utils;

import android.content.Context;

import com.cloudinary.Configuration;
import com.cloudinary.android.MediaManager;
import com.cloudinary.android.UploadRequest;
import com.cloudinary.android.callback.UploadCallback;
import com.cloudinary.android.policy.GlobalUploadPolicy;
import com.cloudinary.android.policy.UploadPolicy;

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

        MediaManager.get().setGlobalUploadPolicy(
                new GlobalUploadPolicy.Builder()
                        .maxConcurrentRequests(5)
                        .networkPolicy(UploadPolicy.NetworkType.UNMETERED)
                        .build());
    }

    public void uploadMedia() {

    }

    public static String uploadImageFile(String fileToUpload, UploadCallback callBack) {
        MediaManager mediaManager = MediaManager.get();
        UploadRequest request = mediaManager.upload(fileToUpload);
        request.option("resource_type", "image");
        String requestId = request.callback(callBack).unsigned("z4vocg0c")
                .dispatch();
        return requestId;
    }
    public static String uploadVideoFile(String fileToUpload, UploadCallback callBack) {
        MediaManager mediaManager = MediaManager.get();
        UploadRequest request = mediaManager.upload(fileToUpload);
        request.option("resource_type", "video");
        String requestId = request.callback(callBack).unsigned("z4vocg0c")
                .dispatch();
        return requestId;
    }
}
