/* 
  * Copyright 2014 (C) The EMMES Corporation 
  *  
  * Created on : 02-06-2014
  * Last Update: Jul 2, 2014 1:07:03 PM
  * Author     : Mahbubur Rahman
  * Title      : Summer intern 2014 
  * Project    : Daily Diary Android Application
  * 
  */
package com.emmes.aps.data;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;

// TODO: Auto-generated Javadoc
/**
 * The Class LocationTrackingData.
 *
 * @author mrahman
 */

public final class LocationTrackingData implements java.io.Serializable{
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The Constant AUTHORITY. */
	public static final String AUTHORITY = "com.emmes.aps";	// The Content Provider Authority

	/** The sdf. */
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy:MM:dd");

	/** The entry_time. */
	public long entry_time;	


	/**
	 * Instantiates a new available medication info in db.
	 *
	 * @param values the values
	 */
	public LocationTrackingData(ContentValues values){
		entry_time = values.getAsLong(LocationTrackingData.LocationPoint.COLUMN_NAME_ENTRYTIME);
	}

	/**
	 * Instantiates a new available medication info in db.
	 *
	 * @param date the date
	 */
	public LocationTrackingData(long date ){
		this.entry_time = date;
	}	

	/*
	 * Returns a ContentValues instance (a map) for this diaryInfo instance. This is useful for
	 * inserting a diaryInfo into a database.
	 */
	/**
	 * Gets the content values.
	 *
	 * @return the content values
	 */
	public ContentValues getContentValues() {
		// Gets a new ContentValues object
		ContentValues v = new ContentValues();

		// Adds map entries for the user-controlled fields in the map
		v.put(LocationTrackingData.LocationPoint.COLUMN_NAME_ENTRYTIME, entry_time);
		return v;

	}    	



	/**
	 * Constant definition to define the mapping of a Diary to the underlying database
	 * Also provides constants to help define the Content Provider.
	 */
	public static final class LocationPoint implements BaseColumns {

		// This class cannot be instantiated
		/**
		 * Instantiates a new DB medication list item.
		 */
		private LocationPoint() {}

		/** The table name offered by this provider. */
		public static final String TABLE_NAME = "tb_location";

		/*
		 * URI definitions
		 */

		/** The scheme part for this provider's URI. */
		private static final String SCHEME = "content://";

		/** Path parts for the URIs. */

		/**
		 * Path part for the diary URI
		 */
		private static final String PATH_LOCATION_IN_DB = "/tb_location";

		/** Path part for the diary ID URI. */
		private static final String PATH_LOCATION_IN_DB_ID = "/tb_location/";

		/** 0-relative position of a diary item ID segment in the path part of a diary item ID URI. */
		public static final int LOCATION_IN_DB_ID_PATH_POSITION = 1;


		/** The content:// style URL for this table. */
		public static final Uri CONTENT_URI =  Uri.parse(SCHEME + AUTHORITY + PATH_LOCATION_IN_DB);

		/**
		 * The content URI base for a single diary item. Callers must
		 * append a numeric diary id to this Uri to retrieve an diary item
		 */
		public static final Uri CONTENT_ID_URI_BASE
		= Uri.parse(SCHEME + AUTHORITY + PATH_LOCATION_IN_DB_ID);        

		/** The default sort order for this table. */
		public static final String DEFAULT_SORT_ORDER = LocationTrackingData.LocationPoint.COLUMN_NAME_ENTRYTIME + " ASC";        


		/*
		 * MIME type definitions
		 */

		/**
		 * The MIME type of {@link #CONTENT_URI} providing a directory of diaries.
		 */
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.aps.medications.LocationTrackingData";

		/** The MIME type of a {@link #CONTENT_URI} sub-directory of a single diary item. */
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.aps.medications.LocationTrackingData";

		/*
		 * Column definitions
		 */

		/**
		 * Column name for the creation timestamp
		 * <P>Type: INTEGER (long from System.curentTimeMillis())</P>
		 */
		public static final String COLUMN_NAME_ENTRYTIME= "entry_time";       

		/** The Constant COLUMN_NAME_MEDICATION_NAME. */
		public static final String GPS_LATITUDE = "gps_lat";

		/** The Constant COLUMN_NAME_DOSAGE. */
		public static final String GPS_LONGITUDE = "gps_long";


		/** The Constant COLUMN_NAME_ID. */
		public static final String COLUMN_NAME_ID = BaseColumns._ID;
		
