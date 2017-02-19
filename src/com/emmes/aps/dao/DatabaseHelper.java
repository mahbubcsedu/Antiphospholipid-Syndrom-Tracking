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

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.emmes.aps.data.AvailableMedicationInfoInDB;
import com.emmes.aps.data.DailyDiaryDB;
import com.emmes.aps.data.DailyMedication;
import com.emmes.aps.data.DatabaseConstants;
import com.emmes.aps.data.FormContentData;
import com.emmes.aps.data.LocationTrackingData;
import com.emmes.aps.data.LocationTrackingDuration;
import com.emmes.aps.data.MedicationList;
import com.emmes.aps.data.PeakFlowDB;
import com.emmes.aps.util.CalendarUtils;

// TODO: Auto-generated Javadoc
/**
 * The Class DatabaseHelper.
 */

public class DatabaseHelper extends SQLiteOpenHelper
{

    /** The Constant DATABASE_NAME. */
    private static final String DATABASE_NAME = "bwell.db";

    /** The Constant DATABASE_VERSION. */
    private static final int DATABASE_VERSION = 1;

    /** The Constant DIARY_TABLE_CREATE. */
    /* private static final String DIARY_TABLE_CREATE = "CREATE TABLE " +
     * Diary.DiaryItem.TABLE_NAME + " (" + Diary.DiaryItem.COLUMN_NAME_ID +
     * " INTEGER PRIMARY KEY," + Diary.DiaryItem.COLUMN_NAME_DIARYDATE + " TEXT"
     * + Diary.DiaryItem.COLUMN_NAME_WHEEZING + " TEXT" +
     * Diary.DiaryItem.COLUMN_NAME_COUGHING + " TEXT" +
     * Diary.DiaryItem.COLUMN_NAME_SHORT_BREATH + " TEXT" +
     * Diary.DiaryItem.COLUMN_NAME_CHEST_TIGHTNESS + " TEXT" +
     * Diary.DiaryItem.COLUMN_NAME_CHEST_PAIN + " TEXT" +
     * Diary.DiaryItem.COLUMN_NAME_FEVER + " TEXT" +
     * Diary.DiaryItem.COLUMN_NAME_NAUSEA_VOMITING + " TEXT" +
     * Diary.DiaryItem.COLUMN_NAME_RUNNY_NOSE + " TEXT" +
     * Diary.DiaryItem.COLUMN_NAME_OTHER + " TEXT" +
     * Diary.DiaryItem.COLUMN_NAME_MISS_SCHOOL_WORK + " TEXT" +
     * Diary.DiaryItem.COLUMN_NAME_PRESCRIPTION + " TEXT" +
     * Diary.DiaryItem.COLUMN_NAME_OVER_THE_COUNTER + " TEXT" +
     * Diary.DiaryItem.COLUMN_NAME_HOURS_SLEPT + " TEXT" +
     * Diary.DiaryItem.COLUMN_NAME_MINUTES_SLEPT + " TEXT" +
     * Diary.DiaryItem.COLUMN_NAME_WAKE + " TEXT" +
     * Diary.DiaryItem.COLUMN_NAME_TIMES_WOKE + " TEXT" +
     * Diary.DiaryItem.COLUMN_NAME_HOURS_SITTING + " TEXT" +
     * Diary.DiaryItem.COLUMN_NAME_MINUTES_SITTING + " TEXT" +
     * Diary.DiaryItem.COLUMN_NAME_EXERCISE + " TEXT" +
     * Diary.DiaryItem.COLUMN_NAME_MINUTES_INDOORS + " TEXT" +
     * Diary.DiaryItem.COLUMN_NAME_MINUTES_OUTDOORS + " TEXT" +
     * Diary.DiaryItem.COLUMN_NAME_HOURS_VEHICLE + " TEXT" +
     * Diary.DiaryItem.COLUMN_NAME_MINUTES_VEHICLE + " TEXT" +
     * Diary.DiaryItem.COLUMN_NAME_SMOKE + " TEXT" +
     * Diary.DiaryItem.COLUMN_NAME_NUM_SMOKE + " TEXT" +
     * Diary.DiaryItem.COLUMN_NAME_PEOPLE_SMOKING + " TEXT" +
     * Diary.DiaryItem.COLUMN_NAME_CAFFEIN + " TEXT" +
     * Diary.DiaryItem.COLUMN_NAME_STRESS + " TEXT" +
     * Diary.DiaryItem.COLUMN_NAME_PEAKFLOW1 + " TEXT" +
     * Diary.DiaryItem.COLUMN_NAME_PEAKFLOW2 + " TEXT" +
     * Diary.DiaryItem.COLUMN_NAME_PEAKFLOW3 + " TEXT" +
     * Diary.DiaryItem.COLUMN_NAME_NITRICOXIDE + " TEXT" + ");"; */

