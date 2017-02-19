package com.emmes.aps.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class NetworkUtils {
    // check if you have internet connection
    /**
     * Checks if is network available.
     * 
     * @param context
     *            the context
     * @return true, if is network available
     */
    public static boolean isNetworkAvailable(Context context) {
	ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	if (connectivity != null)
	{
	    NetworkInfo[] info = connectivity.getAllNetworkInfo();
	    if (info != null)
	    {
		for (int i = 0; i < info.length; i++)
		{
		    Log.w("INTERNET:", String.valueOf(i));
		    if (info[i].getState() == NetworkInfo.State.CONNECTED)
		    {
			Log.w("INTERNET:", "connected!");
			return true;
		    }
		}
	    }
	}
	return false;
    }
}
