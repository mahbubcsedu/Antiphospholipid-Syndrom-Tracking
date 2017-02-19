/* 
  * Copyright 2014 (C) The EMMES Corporation 
  *  
  * Created on : 02-06-2014
  * Last Update: Jun 18, 2014 9:05:37 AM
  * Author     : Mahbubur Rahman
  * Title      : Summer intern 2014 
  * Project    : Daily Diary Android Application
  * 
  */

package com.emmes.aps.locationtracking;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import com.emmes.aps.R;
import com.emmes.aps.data.LocationTrackingData;
import com.emmes.aps.data.LocationTrackingDuration;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

// TODO: Auto-generated Javadoc
/**
 * The Class LocationTrackingActivity.
 */
public class LocationTrackingActivity extends FragmentActivity
implements LoaderManager.LoaderCallbacks<Cursor>{
	
	/** The tv. */
	private TextView tv;
	
	/** The tracking. */
	private PendingIntent tracking;
	
	/** The alarms. */
	private AlarmManager alarms;
	
	/** The adapter. */
	SimpleCursorAdapter adapter;
	
	/** The m last loc prefs. */
	SharedPreferences mLastLocPrefs;
	
	/** The m editor. */
	SharedPreferences.Editor mEditor;
	//private long UPDATE_INTERVAL = 5000;
	//private int START_DELAY = 5;
	/** The debug tag. */
	private String DEBUG_TAG = "LocationServiceActivity";
	
	/**
	 *  Called when the activity is first created.
	 *
	 * @param savedInstanceState the saved instance state
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.locationdsp_dummy);
		drawTable();
		alarms = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
	}

	/**
	 * Start.
	 *
	 * @param view the view
	 */
	public void start(View view){
		tv = (TextView) findViewById(R.id.tv1);
		tv.setText("Start tracking");
		LocationAlarmReceiver aReceiver=new LocationAlarmReceiver();
		aReceiver.setRecurringAlarm(this);
		//setRecurringAlarm(getBaseContext());        
		/*NotificationCompat.Builder builder =
				new NotificationCompat.Builder(this)
		.setSmallIcon(R.drawable.track_loc)
		.setContentTitle("you are being tracking by bwell")
		.setContentText("tracking your location")
		.setAutoCancel(false)
		.setOngoing(true);


		Intent targetIntent = new Intent(this, MainActivity.class);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, targetIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		builder.setContentIntent(contentIntent);
		NotificationManager nManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		nManager.notify(LocationUtils.LOCATION_TRACKING_NOTIFICATION_ID, builder.build());*/
	}

	/**
	 * Stop.
	 *
	 * @param view the view
	 */
	public void stop(View view){
		tv = (TextView) findViewById(R.id.tv1);
		tv.setText("Stop tracking");
		LocationAlarmReceiver aReceiver=new LocationAlarmReceiver();
		aReceiver.CancelRecurringAlarm(this);
		/*Intent intent = new Intent(getBaseContext(), LocationAlarmReceiver.class);
		tracking = PendingIntent.getBroadcast(getBaseContext(), 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
		alarms = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
		alarms.cancel(tracking);*/

		/*NotificationManager nManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

		nManager.cancel(LocationUtils.LOCATION_TRACKING_NOTIFICATION_ID);*/
		Log.d(DEBUG_TAG, ">>>Stop tracking()");
	}

	/**
	 * Sets the recurring alarm.
	 *
	 * @param context the new recurring alarm
	 */
	private void setRecurringAlarm(Context context) {

		// Handle to SharedPreferences for this app
		

		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ",Locale.US);
		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

		// Handle to a SharedPreferences editor
		

		mLastLocPrefs = context.getSharedPreferences(LocationUtils.SHARED_PREFERENCES, Context.MODE_PRIVATE);

		mEditor = mLastLocPrefs.edit();


		// get a Calendar object with current time
		Calendar cal = Calendar.getInstance();
		// add 5 secs to the calendar object
		cal.add(cal.SECOND, LocationUtils.START_DELAY+5);

		mEditor.putLong(LocationUtils.TRACKING_START_TIME, cal.getTime().getTime());
		cal.add(cal.SECOND, 300);
		mEditor.putLong(LocationUtils.TRACKING_END_TIME, cal.getTime().getTime());
		mEditor.commit();
		/*Intent tracking = new Intent(context, ServiceLocationUpdate.class);
        context.startService(tracking);*/
		
		/**
		 * For testing purpose add two time range for location tracking duration
		 */
		
		getContentResolver().delete(LocationTrackingDuration.TrackingDurationItem.CONTENT_URI, null, null);
		long current_time=Calendar.getInstance().getTimeInMillis();
		
		ContentValues values = new ContentValues(); 
		values.put(LocationTrackingDuration.TrackingDurationItem.COLUMN_NAME_ENTRYTIME, current_time); 
		values.put(LocationTrackingDuration.TrackingDurationItem.COLUMN_NAME_START_TIME, current_time+10000);		
		values.put(LocationTrackingDuration.TrackingDurationItem.COLUMN_NAME_END_TIME,  current_time+60000);
		getContentResolver().insert(LocationTrackingDuration.TrackingDurationItem.CONTENT_URI, values);
		
		values.clear();
		
		values.put(LocationTrackingDuration.TrackingDurationItem.COLUMN_NAME_ENTRYTIME, current_time); 
		values.put(LocationTrackingDuration.TrackingDurationItem.COLUMN_NAME_START_TIME, current_time+75000);		
		values.put(LocationTrackingDuration.TrackingDurationItem.COLUMN_NAME_END_TIME,  current_time+110000);
		getContentResolver().insert(LocationTrackingDuration.TrackingDurationItem.CONTENT_URI, values);
		
		
		
		
		cal = Calendar.getInstance();
		cal.add(Calendar.SECOND, LocationUtils.START_DELAY);
		Intent intent = new Intent(context, LocationAlarmReceiver.class);

		tracking = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

		alarms.setInexactRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), LocationUtils.ALARM_UPDATE_INTERVAL_MILLISECONDS, tracking);
	}

	/**
	 * On resume.
	 */
	@Override
	public void onResume() {
		super.onResume();
		drawTable();
	}

	/**
	 * On pause.
	 */
	@Override
	public void onPause() {
		super.onPause();
	}

	/**
	 * Draw table.
	 */
	private void drawTable() {
		// Fields from the database (projection)
		// Must include the _id column for the adapter to work
		String[] from = new String[] { LocationTrackingData.LocationPoint._ID, LocationTrackingData.LocationPoint.COLUMN_NAME_ENTRYTIME, 
				LocationTrackingData.LocationPoint.GPS_LATITUDE, LocationTrackingData.LocationPoint.GPS_LONGITUDE };
		// Fields on the UI to which we map
		int[] to = new int[] { R.id.rowid, R.id.time, R.id.longitude, R.id.latitude };
		getSupportLoaderManager().initLoader(0, null, this);
		adapter = new SimpleCursorAdapter(this, R.layout.locationinforow, null, from,
				to, 0);
		ListView listview = (ListView) findViewById(R.id.list);
		listview.setAdapter(adapter);
	}

	// Creates a new loader after the initLoader () call
	/**
	 * On create loader.
	 *
	 * @param id the id
	 * @param args the args
	 * @return the loader
	 */
	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		String[] projection = { LocationTrackingData.LocationPoint._ID, LocationTrackingData.LocationPoint.COLUMN_NAME_ENTRYTIME, 
				LocationTrackingData.LocationPoint.GPS_LATITUDE, LocationTrackingData.LocationPoint.GPS_LONGITUDE };
		CursorLoader cursorLoader = new CursorLoader(this,
				LocationTrackingData.LocationPoint.CONTENT_URI, projection, null, null, null);
		return cursorLoader;
	}

	/**
	 * On load finished.
	 *
	 * @param loader the loader
	 * @param data the data
	 */
	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		adapter.swapCursor(data);
	}

	/**
	 * On loader reset.
	 *
	 * @param loader the loader
	 */
	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		// data is not available anymore, delete reference
		adapter.swapCursor(null);
	}

}
