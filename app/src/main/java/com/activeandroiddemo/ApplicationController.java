package com.activeandroiddemo;

import android.app.Application;

import com.activeandroid.ActiveAndroid;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by dhananjay on 23/6/15.
 */
public class ApplicationController extends Application {

    public static final String TAG = "VolleyTag";
    private static ApplicationController mInstance;
    private RequestQueue mRequestQueue;

    @Override
    public void onCreate() {
        super.onCreate();
        ActiveAndroid.initialize(this);
        mInstance = this;
    }

    public static synchronized ApplicationController getInstance(){
        return mInstance;
    }

    public RequestQueue getRequestQueue(){

        if(mRequestQueue == null){
            mInstance.mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return mInstance.mRequestQueue;
    }

    public <T> void addToRequestQueue(Request <T> req){
                req.setTag(TAG);
                getRequestQueue().add(req);
    }
}
