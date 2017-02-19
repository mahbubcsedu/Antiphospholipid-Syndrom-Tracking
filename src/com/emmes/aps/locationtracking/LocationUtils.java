/* 
  * Copyright 2014 (C) The EMMES Corporation 
  *  
  * Created on : 02-06-2014
  * Last Update: Jun 18, 2014 9:05:20 AM
  * Author     : Mahbubur Rahman
  * Title      : Summer intern 2014 
  * Project    : Daily Diary Android Application
  * 
  */


package com.emmes.aps.locationtracking;

// TODO: Auto-generated Javadoc
/**
 * Defines app-wide constants and utilities.
 */
public final class LocationUtils {

    // Debugging tag for the application
    /** The Constant APPTAG. */
    public static final String APPTAG = "LastLocationUpdated";

    // Name of shared preferences repository that stores persistent state
    /** The Constant SHARED_PREFERENCES. */
    public static final String SHARED_PREFERENCES =
            "com.emmes.aps.locationtracking.SHARED_PREFERENCES";

    // Key for storing the "updates requested" flag in shared preferences
    /** The Constant KEY_UPDATES_REQUESTED. */
    public static final String KEY_UPDATES_REQUESTED =
            "com.emmes.aps.locationtracking.KEY_UPDATES_REQUESTED";
    
    /** The Constant GPS_LATITUDE_PREF. */
    public static final String GPS_LATITUDE_PREF="com.emmes.aps.locationtracking.GPSLATITUDE";
    
    /** The Constant GPS_LONGITUDE_PREF. */
    public static final String GPS_LONGITUDE_PREF="com.emmes.aps.locationtracking.GPSLONGITUDE";

    /*
     * Define a request code to send to Google Play services
     * This code is returned in Activity.onActivityResult
     */
    /** The Constant CONNECTION_FAILURE_RESOLUTION_REQUEST. */
    public final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    
    /** The Constant LOCATION_TRACKING_NOTIFICATION_ID. */
    public final static int LOCATION_TRACKING_NOTIFICATION_ID=761453;
    
    // in min
    /** The Constant START_DELAY. */
    public final static int START_DELAY=5;
    
    //in milliseconds
    /** The Constant ALARM_UPDATE_INTERVAL_MILLISECONDS. */
    public final static int ALARM_UPDATE_INTERVAL_MILLISECONDS=20000;

    /*
     * Constants for location update parameters
     */
    // Milliseconds per second
    /** The Constant MILLISECONDS_PER_SECOND. */
    public static final int MILLISECONDS_PER_SECOND = 1000;

    // The update interval
    /** The Constant UPDATE_INTERVAL_IN_SECONDS. */
    public static final int UPDATE_INTERVAL_IN_SECONDS = 5;

    // A fast interval ceiling
    /** The Constant FAST_CEILING_IN_SECONDS. */
    public static final int FAST_CEILING_IN_SECONDS = 1;

    // Update interval in milliseconds
    /** The Constant UPDATE_INTERVAL_IN_MILLISECONDS. */
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS =
            MILLISECONDS_PER_SECOND * UPDATE_INTERVAL_IN_SECONDS;

    // A fast ceiling of update intervals, used when the app is visible
    /** The Constant FAST_INTERVAL_CEILING_IN_MILLISECONDS. */
    public static final long FAST_INTERVAL_CEILING_IN_MILLISECONDS =
            MILLISECONDS_PER_SECOND * FAST_CEILING_IN_SECONDS;

    // Create an empty string for initializing strings
    /** The Constant EMPTY_STRING. */
    public static final String EMPTY_STRING = new String();
    
    /** The Constant MIN_DISTANCE_TO_STORE. */
    public static final double MIN_DISTANCE_TO_STORE=0.1;//miles
    
    /** The Constant TRACKING_START_TIME. */
    public static final String TRACKING_START_TIME="start_date";
    
    /** The Constant TRACKING_END_TIME. */
    public static final String TRACKING_END_TIME="end_date";
    
    /** The Constant SPREF_MOBILITY_CONSENT. */
    public static final String SPREF_MOBILITY_CONSENT="pref_consent";
    
    /** The Constant ASK_FOR_CONSENT_AGAIN. */
    public static final String ASK_FOR_CONSENT_AGAIN="pref.should.i.ask";
    /**
     * when to ask again for consent and this should be greater than end time of current range.
     */
    public static final String TIME_TO_ASK_AGAIN_FOR_CONSENT="com.emmes.aps.askagain";
    
    /** The Constant DIRECTING_FROM_LOCATION_RECEIVER_FOR_CONSENT. */
    public static final String DIRECTING_FROM_LOCATION_RECEIVER_FOR_CONSENT="com.emmes.aps.location.directo.consent";

    /**
     * Get the latitude and longitude from the Location object returned by
     * Location Services.
     *
     * @param currentLocation A Location object containing the current location
     * @return The latitude and longitude of the current location, or null if no
     * location is available.
     */
   
}
