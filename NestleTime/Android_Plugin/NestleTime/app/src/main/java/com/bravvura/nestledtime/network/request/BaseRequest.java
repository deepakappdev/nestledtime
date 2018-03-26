package com.bravvura.nestledtime.network.request;

import com.android.volley.Response;
import com.bravvura.nestledtime.network.volley.GsonRequest;
import com.bravvura.nestledtime.network.volley.NetworkError;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class BaseRequest {
    private String requestTag;


    /**
     * @return the base URL
     */
    public abstract String getServiceUrl();

    /**
     * @return the JSON request for sending the server
     */
    abstract public JSONObject getJsonRequest();


    /**
     * Method is used to receive the error
     *
     * @param response
     * @return the error
     * @throws NetworkError
     * @throws JSONException
     */
    public NetworkError parseErrorMessage(JSONObject response) {
        return null;
    }

    /**
     * Method to return the result got from the API call
     *
     * @return result, if any problem occur then return null
     */
    public Object getResult() {
        return null;
    }

    /**
     * Method to return the ResponseCode got from the API call
     *
     * @return result, if any problem occur then return null
     */
    public Object getResponseCode() {
        return null;
    }

    public abstract GsonRequest createServerRequest(Response.ErrorListener errorListener, Response.Listener listener);

    public String getRequestTag() {
        return requestTag;
    }

    public void setRequestTag(String requestTag) {
        this.requestTag = requestTag;
    }

    //public abstract JsonBaseRequest createServerRequest(ErrorListener errorListener, Listener listener);
}
