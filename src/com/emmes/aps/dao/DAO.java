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
package com.emmes.aps.dao;

import java.util.Calendar;
import java.util.HashMap;

import com.emmes.aps.data.DailyMedication;
import com.emmes.aps.data.DatabaseHelper;
import com.emmes.aps.data.Diary;
import com.emmes.aps.data.MedicationList;
import com.emmes.aps.data.Diary.DiaryItem;
import com.emmes.aps.data.MedicationList.MedicationItem;

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
 * The Class DAO.
 */
public class DAO {

	/** The Constant TAG. */
	private static final String TAG="DAO";
	
	/** The m db helper. */
	private DatabaseHelper mDbHelper;
    
    /** The s diarys projection map. */
    private static HashMap<String, String> sDiarysProjectionMap;   
    
    /** The s meds projection map. */
    private static HashMap<String, String> sMedsProjectionMap; 
    
    
    static {
        sDiarysProjectionMap = new HashMap<String, String>();
        sDiarysProjectionMap.put(Diary.DiaryItem.COLUMN_NAME_ID, Diary.DiaryItem.COLUMN_NAME_ID);     
        sDiarysProjectionMap.put(Diary.DiaryItem.COLUMN_NAME_DIARYDATE, Diary.DiaryItem.COLUMN_NAME_DIARYDATE);
        sDiarysProjectionMap.put(Diary.DiaryItem.COLUMN_NAME_WHEEZING, Diary.DiaryItem.COLUMN_NAME_WHEEZING);
        sDiarysProjectionMap.put(Diary.DiaryItem.COLUMN_NAME_COUGHING, Diary.DiaryItem.COLUMN_NAME_COUGHING);
        sDiarysProjectionMap.put(Diary.DiaryItem.COLUMN_NAME_SHORT_BREATH, Diary.DiaryItem.COLUMN_NAME_SHORT_BREATH);
        sDiarysProjectionMap.put(Diary.DiaryItem.COLUMN_NAME_CHEST_TIGHTNESS, Diary.DiaryItem.COLUMN_NAME_CHEST_TIGHTNESS);
        sDiarysProjectionMap.put(Diary.DiaryItem.COLUMN_NAME_CHEST_PAIN, Diary.DiaryItem.COLUMN_NAME_CHEST_PAIN);
        sDiarysProjectionMap.put(Diary.DiaryItem.COLUMN_NAME_FEVER, Diary.DiaryItem.COLUMN_NAME_FEVER);
        sDiarysProjectionMap.put(Diary.DiaryItem.COLUMN_NAME_NAUSEA_VOMITING, Diary.DiaryItem.COLUMN_NAME_NAUSEA_VOMITING);
        sDiarysProjectionMap.put(Diary.DiaryItem.COLUMN_NAME_RUNNY_NOSE, Diary.DiaryItem.COLUMN_NAME_RUNNY_NOSE);
        sDiarysProjectionMap.put(Diary.DiaryItem.COLUMN_NAME_OTHER, Diary.DiaryItem.COLUMN_NAME_OTHER);
        sDiarysProjectionMap.put(Diary.DiaryItem.COLUMN_NAME_MISS_SCHOOL_WORK, Diary.DiaryItem.COLUMN_NAME_MISS_SCHOOL_WORK);
        sDiarysProjectionMap.put(Diary.DiaryItem.COLUMN_NAME_PRESCRIPTION, Diary.DiaryItem.COLUMN_NAME_PRESCRIPTION);
        sDiarysProjectionMap.put(Diary.DiaryItem.COLUMN_NAME_OVER_THE_COUNTER, Diary.DiaryItem.COLUMN_NAME_OVER_THE_COUNTER);
        sDiarysProjectionMap.put(Diary.DiaryItem.COLUMN_NAME_HOURS_SLEPT, Diary.DiaryItem.COLUMN_NAME_HOURS_SLEPT);
        sDiarysProjectionMap.put(Diary.DiaryItem.COLUMN_NAME_MINUTES_SLEPT, Diary.DiaryItem.COLUMN_NAME_MINUTES_SLEPT);
        sDiarysProjectionMap.put(Diary.DiaryItem.COLUMN_NAME_WAKE, Diary.DiaryItem.COLUMN_NAME_WAKE);
        sDiarysProjectionMap.put(Diary.DiaryItem.COLUMN_NAME_TIMES_WOKE, Diary.DiaryItem.COLUMN_NAME_TIMES_WOKE);
        sDiarysProjectionMap.put(Diary.DiaryItem.COLUMN_NAME_HOURS_SITTING, Diary.DiaryItem.COLUMN_NAME_HOURS_SITTING);
        sDiarysProjectionMap.put(Diary.DiaryItem.COLUMN_NAME_MINUTES_SITTING, Diary.DiaryItem.COLUMN_NAME_MINUTES_SITTING);
        sDiarysProjectionMap.put(Diary.DiaryItem.COLUMN_NAME_EXERCISE, Diary.DiaryItem.COLUMN_NAME_EXERCISE);
        sDiarysProjectionMap.put(Diary.DiaryItem.COLUMN_NAME_MINUTES_INDOORS, Diary.DiaryItem.COLUMN_NAME_MINUTES_INDOORS);
        sDiarysProjectionMap.put(Diary.DiaryItem.COLUMN_NAME_MINUTES_OUTDOORS, Diary.DiaryItem.COLUMN_NAME_MINUTES_OUTDOORS);
        sDiarysProjectionMap.put(Diary.DiaryItem.COLUMN_NAME_HOURS_VEHICLE, Diary.DiaryItem.COLUMN_NAME_HOURS_VEHICLE);
        sDiarysProjectionMap.put(Diary.DiaryItem.COLUMN_NAME_MINUTES_VEHICLE, Diary.DiaryItem.COLUMN_NAME_MINUTES_VEHICLE);
        sDiarysProjectionMap.put(Diary.DiaryItem.COLUMN_NAME_SMOKE, Diary.DiaryItem.COLUMN_NAME_SMOKE);
        sDiarysProjectionMap.put(Diary.DiaryItem.COLUMN_NAME_NUM_SMOKE, Diary.DiaryItem.COLUMN_NAME_NUM_SMOKE);
        sDiarysProjectionMap.put(Diary.DiaryItem.COLUMN_NAME_PEOPLE_SMOKING, Diary.DiaryItem.COLUMN_NAME_PEOPLE_SMOKING);
        sDiarysProjectionMap.put(Diary.DiaryItem.COLUMN_NAME_CAFFEIN, Diary.DiaryItem.COLUMN_NAME_CAFFEIN);
        sDiarysProjectionMap.put(Diary.DiaryItem.COLUMN_NAME_STRESS, Diary.DiaryItem.COLUMN_NAME_STRESS);
        sDiarysProjectionMap.put(Diary.DiaryItem.COLUMN_NAME_PEAKFLOW1, Diary.DiaryItem.COLUMN_NAME_PEAKFLOW1);
        sDiarysProjectionMap.put(Diary.DiaryItem.COLUMN_NAME_PEAKFLOW2, Diary.DiaryItem.COLUMN_NAME_PEAKFLOW2);
        sDiarysProjectionMap.put(Diary.DiaryItem.COLUMN_NAME_PEAKFLOW3, Diary.DiaryItem.COLUMN_NAME_PEAKFLOW3);
        sDiarysProjectionMap.put(Diary.DiaryItem.COLUMN_NAME_NITRICOXIDE, Diary.DiaryItem.COLUMN_NAME_NITRICOXIDE);
    }
    
