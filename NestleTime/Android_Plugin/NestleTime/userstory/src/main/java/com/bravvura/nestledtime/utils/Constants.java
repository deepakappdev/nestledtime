package com.bravvura.nestledtime.utils;

/**
 * Created by Deepak Saini on 07-02-2018.
 */

public class Constants {
    public static final String[] FORMAT_IMAGE = new String[]{".PNG", ".JPEG", ".JPG", ".GIF"};
    public static final String[] FORMAT_VIDEO = new String[]{".MP4", ".3GP"};
    public static final int MAX_MEDIA_SELECTED_COUNT = 15;


    public interface REQUEST_CODE {
        int READ_STORAGE = 101;
        int REQUEST_GALLERY_MEDIA = 102;
        int REQUEST_EDIT_IMAGE = 103;
        int REQUEST_LOCATION = 104;
        int PERMISSION_LOCATION = 105;
    }

    public interface BUNDLE_KEY {
        String FOLDER_PATH = "folder_path";
        String MEDIA_MODEL = "media_model";
        String SELECTED_MEDIA_PATH = "selected_media_path";
        String SELECTED_MEDIA = "selected_medias";
        String ADD_PICTURE = "add_picture";
        String SELECTED_LOCATION = "selected_location";
    }
}
