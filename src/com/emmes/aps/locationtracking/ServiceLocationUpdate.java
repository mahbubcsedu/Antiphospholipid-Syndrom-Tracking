/* 
 * Copyright 2014 (C) The EMMES Corporation 
 *  
 * Created on : 02-06-2014
 * Last Update: Jun 18, 2014 9:05:14 AM
 * Author     : Mahbubur Rahman
 * Title      : Summer intern 2014 
 * Project    : Daily Diary Android Application
 * 
 */
package com.emmes.aps.locationtracking;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import com.emmes.aps.PINActivity;
import com.emmes.aps.R;
import com.emmes.aps.data.DatabaseConstants;
import com.emmes.aps.data.LocationTrackingData;
import com.emmes.aps.util.CalendarUtils;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

// TODO: Auto-generated Javadoc
/**
 * <b>ServiceLocationUpdate</b> is an android service which runs background if the user give consent and the current system time is within tracking
 * duration range.
 * <p>
 * This service starts by the android application while all required criteria is fulfilled by the system. Then in the background this service runs until
 * the end tracking time reached. This service thus implements Looper inside service handler. GPS precise location is used here and the minimum time 
 * and distance is updated from location utils. The service interact with the location database of location tracking while get a new location or
 * location is changed. It then stores the changed location point to the database with proper precision. Though there is  security concern over location
 * tracking precisely, this service does not do any reduction of location. This may be done on other implementation.
 * <p> This service also implement notification services which display a simple icon of navigation which will displayed on the action bar and it can not be 
 * canceled by the user. When he tracking time finished, it will disappear automatically.
 * 
 * @author Mahbubur Rahman *
 * @see Service
 * @see LocationListener
 * @see Calendar
 * @see Looper
 * @see Handler
 * @see NotificationCompat
 * @since 1.0
 */

public class ServiceLocationUpdate extends Service implements LocationListener
{

    private final static String TAG="ServiceLocationUpdate";
    /** The m service looper. */
    private Looper mServiceLooper;

    /** The m service handler. */
    private ServiceHandler mServiceHandler;

    /** The debug tag. */
    private final String DEBUG_TAG = "Service:ServiceLocationUpdate";

    /** The mgr. */
    private LocationManager mLocMngr;

    /** The best. */
    private String best;
    // Handle to SharedPreferences for this app
    /** The m last loc prefs. */
    SharedPreferences mLastLocPrefs;

    // Handle to a SharedPreferences editor
    /** The m editor. */
    SharedPreferences.Editor mEditor;

    // Handler that receives messages from the thread

    /**
     * The Class ServiceHandler.
     * @see android.os.Handler
     */
    private final class ServiceHandler extends Handler
    {

	/**
	 * Instantiates a new service handler.
	 * 
	 * @param looper
	 *            the looper
	 *            @see android.os.Looper
	 */
	public ServiceHandler(Looper looper) {
	    super(looper);
	}

	/* (non-Javadoc)
	 * 
	 * @see android.os.Handler#handleMessage(android.os.Message) */
	@Override
	public void handleMessage(Message msg)
	{
	    Location location = mLocMngr.getLastKnownLocation(best);
	   // mServiceHandler.post(new MakeToast(trackLocation(location)));
	    Log.d(TAG,"Last know location"+ trackLocation(location));
	    // Stop the service using the startId, so that we don't stop
	    // the service in the middle of handling another job
	    stopSelf(msg.arg1);
	}
    }

    /* (non-Javadoc)
     * 
     * @see android.app.Service#onDestroy() */
    @Override
    public void onDestroy()
    {
	// TODO Auto-generated method stub
	Log.d(DEBUG_TAG, ">>>onDestroy()");
    }

    /* (non-Javadoc)
     * 
     * @see android.app.Service#onStartCommand(android.content.Intent, int, int) */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
	// TODO Auto-generated method stub
	// Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();
	// For each start request, send a message to start a job and deliver the
	// start ID so we know which request we're stopping when we finish the
	// job
	Message msg = mServiceHandler.obtainMessage();
	msg.arg1 = startId;
	mServiceHandler.sendMessage(msg);
	Log.d(DEBUG_TAG, ">>>onStartCommand()");
	// If we get killed, after returning from here, restart

