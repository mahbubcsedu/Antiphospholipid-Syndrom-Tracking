/* 
 * Copyright 2014 (C) The EMMES Corporation 
 *  
 * Created on : 02-06-2014
 * Last Update: Jun 18, 2014 9:05:26 AM
 * Author     : Mahbubur Rahman
 * Title      : Summer intern 2014 
 * Project    : Daily Diary Android Application
 * 
 */
package com.emmes.aps.locationtracking;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import com.emmes.aps.R;
import com.emmes.aps.data.LocationTrackingDuration;
import com.emmes.aps.util.CalendarUtils;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.BaseColumns;
import android.support.v4.app.NotificationCompat;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;

// TODO: Auto-generated Javadoc
/**
 * The Class LocationAlarmReceiver is for monitoring the location tracking
 * process.
 * <p>
 * Location tracking system works based on two parameter, one is the user
 * consent to track his/her location and the time settled by the server side.
 * Time range for location tracking can be single, twice or many. So for each
 * range of time, the system have to track the location if user give consent.
 * <p>
 * For example, the range can be 2014-12-12 to 2014-12-23,2014-06-12 to
 * 2014-06-23 and 2014-12-12 to 2015-12-23 If current time falls between any
 * range, the system should track the location but off course have to get the
 * user consent.
 * <p>
 * <b>Algorithm:</b><br>
 * Location tracking recurring alarm is checked periodically set by the system.
 * The recurring alarm starts while the system starts working from MainActivity
 * {@linkplain com.emmes.aps.MainActivity}.
 * <p>
 * LocationAlarmReceiver receives the broadcast message related to location
 * tracking.
 * <ul>
 * <li>Upon receiving the message, it pulls out database information of location
 * tracking duration.</li>
 * <li>Check every tracking duration data agains current time.</li>
 * <li>If current time is inside of any one of those ranges, checks for user
 * consent.</li>
 * <li>If user consent is NO, stop the recurring tracking service using the
 * service ID.</li>
 * <li>If No, also generate a notification for user asking their consent for
 * participation to location tracking (this should be done only once for each
 * duration rang.</li>
 * <li>If user consent Yes, start the recurring location tracking service.</li>
 * <li>If current time is outside of all the duration range, stop recurring
 * location tracking process.</li>
 * 
 * </ul>
 * 
 * @author Mahbubur Rahman
 * 
 * @see AlarmManager
 * @see BroadcastReceiver
 * @see Calendar
 * @see PendingIntent
 * @since 1.0
 */
@SuppressLint("DefaultLocale")
public class LocationAlarmReceiver extends BroadcastReceiver
{

    /** The sharedpreferences object to deal with shared preferences. */
    SharedPreferences mLastLocPrefs;
    /**
     * mEditor is an instance of SharedPreference.Editor which is used for
     * editing the sharedpreferences in the application @see SharedPreferences.
     */
    SharedPreferences.Editor mEditor;
    /** The Constant TAG to display TAG related information to logcat. */
    private static final String TAG = "LocationAlarmReceiver";

    /**
     * The tracking is the object of pending intent which will be used for
     * recurring alarm generation.
     */
    private PendingIntent mTracking;

    /** The alarms is an instance of AlarmManager. */
    private AlarmManager mAlarms;

    /**
     * isInRange boolean flag is to check if time is in the range or tracking or
     * not. isConsentGiven is an flag to see if consent is given.
     */
    private boolean isInRange, isConsentGiven;

    /**
     * The rangeIdConsent is the id of range that is selected or the current
     * time is in the range of that range represent by this id.
     */
    private int mRangeIdConsent;

    /**
     * alreadyRequested is an integer flag to check if some items or medications
     * are already selected or not.
     */
    private int mAlreadyRequested;

    /** The adapter. */
    SimpleCursorAdapter mAdapter;

    /* (non-Javadoc)
     * 
     * @see
     * android.content.BroadcastReceiver#onReceive(android.content.Context,
     * android.content.Intent) */
    @SuppressLint("DefaultLocale")
    @Override
    public void onReceive(Context context, Intent intent)
    {
	Log.d(TAG, "Recurring alarm; requesting location tracking.");

	isInRange = false;
	isConsentGiven = false;
	mRangeIdConsent = -1;
	mAlreadyRequested = 0;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.US);
	sdf.setTimeZone(TimeZone.getDefault());

