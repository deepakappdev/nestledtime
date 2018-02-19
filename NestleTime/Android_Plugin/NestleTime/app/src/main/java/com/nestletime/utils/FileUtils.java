package com.nestletime.utils;

import java.io.File;

/**
 * Created by Deepak Saini on 07-02-2018.
 */

public class FileUtils {
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
}