    /** The Constant FORM_CONTENT_CREATE. */
    /* private static final String FORM_CONTENT_CREATE =
     * "CREATE TABLE form_content (" + "form_name C(25)," + "element_id C(25),"
     * + "label C(25)," + "frequency INTEGER," + "window_start C(5)," +
     * "window_end C(5)," + "time_cat C(2)," + "last_collection DATETIME)"; */
    /* private static final String FORM_CONTENT_CREATE = "CREATE TABLE " +
     * FormContentData.FormContentItems.TABLE_NAME + "(" +
     * FormContentData.FormContentItems.COLUMN_NAME_FORM_NAME + " C(25)," +
     * FormContentData.FormContentItems.COLUMN_NAME_ELEMENT_ID + " C(25)," +
     * FormContentData.FormContentItems.COLUMN_NAME_LABEL + " C(25)," +
     * FormContentData.FormContentItems.COLUMN_NAME_FREQ + " INTEGER," +
     * FormContentData.FormContentItems.COLUMN_NAME_WINDOW_START + " C(5)," +
     * FormContentData.FormContentItems.COLUMN_NAME_WINDOW_END + " C(5)," +
     * FormContentData.FormContentItems.COLUMN_NAME_TIME_CATEGORY + " C(2)," +
     * FormContentData.FormContentItems.COLUMN_NAME_LAST_COLLECTED_TIME +
     * " DATETIME)"; */

    /** The Constant FORM_CONTENT_INSERT. */
    // private static final String FORM_CONTENT_INSERT =
    // "INSERT INTO FORM_CONTENT VALUES ('diary',null,'Diary',1,'00:00','23:59','AM',null)";

    /** The Constant DAILY_MEDICATION. */
    /* private static final String DAILY_MEDICATION = "CREATE TABLE " +
     * DailyMedication.MedicationItem.TABLE_NAME + " (" +
     * DailyMedication.MedicationItem.COLUMN_NAME_ID + " INTEGER PRIMARY KEY," +
     * DailyMedication.MedicationItem.COLUMN_NAME_MEDICATION_NAME + " C(40)," +
     * DailyMedication.MedicationItem.COLUMN_NAME_DOSAGE + " C(10)," +
     * DailyMedication.MedicationItem.COLUMN_NAME_FREQ + " INTEGER," +
     * DailyMedication.MedicationItem.COLUMN_NAME_TYPE + " C(10)," +
     * DailyMedication.MedicationItem.COLUMN_NAME_STATUS + " C(10) default 'I',"
     * + DailyMedication.MedicationItem.COLUMN_NAME_MEDICATION_ID + " INTEGER,"
     * + DailyMedication.MedicationItem.COLUMN_NAME_LASTTAKEN + " DATETIME)"; */

