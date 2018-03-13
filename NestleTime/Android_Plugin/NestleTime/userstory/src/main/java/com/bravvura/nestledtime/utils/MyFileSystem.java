package com.bravvura.nestledtime.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import com.yovenny.videocompress.MediaController;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Deepak Saini on 15-02-2018.
 */

public class MyFileSystem {
    public static final String ROOT_COMPRESS_PATH = Environment.getExternalStorageDirectory() + File.separator
            + "NestledTime" + File.separator
            + "CompressFiles" + File.separator;
    public static final int IMAGE_MAX_WIDTH_HEIGHT = 720;
    private static final int MAX_VIDEO_SIZE = 15 * 1024 * 1024;//15 MB
    private static final int MAX_IMAGE_SIZE = 512 * 1024;//0.5 MB


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
            File file = new File(filePath);
            Bitmap bitmap = BitmapFactory.decodeFile(filePath);
            boolean needToCompress = needToCompress(bitmap.getWidth(), bitmap.getHeight());
            if (needToCompress && file.length() > MAX_IMAGE_SIZE) {
                int resultWidth, resultHeight;
                if (bitmap.getWidth() > bitmap.getHeight()) {
                    resultWidth = IMAGE_MAX_WIDTH_HEIGHT;
                    resultHeight = bitmap.getHeight() * resultWidth / bitmap.getWidth();
                } else {
                    resultHeight = IMAGE_MAX_WIDTH_HEIGHT;
                    resultWidth = bitmap.getWidth() * resultHeight / bitmap.getHeight();
                }
                bitmap = Bitmap.createScaledBitmap(bitmap, resultWidth, resultHeight, true);
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                File tempFile = getTempImageFile();
                FileOutputStream fileOutputStream = new FileOutputStream(tempFile);
                fileOutputStream.write(bytes.toByteArray());
                fileOutputStream.close();
                return tempFile.getAbsolutePath();
            }


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

    public static File getTempAudioFile() {
        createDirs();
        File file = new File(ROOT_COMPRESS_PATH + System.currentTimeMillis() + ".mp3");
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
        if (pathFile.length() > MAX_VIDEO_SIZE) {
            File file = getTempVideoFile();
            MediaController.getInstance().convertVideo(pathFile, file.getAbsolutePath());
            return file.getAbsolutePath();
        } else {
            return pathFile;
        }
    }

    public static boolean copyFile(String fromPath, String toPath) {
        try {
            InputStream in = new FileInputStream(fromPath);
            OutputStream out = new FileOutputStream(toPath);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isImageFile(File file) {
        if (file == null) {
            return false;
        }
        if (!file.isFile()) {
            return false;
        }
        String name = file.getName();
        if (name.startsWith(".") || file.length() == 0) {
            return false;
        }
        boolean isCheck = false;

        for (int k = 0; k < Constants.FORMAT_IMAGE.length; k++) {
            if (name.toLowerCase().endsWith(Constants.FORMAT_IMAGE[k].toLowerCase())) {
                isCheck = true;
                break;
            }
        }
        return isCheck;
    }

    public static boolean isVideoFile(File file) {
        if (file == null) {
            return false;
        }
        if (!file.isFile()) {
            return false;
        }
        String name = file.getName();
        if (name.startsWith(".") || file.length() == 0) {
            return false;
        }
        boolean isCheck = false;

        for (int k = 0; k < Constants.FORMAT_VIDEO.length; k++) {
            if (name.toLowerCase().endsWith(Constants.FORMAT_VIDEO[k].toLowerCase())) {
                isCheck = true;
                break;
            }
        }
        return isCheck;
    }

    public static void deleteFile(File outputFile) {
        try {
            if (outputFile != null && outputFile.exists()) {
                outputFile.delete();
            }
        } catch(Exception x){}
    }
}