	NotificationCompat.Builder builder = new NotificationCompat.Builder(this).setSmallIcon(R.drawable.track_loc)
	        .setContentTitle(this.getResources().getString(R.string.loc_running_notification_title))
	        .setContentText(this.getResources().getString(R.string.loc_running_notification_msg)).setAutoCancel(false).setOngoing(true);

	Intent targetIntent = new Intent(this, PINActivity.class);
	PendingIntent contentIntent = PendingIntent.getActivity(this, 0, targetIntent, PendingIntent.FLAG_UPDATE_CURRENT);
	builder.setContentIntent(contentIntent);
	NotificationManager nManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
	nManager.notify(LocationUtils.LOCATION_TRACKING_NOTIFICATION_ID, builder.build());

	return START_STICKY;
    }

    /* (non-Javadoc)
     * 
     * @see android.app.Service#onCreate() */
    @Override
    public void onCreate()
    {
	// TODO Auto-generated method stub
	// super.onCreate();
	// Start up the thread running the service. Note that we create a
	// separate thread because the service normally runs in the process's
	// main thread, which we don't want to block. We also make it
	// background priority so CPU-intensive work will not disrupt our UI.
	HandlerThread thread = new HandlerThread("ServiceStartArguments", android.os.Process.THREAD_PRIORITY_BACKGROUND);

	thread.start();
	Log.d(DEBUG_TAG, ">>>onCreate()");
	// Get the HandlerThread's Looper and use it for our Handler
	mServiceLooper = thread.getLooper();
	mServiceHandler = new ServiceHandler(mServiceLooper);
	mLocMngr = (LocationManager) getSystemService(LOCATION_SERVICE);
	Criteria criteria = new Criteria();
	best = mLocMngr.getBestProvider(criteria, true);
	mLocMngr.requestLocationUpdates(best, DatabaseConstants.location_update_time_interval,
	        DatabaseConstants.location_update_distance_interval_in_meter, this);
    }

    /* (non-Javadoc)
     * 
     * @see
     * android.location.LocationListener#onLocationChanged(android.location.
     * Location) */
    @Override
    public void onLocationChanged(Location location)
    {
	// mHandler.post(new MakeToast(trackLocation(location)));
	double longitude;
	double latitude;
	//String time;
	String result = "Location currently unavailable.";
	Calendar loc_time=Calendar.getInstance();

	// Get an editor

	if (location != null)
	{
	    longitude = location.getLongitude();
	    latitude = location.getLatitude();

	    mLastLocPrefs = getSharedPreferences(LocationUtils.SHARED_PREFERENCES, Context.MODE_PRIVATE);
	   // double lat2 = Double.parseDouble(mLastLocPrefs.getString(LocationUtils.GPS_LATITUDE_PREF, "0"));

	    //double lng2 = Double.parseDouble(mLastLocPrefs.getString(LocationUtils.GPS_LONGITUDE_PREF, "0"));

	    // lat1 and lng1 are the values of a previously stored location
	    // removed the location storing limitation to some distant
	    
	    //if (distanceBetweenTwoLocs(latitude, longitude, lat2, lng2) > LocationUtils.MIN_DISTANCE_TO_STORE)
	    //{
		// if distance < 0.1 miles we take locations as equal

		mEditor = mLastLocPrefs.edit();
		mEditor.putString(LocationUtils.GPS_LATITUDE_PREF, Double.toString(latitude));// .putFloat(LocationUtils.GPS_LATITUDE_PREF,
		                                                                              // latitude);
		mEditor.putString(LocationUtils.GPS_LONGITUDE_PREF, Double.toString(longitude));
		// mEditor.putFloat(LocationUtils.GPS_LATITUDE_PREF, latitude)
		// mEditor.putBoolean(LocationUtils.KEY_UPDATES_REQUESTED,
		// mUpdatesRequested);
		mEditor.commit();
		loc_time.setTimeInMillis(location.getTime());
		//time = parseTime(location.getTime());
		ContentValues values = new ContentValues();
		values.put(LocationTrackingData.LocationPoint.COLUMN_NAME_ENTRYTIME, CalendarUtils.calTimeToDateStringyyyymmddhhmmss(loc_time));
		values.put(LocationTrackingData.LocationPoint.GPS_LATITUDE, latitude);
		values.put(LocationTrackingData.LocationPoint.GPS_LONGITUDE, longitude);
		getContentResolver().insert(LocationTrackingData.LocationPoint.CONTENT_URI, values);
		result = "Location: " + Double.toString(longitude) + ", " + Double.toString(latitude);
		Log.d(TAG, result);
	  //  }

	}
    }

    /**
     * Distance method calculated distance between two GPS location points in miles.
     * @deprecated
     * @param lat1
     *            the latitude of location one object.
     * @param lng1
     *            the longitude of location one object.
     * @param lat2
     *            the latitude of location two object.
     * @param lng2
     *            the longitude of location two object.
     * @return the double
     */
    @Deprecated
	@SuppressWarnings("unused")
    private double distanceBetweenTwoLocs(double lat1, double lng1, double lat2, double lng2)
    {

	double earthRadius = 3958.75; // in miles, change to 6371 for kilometers

	double dLat = Math.toRadians(lat2 - lat1);
	double dLng = Math.toRadians(lng2 - lng1);

	double sindLat = Math.sin(dLat / 2);
	double sindLng = Math.sin(dLng / 2);

	double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2) * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));

	double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

	double dist = earthRadius * c;

	return dist;
    }

    /* (non-Javadoc)
     * 
     * @see android.location.LocationListener#onStatusChanged(java.lang.String,
     * int, android.os.Bundle) */
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras)
    {
	// TODO Auto-generated method stub
	Log.w(DEBUG_TAG, ">>>New Status: " + status);
    }

    /* (non-Javadoc)
     * 
     * @see
     * android.location.LocationListener#onProviderEnabled(java.lang.String) */
    @Override
    public void onProviderEnabled(String provider)
    {
	// TODO Auto-generated method stub
	Log.w(DEBUG_TAG, ">>>provider enabled: " + provider);
    }

    /* (non-Javadoc)
     * 
     * @see
     * android.location.LocationListener#onProviderDisabled(java.lang.String) */
    @Override
    public void onProviderDisabled(String provider)
    {
	// TODO Auto-generated method stub
	Log.w(DEBUG_TAG, ">>>provider disabled: " + provider);
    }

    /* (non-Javadoc)
     * 
     * @see android.app.Service#onBind(android.content.Intent) */
    @Override
    public IBinder onBind(Intent intent)
    {
	// TODO Auto-generated method stub
	return null;
    }

    // obtain current location, insert into database and make toast notification
    // on screen
    /**
     * Track location from Location object. It takes Location object and find out Latitude, Longitude and time to a string object.
     * 
     * @param location
     *            the location object
     * @return the string representation of location.
     */
    private String trackLocation(Location location)
    {
	double longitude;
	double latitude;
	String time;
	String result = getApplicationContext().getResources().getString(R.string.loc_result_text);//"Location currently unavailable.";

	// Insert a new record into the Events data source.
	// You would do something similar for delete and update.

	if (location != null)
	{
	    longitude = location.getLongitude();
	    latitude = location.getLatitude();
	    time = parseTime(location.getTime());
	    result = "Location: " + Double.toString(longitude) + ", " + Double.toString(latitude) +" GPS time ="+time;
	}
	return result;
    }

    /**
     * Parses the time.
     * @deprecated
     * @param t
     *            time in milliseconds
     * @return gmtTime the string representation of the time t.
     */
    @Deprecated
	private String parseTime(long t)
    {
	DateFormat df = DateFormat.getTimeInstance(DateFormat.MEDIUM);
	df.setTimeZone(TimeZone.getTimeZone("GMT-4"));
	String gmtTime = df.format(t);
	return gmtTime;
    }

    /**
     * The Class MakeToast is for Toast message on runtime to the user from the service..
     */
    @SuppressWarnings("unused")
    private class MakeToast implements Runnable
    {

	/** The txt. */
	String mTxt;

	/**
	 * Instantiates a new make toast which will dynmically Toast message from the service to the currently active activity.
	 * @deprecated
	 * @param text
	 *            the message to be displayed.
	 */
	@Deprecated
	@SuppressWarnings("unused")
        public MakeToast(String text) {
	    mTxt = text;
	}

	/* (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run() */
	@Override
	public void run()
	{
	    Log.d(TAG, mTxt);
	}
    }
}
