/*
 * @author  Mahbubur Rahman
 * IT Intern Summer 2014
 * EMMES Corporation
 * Copyright (c) to EMMES Corporation 2014
 */
package com.emmes.aps.dao;

import java.util.Calendar;
import java.util.HashMap;

import com.emmes.aps.data.DatabaseHelper;
import com.emmes.aps.data.Diary;
import com.emmes.aps.data.DailyMedication;
import com.emmes.aps.data.MedicationList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.text.TextUtils;
import android.util.Log;

// TODO: Auto-generated Javadoc
/**
 * The Class MedicationListDAO.
 */
public class MedicationListDAO {

	/** The Constant TAG. */
	private static final String TAG="DAO";

	/** The m db helper. */
	private DatabaseHelper mDbHelper;

	/** The s diarys projection map. */
	private static HashMap<String, String> sDiarysProjectionMap;   

	static {
		sDiarysProjectionMap = new HashMap<String, String>();
		sDiarysProjectionMap.put(Diary.DiaryItem.COLUMN_NAME_ID, Diary.DiaryItem.COLUMN_NAME_ID);     
		sDiarysProjectionMap.put(Diary.DiaryItem.COLUMN_NAME_DIARYDATE, Diary.DiaryItem.COLUMN_NAME_DIARYDATE);  
		sDiarysProjectionMap.put(DailyMedication.MedicationItem.COLUMN_NAME_ID, DailyMedication.MedicationItem.COLUMN_NAME_ID);     
		sDiarysProjectionMap.put(DailyMedication.MedicationItem.COLUMN_NAME_LASTTAKEN, DailyMedication.MedicationItem.COLUMN_NAME_LASTTAKEN);  
		sDiarysProjectionMap.put(MedicationList.MedicationItem.COLUMN_NAME_ID, MedicationList.MedicationItem.COLUMN_NAME_ID);     
		sDiarysProjectionMap.put(MedicationList.MedicationItem.COLUMN_NAME_ENTRYTIME, MedicationList.MedicationItem.COLUMN_NAME_ENTRYTIME);
	}

	/**
	 * Instantiates a new medication list dao.
	 *
	 * @param context the context
	 */
	public MedicationListDAO(Context context) {
		mDbHelper = new DatabaseHelper(context);
	}

	/**
	 * Instantiates a new medication list dao.
	 *
	 * @param context the context
	 * @param testMode the test mode
	 */
	protected MedicationListDAO(Context context, boolean testMode) {
		if(testMode){
			mDbHelper = new DatabaseHelper(context, testMode);
		} else {
			mDbHelper = new DatabaseHelper(context);
		}
	}	

	/**
	 * Delete diarys by id.
	 *
	 * @param id the id
	 * @return the int
	 */
	public int deleteDiarysById(int id) {
		String finalWhere =
				Diary.DiaryItem.COLUMN_NAME_ID + 
				" = " +                          
				id;
		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		// Performs the delete.
		int count = db.delete(
				Diary.DiaryItem.TABLE_NAME,  // The database table name.
				finalWhere,                // The final WHERE clause
				null                  // The incoming where clause values.
				);
		return count;
	}

	/**
	 * Delete medication list by id.
	 *
	 * @param id the id
	 * @return the int
	 */
	public int deleteMedicationListById(int id) {
		String finalWhere =
				MedicationList.MedicationItem.COLUMN_NAME_ID + 
				" = " +                          
				id;
		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		// Performs the delete.
		int count = db.delete(
				MedicationList.MedicationItem.TABLE_NAME,  // The database table name.
				finalWhere,                // The final WHERE clause
				null                  // The incoming where clause values.
				);
		return count;
	}

	/**
	 * Delete medication list.
	 *
	 * @param where the where
	 * @param whereArgs the where args
	 * @return the int
	 */
	public int deleteMedicationList(String where, String[] whereArgs) {

		SQLiteDatabase db = mDbHelper.getWritableDatabase();

		int count = db.delete(
				MedicationList.MedicationItem.TABLE_NAME, // The database table name
				where,                     // The incoming where clause column names
				whereArgs                  // The incoming where clause values
				);
		// Returns the number of rows deleted.
		return count;
	}	