    /** The Constant DAILY_MEDICATION_LIST. */
    /* private static final String DAILY_MEDICATION_LIST = "CREATE TABLE " +
     * MedicationList.MedicationItem.TABLE_NAME + " (" +
     * MedicationList.MedicationItem.COLUMN_NAME_ID + " INTEGER PRIMARY KEY," +
     * MedicationList.MedicationItem.COLUMN_NAME_MEDICATION_NAME + " C(40)," +
     * MedicationList.MedicationItem.COLUMN_NAME_DOSAGE + " C(10)," +
     * MedicationList.MedicationItem.COLUMN_NAME_FREQ + " INTEGER," +
     * MedicationList.MedicationItem.COLUMN_NAME_TYPE + " C(10)," +
     * MedicationList.MedicationItem.COLUMN_NAME_STATUS + " C(10) default 'I',"
     * + MedicationList.MedicationItem.COLUMN_NAME_ENTRYTIME + " DATETIME)"; */
    /** The Constant AVAILABLE_MEDICATION_DB. */
    /* private static final String AVAILABLE_MEDICATION_DB = "CREATE TABLE " +
     * AvailableMedicationInfoInDB.DBMedicationListItem.TABLE_NAME + " (" +
     * AvailableMedicationInfoInDB.DBMedicationListItem.COLUMN_NAME_ID +
     * " INTEGER PRIMARY KEY," +
     * AvailableMedicationInfoInDB.DBMedicationListItem
     * .COLUMN_NAME_MEDICATION_NAME + " C(40)," +
     * AvailableMedicationInfoInDB.DBMedicationListItem.COLUMN_NAME_DOSAGE +
     * " C(10)," +
     * AvailableMedicationInfoInDB.DBMedicationListItem.COLUMN_NAME_TYPE +
     * " C(10)," +
     * AvailableMedicationInfoInDB.DBMedicationListItem.COLUMN_NAME_ENTRYTIME +
     * " DATETIME)"; */

    /**
     * Instantiates a new database helper.
     * 
     * @param context
     *            the context
     */
    public DatabaseHelper(Context context) {
	// If the 2nd parameter is null then the database is held in memory --
	// this form creates a file backed database
	super(context, DATABASE_NAME, null, DATABASE_VERSION);
	// TODO Auto-generated constructor stub
    }

    /**
     * Alternative constructor for test mode.
     * 
     * @param context
     *            the context
     * @param testMode
     *            state of flag is irrelevant. The presence of the 2nd argument
     *            causes the in-memory db to be created
     */
    public DatabaseHelper(Context context, boolean testMode) {
	// If the 2nd parameter is null then the database is held in memory --
	// this form creates an in memory database
	super(context, null, null, DATABASE_VERSION);
    }

    /* (non-Javadoc)
     * 
     * @see
     * android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite
     * .SQLiteDatabase) */
    @Override
    public void onCreate(SQLiteDatabase db)
    {
	/* db.execSQL(DIARY_TABLE_CREATE); db.execSQL(FORM_CONTENT_CREATE);
	 * db.execSQL(FORM_CONTENT_INSERT); */
	// db.execSQL(DIARY_TABLE_CREATE);
	// db.execSQL(DAILY_MEDICATION);
	DailyMedication.DailyMedicationTableManage.onCreate(db);
	AvailableMedicationInfoInDB.AvailableMedicationListTableManage.onCreate(db);
	MedicationList.MedicationListTableManage.onCreate(db);
	// db.execSQL(DAILY_MEDICATION_LIST);
	// db.execSQL(FORM_CONTENT_CREATE);
	// db.execSQL(FORM_CONTENT_INSERT);
	// db.execSQL(AVAILABLE_MEDICATION_DB);
	FormContentData.FormContentDataTableManage.onCreate(db);
	db.beginTransaction();
	// insert lots of stuff...
	String[] avlistofmedication = insertAvailableMedicationList();
	for (String medlist : avlistofmedication)
	{
	    db.execSQL(medlist);
	}
	db.setTransactionSuccessful();
	db.endTransaction();

	db.beginTransaction();
	// insert lots of stuff...
	String[] form_content = insertFormContentInitialData();
	for (String content : form_content)
	{
	    db.execSQL(content);
	}
	db.setTransactionSuccessful();
	db.endTransaction();

	LocationTrackingData.LocationDataTableManage.onCreate(db);
	LocationTrackingDuration.LocationDurationDataTableManage.onCreate(db);
	DailyDiaryDB.DialyDiaryTableManage.onCreate(db);
	PeakFlowDB.PEAKFLOWTableManage.onCreate(db);
    }

