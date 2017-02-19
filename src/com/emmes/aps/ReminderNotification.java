/* 
  * Copyright 2014 (C) The EMMES Corporation 
  *  
  * Created on : 02-06-2014
  * Last Update: Aug 12, 2014 10:50:34 AM
  * Author     : Mahbubur Rahman
  * Title      : Summer intern 2014 
  * Project    : Daily Diary Android Application
  * 
  */
package com.emmes.aps;

import java.util.Calendar;
import java.util.Locale;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.preference.TimePickerPreference;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.text.SimpleDateFormat;

import com.emmes.aps.data.DatabaseConstants;
import com.emmes.aps.data.FormContentData;
import com.emmes.aps.locationtracking.LocationBootReceiver;
import com.emmes.aps.util.CalendarUtils;
import com.emmes.aps.util.NotificationUtils;

// TODO: Auto-generated Javadoc
/**
 * The Class ReminderNotification is for creating different type of notification
 * the user will get based on the local settings of APS app.
 * <p>
 * This is a receiver class which will receive the alarm notification of four
 * different types of notification 1. Diary 2. Nitro 3. AM peak flow and 4. PM
 * peak flow. For different type of notification, the message will be provided
 * based on that. So, while receiving the alarm, it will find out the message
 * from list, and will build a notification. The notification will target to the
 * pin activity.
 * <p>
 * For each type of notification, different request code is provided
 * 
 */
public class ReminderNotification extends BroadcastReceiver
{
    /** The Constant TAG. */
    public static final String TAG = "ReminderNotification";
    /** The reminder. */
    private static PendingIntent mReminder;
    /** The static alarms. */
    private static AlarmManager mStaticAlarms;
    /** The s context. */
    private Context mContext;
    /** The m last loc prefs. */
    static SharedPreferences mLastLocPrefs;
    /** The m static editor. */
    static SharedPreferences.Editor mStaticEditor;
    /** The Constant ACTION_DIARY. */
    static final String ACTION_DIARY = "diary_notification";
    /** The Constant ACTION_NITRO. */
    static final String ACTION_NITRO = "nitro_notification";
    /** The Constant ACTION_AM. */
    static final String ACTION_AM = "am_notification";
    /** The Constant ACTION_PM. */
    static final String ACTION_PM = "pm_notification";
    /** mLastPref is an instance of SharePreferences of the app. */
    SharedPreferences mPrefs;
    /** The mEditor is an instance of editor. */
    SharedPreferences.Editor mEditor;
    NotificationCompat.InboxStyle mInboxStyle;
    /**
     * {@link android.app.PendingIntent}
     */
    PendingIntent mCurrentAlarm;
    /** The context. */
    // Context context;
    /** The alarm manager instance. */
    private AlarmManager mAlarms;

