package com.bravvura.nestledtime.network.request;

import com.android.volley.Request;
import com.android.volley.Response;
import com.bravvura.nestledtime.mediagallery.model.facebook.FacebookResponse;
import com.bravvura.nestledtime.network.volley.GsonRequest;
import com.bravvura.nestledtime.utils.Constants;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Deepak Saini on 4/12/2017.
 */
public class GetFacebookAlbumRequest extends BaseRequest {

    public String getServiceUrl() {
        return Constants.URLS.FACEBOOK_GET_ALBUMS;
    }

    public HashMap<String, String> getParameters() {
        return new HashMap<String, String>();
    }

    private HashMap<String, String> getHeaders() {
        return new HashMap<String, String>();
    }

    public JSONObject getJsonRequest() {
        return new JSONObject();
    }

    public GsonRequest createServerRequest(Response.ErrorListener errorListener, Response.Listener listener) {
        GsonRequest<FacebookResponse> gsonRequest = new GsonRequest<>(
                Request.Method.GET, getServiceUrl(),
                FacebookResponse.class, getParameters(), listener, errorListener, getJsonRequest());
        gsonRequest.setShouldCache(false);
        gsonRequest.setHeader(getHeaders());
        return gsonRequest;
    }
}
