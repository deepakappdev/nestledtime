package com.nestletime.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import com.yovenny.videocompress.MediaController;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Deepak Saini on 15-02-2018.
 */

public class MyFileSystem {
    public static final String ROOT_COMPRESS_PATH = Environment.getExternalStorageDirectory() + File.separator
            + "NestledTime" + File.separator
            + "CompressFiles" + File.separator;
    public static final int IMAGE_MAX_WIDTH_HEIGHT = 720;


    public static boolean removeFile(String pathFile) {
        File file = new File(pathFile);
        return file.exists() && file.isFile() && file.delete();
    }

    public static boolean needToCompress(int originalWidth, int originalHeight) {
        if (originalHeight > originalWidth) {//for portrait
            return originalHeight > IMAGE_MAX_WIDTH_HEIGHT;
        } else {//for landscape
            return originalWidth > IMAGE_MAX_WIDTH_HEIGHT;
        }
    }

    public static String compressImage(String filePath) {
        try {
            Bitmap bitmap = BitmapFactory.decodeFile(filePath);
            boolean needToCompress = needToCompress(bitmap.getWidth(), bitmap.getHeight());
            if(needToCompress) {
                int resultWidth, resultHeight;
                if(bitmap.getWidth()>bitmap.getHeight()) {
                    resultWidth = IMAGE_MAX_WIDTH_HEIGHT;
                    resultHeight = bitmap.getHeight()*resultWidth/bitmap.getWidth();
                } else {
                    resultHeight = IMAGE_MAX_WIDTH_HEIGHT;
                    resultWidth = bitmap.getWidth()*resultHeight/bitmap.getHeight();
                }
                bitmap = Bitmap.createScaledBitmap(bitmap, resultWidth, resultHeight, true);
            }

            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
            File tempFile = getTempImageFile();
            FileOutputStream fileOutputStream = new FileOutputStream(tempFile);
            fileOutputStream.write(bytes.toByteArray());
            fileOutputStream.close();
            return tempFile.getAbsolutePath();
        } catch (Exception x) {
        }
        return null;
    }

    public static File getTempImageFile() {
        createDirs();
        File file = new File(ROOT_COMPRESS_PATH + System.currentTimeMillis() + ".jpg");
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    public static File getTempVideoFile() {
        createDirs();
        File file = new File(ROOT_COMPRESS_PATH + System.currentTimeMillis() + ".mp4");
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    private static void createDirs() {
        File file = new File(ROOT_COMPRESS_PATH);
        if (!file.exists())
            file.mkdirs();
    }

    public static String compressVideo(String pathFile) {
        File file = getTempVideoFile();
        MediaController.getInstance().convertVideo(pathFile, file.getAbsolutePath());
        return file.getAbsolutePath();
    }
}
