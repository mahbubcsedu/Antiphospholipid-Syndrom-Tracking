/* 
  * Copyright 2014 (C) The EMMES Corporation 
  *  
  * Created on : 02-06-2014
  * Last Update: Jul 2, 2014 1:11:31 PM
  * Author     : Mahbubur Rahman
  * Title      : Summer intern 2014 
  * Project    : Daily Diary Android Application
  * 
  */
package com.emmes.aps.sync;

// TODO: Auto-generated Javadoc
/**
 * The Class LocTrackDataFormat.
 */
public class LocTrackDataFormat {

/** The start date. */
String startDate;

/** The end date. */
String endDate;

/** The entered time. */
String enteredTime;

/**
 * Instantiates a new loc track data format.
 *
 * @param startDate the start date
 * @param endDate the end date
 * @param enteredTime the entered time
 */
public LocTrackDataFormat(String startDate, String endDate, String enteredTime) {
	super();
	this.startDate = startDate;
	this.endDate = endDate;
	this.enteredTime = enteredTime;
}

/**
 * Gets the start date.
 *
 * @return the start date
 */
public String getStartDate() {
	return startDate;
}

/**
 * Sets the start date.
 *
 * @param startDate the new start date
 */
public void setStartDate(String startDate) {
	this.startDate = startDate;
}

/**
 * Gets the end date.
 *
 * @return the end date
 */
public String getEndDate() {
	return endDate;
}

/**
 * Sets the end date.
 *
 * @param endDate the new end date
 */
public void setEndDate(String endDate) {
	this.endDate = endDate;
}

/**
 * Gets the entered time.
 *
 * @return the entered time
 */
public String getEnteredTime() {
	return enteredTime;
}

/**
 * Sets the entered time.
 *
 * @param enteredTime the new entered time
 */
public void setEnteredTime(String enteredTime) {
	this.enteredTime = enteredTime;
}
}
