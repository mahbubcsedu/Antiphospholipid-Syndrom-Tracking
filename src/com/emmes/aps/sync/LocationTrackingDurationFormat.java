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
 * The Class LocationTrackingDurationFormat.
 */
public class LocationTrackingDurationFormat {
	
	/** The id. */
	String id;
	
	/** The start date. */
	String startDate;
	
	/** The end date. */
	String endDate;
	
	/** The entry time. */
	String entryTime;


	/**
	 * Instantiates a new location tracking duration format.
	 *
	 * @param id the id
	 * @param startDate the start date
	 * @param endDate the end date
	 * @param entryTime the entry time
	 */
	public LocationTrackingDurationFormat(String id, String startDate, String endDate,
			String entryTime) {
		super();
		this.id = id;
		this.startDate = startDate;
		this.endDate = endDate;
		this.entryTime = entryTime;
	}
	
	/**
	 * Instantiates a new location tracking duration format.
	 */
	public LocationTrackingDurationFormat() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * Gets the start date.
	 *
	 * @return the start date
	 */
	public String getStartDate() 
	{
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
	 * Gets the entry time.
	 *
	 * @return the entry time
	 */
	public String getEntryTime() {
		return entryTime;
	}
	
	/**
	 * Sets the entry time.
	 *
	 * @param entryTime the new entry time
	 */
	public void setEntryTime(String entryTime) {
		this.entryTime = entryTime;
	}
}
