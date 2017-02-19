/* 
  * Copyright 2014 (C) The EMMES Corporation 
  *  
  * Created on : 02-06-2014
  * Last Update: Jul 2, 2014 1:07:02 PM
  * Author     : Mahbubur Rahman
  * Title      : Summer intern 2014 
  * Project    : Daily Diary Android Application
  * 
  */
package com.emmes.aps.data;

// TODO: Auto-generated Javadoc
/**
 * The Class DatabaseConstants.
 */

public final class DatabaseConstants implements java.io.Serializable{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;


	/** The Constant ITEM_TYPE_MEDICATION. */
	public static final String ITEM_TYPE_MEDICATION = "medication";	// The Content Provider Authority
	
	
	/** The item type supplement. */
	public static String ITEM_TYPE_SUPPLEMENT = "supplement";
	
	/** The location_update_time_interval. */
	public static long location_update_time_interval=1500;
	
	/** The location_update_distance_interval_in_meter. */
	public static float location_update_distance_interval_in_meter=1;
	
	/** The form content id diary. */
	public static String FORM_CONTENT_ID_DIARY="diary";
	
	/** The form content id nitro. */
	public static String FORM_CONTENT_ID_NITRO="nitro";
	
	/** The form content id am flow. */
	public static String FORM_CONTENT_ID_AM_FLOW="flow_am";
	
	/** The form content id pm flow. */
	public static String FORM_CONTENT_ID_PM_FLOW="flow_pm";
	
}
