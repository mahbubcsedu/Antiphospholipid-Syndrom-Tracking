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


public final class FormContentData implements java.io.Serializable{
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The Constant AUTHORITY. */
	public static final String AUTHORITY = "com.emmes.aps";	// The Content Provider Authority
	
	/** The sdf. */
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy:MM:dd",Locale.US);
	
	/** The entry_time. */
	public long lastcollectiontime;	

	/**
	 * Instantiates a new available medication info in db.
	 *
	 * @param values the values
	 */
	public FormContentData(ContentValues values){
		lastcollectiontime = values.getAsLong(FormContentData.FormContentItems.COLUMN_NAME_LAST_COLLECTED_TIME);
	}
	
	/**
	 * Instantiates a new available medication info in db.
	 *
	 * @param date the date
	 */
	public FormContentData(long date ){
		this.lastcollectiontime = date;
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
        v.put(FormContentData.FormContentItems.COLUMN_NAME_LAST_COLLECTED_TIME, lastcollectiontime);
        return v;

    }    	
	
    
    
	/**
	 * Constant definition to define the mapping of a Diary to the underlying database
	 * Also provides constants to help define the Content Provider.
	 */
	public static final class FormContentItems implements BaseColumns {
		
        // This class cannot be instantiated
        /**
         * Instantiates a new DB medication list item.
         */
        private FormContentItems() {}

        /** The table name offered by this provider. */
        public static final String TABLE_NAME = "form_content";

        /*
         * URI definitions
         */

        /** The scheme part for this provider's URI. */
        private static final String SCHEME = "content://";

        /** Path parts for the URIs. */

        /**
         * Path part for the diary URI
         */
        private static final String PATH_FORM_IN_DB = "/form_content";

        /** Path part for the diary ID URI. */
        private static final String PATH_FORM_IN_DB_ID = "/form_content/";

        /** 0-relative position of a diary item ID segment in the path part of a diary item ID URI. */
        public static final int FORM_IN_DB_ID_PATH_POSITION = 1;


        /** The content:// style URL for this table. */
        public static final Uri CONTENT_URI =  Uri.parse(SCHEME + AUTHORITY + PATH_FORM_IN_DB);
        
        /**
         * The content URI base for a single diary item. Callers must
         * append a numeric diary id to this Uri to retrieve an diary item
         */
        public static final Uri CONTENT_ID_URI_BASE
            = Uri.parse(SCHEME + AUTHORITY + PATH_FORM_IN_DB_ID);        
        
        /** The default sort order for this table. */
        public static final String DEFAULT_SORT_ORDER = FormContentData.FormContentItems.COLUMN_NAME_LAST_COLLECTED_TIME + " ASC";        
        
        
        /*
         * MIME type definitions
         */

        /**
         * The MIME type of {@link #CONTENT_URI} providing a directory of diaries.
         */
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.aps.form_content";

        /** The MIME type of a {@link #CONTENT_URI} sub-directory of a single diary item. */
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.aps.form_content";
        
        /*
         * Column definitions
         */

        /**
         * Column name for the creation timestamp
         * <P>Type: INTEGER (long from System.curentTimeMillis())</P>
         */
        public static final String COLUMN_NAME_LAST_COLLECTED_TIME= "lastcollectedtime";       
        
        /** The Constant COLUMN_NAME_MEDICATION_NAME. */
        public static final String COLUMN_NAME_FORM_NAME = "form_name";
        
        /** The Constant COLUMN_NAME_DOSAGE. */
        public static final String COLUMN_NAME_ELEMENT_ID = "element_id";
        
        /** The Constant COLUMN_NAME_LABEL. */
        public static final String COLUMN_NAME_LABEL = "label";
        
        /** The Constant COLUMN_NAME_FREQ. */
        public static final String COLUMN_NAME_FREQ = "frequency";
        
        /** The Constant COLUMN_NAME_WINDOW_START. */
        public static final String COLUMN_NAME_WINDOW_START = "window_start";
        
        /** The Constant COLUMN_NAME_WINDOW_END. */
        public static final String COLUMN_NAME_WINDOW_END = "window_end";
        
        /** The Constant COLUMN_NAME_TIME_CATEGORY. */
        public static final String COLUMN_NAME_TIME_CATEGORY = "time_cat";
        