	/**
	 * Delete diarys.
	 *
	 * @param where the where
	 * @param whereArgs the where args
	 * @return the int
	 */
	public int deleteDiarys(String where, String[] whereArgs) {

		SQLiteDatabase db = mDbHelper.getWritableDatabase();

		int count = db.delete(
				Diary.DiaryItem.TABLE_NAME, // The database table name
				where,                     // The incoming where clause column names
				whereArgs                  // The incoming where clause values
				);
		// Returns the number of rows deleted.
		return count;
	}	



	/**
	 * Insert medication list.
	 *
	 * @param MedicationListValues the medication list values
	 * @return the long
	 */
	public long insertMedicationList(ContentValues MedicationListValues) {

		// If the incoming values map is not null, uses it for the new values.
		if (MedicationListValues != null) {
			MedicationListValues = new ContentValues(MedicationListValues);

		} else {
			// Otherwise, create a new value map
			MedicationListValues = new ContentValues();
		}

		// Gets the current system time in milliseconds
		Long now = Long.valueOf(System.currentTimeMillis());

		if (MedicationListValues.containsKey(MedicationList.MedicationItem.COLUMN_NAME_ENTRYTIME) == false) {
			MedicationListValues.put(MedicationList.MedicationItem.COLUMN_NAME_ENTRYTIME, now);
		}

		// Opens the database object in "write" mode.
		SQLiteDatabase db = mDbHelper.getWritableDatabase();

		long rowId = db.insert(
				MedicationList.MedicationItem.TABLE_NAME,        // The table to insert into.
				MedicationList.MedicationItem.COLUMN_NAME_ENTRYTIME,  // A hack, SQLite sets this column value to null
				// if values is empty.
				MedicationListValues                           // A map of column names, and the values to insert
				// into the columns.
				);
		//Log.d(TAG, "Insert ROWID= " + rowId);
		if(rowId < 0){
			throw new SQLException("Failed to insert diary.");
		}

		// ContentValues formValues = new ContentValues();
		// formValues.put("last_collection", Calendar.getInstance().getTime().toString());
		//db.update("form_content",formValues,"form_name='diary'",null);

		return rowId;
	}	




	/**
	 * Insert diary.
	 *
	 * @param diaryValues the diary values
	 * @return the long
	 */
	public long insertDiary(ContentValues diaryValues) {

		// If the incoming values map is not null, uses it for the new values.
		if (diaryValues != null) {
			diaryValues = new ContentValues(diaryValues);

		} else {
			// Otherwise, create a new value map
			diaryValues = new ContentValues();
		}

		// Gets the current system time in milliseconds
		Long now = Long.valueOf(System.currentTimeMillis());

		if (diaryValues.containsKey(Diary.DiaryItem.COLUMN_NAME_DIARYDATE) == false) {
			diaryValues.put(Diary.DiaryItem.COLUMN_NAME_DIARYDATE, now);
		}

		// Opens the database object in "write" mode.
		SQLiteDatabase db = mDbHelper.getWritableDatabase();

		long rowId = db.insert(
				Diary.DiaryItem.TABLE_NAME,        // The table to insert into.
				Diary.DiaryItem.COLUMN_NAME_DIARYDATE,  // A hack, SQLite sets this column value to null
				// if values is empty.
				diaryValues                           // A map of column names, and the values to insert
				// into the columns.
				);
		//Log.d(TAG, "Insert ROWID= " + rowId);
		if(rowId < 0){
			throw new SQLException("Failed to insert diary.");
		}

		ContentValues formValues = new ContentValues();
		formValues.put("last_collection", Calendar.getInstance().getTime().toString());
		db.update("form_content",formValues,"form_name='diary'",null);

		return rowId;
	}	

