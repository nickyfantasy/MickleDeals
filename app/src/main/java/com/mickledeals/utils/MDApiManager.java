package com.mickledeals.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mickledeals.R;
import com.mickledeals.activities.MDApplication;

import org.json.JSONObject;

/**
 * Created by Nicky on 7/15/2015.
 */
public class MDApiManager {

    private static RequestQueue sQueue;
    private static ImageLoader mImageLoader;
    private static Context sContext = MDApplication.sAppContext;
    private static Response.ErrorListener mErrorListener;

    public static void initVolley() {
        sQueue = Volley.newRequestQueue(sContext);
        mErrorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(sContext, R.string.network_error_msg, Toast.LENGTH_SHORT).show();
            }
        };
    }

    public static void sendStringRequest(String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, listener, errorListener);
        sendRequest(stringRequest);
    }

    public static void sendJSONRequest(String url, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null, listener, errorListener);
        sendRequest(jsonRequest);
    }

    public static void sendImageRequest(String url, int maxWidth, int maxHeight, Response.Listener<Bitmap> listener, Response.ErrorListener errorListener) {
        ImageRequest imageRequest = new ImageRequest(url, listener, maxWidth, maxHeight, ImageView.ScaleType.CENTER, null, errorListener);
        sendRequest(imageRequest);
    }

    public static void sendStringRequestWithPOST(String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, listener, errorListener);
        sendRequest(stringRequest);
    }

    private static void sendRequest(Request request) {
        sQueue.add(request);
    }

}
