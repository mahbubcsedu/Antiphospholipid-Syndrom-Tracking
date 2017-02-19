/* 
 * Copyright 2014 (C) The EMMES Corporation 
 *  
 * Created on : 02-06-2014
 * Last Update: Jul 8, 2014 2:23:08 PM
 * Author     : Mahbubur Rahman
 * Title      : Summer intern 2014 
 * Project    : Daily Diary Android Application
 * 
 */
package android.preference;

import java.util.Calendar;

import com.emmes.aps.PINActivity;
import com.emmes.aps.R;
import com.emmes.aps.data.DatabaseConstants;
import com.emmes.aps.data.FormContentData;
import com.emmes.aps.locationtracking.LocationAlarmReceiver;
import com.emmes.aps.locationtracking.LocationUtils;
import com.emmes.aps.util.NotificationUtils;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

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
public class ReminderNotification extends BroadcastReceiver {

	/** The Constant TAG. */
	public static final String TAG = "ReminderNotification";

	static final String ACTION_DIARY = "diary_notification";
	static final String ACTION_NITRO = "nitro_notification";
	static final String ACTION_AM = "am_notification";
	static final String ACTION_PM = "pm_notification";

	/** mLastPref is an instance of SharePreferences of the app. */
	SharedPreferences mPrefs;
	/** The mEditor is an instance of editor. */
	SharedPreferences.Editor mEditor;
	/**
	 * {@link android.app.PendingIntent}
	 */
	PendingIntent currentAlarm;

	/** The context. */
	Context context;