        /** The Constant COLUMN_NAME_ID. */
        public static final String COLUMN_NAME_ID = BaseColumns._ID;
        //public static final String COLUMN_NAME_STATUS="status";
        /**  Projection holding all the columns required to populate and Diary item. */
        public static final String[] FULL_PROJECTION = {
        	COLUMN_NAME_LAST_COLLECTED_TIME,
        	COLUMN_NAME_FORM_NAME,
        	COLUMN_NAME_ELEMENT_ID,
        	COLUMN_NAME_LABEL,
        	COLUMN_NAME_FREQ,
        	COLUMN_NAME_WINDOW_START,
        	COLUMN_NAME_WINDOW_END,
        	COLUMN_NAME_TIME_CATEGORY
        	
        	
            };        
        
        /** The Constant LIST_PROJECTION. */
        public static final String[] LIST_PROJECTION =
            new String[] {
                FormContentData.FormContentItems._ID
        };            

	}
	
	/**
	 * The Class LocationDurationDataTableManage.
	 */
	public static final class FormContentDataTableManage{
		// Database creation SQL statement
		/** The Constant DATABASE_CREATE. */
	    private static final String FORM_CONTENT_CREATE = "CREATE TABLE " + FormContentData.FormContentItems.TABLE_NAME
		    + "(" + FormContentData.FormContentItems.COLUMN_NAME_FORM_NAME + " C(25),"
		    + FormContentData.FormContentItems.COLUMN_NAME_ELEMENT_ID + " C(25),"
		    + FormContentData.FormContentItems.COLUMN_NAME_LABEL + " C(25),"
		    + FormContentData.FormContentItems.COLUMN_NAME_FREQ + " INTEGER,"
		    + FormContentData.FormContentItems.COLUMN_NAME_WINDOW_START + " C(5),"
		    + FormContentData.FormContentItems.COLUMN_NAME_WINDOW_END + " C(5),"
		    + FormContentData.FormContentItems.COLUMN_NAME_TIME_CATEGORY + " C(2),"
		    + FormContentData.FormContentItems.COLUMN_NAME_LAST_COLLECTED_TIME + " DATETIME)";

		/**
		 * On create.
		 *
		 * @param database the database
		 */
		public static void onCreate(SQLiteDatabase database) {
			database.execSQL(FORM_CONTENT_CREATE);
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
			Log.w(FormContentDataTableManage.class.getName(), "Upgrading database from version "
					+ oldVersion + " to " + newVersion
					+ ", which will destroy all old data");
			database.execSQL("DROP TABLE IF EXISTS " + FormContentItems.TABLE_NAME);
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
		public static final FormContentData[] getFormsFromCursor(Cursor cursor){
			FormContentData[] diaries = null;
			int rows = cursor.getCount();
			if(rows > 0){
				diaries = new FormContentData[rows];
				int i=0;
				while(cursor.moveToNext()){
					diaries[i++] = new FormContentData( cursor.getLong(0));
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
		public static final JSONObject dbFormToJSON(FormContentData e) throws JSONException{
			JSONObject jObj = new JSONObject();
			jObj.put("lasttaken", e.lastcollectiontime);
			return jObj;
		}
		
		/**
		 * Dbmedicationlist array to json.
		 *
		 * @param formcontent the formcontent
		 * @return the JSON array
		 * @throws JSONException the JSON exception
		 */
		public static final JSONArray dbDurationArrayToJSON(FormContentData[] formcontent)  throws JSONException{
			JSONArray jArray = new JSONArray();
			System.out.println("Converting  " + formcontent.length + " dbmedicationlist to JSON");
			for(FormContentData e: formcontent){
				jArray.put(dbFormToJSON(e));
			}
			return jArray;
		}	
		
		/**
		 * Dblist array to csv.
		 *
		 * @param formcontent the formcontent
		 * @return the string builder
		 */
		public static final StringBuilder dbFormArrayToCsv(FormContentData[] formcontent){
			StringBuilder result= new StringBuilder();
			for(FormContentData e: formcontent){
				result.append(duFormToCsv(e));
			}
			return result;
		}		
		
		/**
		 * Dblist to csv.
		 *
		 * @param e the e
		 * @return the string builder
		 */
		public static final StringBuilder duFormToCsv(FormContentData e){
			StringBuilder builder = new StringBuilder();
			builder.append(sdf.format(new Date(e.lastcollectiontime)));
			builder.append('\n');
			return builder;
		}	
				
	}
	
	
	
}
