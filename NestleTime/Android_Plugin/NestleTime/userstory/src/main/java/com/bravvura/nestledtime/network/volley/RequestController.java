package com.bravvura.nestledtime.network.volley;

import android.content.Context;

import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.bravvura.nestledtime.MyApplication;
import com.bravvura.nestledtime.network.request.BaseRequest;
import com.bravvura.nestledtime.network.request.GetFacebookPhotoRequest;
import com.bravvura.nestledtime.network.request.GetGooglePlaceRequest;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class RequestController {
    public static final String TAG = "RequestController";
    private static final int DEFAULT_MAX_RETRIES = 2;
    private static RetryPolicy rp = new DefaultRetryPolicy((int) TimeUnit.SECONDS.toMillis(60), DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
    private static RequestQueue mRequestQueue;

    public static RetryPolicy getRetryPolicy() {
        return rp;
    }

    private static Response.ErrorListener getErrorListener(final RequestCallback callback) {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (callback != null)
                    callback.error(new NetworkError(volleyError));
            }
        };
    }

    private static Response.Listener getListener(final RequestCallback callback, final BaseRequest request) {
        return new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                if(callback != null) {
                    if (response == null) {
                        callback.error(new NetworkError(new VolleyError("Something went wrong")));
                    } else {
                        callback.success(response);
                    }
                }
            }
        };
    }

    public static synchronized RequestQueue getmRequestQueue() {
        if (mRequestQueue != null) return mRequestQueue;
        Cache cache = new DiskBasedCache(MyApplication.context.getCacheDir(), 1024 * 1024); // 1/8MB cap
        //System.setProperty("http.proxyHost", "192.168.2.5");//for charles
        //System.setProperty("http.proxyPort", "8888");
        Network network = new BasicNetwork(new HurlStack());
        mRequestQueue = new RequestQueue(cache, network);
        mRequestQueue.start();
        return mRequestQueue;
    }

    public static void getGooglePlace(String filterText, RequestCallback callBack) {
        GetGooglePlaceRequest request = new GetGooglePlaceRequest(filterText);
        GsonRequest gsonRequest = request.createServerRequest(getErrorListener(callBack), getListener(callBack, request));
        getmRequestQueue().add(gsonRequest);
    }


    public static void getFacebookPhotos(RequestCallback callBack) {
        GetFacebookPhotoRequest request = new GetFacebookPhotoRequest();
        GsonRequest gsonRequest = request.createServerRequest(getErrorListener(callBack), getListener(callBack, request));
        getmRequestQueue().add(gsonRequest);
    }
    public static void getFacebookAlbum(RequestCallback callBack) {
        GetFacebookPhotoRequest request = new GetFacebookPhotoRequest();
        GsonRequest gsonRequest = request.createServerRequest(getErrorListener(callBack), getListener(callBack, request));
        getmRequestQueue().add(gsonRequest);
    }

}