        static {
    	sMedsProjectionMap = new HashMap<String, String>();
    	sMedsProjectionMap.put(DailyMedication.MedicationItem.COLUMN_NAME_ID, DailyMedication.MedicationItem.COLUMN_NAME_ID);     
    	sMedsProjectionMap.put(DailyMedication.MedicationItem.COLUMN_NAME_MEDICATION_NAME, DailyMedication.MedicationItem.COLUMN_NAME_MEDICATION_NAME);  
    	sMedsProjectionMap.put(MedicationList.MedicationItem.COLUMN_NAME_DOSAGE, DailyMedication.MedicationItem.COLUMN_NAME_DOSAGE);  
    	sMedsProjectionMap.put(MedicationList.MedicationItem.COLUMN_NAME_FREQ, MedicationList.MedicationItem.COLUMN_NAME_FREQ);  
    }
	
	/**
	 * Instantiates a new dao.
	 *
	 * @param context the context
	 */
	public DAO(Context context) {
		mDbHelper = new DatabaseHelper(context);
	}
	
	/**
	 * Instantiates a new dao.
	 *
	 * @param context the context
	 * @param testMode the test mode
	 */
	protected DAO(Context context, boolean testMode) {
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
     * Delete diarys.
     *
     * @param where the where
     * @param whereArgs the where args
     * @return the int
     */
    public int deleteDiarys(String where, String[] whereArgs) {

        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        int count = db.delete(
                    Diary.DiaryItem.TABLE_NAME,  // The database table name
                    where,                     // The incoming where clause column names
                    whereArgs                  // The incoming where clause values
                );
        // Returns the number of rows deleted.
        return count;
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
    
	/**
	 * Delete meds by id.
	 *
	 * @param id the id
	 * @return the int
	 */
	public int deleteMedsById(int id) {
        String finalWhere =
            DailyMedication.MedicationItem.COLUMN_NAME_ID + 
            " = " +                          
            id;
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
	    // Performs the delete.
	    int count = db.delete(
	    	DailyMedication.MedicationItem.TABLE_NAME,  // The database table name.
	        finalWhere,                // The final WHERE clause
	        null                  // The incoming where clause values.
	    );
		return count;
	}
	
    /**
     * Delete meds.
     *
     * @param where the where
     * @param whereArgs the where args
     * @return the int
     */
    public int deleteMeds(String where, String[] whereArgs) {

        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        int count = db.delete(
        			DailyMedication.MedicationItem.TABLE_NAME,  // The database table name
                    where,                     // The incoming where clause column names
                    whereArgs                  // The incoming where clause values
                );
        // Returns the number of rows deleted.
        return count;
    }	
	
	/**
	 * Insert med.
	 *
	 * @param medValues the med values
	 * @return the long
	 */
	public long insertMed(ContentValues medValues) {

     // If the incoming values map is not null, uses it for the new values.
     if (medValues != null) {
    	 medValues = new ContentValues(medValues);

     } else {
         // Otherwise, create a new value map
    	 medValues = new ContentValues();
     }

     if (medValues.containsKey(DailyMedication.MedicationItem.COLUMN_NAME_MEDICATION_NAME) == false) {
    	 medValues.put(DailyMedication.MedicationItem.COLUMN_NAME_MEDICATION_NAME, "");
     }

     if (medValues.containsKey(DailyMedication.MedicationItem.COLUMN_NAME_DOSAGE) == false) {
    	 medValues.put(DailyMedication.MedicationItem.COLUMN_NAME_DOSAGE, "");
     }

     if (medValues.containsKey(DailyMedication.MedicationItem.COLUMN_NAME_FREQ) == false) {
    	 medValues.put(DailyMedication.MedicationItem.COLUMN_NAME_FREQ, "");
     }

     // Opens the database object in "write" mode.
     SQLiteDatabase db = mDbHelper.getWritableDatabase();

     long rowId = db.insert(
    	 DailyMedication.MedicationItem.TABLE_NAME,        // The table to insert into.
         DailyMedication.MedicationItem.COLUMN_NAME_MEDICATION_NAME,  // A hack, SQLite sets this column value to null
                                          // if values is empty.
         medValues                           // A map of column names, and the values to insert
                                          // into the columns.
     );
     //Log.d(TAG, "Insert ROWID= " + rowId);
     if(rowId < 0){
    	 throw new SQLException("Failed to insert medication.");
     }
     
     ContentValues formValues = new ContentValues();
     formValues.put("last_collection", Calendar.getInstance().getTime().toString());
     db.update("form_content",formValues,"form_name='medication'",null);

     return rowId;
	}	
	
	
	/**
	 * Query med by id.
	 *
	 * @param medId the med id
	 * @param projection the projection
	 * @return the cursor
	 */
	public Cursor queryMedById(int medId, String[] projection)
	{
	       // Constructs a new query builder and sets its table name
	       SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
	       qb.setTables(DailyMedication.MedicationItem.TABLE_NAME);
	       qb.appendWhere(DailyMedication.MedicationItem.COLUMN_NAME_ID +    // the name of the ID column
	    	        "=" +
	    	        medId);
	       return performMedQuery(qb, projection, null, null, null);		
	}
	
	/**
	 * Query meds.
	 *
	 * @param projection the projection
	 * @param selection the selection
	 * @param selectionArgs the selection args
	 * @param sortOrder the sort order
	 * @return the cursor
	 */
	public Cursor queryMeds(String[] projection, String selection, String[] selectionArgs, String sortOrder)
	{
	       // Constructs a new query builder and sets its table name
	       SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
	       qb.setTables(DailyMedication.MedicationItem.TABLE_NAME);
	       
	       return performMedQuery(qb, projection, selection, selectionArgs, sortOrder);
	}
	
	/**
	 * Perform med query.
	 *
	 * @param qb the qb
	 * @param projection the projection
	 * @param selection the selection
	 * @param selectionArgs the selection args
	 * @param sortOrder the sort order
	 * @return the cursor
	 */
	private Cursor performMedQuery(SQLiteQueryBuilder qb, String[] projection, String selection, String[] selectionArgs, String sortOrder)
	{

	       
	       // Opens the database object in "read" mode, since no writes need to be done.
	       SQLiteDatabase db = mDbHelper.getReadableDatabase();
	       
	       qb.setProjectionMap(sMedsProjectionMap);
	       
	       String orderBy;
	       // If no sort order is specified, uses the default
	       if (TextUtils.isEmpty(sortOrder)) {
	           orderBy = DailyMedication.MedicationItem.DEFAULT_SORT_ORDER;
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
     * Update med by id.
     *
     * @param medId the med id
     * @param values the values
     * @return the int
     */
    public int updateMedById( long medId, ContentValues values) {

        // Opens the database object in "write" mode.
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int count;
        String where =
                        Diary.DiaryItem.COLUMN_NAME_ID +                              // The ID column name
                        " = " +  medId;                                        // test for equality

        // Does the update and returns the number of rows updated.
        count = db.update(
        	DailyMedication.MedicationItem.TABLE_NAME, // The database table name.
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
     * Update meds.
     *
     * @param values the values
     * @param where the where
     * @param whereArgs the where args
     * @return the int
     */
    public int updateMeds( ContentValues values, String where, String[] whereArgs) {

        // Opens the database object in "write" mode.
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int count;

        // Does the update and returns the number of rows updated.
        count = db.update(
        	DailyMedication.MedicationItem.TABLE_NAME, // The database table name.
            values,                   // A map of column names and new values to use.
            where,                    // The where clause column names.
            whereArgs                 // The where clause column values to select on.
        );
        
        return count;
    }	
}
