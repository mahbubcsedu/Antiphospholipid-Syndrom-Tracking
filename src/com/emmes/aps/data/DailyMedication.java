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
 * The Class Medication.
 */

public final class DailyMedication implements java.io.Serializable{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The Constant AUTHORITY. */
	public static final String AUTHORITY = "com.emmes.aps";	// The Content Provider Authority

	/** The sdf. */
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy:MM:dd");

	/** The lasttaken. */
	public long lasttaken;	

	/**
	 * Instantiates a new medication.
	 *
	 * @param values the values
	 */
	public DailyMedication(ContentValues values){
		lasttaken = values.getAsLong(DailyMedication.MedicationItem.COLUMN_NAME_LASTTAKEN);
	}

	/**
	 * Instantiates a new medication.
	 *
	 * @param date the date
	 */
	public DailyMedication(long date ){
		this.lasttaken = date;
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
		v.put(DailyMedication.MedicationItem.COLUMN_NAME_LASTTAKEN, lasttaken);
		return v;

	}    	

	/**
	 * Constant definition to define the mapping of a Diary to the underlying database
	 * Also provides constants to help define the Content Provider.
	 */
	public static final class MedicationItem implements BaseColumns {

		// This class cannot be instantiated
		/**
		 * Instantiates a new medication item.
		 */
		private MedicationItem() {}

		/** The table name offered by this provider. */
		public static final String TABLE_NAME = "medications";

		/*
		 * URI definitions
		 */

		/** The scheme part for this provider's URI. */
		private static final String SCHEME = "content://";

		/** Path parts for the URIs. */

		/**
		 * Path part for the diary URI
		 */
		private static final String PATH_MEDICATIONS = "/medications";

		/** Path part for the diary ID URI. */
		private static final String PATH_MEDICATIONS_ID = "/medications/";

		/** 0-relative position of a diary item ID segment in the path part of a diary item ID URI. */
		public static final int MEDICATIONS_ID_PATH_POSITION = 1;


		/** The content:// style URL for this table. */
		public static final Uri CONTENT_URI =  Uri.parse(SCHEME + AUTHORITY + PATH_MEDICATIONS);

		/**
		 * The content URI base for a single diary item. Callers must
		 * append a numeric diary id to this Uri to retrieve an diary item
		 */
		public static final Uri CONTENT_ID_URI_BASE
		= Uri.parse(SCHEME + AUTHORITY + PATH_MEDICATIONS_ID);        

		/** The default sort order for this table. */
		public static final String DEFAULT_SORT_ORDER = DailyMedication.MedicationItem.COLUMN_NAME_LASTTAKEN + " ASC";        


		/*
		 * MIME type definitions
		 */

		/**
		 * The MIME type of {@link #CONTENT_URI} providing a directory of diaries.
		 */
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.aps.medications.medications";

		/** The MIME type of a {@link #CONTENT_URI} sub-directory of a single diary item. */
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.aps.medications.medications";

		/*
		 * Column definitions
		 */

		/**
		 * Column name for the creation timestamp
		 * <P>Type: INTEGER (long from System.curentTimeMillis())</P>
		 */
		public static final String COLUMN_NAME_LASTTAKEN= "lasttaken";       

		/** The Constant COLUMN_NAME_MEDICATION_NAME. */
		public static final String COLUMN_NAME_MEDICATION_NAME = "medication_name";

		/** The Constant COLUMN_NAME_DOSAGE. */
		public static final String COLUMN_NAME_DOSAGE = "dosage";

		/** The Constant COLUMN_NAME_FREQ. */
		public static final String COLUMN_NAME_FREQ = "frequency";

		/** The Constant COLUMN_NAME_TYPE. */
		public static final String COLUMN_NAME_TYPE = "item_type";

		/** The Constant COLUMN_NAME_ID. */
		public static final String COLUMN_NAME_ID = BaseColumns._ID;
		/** this is for keeping track of foreign key, that is the id of the medication  in the medication list*/
		public static final String COLUMN_NAME_MEDICATION_ID = "medication_id";
		/** The Constant COLUMN_NAME_STATUS. */
		public static final String COLUMN_NAME_STATUS="status";
		/**  Projection holding all the columns required to populate and Diary item. */
		public static final String[] FULL_PROJECTION = {
			COLUMN_NAME_LASTTAKEN,
			COLUMN_NAME_MEDICATION_NAME,
			COLUMN_NAME_DOSAGE,
			COLUMN_NAME_FREQ,
			COLUMN_NAME_STATUS

		};        

