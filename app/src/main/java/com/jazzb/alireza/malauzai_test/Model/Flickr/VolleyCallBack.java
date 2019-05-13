package com.jazzb.alireza.malauzai_test.Model.Flickr;

import com.android.volley.VolleyError;

import org.json.JSONObject;

public interface VolleyCallBack {
    void onSuccessResponse(String result);
    void onFailureResponse(VolleyError error);
}
