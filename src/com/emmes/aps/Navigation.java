package com.emmes.aps;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

/*
 * Application-wide class that defines common actions
 * requested by NavigationActivity
 */

public class Navigation extends Application {
    protected static void registerAction(Context context)
    {
        Log.i("Navigation","Registering Action");
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor peditor = prefs.edit();
		peditor.putLong("lastactiontime", System.currentTimeMillis());
		peditor.commit();
		
    }
}
