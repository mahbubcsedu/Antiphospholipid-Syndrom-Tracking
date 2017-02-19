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


public final class PeakFlowDB implements java.io.Serializable{
	
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
	public PeakFlowDB(ContentValues values){
		entry_time = values.getAsLong(PeakFlowDB.PeakFlowItem.COLUMN_NAME_ENTRYTIME);
	}
	
	/**
	 * Instantiates a new available medication info in db.
	 *
	 * @param date the date
	 */
	public PeakFlowDB(long date ){
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
        v.put(PeakFlowDB.PeakFlowItem.COLUMN_NAME_ENTRYTIME, entry_time);
        return v;

    }    	
	
    
    
	/**
	 * Constant definition to define the mapping of a Diary to the underlying database
	 * Also provides constants to help define the Content Provider.
	 */
	public static final class PeakFlowItem implements BaseColumns {
		
        // This class cannot be instantiated
        /**
         * Instantiates a new DB medication list item.
         */
        private PeakFlowItem() {}

        /** The table name offered by this provider. */
        public static final String TABLE_NAME = "tb_peakflow";

        /*
         * URI definitions
         */

        /** The scheme part for this provider's URI. */
        private static final String SCHEME = "content://";

        /** Path parts for the URIs. */

        /**
         * Path part for the diary URI
         */
        private static final String PATH_PEAK_FLOW = "/tb_peakflow";

        /** Path part for the diary ID URI. */
        private static final String PATH_PEAK_FLOW_ID = "/tb_peakflow/";

        /** 0-relative position of a diary item ID segment in the path part of a diary item ID URI. */
        public static final int PEAK_FLOW_ID_PATH_POSITION = 1;


        /** The content:// style URL for this table. */
        public static final Uri CONTENT_URI =  Uri.parse(SCHEME + AUTHORITY + PATH_PEAK_FLOW);
        
        /**
         * The content URI base for a single diary item. Callers must
         * append a numeric diary id to this Uri to retrieve an diary item
         */
        public static final Uri CONTENT_ID_URI_BASE
            = Uri.parse(SCHEME + AUTHORITY + PATH_PEAK_FLOW_ID);        
        
        /** The default sort order for this table. */
        public static final String DEFAULT_SORT_ORDER = PeakFlowDB.PeakFlowItem.COLUMN_NAME_ENTRYTIME + " ASC";        
        
        
        /*
         * MIME type definitions
         */

        /**
         * The MIME type of {@link #CONTENT_URI} providing a directory of diaries.
         */
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.aps.tb_peakflow";

        /** The MIME type of a {@link #CONTENT_URI} sub-directory of a single diary item. */
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.aps.tb_peakflow";
        
        /*
         * Column definitions
         */

        /**
         * Column name for the creation timestamp
         * <P>Type: INTEGER (long from System.curentTimeMillis())</P>
         */
        public static final String COLUMN_NAME_ENTRYTIME= "entry_time";       
        
        /** The Constant COLUMN_NAME_MEDICATION_NAME. */
        public static final String COLUMN_NAME_PEAK_FLOW1 = "peak_flow1";
        /** The Constant COLUMN_NAME_MEDICATION_NAME. */
        public static final String COLUMN_NAME_PEAK_FLOW2 = "peak_flow2";
        /** The Constant COLUMN_NAME_MEDICATION_NAME. */
        public static final String COLUMN_NAME_PEAK_FLOW3 = "peak_flow3";
        /** The Constant COLUMN_NAME_MEDICATION_NAME. */
        public static final String COLUMN_NAME_PEAK_FLOW_CATEGORY = "category";
        /** The Constant COLUMN_NAME_MEDICATION_NAME. */
        public static final String COLUMN_NAME_STATUS = "status";
        
        
        /** The Constant COLUMN_NAME_ID. */
        public static final String COLUMN_NAME_ID = BaseColumns._ID;
        //public static final String COLUMN_NAME_STATUS="status";
        /**  Projection holding all the columns required to populate and Diary item. */
        public static final String[] FULL_PROJECTION = {
        	COLUMN_NAME_ENTRYTIME,
        	COLUMN_NAME_PEAK_FLOW1,
        	COLUMN_NAME_PEAK_FLOW2,
        	COLUMN_NAME_PEAK_FLOW3,
        	COLUMN_NAME_PEAK_FLOW_CATEGORY
        	        	
            };        
        
        /** The Constant LIST_PROJECTION. */
        public static final String[] LIST_PROJECTION =
            new String[] {
                PeakFlowDB.PeakFlowItem._ID
        };            

	}
	
	/**
	 * The Class LocationDurationDataTableManage.
	 */
	public static final class PEAKFLOWTableManage{
		// Database creation SQL statement
		/** The Constant DATABASE_CREATE. */
		private static final String DATABASE_CREATE = "create table " 
				+ PeakFlowItem.TABLE_NAME
				+ " (" 
				+ PeakFlowItem._ID + " integer primary key autoincrement, " 
				+ PeakFlowItem.COLUMN_NAME_ENTRYTIME + " text not null, "
				+ PeakFlowItem.COLUMN_NAME_STATUS + " C(10) default 'I',"
				+ PeakFlowItem.COLUMN_NAME_PEAK_FLOW1 + " integer, " 	
				+ PeakFlowItem.COLUMN_NAME_PEAK_FLOW2 + " integer,"
				+ PeakFlowItem.COLUMN_NAME_PEAK_FLOW3 + " integer,"
				+ PeakFlowItem.COLUMN_NAME_PEAK_FLOW_CATEGORY + " c(10)"
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
			Log.w(PEAKFLOWTableManage.class.getName(), "Upgrading database from version "
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
		public static final PeakFlowDB[] getMedicationsFromCursor(Cursor cursor){
			PeakFlowDB[] diaries = null;
			int rows = cursor.getCount();
			if(rows > 0){
				diaries = new PeakFlowDB[rows];
				int i=0;
				while(cursor.moveToNext()){
					diaries[i++] = new PeakFlowDB( cursor.getLong(0));
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
		public static final JSONObject PEAKFLOWToJSON(PeakFlowDB e) throws JSONException{
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
		public static final JSONArray PEAKFLOWArrayToJSON(PeakFlowDB[] duration)  throws JSONException{
			JSONArray jArray = new JSONArray();
			System.out.println("Converting  " + duration.length + " dbmedicationlist to JSON");
			for(PeakFlowDB e: duration){
				jArray.put(PEAKFLOWToJSON(e));
			}
			return jArray;
		}	
		
		/**
		 * Dblist array to csv.
		 *
		 * @param duration the duration
		 * @return the string builder
		 */
		public static final StringBuilder PEAKFLOWArrayToCsv(PeakFlowDB[] duration){
			StringBuilder result= new StringBuilder();
			for(PeakFlowDB e: duration){
				result.append(PEAKFLOWToCsv(e));
			}
			return result;
		}		
		
		/**
		 * Dblist to csv.
		 *
		 * @param e the e
		 * @return the string builder
		 */
		public static final StringBuilder PEAKFLOWToCsv(PeakFlowDB e){
			StringBuilder builder = new StringBuilder();
			builder.append(sdf.format(new Date(e.entry_time)));
			builder.append('\n');
			return builder;
		}	
				
	}
	
	
	
}
