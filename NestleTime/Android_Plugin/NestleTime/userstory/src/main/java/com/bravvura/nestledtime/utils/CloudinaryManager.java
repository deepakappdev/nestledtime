package com.bravvura.nestledtime.utils;

import android.content.Context;

import com.cloudinary.Configuration;
import com.cloudinary.Transformation;
import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.UploadCallback;
import com.cloudinary.android.policy.GlobalUploadPolicy;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Deepak Saini on 15-02-2018.
 */

public class CloudinaryManager {
    public static final String CLOUD_NAME = "nestled-time-alpha";
    public static final String API_KEY = "752819916288738";
    public static final String C_NAME = "https://api.cloudinary.com/v1_1/{media_bucket}/upload";
    public static final String API_SECRET = "bQjZWZavtRdSxhIHGsDjrId5SeE";
    public static final String UPLOAD_PREFIX = "https://api.cloudinary.com";
    public static final String UPLOAD_PRESET = "z4vocg0c";

    public static void init(Context context) {
        Configuration config = new Configuration();
        config.cloudName = CLOUD_NAME;
        config.apiKey = API_KEY;
        config.secure = true;
        config.cname = C_NAME;
        config.apiSecret = API_SECRET;
        config.uploadPrefix = UPLOAD_PREFIX;
        MediaManager.init(context, config);
    }

    public static String uploadImageFile(String fileToUpload, final UploadCallback callBack) {
        String requestId = MediaManager.get()
                .upload(fileToUpload).option("resource_type", "image")
                .policy(GlobalUploadPolicy.defaultPolicy())
                .unsigned(UPLOAD_PRESET).callback(callBack).dispatch();
        return requestId;
    }

    public static String uploadVideoFile(String fileToUpload, UploadCallback callBack) {
        ArrayList<Transformation> transformations = new ArrayList<>();
        Transformation transformation = new Transformation();
        transformation.fetchFormat("m3u8");
        transformations.add(transformation);
        String requestId = MediaManager.get()
                .upload(fileToUpload).option("resource_type", "video")
                .option("eager", transformations)
                .policy(GlobalUploadPolicy.defaultPolicy())
                /*.unsigned(UPLOAD_PRESET)*/
                .callback(callBack).dispatch();
        return requestId;
    }

    public static String uploadAudioFile(String fileToUpload, UploadCallback callBack) {
        String requestId = MediaManager.get()
                .upload(fileToUpload).option("resource_type", "video")
                .policy(GlobalUploadPolicy.defaultPolicy())
                .callback(callBack).dispatch();
        return requestId;
    }

    public static Map deleteFile(String publicId) {
        try {
            return MediaManager.get().getCloudinary().uploader().destroy(publicId, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getVideoThumbnail(String publicId) {
        return MediaManager.get().url()
                .transformation(new Transformation().startOffset("0"))
                .resourceType("video").generate(publicId + ".png");
    }

    public static String getVideoThumbnail(String publicId, int width, int height) {
        return MediaManager.get().url()
                .transformation(new Transformation().startOffset("0").width(width).height(height).crop("fill"))
                .resourceType("video").generate(publicId + ".png");
    }

    public static String getVideoUrl(String publicId, int width, int height) {
        return MediaManager.get().url()
                .transformation(new Transformation().width(width).height(height).crop("fill"))
                .resourceType("video").generate(publicId + ".m3u8");
    }


    public static String getFacesThumbnail(String publicId, int width, int height) {
        return MediaManager.get().url().transformation(
                new Transformation().gravity("faces").width(width).height(height).crop("fill")).
                generate(publicId);
    }

    public static String getAudioWaveUrl(String publicId) {
        return MediaManager.get().url().transformation(
                new Transformation().height(150).width(500).flags("waveform").color("white").background("transparent").startOffset(0).endOffset(0)).resourceType("video").generate(publicId + ".png");
    }
}
