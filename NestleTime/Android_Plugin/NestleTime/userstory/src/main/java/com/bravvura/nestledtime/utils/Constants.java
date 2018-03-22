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
        int REQUEST_AUDIO = 106;
        int REQUEST_EDIT_GALLERY_MEDIA = 107;
        int REQUEST_EDIT_LOCATION = 108;
        int REQUEST_RECORD_AUDIO = 109;
        int REQUEST_CAMERA = 110;
        int REQUEST_SOCIAL_FB_LOGIN = 111;
        int REQUEST_SOCIAL_FB_LOGOUT = 112;
        int REQUEST_PERMISSION_LOCATION = 113;
        int REQUEST_CHECK_LOCATION_SETTINGS = 114;
        int PERMISSION_LOCATION_FOR_USER = 115;
    }

    public interface BUNDLE_KEY {
        String FOLDER_PATH = "folder_path";
        String MEDIA_MODEL = "media_model";
        String SELECTED_MEDIA_PATH = "selected_media_path";
        String SELECTED_MEDIA = "selected_medias";
        String ADD_PICTURE = "add_picture";
        String SELECTED_LOCATION = "selected_location";
        String USER_STORY_MEDIA_MODEL = "user_story_media_model";
        String INDEX = "index";
    }

    public interface URLS {
        String FACEBOOK_GET_PHOTOS = "http://nestledtime.com/api/?q=pranjul&fbid=1579591118800311&allimage=1";
        String FACEBOOK_GET_ALBUMS = "http://api.nestledtime.com/newapi/index.php?fbid=125289771505044";

    }
}