	mLastLocPrefs = context.getSharedPreferences(LocationUtils.SHARED_PREFERENCES, Context.MODE_PRIVATE);
	SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
	if (prefs.getString(LocationUtils.SPREF_MOBILITY_CONSENT, "").toLowerCase()
	        .equals(new String(context.getResources().getString(R.string.quest_yes))))
	    isConsentGiven = true;
	Log.d(TAG, "Consent status=" + isConsentGiven);
	// start the service
	/**
	 * Check shared preference for consent and database for start and end
	 * date;
	 */

	String[] projection_columns = new String[5];

	projection_columns[0] = LocationTrackingDuration.TrackingDurationItem.COLUMN_NAME_ENTRYTIME;
	projection_columns[1] = LocationTrackingDuration.TrackingDurationItem.COLUMN_NAME_START_TIME;
	projection_columns[2] = LocationTrackingDuration.TrackingDurationItem.COLUMN_NAME_END_TIME;
	projection_columns[3] = LocationTrackingDuration.TrackingDurationItem.COLUMN_NAME_ASK_CONSENT_FOR_THIS_RANGE;
	projection_columns[4] = BaseColumns._ID;

	int ENTRY_TIME = 0, START_TIME = 1, END_TIME = 2, ALREADY_ASK_FOR_CONSENT = 3, CONSENT_ID = 4;
	Cursor cursor = context.getContentResolver().query(LocationTrackingDuration.TrackingDurationItem.CONTENT_URI, projection_columns, null, null,
	        null);
	// ContentUris.parseId(newItemUri);

	// ArrayList<HeaderInfo> medicationList = new ArrayList<HeaderInfo>();
	Calendar currentTime = Calendar.getInstance();
	currentTime.setTimeZone(TimeZone.getDefault());

	cursor.moveToFirst();

	Calendar startTrackingTime = Calendar.getInstance();
	Calendar endTrackingTime = Calendar.getInstance();
	Date d1, d2;
	if (cursor.getCount() > 0)
	{// if returned data is not empty

	    while (!cursor.isAfterLast())
	    {
		startTrackingTime = CalendarUtils.dateString2Calendaryyyymmddhhmmss(cursor.getString(START_TIME));
		endTrackingTime = CalendarUtils.dateString2Calendaryyyymmddhhmmss(cursor.getString(END_TIME));
		// d1=new Date(cursor.getLong(1));
		// d2=new Date(cursor.getLong(2));
		if (CalendarUtils.isInTimeWindow(startTrackingTime, endTrackingTime))
		{
		    isInRange = true;
		    mRangeIdConsent = cursor.getInt(CONSENT_ID);
		    mAlreadyRequested = cursor.getInt(ALREADY_ASK_FOR_CONSENT);
		    Log.d(TAG, "requested=" + mAlreadyRequested + "id=" + mRangeIdConsent);
		}
		/* if(currentTime.after(d1) && currentTime.before(d2)) {
	         * isInRange=true; rangeIdConsent=cursor.getInt(4);
	         * alreadyRequested=cursor.getInt(3);
	         * Log.d(TAG,"requested="+alreadyRequested
	         * +"id="+rangeIdConsent); } */
		Log.d(TAG, "Start time=" + sdf.format(startTrackingTime.getTime()) + " Current time= " + sdf.format(currentTime.getTime())
		        + "End time=" + sdf.format(endTrackingTime.getTime()));
		cursor.moveToNext();
	    }
	    // make sure to close the cursor
	    cursor.close();

	}

