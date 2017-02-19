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
 * <h1>Medication list tables maintenance</h1> This class will manage the
 * creation of available medication list in the database.
 * <p>
 * <b>Note:</b> This database table will be created at the beginning and contain
 * the available list of medication.
 * 
 * @author Mahbubur Rahman
 * @version 1.0
 * @since 2014-06-02
 */

public final class AvailableMedicationInfoInDB implements java.io.Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The Constant AUTHORITY. */
	public static final String AUTHORITY = "com.emmes.aps"; // The Content
															// Provider
															// Authority

	/** The sdf. */
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy:MM:dd");

	/** The entry_time. */
	public long entry_time;

	/**
	 * Instantiates a new available medication info in db.
	 * 
	 * @param values
	 *            the values
	 */
	public AvailableMedicationInfoInDB(ContentValues values) {
		entry_time = values
				.getAsLong(AvailableMedicationInfoInDB.DBMedicationListItem.COLUMN_NAME_ENTRYTIME);
	}

	/**
	 * Instantiates a new available medication info in db.
	 * 
	 * @param date
	 *            the date
	 */
	public AvailableMedicationInfoInDB(long date) {
		this.entry_time = date;
	}

	/*
	 * Returns a ContentValues instance (a map) for this diaryInfo instance.
	 * This is useful for inserting a diaryInfo into a database.
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
		v.put(AvailableMedicationInfoInDB.DBMedicationListItem.COLUMN_NAME_ENTRYTIME,
				entry_time);
		return v;

	}

	/**
	 * Constant definition to define the mapping of a Diary to the underlying
	 * database Also provides constants to help define the Content Provider.
	 */
	public static final class DBMedicationListItem implements BaseColumns {

		// This class cannot be instantiated
		/**
		 * Instantiates a new DB medication list item.
		 */
		private DBMedicationListItem() {
		}

		/** The table name offered by this provider. */
		public static final String TABLE_NAME = "availablemedicationdb";

		/*
		 * URI definitions
		 */

		/** The scheme part for this provider's URI. */
		private static final String SCHEME = "content://";

		/** Path parts for the URIs. */

		/**
		 * Path part for the diary URI
		 */
		private static final String PATH_MEDICATIONS_IN_DB = "/availablemedicationdb";

		/** Path part for the diary ID URI. */
		private static final String PATH_MEDICATIONS_IN_DB_ID = "/availablemedicationdb/";

		/**
		 * 0-relative position of a diary item ID segment in the path part of a
		 * diary item ID URI.
		 */
		public static final int MEDICATIONS_IN_DB_ID_PATH_POSITION = 1;

		/** The content:// style URL for this table. */
		public static final Uri CONTENT_URI = Uri.parse(SCHEME + AUTHORITY
				+ PATH_MEDICATIONS_IN_DB);

		/**
		 * The content URI base for a single diary item. Callers must append a
		 * numeric diary id to this Uri to retrieve an diary item
		 */
		public static final Uri CONTENT_ID_URI_BASE = Uri.parse(SCHEME
				+ AUTHORITY + PATH_MEDICATIONS_IN_DB_ID);

		/** The default sort order for this table. */
		public static final String DEFAULT_SORT_ORDER = AvailableMedicationInfoInDB.DBMedicationListItem.COLUMN_NAME_ENTRYTIME
				+ " ASC";

		/*
		 * MIME type definitions
		 */

		/**
		 * The MIME type of {@link #CONTENT_URI} providing a directory of
		 * diaries.
		 */
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.aps.medications.availablemedicationdb";

		/**
		 * The MIME type of a {@link #CONTENT_URI} sub-directory of a single
		 * diary item.
		 */
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.aps.medications.availablemedicationdb";

		/*
		 * Column definitions
		 */

		/**
		 * Column name for the creation timestamp
		 * <P>
		 * Type: INTEGER (long from System.curentTimeMillis())
		 * </P>
		 */
		public static final String COLUMN_NAME_ENTRYTIME = "entry_time";

		/** The Constant COLUMN_NAME_MEDICATION_NAME. */
		public static final String COLUMN_NAME_MEDICATION_NAME = "medication_name";

		/** The Constant COLUMN_NAME_DOSAGE. */
		public static final String COLUMN_NAME_DOSAGE = "dosage";
		// public static final String COLUMN_NAME_FREQ = "frequency";
		/** The Constant COLUMN_NAME_TYPE. */
		public static final String COLUMN_NAME_TYPE = "item_type";

		/** The Constant COLUMN_NAME_ID. */
		public static final String COLUMN_NAME_ID = BaseColumns._ID;

		/**
		 * Projection holding all the columns required to populate and Diary
		 * item.
		 */
		public static final String[] FULL_PROJECTION = { COLUMN_NAME_ENTRYTIME,
				COLUMN_NAME_MEDICATION_NAME, COLUMN_NAME_DOSAGE,

		};

		/** The Constant LIST_PROJECTION. */
		public static final String[] LIST_PROJECTION = new String[] { AvailableMedicationInfoInDB.DBMedicationListItem._ID };

	}
	/**
	 * The Class LocationDurationDataTableManage.
	 */
	public static final class AvailableMedicationListTableManage{
		// Database creation SQL statement
		/** The Constant DATABASE_CREATE. */
	    /** The Constant DAILY_MEDICATION_LIST. */
	    private static final String AVAILABLE_MEDICATION_DB = "CREATE TABLE "
		    + AvailableMedicationInfoInDB.DBMedicationListItem.TABLE_NAME + " ("
		    + AvailableMedicationInfoInDB.DBMedicationListItem.COLUMN_NAME_ID + " INTEGER PRIMARY KEY,"
		    + AvailableMedicationInfoInDB.DBMedicationListItem.COLUMN_NAME_MEDICATION_NAME + " C(40),"
		    + AvailableMedicationInfoInDB.DBMedicationListItem.COLUMN_NAME_DOSAGE + " C(10),"
		    + AvailableMedicationInfoInDB.DBMedicationListItem.COLUMN_NAME_TYPE + " C(10),"
		    + AvailableMedicationInfoInDB.DBMedicationListItem.COLUMN_NAME_ENTRYTIME + " DATETIME)";

		/**
		 * On create.
		 *
		 * @param database the database
		 */
		public static void onCreate(SQLiteDatabase database) {
			database.execSQL(AVAILABLE_MEDICATION_DB);
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
			Log.w(AvailableMedicationListTableManage.class.getName(), "Upgrading database from version "
					+ oldVersion + " to " + newVersion
					+ ", which will destroy all old data");
			database.execSQL("DROP TABLE IF EXISTS " + DBMedicationListItem.TABLE_NAME);
			onCreate(database);
		}
	}
	/**
	 * The Class Helper.
	 */
	public static final class Helper {
		/**
		 * Converts a cursor to an array of Diary Note that this method is
		 * "mean and lean" with little error checking. It assumes that the
		 * projection used is DiaryItem.FULL_PROJECTION
		 * 
		 * @param cursor
		 *            A cursor loaded with Diary data
		 * @return populated array of Diaries
		 */
		public static final AvailableMedicationInfoInDB[] getMedicationsFromCursor(
				Cursor cursor) {
			AvailableMedicationInfoInDB[] diaries = null;
			int rows = cursor.getCount();
			if (rows > 0) {
				diaries = new AvailableMedicationInfoInDB[rows];
				int i = 0;
				while (cursor.moveToNext()) {
					diaries[i++] = new AvailableMedicationInfoInDB(
							cursor.getLong(0));
				}
			}
			return diaries;
		}

		/**
		 * Dbmedicationlist to json.
		 * 
		 * @param e
		 *            the e
		 * @return the JSON object
		 * @throws JSONException
		 *             the JSON exception
		 */
		public static final JSONObject dbmedicationlistToJSON(
				AvailableMedicationInfoInDB e) throws JSONException {
			JSONObject jObj = new JSONObject();
			jObj.put("lasttaken", e.entry_time);
			return jObj;
		}

		/**
		 * Dbmedicationlist array to json.
		 * 
		 * @param dbmedicationlist
		 *            the dbmedicationlist
		 * @return the JSON array
		 * @throws JSONException
		 *             the JSON exception
		 */
		public static final JSONArray dbmedicationlistArrayToJSON(
				AvailableMedicationInfoInDB[] dbmedicationlist)
				throws JSONException {
			JSONArray jArray = new JSONArray();
			System.out.println("Converting  " + dbmedicationlist.length
					+ " dbmedicationlist to JSON");
			for (AvailableMedicationInfoInDB e : dbmedicationlist) {
				jArray.put(dbmedicationlistToJSON(e));
			}
			return jArray;
		}

		/**
		 * Dblist array to csv.
		 * 
		 * @param dblist
		 *            the dblist
		 * @return the string builder
		 */
		public static final StringBuilder dblistArrayToCsv(
				AvailableMedicationInfoInDB[] dblist) {
			StringBuilder result = new StringBuilder();
			for (AvailableMedicationInfoInDB e : dblist) {
				result.append(dblistToCsv(e));
			}
			return result;
		}

		
		/**
		 * Dblist to csv.
		 * 
		 * @param e
		 *            the e
		 * @return the string builder
		 */
		public static final StringBuilder dblistToCsv(
				AvailableMedicationInfoInDB e) {
			StringBuilder builder = new StringBuilder();
			builder.append(sdf.format(new Date(e.entry_time)));
			builder.append('\n');
			return builder;
		}

	}

}