    /**
     * Not sure what to do with this. Could ignore for the course...
     * 
     * @param db
     *            the db
     * @param oldVersion
     *            the old version
     * @param newVersion
     *            the new version
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
	// TODO Auto-generated method stub
	DailyMedication.DailyMedicationTableManage.onUpgrade(db, oldVersion, newVersion);
	MedicationList.MedicationListTableManage.onUpgrade(db, oldVersion, newVersion);
	AvailableMedicationInfoInDB.AvailableMedicationListTableManage.onUpgrade(db, oldVersion, newVersion);
	FormContentData.FormContentDataTableManage.onUpgrade(db, oldVersion, newVersion);
	LocationTrackingData.LocationDataTableManage.onUpgrade(db, oldVersion, newVersion);
	LocationTrackingDuration.LocationDurationDataTableManage.onUpgrade(db, oldVersion, newVersion);
	DailyDiaryDB.DialyDiaryTableManage.onUpgrade(db, oldVersion, newVersion);
	PeakFlowDB.PEAKFLOWTableManage.onUpgrade(db, oldVersion, newVersion);
    }

    /**
     * Insert form content initial data.
     * 
     * @return array of string each containing one insert sql to form_content
     *         table;
     */
    public static String[] insertFormContentInitialData()
    {
	Long now = Long.valueOf(System.currentTimeMillis());
	Calendar cal_now = Calendar.getInstance();
	String formattedTime_now = CalendarUtils.calTimeToDateStringyyyymmddhhmmss(cal_now);

	String[] form_content = {
	        "INSERT INTO FORM_CONTENT VALUES ('" + DatabaseConstants.FORM_CONTENT_ID_DIARY + "',null,'Diary',1,'00:00','23:59','AM','"
	                + formattedTime_now + "')",
	        "INSERT INTO FORM_CONTENT VALUES ('" + DatabaseConstants.FORM_CONTENT_ID_NITRO + "','q_nitro','Nitrogen',2,'00:00','23:59','AM','"
	                + formattedTime_now + "')",
	        "INSERT INTO FORM_CONTENT VALUES ('" + DatabaseConstants.FORM_CONTENT_ID_AM_FLOW + "','q_flow_am','AM Flow',1,'00:00','11:59','AM','"
	                + formattedTime_now + "')",
	        "INSERT INTO FORM_CONTENT VALUES ('" + DatabaseConstants.FORM_CONTENT_ID_PM_FLOW + "','q_flow_pm','PM Flow',1,'12:00','23:59','PM','"
	                + formattedTime_now + "')", "INSERT INTO FORM_CONTENT VALUES ('extra',null,'Extra',1,'00:00','23:59','AM',null)" };
	return form_content;
    }

