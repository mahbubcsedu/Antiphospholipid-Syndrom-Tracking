/* 
  * Copyright 2014 (C) The EMMES Corporation 
  *  
  * Created on : 02-06-2014
  * Last Update: Aug 18, 2014 4:33:07 PM
  * Author     : Mahbubur Rahman
  * Title      : Summer intern 2014 
  * Project    : Daily Diary Android Application
  * 
  */
package com.emmes.aps.util;

import android.app.TimePickerDialog;
import android.content.Context;
import android.widget.TimePicker;

// TODO: Auto-generated Javadoc
/**
 * The Class HoursMinutesPickerDialog.
 *
 * @author mrahman
 */
public class HoursMinutesPickerDialog extends TimePickerDialog
{

    /**
     * Instantiates a new hours minutes picker dialog.
     *
     * @param context the context
     * @param callBack the call back
     * @param hourOfDay the hour of day
     * @param minute the minute
     * @param is24HourView the is24 hour view
     */
    public HoursMinutesPickerDialog(Context context, OnTimeSetListener callBack, int hourOfDay, int minute,
	    boolean is24HourView) {
	super(context, callBack, hourOfDay, minute, is24HourView);
	
	// TODO Auto-generated constructor stub
    }

    /* (non-Javadoc)
     * @see android.app.TimePickerDialog#onTimeChanged(android.widget.TimePicker, int, int)
     */
    @Override
    public void onTimeChanged(TimePicker view, int hourOfDay, int minute)
    {
	// TODO Auto-generated method stub
	//super.onTimeChanged(view, hourOfDay, minute);
    }

}