	/**
	 * Query medication list by id.
	 *
	 * @param medicationId the medication id
	 * @param projection the projection
	 * @return the cursor
	 */
	public Cursor queryMedicationListById(int medicationId, String[] projection)
	{
		// Constructs a new query builder and sets its table name
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setTables(MedicationList.MedicationItem.TABLE_NAME);
		qb.appendWhere(MedicationList.MedicationItem.COLUMN_NAME_ID +    // the name of the ID column
				"=" +
				medicationId);
		return performMedicationListQuery(qb, projection, null, null, null);		
	}

	/**
	 * Query diary by id.
	 *
	 * @param diaryId the diary id
	 * @param projection the projection
	 * @return the cursor
	 */
	public Cursor queryDiaryById(int diaryId, String[] projection)
	{
		// Constructs a new query builder and sets its table name
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setTables(Diary.DiaryItem.TABLE_NAME);
		qb.appendWhere(Diary.DiaryItem.COLUMN_NAME_ID +    // the name of the ID column
				"=" +
				diaryId);
		return performQuery(qb, projection, null, null, null);		
	}

	/**
	 * Query medication list.
	 *
	 * @param projection the projection
	 * @param selection the selection
	 * @param selectionArgs the selection args
	 * @param sortOrder the sort order
	 * @return the cursor
	 */
	public Cursor queryMedicationList(String[] projection, String selection, String[] selectionArgs, String sortOrder)
	{
		// Constructs a new query builder and sets its table name
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setTables(MedicationList.MedicationItem.TABLE_NAME);

		return performMedicationListQuery(qb, projection, selection, selectionArgs, sortOrder);
	}

	/**
	 * Query diarys.
	 *
	 * @param projection the projection
	 * @param selection the selection
	 * @param selectionArgs the selection args
	 * @param sortOrder the sort order
	 * @return the cursor
	 */
	public Cursor queryDiarys(String[] projection, String selection, String[] selectionArgs, String sortOrder)
	{
		// Constructs a new query builder and sets its table name
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setTables(Diary.DiaryItem.TABLE_NAME);

		return performQuery(qb, projection, selection, selectionArgs, sortOrder);
	}

	/**
	 * Perform query.
	 *
	 * @param qb the qb
	 * @param projection the projection
	 * @param selection the selection
	 * @param selectionArgs the selection args
	 * @param sortOrder the sort order
	 * @return the cursor
	 */
	private Cursor performQuery(SQLiteQueryBuilder qb, String[] projection, String selection, String[] selectionArgs, String sortOrder)
	{


		// Opens the database object in "read" mode, since no writes need to be done.
		SQLiteDatabase db = mDbHelper.getReadableDatabase();

		qb.setProjectionMap(sDiarysProjectionMap);

		String orderBy;
		// If no sort order is specified, uses the default
		if (TextUtils.isEmpty(sortOrder)) {
			orderBy = Diary.DiaryItem.DEFAULT_SORT_ORDER;
		} else {
			// otherwise, uses the incoming sort order
			orderBy = sortOrder;
		}

		Log.i(TAG, "performQuery. Projection: " + projection);
		/*
		 * Performs the query. If no problems occur trying to read the database, then a Cursor
		 * object is returned; otherwise, the cursor variable contains null. If no records were
		 * selected, then the Cursor object is empty, and Cursor.getCount() returns 0.
		 */
		Cursor c = qb.query(
				db,            // The database to query
				projection,    // The columns to return from the query
				selection,     // The columns for the where clause
				selectionArgs, // The values for the where clause
				null,          // don't group the rows
				null,          // don't filter by row groups
				orderBy        // The sort order
				);
		return c;
	}


