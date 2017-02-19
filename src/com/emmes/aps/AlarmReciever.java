/* 
  * Copyright 2014 (C) The EMMES Corporation 
  *  
  * Created on : 02-06-2014
  * Last Update: Aug 19, 2014 3:17:45 PM
  * Author     : Mahbubur Rahman, Omid Neyzari, Glenn Tucker
  * Title      : Summer intern 2014 
  * Project    : Daily Diary Android Application
  * 
  */
package com.emmes.aps;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

// TODO: Auto-generated Javadoc
/**
 * The Class AlarmReciever.
 */
public class AlarmReciever extends BroadcastReceiver
{
	
	/* (non-Javadoc)
	 * @see android.content.BroadcastReceiver#onReceive(android.content.Context, android.content.Intent)
	 */
	@Override
	public void onReceive(Context context, Intent intent)
	{

		// Get the current activity to determine if app is in use or not
		ActivityManager am = (ActivityManager) context.getSystemService("activity");
		List<RunningTaskInfo> taskInfo = am.getRunningTasks(1);
		ComponentName componentInfo = taskInfo.get(0).topActivity;
		
		// Do not display reminder notifications when the app is actively used
		if (!componentInfo.getPackageName().equals(this.getClass().getPackage().getName())) {

			int icon = R.drawable.ic_launcher;
			CharSequence notiTickerText = context.getString(R.string.reminder_ticker);    
			CharSequence notiTitle = context.getString(R.string.app_name);  
			CharSequence notiContent = context.getString(R.string.reminder_content);


			// So that Back button will clear the reminder rather than return to 
			// what scheduled the alarm (currently PINActivity)
			// notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
			// notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

			// Use to open an activity as notification 
			// Canceling out displays last app activity in stack which is a problem 
			//Intent notificationIntent = new Intent(context, ReminderActivity.class);
			//PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);

			Intent bannerIntent = new Intent(context, MainActivity.class);
			PendingIntent pendingbannerIntent = PendingIntent.getActivity(context, 0, bannerIntent, 0);

			Notification.Builder notiBuilder = new Notification.Builder(context);
			notiBuilder
			.setSmallIcon(icon)
			.setTicker(notiTickerText)
			.setWhen(System.currentTimeMillis())
			.setAutoCancel(true)
			.setContentText(notiContent)
			.setContentTitle(notiTitle)
			//.setFullScreenIntent(pendingIntent,true)
			.setContentIntent(pendingbannerIntent);
			
			Notification notification = notiBuilder.build();	
			String ns = Context.NOTIFICATION_SERVICE;
			NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(ns);
			mNotificationManager.notify(-2, notification);

		}

	}

}
