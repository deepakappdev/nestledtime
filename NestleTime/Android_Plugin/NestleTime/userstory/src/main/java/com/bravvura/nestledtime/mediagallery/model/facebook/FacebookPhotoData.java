package com.bravvura.nestledtime.mediagallery.model.facebook;

/**
 * Project Name Nestled Time
 * Created by Deepak Saini on 20-03-2018.
 */

public class FacebookPhotoData {
    public String title;
    public String desc;
    public String time;
    public String created_date;//1970-01-01 00:00:00
    public String updated_date;//1970-01-01 00:00:00
    public String fetch_date;//2017-12-08 08:27:24
    public String search_category;//image
    public String source;//Facebook
    public String id;//1546058235486933
    public FacebookImage images;

    public static class FacebookImage {
        public String source;
        public String id;
    }
}
