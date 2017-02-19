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
 * The Class LocationDataFormat.
 */
public class LocationDataFormat {
	
	/** The latitude. */
	double latitude;
	
	/** The longitude. */
	double longitude;
	
	/** The timeofcollection. */
	long timeofcollection;
	
	/** The db id. */
	int dbId;
	
	/**
	 * Gets the db id.
	 *
	 * @return the db id
	 */
	public int getDbId() {
		return dbId;
	}
	
	/**
	 * Sets the db id.
	 *
	 * @param dbId the new db id
	 */
	public void setDbId(int dbId) {
		this.dbId = dbId;
	}
	
	/**
	 * Instantiates a new location data format.
	 *
	 * @param latitude the latitude
	 * @param longitude the longitude
	 * @param timeofcollection the timeofcollection
	 */
	public LocationDataFormat(double latitude, double longitude,
			long timeofcollection) {
		super();
		this.latitude = latitude;
		this.longitude = longitude;
		this.timeofcollection = timeofcollection;
	}
	
	/**
	 * Instantiates a new location data format.
	 */
	public LocationDataFormat() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Gets the timeofcollection.
	 *
	 * @return the timeofcollection
	 */
	public long getTimeofcollection() {
		return timeofcollection;
	}
	
	/**
	 * Sets the timeofcollection.
	 *
	 * @param timeofcollection the new timeofcollection
	 */
	public void setTimeofcollection(long timeofcollection) {
		this.timeofcollection = timeofcollection;
	}
	
	/**
	 * Gets the latitude.
	 *
	 * @return the latitude
	 */
	public double getLatitude() {
		return latitude;
	}
	
	/**
	 * Sets the latitude.
	 *
	 * @param latitude the new latitude
	 */
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	
	/**
	 * Gets the longitude.
	 *
	 * @return the longitude
	 */
	public double getLongitude() {
		return longitude;
	}
	
	/**
	 * Sets the longitude.
	 *
	 * @param longitude the new longitude
	 */
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

}