	if (isInRange)
	{
	    if (isConsentGiven)
	    {
		Log.d(TAG, "service started");

		Intent tracking = new Intent(context, ServiceLocationUpdate.class);
		context.startService(tracking);
		isInRange = false;
	    }
	    else
	    {

		Log.d(TAG, "Consent is not given, checking if already requested or not");

		if (mAlreadyRequested != 1)
		{

		    ContentValues values = new ContentValues();

		    // values.put(MedicationList.MedicationItem._ID, med_id);
		    values.put(LocationTrackingDuration.TrackingDurationItem.COLUMN_NAME_ASK_CONSENT_FOR_THIS_RANGE, 1);

		    String where = BaseColumns._ID + "=" + mRangeIdConsent;
		    // String[] args = new String[] { Integer.toString(med_id)
// };
		    context.getContentResolver().update(LocationTrackingDuration.TrackingDurationItem.CONTENT_URI, values, where, null);

		    // notification is non clickable
		    // Intent notificationtarget = new Intent(context,
// MainActivity.class);
		    Intent notificationtarget = new Intent();
		    notificationtarget.putExtra(LocationUtils.DIRECTING_FROM_LOCATION_RECEIVER_FOR_CONSENT, "yes");
		    notificationtarget.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
		    // notificationtarget.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
// | Intent.FLAG_ACTIVITY_CLEAR_TOP);
		    PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationtarget, PendingIntent.FLAG_UPDATE_CURRENT);

		    NotificationCompat.Builder builder = new NotificationCompat.Builder(context).setSmallIcon(R.drawable.ic_launcher)
			    .setContentTitle(context.getResources().getString(R.string.loc_consent_notification_title))
			    .setContentText(context.getResources().getString(R.string.loc_consent_notification_msg)).setAutoCancel(true)
			    .setContentIntent(contentIntent)
			    .setSound(Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.sound1)).setOngoing(true);

		    NotificationManager nManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		    nManager.notify(LocationUtils.LOCATION_TRACKING_NOTIFICATION_ID, builder.build());
		}

	    }

	}
	else
	{
	    Log.d(TAG, "service stopped");
	    NotificationManager nManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
	    nManager.cancel(LocationUtils.LOCATION_TRACKING_NOTIFICATION_ID);
	    Log.d(TAG, "notification " + LocationUtils.LOCATION_TRACKING_NOTIFICATION_ID + "stopped");
	    Intent tracking = new Intent(context, ServiceLocationUpdate.class);
	    context.stopService(tracking);
	}
    }

    /**
     * The method stopRecurringAlarm is stopping the recurring alarm .
     * @deprecated
     * @param context the context of the class from where it is called
     * @return void
     * @throws none
     * @since 1.0
     * @author Mahbubur Rahman
     */
    @Deprecated
	private void stopRecurringAlarm(Context context)
    {
	Intent intent = new Intent(context, LocationAlarmReceiver.class);
	mTracking = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
	mAlarms = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
	mAlarms.cancel(mTracking);
	Log.d(TAG, "recurring alarm stopped");

	/* NotificationManager nManager = (NotificationManager)
	 * context.getSystemService(Context.NOTIFICATION_SERVICE);
	 * 
	 * nManager.cancel(LocationUtils.LOCATION_TRACKING_NOTIFICATION_ID); */
    }
    /**
     * The method setRecurringAlarm is set time for start an recurring alarm for location tracking. This will start after a few seconds
     * which is set in locationUtils and will run at certain interval which will also be set by LocationUtils. This has been done in this way
     * becasue we don't know when ther tracking duration time is available. So we have to check the system periodically. This will run periodically
     * may be once a day or twice a day to save the energy. When run, it will check the duration time and consent and will track the location as well.
     * <p>The only problem in this approach is, if the checking or recurring period is once a day, then for next checking would take place after 24 hours
     * and location tracking will not start before that.  
     * <p> All alarm are disable as the device restarted, so in this method, {@linkplain LocationBootReceiver} has also been called to restart the alarm
     * after restarting device.    
     * @param aContext the context of the class from where it is called
     * @return void
     * @throws none
     * @since 1.0
     * @author Mahbubur Rahman
     */
    public void setRecurringAlarm(Context aContext)
    {

	// Handle to SharedPreferences for this app
	mAlarms = (AlarmManager) aContext.getSystemService(Context.ALARM_SERVICE);

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.US);
	sdf.setTimeZone(TimeZone.getDefault());

	// Handle to a SharedPreferences editor

	mLastLocPrefs = aContext.getSharedPreferences(LocationUtils.SHARED_PREFERENCES, Context.MODE_PRIVATE);

	mEditor = mLastLocPrefs.edit();

	// get a Calendar object with current time
	Calendar cal = Calendar.getInstance();
	// add 5 secs to the calendar object
	cal.add(Calendar.SECOND, LocationUtils.START_DELAY + 5);

	mEditor.putLong(LocationUtils.TRACKING_START_TIME, cal.getTime().getTime());
	cal.add(Calendar.SECOND, 300);
	mEditor.putLong(LocationUtils.TRACKING_END_TIME, cal.getTime().getTime());
	mEditor.commit();
	/* Intent tracking = new Intent(context, ServiceLocationUpdate.class);
	 * context.startService(tracking); */

	/**
	 * For testing purpose add two time range for location tracking duration
	 */

	aContext.getContentResolver().delete(LocationTrackingDuration.TrackingDurationItem.CONTENT_URI, null, null);
	Calendar current_time = Calendar.getInstance();// .getTimeInMillis();
	Calendar nonmodifiedtime = Calendar.getInstance();

	ContentValues values = new ContentValues();
	values.put(LocationTrackingDuration.TrackingDurationItem.COLUMN_NAME_ENTRYTIME,
	        CalendarUtils.calTimeToDateStringyyyymmddhhmmss(nonmodifiedtime));
	current_time.add(Calendar.SECOND, 20);
	values.put(LocationTrackingDuration.TrackingDurationItem.COLUMN_NAME_START_TIME,
	        CalendarUtils.calTimeToDateStringyyyymmddhhmmss(current_time));
	current_time.add(Calendar.SECOND, 80);
	values.put(LocationTrackingDuration.TrackingDurationItem.COLUMN_NAME_END_TIME, CalendarUtils.calTimeToDateStringyyyymmddhhmmss(current_time));
	aContext.getContentResolver().insert(LocationTrackingDuration.TrackingDurationItem.CONTENT_URI, values);

	values.clear();
	current_time.add(Calendar.MINUTE, 2);
	values.put(LocationTrackingDuration.TrackingDurationItem.COLUMN_NAME_ENTRYTIME,
	        CalendarUtils.calTimeToDateStringyyyymmddhhmmss(nonmodifiedtime));
	values.put(LocationTrackingDuration.TrackingDurationItem.COLUMN_NAME_START_TIME,
	        CalendarUtils.calTimeToDateStringyyyymmddhhmmss(current_time));
	current_time.add(Calendar.MINUTE, 4);
	values.put(LocationTrackingDuration.TrackingDurationItem.COLUMN_NAME_END_TIME, CalendarUtils.calTimeToDateStringyyyymmddhhmmss(current_time));
	aContext.getContentResolver().insert(LocationTrackingDuration.TrackingDurationItem.CONTENT_URI, values);

	cal = Calendar.getInstance();
	cal.add(Calendar.SECOND, LocationUtils.START_DELAY);
	Intent intent = new Intent(aContext, LocationAlarmReceiver.class);

	mTracking = PendingIntent.getBroadcast(aContext, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

	mAlarms.setInexactRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), LocationUtils.ALARM_UPDATE_INTERVAL_MILLISECONDS, mTracking);
	// Enable {@code SampleBootReceiver} to automatically restart the alarm