		/** The Constant LIST_PROJECTION. */
		public static final String[] LIST_PROJECTION =
				new String[] {
			DailyMedication.MedicationItem._ID
		};            

	}
	/**
	 * The Class LocationDurationDataTableManage.
	 */
	public static final class DailyMedicationTableManage{
		// Database creation SQL statement
		/** The Constant DATABASE_CREATE. */
	    private static final String DAILY_MEDICATION = "CREATE TABLE " + DailyMedication.MedicationItem.TABLE_NAME + " ("
		    + DailyMedication.MedicationItem.COLUMN_NAME_ID + " INTEGER PRIMARY KEY,"
		    + DailyMedication.MedicationItem.COLUMN_NAME_MEDICATION_NAME + " C(40),"
		    + DailyMedication.MedicationItem.COLUMN_NAME_DOSAGE + " C(10),"
		    + DailyMedication.MedicationItem.COLUMN_NAME_FREQ + " INTEGER,"
		    + DailyMedication.MedicationItem.COLUMN_NAME_TYPE + " C(10),"
		    + DailyMedication.MedicationItem.COLUMN_NAME_STATUS + " C(10) default 'I',"
		    + DailyMedication.MedicationItem.COLUMN_NAME_MEDICATION_ID + " INTEGER,"
		    + DailyMedication.MedicationItem.COLUMN_NAME_LASTTAKEN + " DATETIME)";

		/**
		 * On create.
		 *
		 * @param database the database
		 */
		public static void onCreate(SQLiteDatabase database) {
			database.execSQL(DAILY_MEDICATION);
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
			Log.w(DailyMedicationTableManage.class.getName(), "Upgrading database from version "
					+ oldVersion + " to " + newVersion
					+ ", which will destroy all old data");
			database.execSQL("DROP TABLE IF EXISTS " + MedicationItem.TABLE_NAME);
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
		public static final DailyMedication[] getMedicationsFromCursor(Cursor cursor){
			DailyMedication[] diaries = null;
			int rows = cursor.getCount();
			if(rows > 0){
				diaries = new DailyMedication[rows];
				int i=0;
				while(cursor.moveToNext()){
					diaries[i++] = new DailyMedication( cursor.getLong(0));
				}
			}
			return diaries;
		}

		/**
		 * Diary to json.
		 *
		 * @param e the e
		 * @return the JSON object
		 * @throws JSONException the JSON exception
		 */
		public static final JSONObject diaryToJSON(DailyMedication e) throws JSONException{
			JSONObject jObj = new JSONObject();
			jObj.put("lasttaken", e.lasttaken);
			return jObj;
		}

		/**
		 * Diary array to json.
		 *
		 * @param diaries the diaries
		 * @return the JSON array
		 * @throws JSONException the JSON exception
		 */
		public static final JSONArray diaryArrayToJSON(DailyMedication[] diaries)  throws JSONException{
			JSONArray jArray = new JSONArray();
			System.out.println("Converting  " + diaries.length + " diaries to JSON");
			for(DailyMedication e: diaries){
				jArray.put(diaryToJSON(e));
			}
			return jArray;
		}	

		/**
		 * Diary array to csv.
		 *
		 * @param diaries the diaries
		 * @return the string builder
		 */
		public static final StringBuilder diaryArrayToCsv(DailyMedication[] diaries){
			StringBuilder result= new StringBuilder();
			for(DailyMedication e: diaries){
				result.append(diaryToCsv(e));
			}
			return result;
		}		

		/**
		 * Diary to csv.
		 *
		 * @param e the e
		 * @return the string builder
		 */
		public static final StringBuilder diaryToCsv(DailyMedication e){
			StringBuilder builder = new StringBuilder();
			builder.append(sdf.format(new Date(e.lasttaken)));
			builder.append('\n');
			return builder;
		}	

	}

}
