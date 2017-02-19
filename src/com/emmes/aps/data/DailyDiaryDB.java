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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.emmes.aps.data.LocationTrackingData.LocationPoint;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;

// TODO: Auto-generated Javadoc
/**
* <h1>Medication list tables maintenance</h1>
* This class will manage the creation of available medication list in the database.
* <p>
* <b>Note:</b> This database table will be created at the beginning and contain the available list of medication.
*
* @author  Mahbubur Rahman
* @version 1.0
* @since   2014-06-02
*/


public final class DailyDiaryDB implements java.io.Serializable{
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The Constant AUTHORITY. */
	public static final String AUTHORITY = "com.emmes.aps";	// The Content Provider Authority
	
	/** The sdf. */
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy:MM:dd",Locale.US);
	
	/** The entry_time. */
	public long entry_time;	

	/**
	 * Instantiates a new available medication info in db.
	 *
	 * @param values the values
	 */
	public DailyDiaryDB(ContentValues values){
		entry_time = values.getAsLong(DailyDiaryDB.DailyDiaryItem.COLUMN_NAME_ENTRYTIME);
	}
	
	/**
	 * Instantiates a new available medication info in db.
	 *
	 * @param date the date
	 */
	public DailyDiaryDB(long date ){
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
        v.put(DailyDiaryDB.DailyDiaryItem.COLUMN_NAME_ENTRYTIME, entry_time);
        return v;

    }    	
	
    
    
	/**
	 * Constant definition to define the mapping of a Diary to the underlying database
	 * Also provides constants to help define the Content Provider.
	 */
	public static final class DailyDiaryItem implements BaseColumns {
		
        // This class cannot be instantiated
        /**
         * Instantiates a new DB medication list item.
         */
        private DailyDiaryItem() {}

        /** The table name offered by this provider. */
        public static final String TABLE_NAME = "tb_dailydiary";

        /*
         * URI definitions
         */

        /** The scheme part for this provider's URI. */
        private static final String SCHEME = "content://";

        /** Path parts for the URIs. */

        /**
         * Path part for the diary URI
         */
        private static final String PATH_DAILY_DIARY = "/tb_dailydiary";

        /** Path part for the diary ID URI. */
        private static final String PATH_DAILY_DIARY_ID = "/tb_dailydiary/";

        /** 0-relative position of a diary item ID segment in the path part of a diary item ID URI. */
        public static final int DAILY_DIARY_ID_PATH_POSITION = 1;


        /** The content:// style URL for this table. */
        public static final Uri CONTENT_URI =  Uri.parse(SCHEME + AUTHORITY + PATH_DAILY_DIARY);
        
        /**
         * The content URI base for a single diary item. Callers must
         * append a numeric diary id to this Uri to retrieve an diary item
         */
        public static final Uri CONTENT_ID_URI_BASE
            = Uri.parse(SCHEME + AUTHORITY + PATH_DAILY_DIARY_ID);        
        
        /** The default sort order for this table. */
        public static final String DEFAULT_SORT_ORDER = DailyDiaryDB.DailyDiaryItem.COLUMN_NAME_ENTRYTIME + " ASC";        
        
        
        /*
         * MIME type definitions
         */

        /**
         * The MIME type of {@link #CONTENT_URI} providing a directory of diaries.
         */
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.aps.tb_dailydiary";

        /** The MIME type of a {@link #CONTENT_URI} sub-directory of a single diary item. */
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.aps.tb_dailydiary";
        
        /*
         * Column definitions
         */

        /**
         * Column name for the creation timestamp
         * <P>Type: INTEGER (long from System.curentTimeMillis())</P>
         */
        public static final String COLUMN_NAME_ENTRYTIME= "entry_time";       
        
        /** The Constant COLUMN_NAME_MEDICATION_NAME. */
        public static final String COLUMN_NAME_DIARY_CONTENT = "diary_content";
        /** The Constant COLUMN_NAME_MEDICATION_NAME. */
        public static final String COLUMN_NAME_STATUS = "status";
        
        
        /** The Constant COLUMN_NAME_ID. */
        public static final String COLUMN_NAME_ID = BaseColumns._ID;
        //public static final String COLUMN_NAME_STATUS="status";
        /**  Projection holding all the columns required to populate and Diary item. */
        public static final String[] FULL_PROJECTION = {
        	COLUMN_NAME_ENTRYTIME,
        	COLUMN_NAME_DIARY_CONTENT,
        	        	
            };        
        
        /** The Constant LIST_PROJECTION. */
        public static final String[] LIST_PROJECTION =
            new String[] {
                DailyDiaryDB.DailyDiaryItem._ID
        };            

	}
	
	/**
	 * The Class LocationDurationDataTableManage.
	 */
	public static final class DialyDiaryTableManage{
		// Database creation SQL statement
		/** The Constant DATABASE_CREATE. */
		private static final String DATABASE_CREATE = "create table " 
				+ DailyDiaryItem.TABLE_NAME
				+ " (" 
				+ DailyDiaryItem._ID + " integer primary key autoincrement, " 
				+ DailyDiaryItem.COLUMN_NAME_ENTRYTIME + " text not null, "
				+ DailyDiaryItem.COLUMN_NAME_STATUS + " C(10) default 'I',"
				+ DailyDiaryItem.COLUMN_NAME_DIARY_CONTENT + " text not null " 				
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
			Log.w(DialyDiaryTableManage.class.getName(), "Upgrading database from version "
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
		public static final DailyDiaryDB[] getMedicationsFromCursor(Cursor cursor){
			DailyDiaryDB[] diaries = null;
			int rows = cursor.getCount();
			if(rows > 0){
				diaries = new DailyDiaryDB[rows];
				int i=0;
				while(cursor.moveToNext()){
					diaries[i++] = new DailyDiaryDB( cursor.getLong(0));
				}
			}
			return diaries;
		}
		
		/**
		 * Dbmedicationlist to json.
		 *
		 * @param e the e
		 * @return the JSON object
		 * @throws JSONException the JSON exception
		 */
		public static final JSONObject dailyDiaryToJSON(DailyDiaryDB e) throws JSONException{
			JSONObject jObj = new JSONObject();
			jObj.put("lasttaken", e.entry_time);
			return jObj;
		}
		
		/**
		 * Dbmedicationlist array to json.
		 *
		 * @param duration the duration
		 * @return the JSON array
		 * @throws JSONException the JSON exception
		 */
		public static final JSONArray dailyDiaryArrayToJSON(DailyDiaryDB[] duration)  throws JSONException{
			JSONArray jArray = new JSONArray();
			System.out.println("Converting  " + duration.length + " dbmedicationlist to JSON");
			for(DailyDiaryDB e: duration){
				jArray.put(dailyDiaryToJSON(e));
			}
			return jArray;
		}	
		
		/**
		 * Dblist array to csv.
		 *
		 * @param duration the duration
		 * @return the string builder
		 */
		public static final StringBuilder dailyDiaryArrayToCsv(DailyDiaryDB[] duration){
			StringBuilder result= new StringBuilder();
			for(DailyDiaryDB e: duration){
				result.append(dailydiaryToCsv(e));
			}
			return result;
		}		
		
		/**
		 * Dblist to csv.
		 *
		 * @param e the e
		 * @return the string builder
		 */
		public static final StringBuilder dailydiaryToCsv(DailyDiaryDB e){
			StringBuilder builder = new StringBuilder();
			builder.append(sdf.format(new Date(e.entry_time)));
			builder.append('\n');
			return builder;
		}	
				
	}
	
	
	
}