		/** The Constant COLUMN_NAME_STATUS. */
		public static final String COLUMN_NAME_STATUS="status";
		/**  Projection holding all the columns required to populate and Diary item. */
		public static final String[] FULL_PROJECTION = {
			COLUMN_NAME_ENTRYTIME,
			GPS_LATITUDE,
			GPS_LONGITUDE,
			COLUMN_NAME_STATUS,


		};        

		/** The Constant LIST_PROJECTION. */
		public static final String[] LIST_PROJECTION =
				new String[] {
			LocationTrackingData.LocationPoint._ID
		};            

	}

	/**
	 * The Class LocationDataTableManage.
	 */
	public static final class LocationDataTableManage{
		// Database creation SQL statement
		/** The Constant DATABASE_CREATE. */
		private static final String DATABASE_CREATE = "create table " 
				+ LocationPoint.TABLE_NAME
				+ " (" 
				+ LocationPoint._ID + " integer primary key autoincrement, " 
				+ LocationPoint.COLUMN_NAME_ENTRYTIME + " text not null, "
				+ LocationPoint.GPS_LONGITUDE + " text not null, " 
				+ LocationPoint.GPS_LATITUDE + " text not null, "
				+ LocationPoint.COLUMN_NAME_STATUS + " C(10) default 'I'"
				+ ");";

		/**
		 * On create.
		 *
		 * @param database the database
		 */
		public static void onCreate(SQLiteDatabase database) {
			database.execSQL(DATABASE_CREATE);
		}

		/**
		 * On upgrade.
		 *
		 * @param database the database
		 * @param oldVersion the old version
		 * @param newVersion the new version
		 */
		public static void onUpgrade(SQLiteDatabase database, int oldVersion,
				int newVersion) {
			Log.w(LocationDataTableManage.class.getName(), "Upgrading database from version "
					+ oldVersion + " to " + newVersion
					+ ", which will destroy all old data");
			database.execSQL("DROP TABLE IF EXISTS " + LocationPoint.TABLE_NAME);
			onCreate(database);
		}
	}

	/**
	 * The Class Helper.
	 */
	public static final class Helper {
		/**
		 * Converts a cursor to an array of Diary 
		 * Note that this method is "mean and lean" with little error checking.
		 * It assumes that the projection used is DiaryItem.FULL_PROJECTION
		 * @param cursor A cursor loaded with Diary data
		 * @return populated array of Diaries
		 */
		public static final LocationTrackingData[] getLocationsFromCursor(Cursor cursor){
			LocationTrackingData[] points = null;
			int rows = cursor.getCount();
			if(rows > 0){
				points = new LocationTrackingData[rows];
				int i=0;
				while(cursor.moveToNext()){
					points[i++] = new LocationTrackingData(cursor.getLong(0));
				}
			}
			return points;
		}

		/**
		 * Dbmedicationlist to json.
		 *
		 * @param e the e
		 * @return the JSON object
		 * @throws JSONException the JSON exception
		 */
		public static final JSONObject dbGPSPointsToJSON(LocationTrackingData e) throws JSONException{
			JSONObject jObj = new JSONObject();
			jObj.put("lasttaken", e.entry_time);
			return jObj;
		}

		/**
		 * Dbmedicationlist array to json.
		 *
		 * @param dbmedicationlist the dbmedicationlist
		 * @return the JSON array
		 * @throws JSONException the JSON exception
		 */
		public static final JSONArray dbGPSPointsArrayToJSON(LocationTrackingData[] dbmedicationlist)  throws JSONException{
			JSONArray jArray = new JSONArray();
			System.out.println("Converting  " + dbmedicationlist.length + " dbmedicationlist to JSON");
			for(LocationTrackingData e: dbmedicationlist){
				jArray.put(dbGPSPointsToJSON(e));
			}
			return jArray;
		}	

		/**
		 * Dblist array to csv.
		 *
		 * @param dblist the dblist
		 * @return the string builder
		 */
		public static final StringBuilder dblistArrayToCsv(LocationTrackingData[] dblist){
			StringBuilder result= new StringBuilder();
			for(LocationTrackingData e: dblist){
				result.append(dblistToCsv(e));
			}
			return result;
		}		

		/**
		 * Dblist to csv.
		 *
		 * @param e the e
		 * @return the string builder
		 */
		public static final StringBuilder dblistToCsv(LocationTrackingData e){
			StringBuilder builder = new StringBuilder();
			builder.append(sdf.format(new Date(e.entry_time)));
			builder.append('\n');
			return builder;
		}	

	}
}
