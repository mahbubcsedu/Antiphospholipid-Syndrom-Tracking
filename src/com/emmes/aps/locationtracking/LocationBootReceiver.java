/* 
  * Copyright 2014 (C) The EMMES Corporation 
  *  
  * Created on : 02-06-2014
  * Last Update: Jun 18, 2014 9:05:31 AM
  * Author     : Mahbubur Rahman
  * Title      : Summer intern 2014 
  * Project    : Daily Diary Android Application
  * 
  */

package com.emmes.aps.locationtracking;

import com.emmes.aps.ReminderNotification;
import com.emmes.aps.util.NotificationUtils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

// TODO: Auto-generated Javadoc
/**
 * This BroadcastReceiver automatically (re)starts the alarm when the device is
 * rebooted. This receiver is set to be disabled (android:enabled="false") in the
 * application's manifest file. When the user sets the alarm, the receiver is enabled.
 * When the user cancels the alarm, the receiver is disabled, so that rebooting the
 * device will not trigger this receiver.
 */
// BEGIN_INCLUDE(autostart)
public class LocationBootReceiver extends BroadcastReceiver {
    
    /** The alarm. */
    LocationAlarmReceiver alarm = new LocationAlarmReceiver();
   
    
    /* (non-Javadoc)
     * @see android.content.BroadcastReceiver#onReceive(android.content.Context, android.content.Intent)
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED"))
        {
            alarm.setRecurringAlarm(context);
            ReminderNotification.setRepeatingReminder(context, NotificationUtils.REMINDER_NOTIFICATION_MORNING_CODE);
            ReminderNotification.setRepeatingReminder(context, NotificationUtils.REMINDER_NOTIFICATION_EVENING_CODE);
        }
    }
}
//END_INCLUDE(autostart)
