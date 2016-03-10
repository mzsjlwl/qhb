package com.handsome.qhb.utils;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

/**
 * Created by zhang on 2016/3/10.
 */
public class VolleyUtils {

    private static JSONObject jsonObject;

    public static JSONObject getJsonObject(String url){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                         jsonObject = response;
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", error.getMessage(), error);
            }
        });
        RequestQueueController.getInstance().add(jsonObjectRequest);
       return jsonObject;
    }
}
