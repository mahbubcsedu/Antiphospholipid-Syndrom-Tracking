/* 
  * Copyright 2014 (C) The EMMES Corporation 
  *  
  * Created on : 02-06-2014
  * Last Update: Aug 18, 2014 4:35:15 PM
  * Author     : Mahbubur Rahman
  * Title      : Summer intern 2014 
  * Project    : Daily Diary Android Application
  * 
  */
package com.emmes.aps;

import com.emmes.aps.locationtracking.LocationAlarmReceiver;
import com.emmes.aps.locationtracking.LocationUtils;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

// TODO: Auto-generated Javadoc
/**
 * The Class MainActivity.
 */
public class MainActivity extends Activity
{

    /** The Constant TAG. */
    private static final String TAG = "MainActivity";
    
    /** The is redirected consent. */
    private boolean isRedirectedConsent;

    /* (non-Javadoc)
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
	Log.i(TAG, "OnCreate");
	super.onCreate(savedInstanceState);
	Log.i(TAG, "OnCreatePost");
	isRedirectedConsent = false;

	NotificationManager mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
	mNotificationManager.cancel(-2);
	Log.i(TAG, getIntent().getBooleanExtra("EXIT", false) ? "one" : "two");
	if (getIntent().getBooleanExtra("EXIT", false))
	{
	    Log.i(TAG, "finish");
	    finish();
	}
	else if (settingsExist())
	{
	    Intent launchingIntent = new Intent(this, PINActivity.class);
	    launchingIntent.putExtra(LocationUtils.DIRECTING_FROM_LOCATION_RECEIVER_FOR_CONSENT, isRedirectedConsent);
	    startActivity(launchingIntent);
	}
	else
	{
	    Intent launchingIntent = new Intent(this, SettingsActivity.class);
	    startActivity(launchingIntent);
	}

	LocationAlarmReceiver aReceiver = new LocationAlarmReceiver();
	aReceiver.setRecurringAlarm(this);

    }

    /**
     * Decision making chunk of code required to write on onResume method, as
     * the @method onNewIntent(Intent intent) is called for some activities
     * which are already running and somewhere in stack, so it can not call
     * onCreate method of the activity rather it call on pause first and then
     * onNewIntent(Intent intent) method.
     */
    @Override
    protected void onResume()
    {
	// TODO Auto-generated method stub
	super.onResume();
	if (getIntent().getBooleanExtra("EXIT", false))
	{
	    Log.i(TAG, "finish");
	    finish();
	}
	else if (settingsExist())
	{
	    Intent launchingIntent = new Intent(this, PINActivity.class);
	    launchingIntent.putExtra(LocationUtils.DIRECTING_FROM_LOCATION_RECEIVER_FOR_CONSENT, isRedirectedConsent);
	    startActivity(launchingIntent);
	}
	else
	{
	    Intent launchingIntent = new Intent(this, SettingsActivity.class);
	    startActivity(launchingIntent);
	}
    }

    /**
     * Settings exist.
     *
     * @return true, if successful
     */
    private boolean settingsExist()
    {
	// Have settings been established?
	SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
	if (prefs.getString(this.getResources().getString(R.string.spref_pref_pin), "").equals(""))
	{
	    return false;
	}
	else
	{
	    return true;
	}
    }

    /* (non-Javadoc)
     * @see android.app.Activity#onDestroy()
     */
    @Override
    public void onDestroy()
    {
	Log.i(TAG, "onDestroy()");
	super.onDestroy();
    }

    /* (non-Javadoc)
     * @see android.app.Activity#onNewIntent(android.content.Intent)
     */
    @Override
    public void onNewIntent(Intent intent)
    {
	super.onNewIntent(intent);

	Bundle extras = intent.getExtras();
	if (extras != null)
	{
	    if (extras.containsKey(LocationUtils.DIRECTING_FROM_LOCATION_RECEIVER_FOR_CONSENT))
	    {
		String consent = extras.getString(LocationUtils.DIRECTING_FROM_LOCATION_RECEIVER_FOR_CONSENT, "no");

		intent.putExtra(LocationUtils.DIRECTING_FROM_LOCATION_RECEIVER_FOR_CONSENT, "no");// ;.replaceExtras(extras).removeExtra(name);

		if (consent.equalsIgnoreCase("yes"))
		    isRedirectedConsent = true;
		else
		    isRedirectedConsent = false;
	    }
	}

    }
}