    /**
     * Insert available medication list.
     * 
     * @return the string[]
     */
    public static String[] insertAvailableMedicationList()
    {
	Long now = Long.valueOf(System.currentTimeMillis());

	String[] medicationList = {
	        "INSERT INTO " + AvailableMedicationInfoInDB.DBMedicationListItem.TABLE_NAME + "('"
	                + AvailableMedicationInfoInDB.DBMedicationListItem.COLUMN_NAME_MEDICATION_NAME + "','"
	                + AvailableMedicationInfoInDB.DBMedicationListItem.COLUMN_NAME_DOSAGE + "','"
	                + AvailableMedicationInfoInDB.DBMedicationListItem.COLUMN_NAME_TYPE + "','"
	                + AvailableMedicationInfoInDB.DBMedicationListItem.COLUMN_NAME_ENTRYTIME + "') values('Benzyle','50mg','"
	                + DatabaseConstants.ITEM_TYPE_MEDICATION + "'," + now + ")",

	        "INSERT INTO " + AvailableMedicationInfoInDB.DBMedicationListItem.TABLE_NAME + "('"
	                + AvailableMedicationInfoInDB.DBMedicationListItem.COLUMN_NAME_MEDICATION_NAME + "','"
	                + AvailableMedicationInfoInDB.DBMedicationListItem.COLUMN_NAME_DOSAGE + "','"
	                + AvailableMedicationInfoInDB.DBMedicationListItem.COLUMN_NAME_TYPE + "','"
	                + AvailableMedicationInfoInDB.DBMedicationListItem.COLUMN_NAME_ENTRYTIME + "') values('Benadryl','3mg','"
	                + DatabaseConstants.ITEM_TYPE_MEDICATION + "'," + now + ")",

	        "INSERT INTO " + AvailableMedicationInfoInDB.DBMedicationListItem.TABLE_NAME + "('"
	                + AvailableMedicationInfoInDB.DBMedicationListItem.COLUMN_NAME_MEDICATION_NAME + "','"
	                + AvailableMedicationInfoInDB.DBMedicationListItem.COLUMN_NAME_DOSAGE + "','"
	                + AvailableMedicationInfoInDB.DBMedicationListItem.COLUMN_NAME_TYPE + "','"
	                + AvailableMedicationInfoInDB.DBMedicationListItem.COLUMN_NAME_ENTRYTIME + "') values('Antacid','50mg','"
	                + DatabaseConstants.ITEM_TYPE_MEDICATION + "'," + now + ")",
	        "INSERT INTO " + AvailableMedicationInfoInDB.DBMedicationListItem.TABLE_NAME + "('"
	                + AvailableMedicationInfoInDB.DBMedicationListItem.COLUMN_NAME_MEDICATION_NAME + "','"
	                + AvailableMedicationInfoInDB.DBMedicationListItem.COLUMN_NAME_DOSAGE + "','"
	                + AvailableMedicationInfoInDB.DBMedicationListItem.COLUMN_NAME_TYPE + "','"
	                + AvailableMedicationInfoInDB.DBMedicationListItem.COLUMN_NAME_ENTRYTIME + "') values('Benzyle2','1mg','"
	                + DatabaseConstants.ITEM_TYPE_SUPPLEMENT + "'," + now + ")",
	        "INSERT INTO " + AvailableMedicationInfoInDB.DBMedicationListItem.TABLE_NAME + "('"
	                + AvailableMedicationInfoInDB.DBMedicationListItem.COLUMN_NAME_MEDICATION_NAME + "','"
	                + AvailableMedicationInfoInDB.DBMedicationListItem.COLUMN_NAME_DOSAGE + "','"
	                + AvailableMedicationInfoInDB.DBMedicationListItem.COLUMN_NAME_TYPE + "','"
	                + AvailableMedicationInfoInDB.DBMedicationListItem.COLUMN_NAME_ENTRYTIME + "') values('Benadryl2','2mg','"
	                + DatabaseConstants.ITEM_TYPE_SUPPLEMENT + "'," + now + ")",
	        "INSERT INTO " + AvailableMedicationInfoInDB.DBMedicationListItem.TABLE_NAME + "('"
	                + AvailableMedicationInfoInDB.DBMedicationListItem.COLUMN_NAME_MEDICATION_NAME + "','"
	                + AvailableMedicationInfoInDB.DBMedicationListItem.COLUMN_NAME_DOSAGE + "','"
	                + AvailableMedicationInfoInDB.DBMedicationListItem.COLUMN_NAME_TYPE + "','"
	                + AvailableMedicationInfoInDB.DBMedicationListItem.COLUMN_NAME_ENTRYTIME + "') values('Antacid2','3mg','"
	                + DatabaseConstants.ITEM_TYPE_SUPPLEMENT + "'," + now + ")" };
	return medicationList;
    }
}
