package com.activeandroiddemo;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by dhananjay on 23/6/15.
 */
public class ConnectionDetector {

    Context mContext;
    public ConnectionDetector(Context mContext)
    {
        super();
        this.mContext = mContext;
    }
    public Boolean GetNetworkInfo()
    {
        ConnectivityManager cm = (ConnectivityManager)mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }
}
