/* 
  * Copyright 2014 (C) The EMMES Corporation 
  *  
  * Created on : 02-06-2014
  * Last Update: Jul 14, 2014 11:55:52 AM
  * Author     : Mahbubur Rahman
  * Title      : Summer intern 2014 
  * Project    : Daily Diary Android Application
  * 
  */


package com.emmes.aps.util;

// TODO: Auto-generated Javadoc
/**
 * Defines app-wide constants and utilities.
 * Data status is maintained for local data sync with server.
 * When the data is inserted or the insertion part is going on, The status is defined as Incomplete or I.
 * When the data is ready for sync because the date is expired or the user submit button is pressed, The data status becomes Ready to Sync (S).
 * When the data is trying to send some data to server over the network, The data transfer may fail or may succeed, but need to keep track of that. The 
 * data which was in transition state is represents with the status IN_QUEUE (Q).
 * If data transfer successfully, The status is set as Complete or C
 * 
 * @author Mahbubur Rahman
 * @version 1.0
 */
public final class DataTransferUtils {

	 /** The Constant STATUS_DATA_TRANSFER_INCOMPLETE. */
 	public static final String STATUS_DATA_TRANSFER_INCOMPLETE = "I";
	 
	 /** The Constant STATUS_DATA_TRANSFER_READY. */
 	public static final String STATUS_DATA_TRANSFER_READY = "S";
	 
	 /** The Constant STATUS_DATA_TRANSFER_IN_QUEUE. */
 	public static final String STATUS_DATA_TRANSFER_IN_QUEUE = "Q";
	 
	 /** The Constant STATUS_DATA_TRANSFER_COMPLETE. */
 	public static final String STATUS_DATA_TRANSFER_COMPLETE="C";
}