// when the
	// device is rebooted.
	ComponentName receiver = new ComponentName(aContext, LocationBootReceiver.class);
	PackageManager pm = aContext.getPackageManager();

	pm.setComponentEnabledSetting(receiver, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);

    }

    /**
     * The method CancelRecurringAlarm is to stop the recurrent alarm  
     * @param aContext the context of the class from where it is called
     * @return void
     * @deprecated
     * @throws none
     * @since 1.0
     * @author Mahbubur Rahman
     */
    @Deprecated
	public void CancelRecurringAlarm(Context aContext)
    {

	Intent intent = new Intent(aContext, LocationAlarmReceiver.class);
	mTracking = PendingIntent.getBroadcast(aContext, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
	mAlarms = (AlarmManager) aContext.getSystemService(Context.ALARM_SERVICE);
	mAlarms.cancel(mTracking);

	// Disable {@code SampleBootReceiver} so that it doesn't automatically
// restart the
	// alarm when the device is rebooted.
	ComponentName receiver = new ComponentName(aContext, LocationBootReceiver.class);
	PackageManager pm = aContext.getPackageManager();

	pm.setComponentEnabledSetting(receiver, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);

	/* NotificationManager nManager = (NotificationManager)
	 * getSystemService(Context.NOTIFICATION_SERVICE);
	 * 
	 * nManager.cancel(LocationUtils.LOCATION_TRACKING_NOTIFICATION_ID); */
	Log.d(TAG, ">>>Stop tracking()");
    }
}