	/** The alarm manager instance. */
	private AlarmManager alarms;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.content.BroadcastReceiver#onReceive(android.content.Context,
	 * android.content.Intent)
	 */
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub

		this.context = context;
		String action = intent.getAction();
		String msg = "";

		Log.d(TAG,"action caught="+action);
		
		if (action.equals(ACTION_DIARY))
			msg = context.getResources().getString(
					R.string.diary_notification_msg);
		else if (action.equals(ACTION_NITRO))
			msg = context.getResources().getString(
					R.string.nitro_notification_msg);
		else if (action.equals(ACTION_AM))
			msg = context.getResources()
					.getString(R.string.am_notification_msg);
		else if (action.equals(ACTION_PM))
			msg = context.getResources()
					.getString(R.string.pm_notification_msg);

		NotificationCompat.Builder builder = new NotificationCompat.Builder(
				context)
				.setSmallIcon(R.drawable.track_loc)
				.setContentTitle(
						context.getResources().getString(
								R.string.notification_title))
				.setContentText(msg)
				.setSound(
						Uri.parse("android.resource://"
								+ context.getPackageName() + "/" + R.raw.sound1))
				.setAutoCancel(true).setOngoing(false);
		Log.d(TAG,"message selected ="+msg);
		Intent targetIntent = new Intent(context, PINActivity.class);
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
				targetIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		builder.setContentIntent(contentIntent);
		NotificationManager nManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		nManager.notify(LocationUtils.LOCATION_TRACKING_NOTIFICATION_ID,
				builder.build());

	}

	/**
	 * Stop alarm which already have been scheduled.
	 * 
	 * @param context
	 *            is the context based on which the alarm is stopping.
	 * @param requestCode
	 *            is the unique code for each notification, like diary, nitro
	 *            and peak flow have different requestCode.
	 * @return nothing
	 * @throws none
	 */
	private void stopAlarm(Context context, int requestCode) {
		Intent intent = new Intent(context, ReminderNotification.class);
		currentAlarm = PendingIntent.getBroadcast(context, requestCode, intent,
				PendingIntent.FLAG_CANCEL_CURRENT);
		alarms = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		alarms.cancel(currentAlarm);
		Log.d(TAG, "alarm concelled");

	}

	/**
	 * Sets the recurring alarm.
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
	 * @throws none
	 * @return nothing;
	 */
	public void setNotificationAlarm(Context aContext, int requestCode,
			long lastcollection) {

		// TODO have to calculate time for each time of alarm and set the next
		// alarm time to user defined morning and evening time together with the
		// frequency*24hours time

		
		 
		 
		Long timeofnextAlarm;
		int frequency = 0;
		// Calendar alarmtimenext=Calendar.getInstance();
		String[] PROJECTION_COLUMNS = {
				FormContentData.FormContentItems.COLUMN_NAME_FREQ,
				FormContentData.FormContentItems.COLUMN_NAME_LAST_COLLECTED_TIME };
		String selection = FormContentData.FormContentItems.COLUMN_NAME_FORM_NAME
				+ "=?";
		String[] selArg = new String[1];
		Cursor cursor = null;
		Intent intent = new Intent(aContext, LocationAlarmReceiver.class);

		switch (requestCode) {
		case NotificationUtils.DIARY_NOTIFICATION_REQUEST_CODE:
			intent.setAction(ACTION_DIARY);
			selArg[0] = new String(DatabaseConstants.FORM_CONTENT_ID_DIARY);

			cursor = aContext.getContentResolver().query(
					FormContentData.FormContentItems.CONTENT_URI,
					PROJECTION_COLUMNS, selection, selArg, null);
			break;
		case NotificationUtils.NITRO_NOTIFICATION_REQUEST_CODE:
			intent.setAction(ACTION_NITRO);
			selArg[0] = new String(DatabaseConstants.FORM_CONTENT_ID_NITRO);
			cursor = this.context.getContentResolver().query(
					FormContentData.FormContentItems.CONTENT_URI,
					PROJECTION_COLUMNS, selection, selArg, null);
			break;

		case NotificationUtils.AM_FLOW_NOTIFICATION_REQUEST_CODE:
			intent.setAction(ACTION_AM);
			selArg[0] = new String(DatabaseConstants.FORM_CONTENT_ID_AM_FLOW);
			cursor = this.context.getContentResolver().query(
					FormContentData.FormContentItems.CONTENT_URI,
					PROJECTION_COLUMNS, selection, selArg, null);
			break;
		case NotificationUtils.PM_FLOW_NOTIFICATION_REQUEST_CODE:
			intent.setAction(ACTION_PM);
			selArg[0] = new String(DatabaseConstants.FORM_CONTENT_ID_PM_FLOW);
			cursor = this.context.getContentResolver().query(
					FormContentData.FormContentItems.CONTENT_URI,
					PROJECTION_COLUMNS, selection, selArg, null);
			break;

		}
		// get the frequency from form_content database tables
		if (cursor != null) {
			cursor.moveToFirst();
			frequency = cursor.getInt(0);
			timeofnextAlarm = cursor.getLong(1);
			/*while (cursor.isAfterLast()) {
				frequency = cursor.getInt(0);
				timeofnextAlarm = cursor.getLong(1);
				cursor.moveToNext();
			}*/
		}
		cursor.close();
		// Handle to SharedPreferences for this app
		alarms = (AlarmManager) aContext
				.getSystemService(Context.ALARM_SERVICE);
		/*mPrefs = aContext.getSharedPreferences(
				LocationUtils.SHARED_PREFERENCES, Context.MODE_PRIVATE);*/

		mPrefs = PreferenceManager.getDefaultSharedPreferences(aContext);
		
		Calendar cal = Calendar.getInstance();

		String am_time = mPrefs.getString(aContext.getResources().getString(R.string.am_alarm_time), "10:00");
		//cal.add(Calendar.DAY_OF_YEAR, frequency);
		cal.set(Calendar.HOUR_OF_DAY, TimePickerPreference.getHour(am_time));
		cal.set(Calendar.MINUTE, TimePickerPreference.getMinute(am_time));
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);

		currentAlarm = PendingIntent.getBroadcast(aContext, requestCode,
				intent, PendingIntent.FLAG_CANCEL_CURRENT);

		alarms.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), currentAlarm);

		Calendar cal_pm = Calendar.getInstance();

		String pm_time = mPrefs.getString(aContext.getResources().getString(R.string.pm_alarm_time), "16:00");
		//cal_pm.set(Calendar.DAY_OF_YEAR, frequency);
		cal_pm.set(Calendar.HOUR_OF_DAY, TimePickerPreference.getHour(pm_time));
		cal_pm.set(Calendar.MINUTE, TimePickerPreference.getMinute(pm_time));
		cal_pm.set(Calendar.SECOND, 0);
		cal_pm.set(Calendar.MILLISECOND, 0);

		// Intent intent = new Intent(aContext, LocationAlarmReceiver.class);

		// currentAlarm = PendingIntent.getBroadcast(aContext, requestCode,
		// intent, PendingIntent.FLAG_CANCEL_CURRENT);

		alarms.set(AlarmManager.RTC_WAKEUP, cal_pm.getTimeInMillis(),
				currentAlarm);

		// Handle to a SharedPreferences editor

		// mEditor = mPrefs.edit();
		/**
		 * For test, create two times just 5 seconds of current time and then 15
		 * seconds of current time as morning and evening time alarm
		 */
		/*
		 * Calendar cal = Calendar.getInstance(); cal.add(Calendar.SECOND, 5);
		 * SimpleDateFormat sdf=new SimpleDateFormat("hh:mm");
		 * mEditor.putString("alarm_morning", sdf.format(cal.getTime()));
		 * cal.add(Calendar.SECOND, 15); mEditor.putString("alarm_evening",
		 * sdf.format(cal.getTime())); mEditor.commit();
		 */
		/*
		 * Intent tracking = new Intent(context, ServiceLocationUpdate.class);
		 * context.startService(tracking);
		 */

		/*
		 * cal = Calendar.getInstance(); cal.add(Calendar.SECOND,
		 * LocationUtils.START_DELAY); Intent intent = new Intent(aContext,
		 * LocationAlarmReceiver.class);
		 * 
		 * currentAlarm = PendingIntent.getBroadcast(aContext, requestCode,
		 * intent, PendingIntent.FLAG_CANCEL_CURRENT);
		 * 
		 * alarms.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
		 * currentAlarm); // Enable {@code SampleBootReceiver} to automatically
		 * restart the alarm when the // device is rebooted. ComponentName
		 * receiver = new ComponentName(aContext, LocationBootReceiver.class);
		 * PackageManager pm = aContext.getPackageManager();
		 * 
		 * pm.setComponentEnabledSetting(receiver,
		 * PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
		 * PackageManager.DONT_KILL_APP);
		 */

	}

}
