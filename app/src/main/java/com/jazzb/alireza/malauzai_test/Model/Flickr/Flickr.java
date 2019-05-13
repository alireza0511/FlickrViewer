package com.jazzb.alireza.malauzai_test.Model.Flickr;


import android.net.Uri;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Flickr {

    private FlickrConstants flickrConstants;

    public void getRequestFlickr (RequestQueue mRequestQueue, String pageNum,
                                final VolleyCallBack callback) {

        flickrConstants = new FlickrConstants();
        Uri.Builder uriBuilder = new Uri.Builder();

        uriBuilder.scheme(flickrConstants.apiScheme())
                .authority(flickrConstants.apiHost())
                .appendPath(flickrConstants.apiPath1st())
                .appendPath(flickrConstants.apiPath2nd())
                .appendQueryParameter(flickrConstants.parameterKeyApi(),flickrConstants.parameterValueAPIKey())
                .appendQueryParameter(flickrConstants.parameterKeyGalleryID(),flickrConstants.parameterValueGalleryID())
                .appendQueryParameter(flickrConstants.parameterKeyFormat(),flickrConstants.parameterValueResponseFormat())
                .appendQueryParameter(flickrConstants.parameterKeyExtras(),flickrConstants.parameterValueMediumURL())
                .appendQueryParameter(flickrConstants.parameterKeyMethod(),flickrConstants.parameterValueSearchMethod())
                .appendQueryParameter(flickrConstants.parameterKeyNoJSONCallback(),flickrConstants.parameterValueDisableJSONCallback())
                .appendQueryParameter(flickrConstants.parameterKeyPhotosPerPage(),flickrConstants.parameterValuePhotosPerPage())
                .appendQueryParameter(flickrConstants.parameterKeyPage(),pageNum);

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET, uriBuilder.toString(),null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String photoResult = queryUtils(String.valueOf(response));
                        callback.onSuccessResponse(photoResult);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onFailureResponse(error);
            }
        })
        {
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                return params;
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Authorization","Bearer "+ "userToken");
                return params;
            }
        };

        mRequestQueue.add(request);
    }

    private String queryUtils(String object){

        try {
            JSONObject baseJsonResponse = new JSONObject(object);
            JSONObject rootObject = baseJsonResponse.getJSONObject(flickrConstants.responseKeyPhotos());
            JSONArray rootPhotoArray = rootObject.getJSONArray(flickrConstants.responseKeyPhoto());

            return String.valueOf(rootPhotoArray);

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the  JSON results", e);
        }
        return null;
    }
}