	/**
	 * Perform medication list query.
	 *
	 * @param qb the qb
	 * @param projection the projection
	 * @param selection the selection
	 * @param selectionArgs the selection args
	 * @param sortOrder the sort order
	 * @return the cursor
	 */
	private Cursor performMedicationListQuery(SQLiteQueryBuilder qb, String[] projection, String selection, String[] selectionArgs, String sortOrder)
	{


		// Opens the database object in "read" mode, since no writes need to be done.
		SQLiteDatabase db = mDbHelper.getReadableDatabase();

		//qb.setProjectionMap(sDiarysProjectionMap);

		String orderBy;
		// If no sort order is specified, uses the default
		if (TextUtils.isEmpty(sortOrder)) {
			orderBy = MedicationList.MedicationItem.DEFAULT_SORT_ORDER;
		} else {
			// otherwise, uses the incoming sort order
			orderBy = sortOrder;
		}

		Log.i(TAG, "performQuery. Projection: " + projection);
		/*
		 * Performs the query. If no problems occur trying to read the database, then a Cursor
		 * object is returned; otherwise, the cursor variable contains null. If no records were
		 * selected, then the Cursor object is empty, and Cursor.getCount() returns 0.
		 */
		Cursor c = qb.query(
				db,            // The database to query
				projection,    // The columns to return from the query
				selection,     // The columns for the where clause
				selectionArgs, // The values for the where clause
				null,          // don't group the rows
				null,          // don't filter by row groups
				orderBy        // The sort order
				);
		return c;
	}

	/**
	 * Update medication list by id.
	 *
	 * @param medicationId the medication id
	 * @param values the values
	 * @return the int
	 */
	public int updateMedicationListById( long medicationId, ContentValues values) {

		// Opens the database object in "write" mode.
		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		int count;
		String where =
				MedicationList.MedicationItem.COLUMN_NAME_ID +                              // The ID column name
				" = " +  medicationId;                                        // test for equality

		// Does the update and returns the number of rows updated.
		count = db.update(
				MedicationList.MedicationItem.TABLE_NAME, // The database table name.
				values,                   // A map of column names and new values to use.
				where,               // The final WHERE clause to use
				// placeholders for whereArgs
				null                 // The where clause column values to select on, or
				// null if the values are in the where argument.
				);

		// Returns the number of rows updated.
		return count;
	}

	/**
	 * Update diary by id.
	 *
	 * @param diaryId the diary id
	 * @param values the values
	 * @return the int
	 */
	public int updateDiaryById( long diaryId, ContentValues values) {

		// Opens the database object in "write" mode.
		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		int count;
		String where =
				Diary.DiaryItem.COLUMN_NAME_ID +                              // The ID column name
				" = " +  diaryId;                                        // test for equality

		// Does the update and returns the number of rows updated.
		count = db.update(
				Diary.DiaryItem.TABLE_NAME, // The database table name.
				values,                   // A map of column names and new values to use.
				where,               // The final WHERE clause to use
				// placeholders for whereArgs
				null                 // The where clause column values to select on, or
				// null if the values are in the where argument.
				);

		// Returns the number of rows updated.
		return count;
	}

	/**
	 * Update medication list.
	 *
	 * @param values the values
	 * @param where the where
	 * @param whereArgs the where args
	 * @return the int
	 */
	public int updateMedicationList( ContentValues values, String where, String[] whereArgs) {

		// Opens the database object in "write" mode.
		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		int count;

		// Does the update and returns the number of rows updated.
		count = db.update(
				MedicationList.MedicationItem.TABLE_NAME, // The database table name.
				values,                   // A map of column names and new values to use.
				where,                    // The where clause column names.
				whereArgs                 // The where clause column values to select on.
				);

		return count;
	}	

	/**
	 * Update diarys.
	 *
	 * @param values the values
	 * @param where the where
	 * @param whereArgs the where args
	 * @return the int
	 */
	public int updateDiarys( ContentValues values, String where, String[] whereArgs) {

		// Opens the database object in "write" mode.
		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		int count;

		// Does the update and returns the number of rows updated.
		count = db.update(
				Diary.DiaryItem.TABLE_NAME, // The database table name.
				values,                   // A map of column names and new values to use.
				where,                    // The where clause column names.
				whereArgs                 // The where clause column values to select on.
				);

		return count;
	}	

	/**
	 * Gets the db helper for test.
	 *
	 * @return the db helper for test
	 */
	public DatabaseHelper getDbHelperForTest() {
		return mDbHelper;
	}    

}
