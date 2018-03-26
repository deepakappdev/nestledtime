package com.bravvura.nestledtime.network.request;

import com.android.volley.Request;
import com.android.volley.Response;
import com.bravvura.nestledtime.model.googleaddress.PlacePredictions;
import com.bravvura.nestledtime.network.volley.GsonRequest;
import com.bravvura.nestledtime.utils.Utils;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Deepak Saini on 4/12/2017.
 */
public class GetGooglePlaceRequest extends BaseRequest {


    private final String filterText;

    public GetGooglePlaceRequest(String filterText) {
        this.filterText = filterText;
    }

    public String getServiceUrl() {
        return Utils.getPlaceAutoCompleteUrl(filterText);
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
        GsonRequest<PlacePredictions> gsonRequest = new GsonRequest<>(
                Request.Method.GET, getServiceUrl(),
                PlacePredictions.class, getParameters(), listener, errorListener, getJsonRequest());
        gsonRequest.setShouldCache(false);
        gsonRequest.setHeader(getHeaders());
        return gsonRequest;
    }
}
