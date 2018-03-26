package com.bravvura.nestledtime.utils;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import com.bravvura.nestledtime.MyApplication;
import com.bravvura.nestledtime.R;
import com.bravvura.nestledtime.userstory.model.UserStoryElement;
import com.bumptech.glide.GenericRequestBuilder;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Project Name Nestled Time
 * Created by Deepak Saini on 27-02-2018.
 */

public class Utils {

    public static Address getValidAddress(Context context, double lat, double lon) {
        if (context == null) return null;
        Geocoder selected_place_geocoder = new Geocoder(context);
        try {
            List<Address> addresses = selected_place_geocoder.getFromLocation(lat, lon, 1);
            if (addresses.size() == 0) return null;
            return addresses.get(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Address> getValidAddress(Context context, String addressText) {
        Geocoder geocoder = new Geocoder(context);
        try {
            return geocoder.getFromLocationName(addressText, 20);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getPlaceAutoCompleteUrl(String input) {
        StringBuilder urlBuilder = new StringBuilder();
        urlBuilder.append("https://maps.googleapis.com/maps/api/geocode/json?");
        try {
            urlBuilder.append("&address=").append(URLEncoder.encode(input, "utf8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        urlBuilder.append("&key=" + MyApplication.context.getString(R.string.google_maps_key));
//        urlString.append("&location=");
//        urlString.append(latitude + "," + longitude); // append lat long of current location to show nearby results.
        Log.d("FINAL URL:::   ", urlBuilder.toString());
        return urlBuilder.toString();
    }

    public static boolean isEmpty(ArrayList list) {
        if (list == null || list.size() == 0)
            return true;
        else return false;
    }

    public static String getStaticMapUrl(LatLng markerLocation) {
        return "https://maps.googleapis.com/maps/api/staticmap?" +
                "center=" + markerLocation.latitude + "," + markerLocation.longitude +
                "&size=720x400&maptype=roadmap&zoom=17" +
                "&markers=" + markerLocation.latitude + "," + markerLocation.longitude;
    }
}