    /* (non-Javadoc)
     * 
     * @see
     * android.content.BroadcastReceiver#onReceive(android.content.Context,
     * android.content.Intent) <p> In this receiver, check whether the system
     * need to send notification to user or not. If needed, it will build a
     * notification builder and will generate notification. */
    @Override
    public void onReceive(Context context, Intent intent)
    {
	// TODO Auto-generated method stub
	this.mContext = context;
	this.mInboxStyle = new NotificationCompat.InboxStyle();
	mInboxStyle.setBigContentTitle(context.getResources().getString(R.string.big_notification_title));
	if (willNotifyUser())
	{

	   /* NotificationCompat.Builder builder = new NotificationCompat.Builder(context).setSmallIcon(R.drawable.track_loc)
		    .setContentTitle(context.getResources().getString(R.string.notification_title))
		    .setSound(Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.sound1)).setAutoCancel(true).setOngoing(false);
	    builder.setStyle(this.inboxStyle);*/
	    
	 // Sets up the Snooze and Dismiss action buttons that will appear in the
	 // big view of the notification.
	/* Intent dismissIntent = new Intent(context, PingService.class);
	 dismissIntent.setAction(CommonConstants.ACTION_DISMISS);
	 PendingIntent piDismiss = PendingIntent.getService(this, 0, dismissIntent, 0);

	 Intent snoozeIntent = new Intent(this, PingService.class);
	 snoozeIntent.setAction(CommonConstants.ACTION_SNOOZE);
	 PendingIntent piSnooze = PendingIntent.getService(this, 0, snoozeIntent, 0);*/
	    
	    NotificationCompat.Builder builder =
		        new NotificationCompat.Builder(context)
		        .setSmallIcon(R.drawable.ic_stat_notification)
		        .setContentTitle(context.getString(R.string.big_notification_title))
		        .setContentText(context.getString(R.string.default_diary_notification_msg))
		        .setDefaults(Notification.DEFAULT_ALL)
		        .setSound(Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.sound1))
		        .setAutoCancel(true)
		        .setOngoing(false) // requires VIBRATE permission
		        /*
		         * Sets the big view "big text" style and supplies the
		         * text (the user's reminder message) that will be displayed
		         * in the detail area of the expanded notification.
		         * These calls are ignored by the support library for
		         * pre-4.1 devices.
		         */
		        .setStyle(new NotificationCompat.BigTextStyle()
		                .bigText(context.getString(R.string.default_diary_notification_msg)));
	    
	    Intent targetIntent = new Intent(context, PINActivity.class);

	    PendingIntent contentIntent = PendingIntent.getActivity(context, 0, targetIntent, PendingIntent.FLAG_UPDATE_CURRENT);
	    builder.setContentIntent(contentIntent);
	    NotificationManager nManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
	    nManager.notify(NotificationUtils.REMINDER_NOTIFICATION_ID, builder.build());
	}
    }

    /**
     * Stop alarm which already have been scheduled(Not using currently).
     * 
     * @param context
     *            is the context based on which the alarm is stopping.
     * @param requestCode
     *            is the unique code for each notification, like diary, nitro
     *            and peak flow have different requestCode.
     * @return nothing
     */
 /*   private void stopAlarm(Context context, int requestCode)
    {
	Intent intent = new Intent(context, ReminderNotification.class);
	currentAlarm = PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_CANCEL_CURRENT);
	alarms = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
	alarms.cancel(currentAlarm);
	Log.d(TAG, "alarm concelled");
    }
*/
    /**
     * Sets the recurring alarm(Currently not using in the program).
     * <p>
     * Algorithm:<br>
     * The process works here as
     * <ul>
     * <li>
     * <li>For all kinds of alarms from the form content, the next alarm time is
     * defined without recurrence.</li>
     * <li>Flag_cancel is selected, so if the next alarm is reset, previous
     * alarm should be canceled if exists</li>
     * <li>Make sure that alarm is not canceled as soon as the devices shut
     * down.</li>
     * <li>Frequency of certain alarm is retrieved from database by query
     * specific types of notification.</li>
     * <li>Next alarm time is defined using Frequency* day + lastcollection (all
     * of the case, it should be now).</li>
     * </ul>
     * 
     * @param aContext
     *            which context the alarm will be settled
     * @param requestCode
     *            for each type of alarm, there should have unique requestCode.
     * @param lastcollection
     *            as the notification will be set while the work is done which
     *            also keeps track of last time taken time. Based on that the
     *            system can calculate the next alarm time.
     * @return nothing;
     */
    public void setNotificationAlarm(Context aContext, int requestCode, long lastcollection)
    {
	// Calendar alarmtimenext=Calendar.getInstance();
	String[] PROJECTION_COLUMNS = { FormContentData.FormContentItems.COLUMN_NAME_FREQ,
	        FormContentData.FormContentItems.COLUMN_NAME_LAST_COLLECTED_TIME };
	String selection = FormContentData.FormContentItems.COLUMN_NAME_FORM_NAME + "=?";
	String[] selArg = new String[1];
	Cursor cursor = null;
	Intent intent = new Intent(aContext, ReminderNotification.class);
	switch (requestCode)
	{
	case NotificationUtils.DIARY_NOTIFICATION_REQUEST_CODE:
	    intent.setAction(ACTION_DIARY);
	    selArg[0] = new String(DatabaseConstants.FORM_CONTENT_ID_DIARY);
	    cursor = aContext.getContentResolver().query(FormContentData.FormContentItems.CONTENT_URI, PROJECTION_COLUMNS, selection, selArg, null);
	    break;
	case NotificationUtils.NITRO_NOTIFICATION_REQUEST_CODE:
	    intent.setAction(ACTION_NITRO);
	    selArg[0] = new String(DatabaseConstants.FORM_CONTENT_ID_NITRO);
	    cursor = this.mContext.getContentResolver().query(FormContentData.FormContentItems.CONTENT_URI, PROJECTION_COLUMNS, selection, selArg,
		    null);
	    break;
	case NotificationUtils.AM_FLOW_NOTIFICATION_REQUEST_CODE:
	    intent.setAction(ACTION_AM);
	    selArg[0] = new String(DatabaseConstants.FORM_CONTENT_ID_AM_FLOW);
	    cursor = this.mContext.getContentResolver().query(FormContentData.FormContentItems.CONTENT_URI, PROJECTION_COLUMNS, selection, selArg,
		    null);
	    break;
	case NotificationUtils.PM_FLOW_NOTIFICATION_REQUEST_CODE:
	    intent.setAction(ACTION_PM);
	    selArg[0] = new String(DatabaseConstants.FORM_CONTENT_ID_PM_FLOW);
	    cursor = this.mContext.getContentResolver().query(FormContentData.FormContentItems.CONTENT_URI, PROJECTION_COLUMNS, selection, selArg,
		    null);
	    break;
	}
	// get the frequency from form_content database tables
	if (cursor != null)
	{
	    cursor.moveToFirst();
	    cursor.getInt(0);
	    cursor.getLong(1);
	}
	cursor.close();
	// Handle to SharedPreferences for this app
	mAlarms = (AlarmManager) aContext.getSystemService(Context.ALARM_SERVICE);
	/* mPrefs = aContext.getSharedPreferences(
	 * LocationUtils.SHARED_PREFERENCES, Context.MODE_PRIVATE); */
	mPrefs = PreferenceManager.getDefaultSharedPreferences(aContext);
	/* Just for testing purpose */
	SimpleDateFormat sdf = new SimpleDateFormat("hh:mm",Locale.US);
	mEditor = mPrefs.edit();
	Calendar testcal = Calendar.getInstance();
	testcal.add(Calendar.MINUTE, 2);
	mEditor.putString(aContext.getResources().getString(R.string.am_alarm_time), sdf.format(testcal.getTime()));
	testcal.add(Calendar.MINUTE, 2);
	mEditor.putString(aContext.getResources().getString(R.string.pm_alarm_time), sdf.format(testcal.getTime()));
	mEditor.commit();
	/**
	 * commented out after development and testing done
	 */
	Calendar cal = Calendar.getInstance();
	String am_time = mPrefs.getString(aContext.getResources().getString(R.string.am_alarm_time), "10:00");
	// cal.add(Calendar.DAY_OF_YEAR, frequency);
	cal.set(Calendar.HOUR_OF_DAY, TimePickerPreference.getHour(am_time));
	cal.set(Calendar.MINUTE, TimePickerPreference.getMinute(am_time));
	cal.set(Calendar.SECOND, 0);
	cal.set(Calendar.MILLISECOND, 0);
	mCurrentAlarm = PendingIntent.getBroadcast(aContext, requestCode, intent, PendingIntent.FLAG_CANCEL_CURRENT);
	mAlarms.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), mCurrentAlarm);
	Calendar cal_pm = Calendar.getInstance();
	String pm_time = mPrefs.getString(aContext.getResources().getString(R.string.pm_alarm_time), "16:00");
	// cal_pm.set(Calendar.DAY_OF_YEAR, frequency);
	cal_pm.set(Calendar.HOUR_OF_DAY, TimePickerPreference.getHour(pm_time));
	cal_pm.set(Calendar.MINUTE, TimePickerPreference.getMinute(pm_time));
	cal_pm.set(Calendar.SECOND, 0);
	cal_pm.set(Calendar.MILLISECOND, 0);
	// Intent intent = new Intent(aContext, LocationAlarmReceiver.class);
	// currentAlarm = PendingIntent.getBroadcast(aContext, requestCode,
	// intent, PendingIntent.FLAG_CANCEL_CURRENT);
	mAlarms.set(AlarmManager.RTC_WAKEUP, cal_pm.getTimeInMillis(), mCurrentAlarm);
	// Handle to a SharedPreferences editor
	// mEditor = mPrefs.edit();

    }

   /* *//**
     * Checks if two date are same day.
     * 
     * @param date_to_compare
     *            is the date which is present in the database and will compare
     *            to current date
     * @param dcurrent
     *            is the current date Calendar object @see java.util.Calendar
     * @return true, if two date are same day else false;
     *//*
    boolean isSameDay(Calendar date_to_compare, Calendar dcurrent)
    {
	boolean sameDay = false;
	sameDay = date_to_compare.get(Calendar.YEAR) == dcurrent.get(Calendar.YEAR)
	        && date_to_compare.get(Calendar.DAY_OF_YEAR) == dcurrent.get(Calendar.DAY_OF_YEAR);
	return sameDay;
    }

    *//**
     * Checks if a day is earlier than current date.
     * 
     * @param d_to_compare
     *            date_to_compare is the date which is present in the database
     *            and will compare to current date
     * @param dcurrent
     *            is the current date Calendar object @see java.util.Calendar
     * @return true, if the date is earlier than current date, otherwise return
     *         false
     *//*
    boolean isEarlier(Calendar d_to_compare, Calendar dcurrent)
    {
	boolean earlier = false;
	earlier = d_to_compare.get(Calendar.YEAR) == dcurrent.get(Calendar.YEAR)
	        && d_to_compare.get(Calendar.DAY_OF_YEAR) < dcurrent.get(Calendar.DAY_OF_YEAR);
	return earlier;
    }

    boolean isInTimeWindow(Calendar starttime, Calendar endtime)
    {
	Calendar currenttime = Calendar.getInstance();
	if (currenttime.after(starttime) && currenttime.before(endtime))
	    return true;
	else
	    return false;
    }*/

    /**
     * This method will decide whether to generate notification for the user or
     * not. If need to generate notification, then what type of message should
     * be included and what not to be included.
     * <p>
     * The method will decide about four types of notification condition for the
     * next notification message. If one item information is submitted for the
     * day then that message should not be included to the notification. This
     * will also add all the message together for notification and update the @link
     * {@link com.emmes.aps.ReminderNotification#mNotificationMsg} which will be
     * displayed to the user as notification.
     * <p>
     * <h3>Procedure</h3>
     * <ul>
     * <li>Retrieve frequency and last collection time from database for each
     * type of form from the database table</li>
     * <li>if frequency is greater than 0, then add number of days equal to
     * frequency with the last collected calendar object</li>
     * <li>Compare result date object with current date</li>
     * <li>If current date and updated last collected date is today or earlier,
     * add notification to the message. Otherwise ignore message</li>
     * <li>Return true if any one of those are true otherwise return false.</li>
     * 
     * </ul>
     * 
     * @return true, if any of the four notification have to be send to the
     *         user.
     */
    private boolean willNotifyUser()
    {
	String notificationMessage = "";
	boolean notifyDiary = false, notifyNitro = false, notifyAMFlow = false, notifyPMFlow = false;
	String[] PROJECTION_COLUMNS = { FormContentData.FormContentItems.COLUMN_NAME_FORM_NAME, FormContentData.FormContentItems.COLUMN_NAME_FREQ,
	        FormContentData.FormContentItems.COLUMN_NAME_LAST_COLLECTED_TIME, FormContentData.FormContentItems.COLUMN_NAME_WINDOW_START,
	        FormContentData.FormContentItems.COLUMN_NAME_WINDOW_END, FormContentData.FormContentItems.COLUMN_NAME_TIME_CATEGORY };
	int FORM_NAME = 0, FREQ = 1, LAST_COLLECTION = 2, WINDOW_START = 3, WINDOW_END = 4;
	Cursor cursor = mContext.getContentResolver().query(FormContentData.FormContentItems.CONTENT_URI, PROJECTION_COLUMNS, null, null, null);
	int freq = 0;
	Calendar cur_time = Calendar.getInstance();
	Calendar lastcollected = Calendar.getInstance();
	Calendar starttime = Calendar.getInstance();
	Calendar endtime = Calendar.getInstance();
	if (cursor != null)
	{
	    cursor.moveToFirst();
	    while (cursor.moveToNext())
	    {
		lastcollected = Calendar.getInstance();
		freq = cursor.getInt(FREQ);
		if (freq < 0)
		{
		    Log.d(TAG, "frequency is neqative");
		    return false;
		}
		lastcollected = CalendarUtils.dateString2Calendaryyyymmddhhmmss(cursor.getString(LAST_COLLECTION));
		// lastcollected.setTimeInMillis(cursor.getLong(LAST_COLLECTION));
		// add number of day*frequency for repeated alarm
		lastcollected.add(Calendar.DAY_OF_YEAR, freq);
		starttime.set(Calendar.HOUR_OF_DAY, TimePickerPreference.getHour(cursor.getString(WINDOW_START)));
		starttime.set(Calendar.MINUTE, TimePickerPreference.getMinute(cursor.getString(WINDOW_START)));
		starttime.set(Calendar.SECOND, 0);
		endtime.set(Calendar.HOUR_OF_DAY, TimePickerPreference.getHour(cursor.getString(WINDOW_END)));
		endtime.set(Calendar.MINUTE, TimePickerPreference.getMinute(cursor.getString(WINDOW_END)));
		endtime.set(Calendar.SECOND, 0);
		if (cursor.getString(FORM_NAME).equals(DatabaseConstants.FORM_CONTENT_ID_DIARY))
		{
		    if ((CalendarUtils.isSameDay(lastcollected, cur_time) || CalendarUtils.isEarlier(lastcollected, cur_time))
			    && CalendarUtils.isInTimeWindow(starttime, endtime))
		    {
			notifyDiary = true;
			// this.inboxStyle.addLine(sContext.getResources().getString(R.string.diary_notification_msg));

			notificationMessage = notificationMessage + mContext.getResources().getString(R.string.diary_notification_msg) + "\n";
		    }
		}
		else if (cursor.getString(FORM_NAME).equals(DatabaseConstants.FORM_CONTENT_ID_NITRO))
		{
		    if ((CalendarUtils.isSameDay(lastcollected, cur_time) || CalendarUtils.isEarlier(lastcollected, cur_time))
			    && CalendarUtils.isInTimeWindow(starttime, endtime))
		    {
			notifyNitro = true;
			// this.inboxStyle.addLine(sContext.getResources().getString(R.string.nitro_notification_msg));
			notificationMessage = notificationMessage + mContext.getResources().getString(R.string.nitro_notification_msg) + "\n";
		    }
		}
		else if (cursor.getString(FORM_NAME).equals(DatabaseConstants.FORM_CONTENT_ID_AM_FLOW))
		{
		    if ((CalendarUtils.isSameDay(lastcollected, cur_time) || CalendarUtils.isEarlier(lastcollected, cur_time))
			    && CalendarUtils.isInTimeWindow(starttime, endtime))
		    {
			notifyAMFlow = true;
			// this.inboxStyle.addLine(sContext.getResources().getString(R.string.am_notification_msg));
			notificationMessage = notificationMessage + mContext.getResources().getString(R.string.am_notification_msg) + "\n";
		    }
		}
		else if (cursor.getString(FORM_NAME).equals(DatabaseConstants.FORM_CONTENT_ID_PM_FLOW))
		{
		    if ((CalendarUtils.isSameDay(lastcollected, cur_time) || CalendarUtils.isEarlier(lastcollected, cur_time))
			    && CalendarUtils.isInTimeWindow(starttime, endtime))
		    {
			notifyPMFlow = true;
			// this.inboxStyle.addLine(sContext.getResources().getString(R.string.pm_notification_msg));
			notificationMessage = notificationMessage + mContext.getResources().getString(R.string.pm_notification_msg) + "\n";
		    }
		}
		// cursor.moveToNext();
	    }
	}
	cursor.close();
	// this.notificationMsg =
// sContext.getResources().getString(R.string.default_diary_notification_msg);
	this.mInboxStyle.addLine(mContext.getResources().getString(R.string.default_diary_notification_msg));
	return notifyDiary || notifyNitro || notifyAMFlow || notifyPMFlow;
    }

    /**
     * This static method will start the recurring alarm for reminder.
     * <p>
     * This method will create repeating alarm daily basis. It will accept the
     * argument of alarmStart time which will determine when the alarm will
     * first appear and alarmCode which will represents different alarms. As we
     * have morning alarm and evening alarm, we need two different alarmCode.
     * 
     * @param context
     *            which context the alarm will use to produce
     * @param alarmCode
     *            we have two types of alarm, morning alarm and evening alarm,
     *            for each alarm two separate code should be provided to create
     *            multiple alarm.
     */
    public static void setRepeatingReminder(Context context, int alarmCode)
    {
	/* mLastLocPrefs =
	 * SettingsActivity.sContext.getSharedPreferences(LocationUtils
	 * .SHARED_PREFERENCES, Context.MODE_PRIVATE);
	 * 
	 * mStaticEditor = mLastLocPrefs.edit();
	 * 
	 * 
	 * // get a Calendar object with current time Calendar cal =
	 * Calendar.getInstance(); // add 5 secs to the calendar object
	 * cal.add(cal.SECOND, LocationUtils.START_DELAY+5);
	 * 
	 * mStaticEditor.putLong(LocationUtils.TRACKING_START_TIME,
	 * cal.getTime().getTime()); cal.add(cal.SECOND, 100);
	 * mStaticEditor.putLong(LocationUtils.TRACKING_END_TIME,
	 * cal.getTime().getTime()); mStaticEditor.commit();
	 * 
	 * cal = Calendar.getInstance(); cal.add(Calendar.SECOND,
	 * LocationUtils.START_DELAY); */
	Calendar alarmStartTime = Calendar.getInstance();
	mLastLocPrefs = PreferenceManager.getDefaultSharedPreferences(SettingsActivity.sContext);
	/**
	 * Find out alarm start time of current alarmCode
	 */
	if (alarmCode == NotificationUtils.REMINDER_NOTIFICATION_MORNING_CODE)
	{
	    String am_time = NotificationUtils.DEFAULT_MORNING_NOTIFICATION_TIME;
	    am_time = mLastLocPrefs.getString(SettingsActivity.sContext.getResources().getString(R.string.am_alarm_time),
		    NotificationUtils.DEFAULT_MORNING_NOTIFICATION_TIME);
	    // cal.add(Calendar.DAY_OF_YEAR, frequency);
	    alarmStartTime.set(Calendar.HOUR_OF_DAY, TimePickerPreference.getHour(am_time));
	    alarmStartTime.set(Calendar.MINUTE, TimePickerPreference.getMinute(am_time));
	    alarmStartTime.set(Calendar.SECOND, 0);
	    alarmStartTime.set(Calendar.MILLISECOND, 0);
	    Log.d(TAG, "morning repeating alarm time set");
	}
	else if (alarmCode == NotificationUtils.REMINDER_NOTIFICATION_EVENING_CODE)
	{
	    String pm_time = NotificationUtils.DEFAULT_EVENING_NOTIFICATION_TIME;
	    pm_time = mLastLocPrefs.getString(SettingsActivity.sContext.getResources().getString(R.string.pm_alarm_time),
		    NotificationUtils.DEFAULT_EVENING_NOTIFICATION_TIME);
	    // cal.add(Calendar.DAY_OF_YEAR, frequency);
	    alarmStartTime.set(Calendar.HOUR_OF_DAY, TimePickerPreference.getHour(pm_time));
	    alarmStartTime.set(Calendar.MINUTE, TimePickerPreference.getMinute(pm_time));
	    alarmStartTime.set(Calendar.SECOND, 0);
	    alarmStartTime.set(Calendar.MILLISECOND, 0);
	    Log.d(TAG, "evening repeating alarm time set");
	}
	else
	{
	    Log.d(TAG, "Invalid alarm code received");
	}
	/**
	 * Set alarm on selected start time with day interval
	 */
	Intent intent = new Intent(context, ReminderNotification.class);
	mStaticAlarms = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
	mReminder = PendingIntent.getBroadcast(context, alarmCode, intent, PendingIntent.FLAG_CANCEL_CURRENT);
	mStaticAlarms.setInexactRepeating(AlarmManager.RTC_WAKEUP, alarmStartTime.getTimeInMillis(), AlarmManager.INTERVAL_DAY, mReminder);
	Log.d(TAG, ">>>recurring remider started");
	// Enable {@code SampleBootReceiver} to automatically
	// restart the alarm when the // device is rebooted.
	ComponentName receiver = new ComponentName(context, LocationBootReceiver.class);
	PackageManager pm = context.getPackageManager();
	pm.setComponentEnabledSetting(receiver, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
    }

    /**
     * Stop recurring alarm.
     * 
     * @param context
     *            the context
     * @param alarmCode
     *            the alarm code
     */
    private static void stopRecurringAlarm(Context context, int alarmCode)
    {
	Intent intent = new Intent(context, ReminderNotification.class);
	mReminder = PendingIntent.getBroadcast(SettingsActivity.sContext, alarmCode, intent, PendingIntent.FLAG_CANCEL_CURRENT);
	mStaticAlarms = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
	mStaticAlarms.cancel(mReminder);
	Log.d(TAG, "recurring alarm stopped");
	NotificationManager nManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
	nManager.cancel(alarmCode);
	Log.d(TAG, "notification " + alarmCode + "stopped");
    }
}
