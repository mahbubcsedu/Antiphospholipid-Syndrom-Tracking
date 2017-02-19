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

import java.util.Arrays;
import java.util.HashSet;




//import com.emmes.aps.dao.DAO;

import com.emmes.aps.data.AvailableMedicationInfoInDB;
import com.emmes.aps.data.DailyDiaryDB;
import com.emmes.aps.data.DailyMedication;
import com.emmes.aps.data.FormContentData;
import com.emmes.aps.data.LocationTrackingData;
import com.emmes.aps.data.LocationTrackingDuration;
import com.emmes.aps.data.MedicationList;
import com.emmes.aps.data.PeakFlowDB;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;
import android.provider.BaseColumns;

// TODO: Auto-generated Javadoc
/**
 * The Class DiaryProvider.
 */
public class DiaryProvider extends ContentProvider {

	/** The m dao. */
	//private DAO mDAO;
	
	/** The aps dao. */
	private ApsDao apsDao;
	
	/** The Constant sUriMatcher. */
	private static final UriMatcher sUriMatcher;
	
	/** The Constant DIARYS. */
	private static final int DIARYS = 1;
	
	/** The Constant DIARY_ID. */
	private static final int DIARY_ID = 2;
	
	/** The Constant MEDICATIONS. */
	private static final int MEDICATIONS=3;
	
	/** The Constant MEDICATIONS_ID. */
	private static final int MEDICATIONS_ID=4;
	
	/** The Constant MEDICATIONSLIST. */
	private static final int MEDICATIONSLIST=5;
	
	/** The Constant MEDICATIONSLIST_ID. */
	private static final int MEDICATIONSLIST_ID=6;
	
	/** The Constant DBAVAILABLE_MEDICATIONS. */
	private static final int DBAVAILABLE_MEDICATIONS=7;
	
	/** The Constant DBAVAILABLE_MEDICATIONS_ID. */
	private static final int DBAVAILABLE_MEDICATIONS_ID=8;
	
	/** The Constant TB_GPS_LOCATION. */
	private static final int TB_GPS_LOCATION=19;
	
	/** The Constant TB_GPS_LOCATION_ID. */
	private static final int TB_GPS_LOCATION_ID=20;
	
	/** The Constant TB_LOCATION_DURATION. */
	private static final int TB_LOCATION_DURATION=30;
	
	/** The Constant TB_LOCATION_DURATION_ID. */
	private static final int TB_LOCATION_DURATION_ID=31;
	
	/** The Constant FORM_CONTENT. */
	private static final int FORM_CONTENT=32;
	
	/** The Constant FORM_CONTENT_ID. */
	private static final int FORM_CONTENT_ID=33;
	private static final int DAILY_DIARY=55;
	private static final int DAILY_DIARY_ID=56;
	
	private static final int PEAK_FLOW=66;
	private static final int PEAK_FLOW_ID=67;
	
	

	static {
		sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		//sUriMatcher.addURI(Diary.AUTHORITY, "diary", DIARYS);
		//sUriMatcher.addURI(Diary.AUTHORITY, "diary/#", DIARY_ID);  
		sUriMatcher.addURI(DailyMedication.AUTHORITY, "medications", MEDICATIONS);
		sUriMatcher.addURI(DailyMedication.AUTHORITY, "medications/#", MEDICATIONS_ID);
		sUriMatcher.addURI(MedicationList.AUTHORITY, "medicationslist", MEDICATIONSLIST);
		sUriMatcher.addURI(MedicationList.AUTHORITY, "medicationslist/#", MEDICATIONSLIST_ID);
		sUriMatcher.addURI(AvailableMedicationInfoInDB.AUTHORITY, "availablemedicationdb", DBAVAILABLE_MEDICATIONS);
		sUriMatcher.addURI(AvailableMedicationInfoInDB.AUTHORITY, "availablemedicationdb/#", DBAVAILABLE_MEDICATIONS_ID);
		sUriMatcher.addURI(LocationTrackingData.AUTHORITY, "tb_location", TB_GPS_LOCATION);
		sUriMatcher.addURI(LocationTrackingData.AUTHORITY, "tb_location/#", TB_GPS_LOCATION_ID);
		sUriMatcher.addURI(LocationTrackingDuration.AUTHORITY, "tb_trackingduration", TB_LOCATION_DURATION);
		sUriMatcher.addURI(LocationTrackingData.AUTHORITY, "tb_trackingduration/#", TB_LOCATION_DURATION_ID);
		sUriMatcher.addURI(FormContentData.AUTHORITY, "form_content", FORM_CONTENT);
		sUriMatcher.addURI(FormContentData.AUTHORITY, "form_content/#", FORM_CONTENT_ID);
		sUriMatcher.addURI(DailyDiaryDB.AUTHORITY, "tb_dailydiary", DAILY_DIARY);
		sUriMatcher.addURI(DailyDiaryDB.AUTHORITY, "tb_dailydiary/#", DAILY_DIARY_ID);
		sUriMatcher.addURI(PeakFlowDB.AUTHORITY, "tb_peakflow", PEAK_FLOW);
		sUriMatcher.addURI(PeakFlowDB.AUTHORITY, "tb_peakflow/#", PEAK_FLOW_ID);
	}

	/* (non-Javadoc)
	 * @see android.content.ContentProvider#delete(android.net.Uri, java.lang.String, java.lang.String[])
	 */
	@Override
	public int delete(Uri uri, String where, String[] whereArgs) {
		int count;
		int id;
		switch (sUriMatcher.match(uri)) {
		/*case DIARYS:
			count = mDAO.deleteDiarys(where, whereArgs);
			break;
		case DIARY_ID:
			int diaryId = Integer.parseInt(uri.getPathSegments().get(Diary.DiaryItem.DIARY_ID_PATH_POSITION));
			count = mDAO.deleteDiarysById(diaryId);
			break;*/
		case MEDICATIONSLIST:
			count = apsDao.delete(where, whereArgs,MedicationList.MedicationItem.TABLE_NAME);
			break;
		case MEDICATIONSLIST_ID:
			id = Integer.parseInt(uri.getPathSegments().get(MedicationList.MedicationItem.MEDICATIONS_ID_PATH_POSITION));
			count = apsDao.deleteById(id,BaseColumns._ID,MedicationList.MedicationItem.TABLE_NAME);
			break;
		case MEDICATIONS:
			count = apsDao.delete(where, whereArgs,DailyMedication.MedicationItem.TABLE_NAME);
			break;
		case MEDICATIONS_ID:
			id = Integer.parseInt(uri.getPathSegments().get(DailyMedication.MedicationItem.MEDICATIONS_ID_PATH_POSITION));
			count = apsDao.deleteById(id,BaseColumns._ID,DailyMedication.MedicationItem.TABLE_NAME);
			break;
		case DBAVAILABLE_MEDICATIONS:
			count = apsDao.delete(where, whereArgs,AvailableMedicationInfoInDB.DBMedicationListItem.TABLE_NAME);
			break;
		case DBAVAILABLE_MEDICATIONS_ID:
			id = Integer.parseInt(uri.getPathSegments().get(AvailableMedicationInfoInDB.DBMedicationListItem.MEDICATIONS_IN_DB_ID_PATH_POSITION));
			count = apsDao.deleteById(id,BaseColumns._ID,AvailableMedicationInfoInDB.DBMedicationListItem.TABLE_NAME);
			break;
		case TB_GPS_LOCATION:
			count = apsDao.delete(where, whereArgs,LocationTrackingData.LocationPoint.TABLE_NAME);
			break;
		case TB_GPS_LOCATION_ID:
			id = Integer.parseInt(uri.getPathSegments().get(LocationTrackingData.LocationPoint.LOCATION_IN_DB_ID_PATH_POSITION));
			count = apsDao.deleteById(id,BaseColumns._ID,LocationTrackingData.LocationPoint.TABLE_NAME);
			break;
		case TB_LOCATION_DURATION:
			count = apsDao.delete(where, whereArgs,LocationTrackingDuration.TrackingDurationItem.TABLE_NAME);
			break;
		case TB_LOCATION_DURATION_ID:
			id = Integer.parseInt(uri.getPathSegments().get(LocationTrackingData.LocationPoint.LOCATION_IN_DB_ID_PATH_POSITION));
			count = apsDao.deleteById(id,BaseColumns._ID,LocationTrackingDuration.TrackingDurationItem.TABLE_NAME);
			break;
		case FORM_CONTENT:
			count = apsDao.delete(where, whereArgs,FormContentData.FormContentItems.TABLE_NAME);
			break;
		case FORM_CONTENT_ID:
			id = Integer.parseInt(uri.getPathSegments().get(FormContentData.FormContentItems.FORM_IN_DB_ID_PATH_POSITION));
			count = apsDao.deleteById(id,BaseColumns._ID,FormContentData.FormContentItems.TABLE_NAME);
			break;
		case DAILY_DIARY:
			count = apsDao.delete(where, whereArgs,DailyDiaryDB.DailyDiaryItem.TABLE_NAME);
			break;
		case DAILY_DIARY_ID:
			id = Integer.parseInt(uri.getPathSegments().get(DailyDiaryDB.DailyDiaryItem.DAILY_DIARY_ID_PATH_POSITION));
			count = apsDao.deleteById(id,BaseColumns._ID,DailyDiaryDB.DailyDiaryItem.TABLE_NAME);
			break;
		case PEAK_FLOW:
			count = apsDao.delete(where, whereArgs,PeakFlowDB.PeakFlowItem.TABLE_NAME);
			break;
		case PEAK_FLOW_ID:
			id = Integer.parseInt(uri.getPathSegments().get(PeakFlowDB.PeakFlowItem.PEAK_FLOW_ID_PATH_POSITION));
			count = apsDao.deleteById(id,BaseColumns._ID,PeakFlowDB.PeakFlowItem.TABLE_NAME);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		getContext().getContentResolver().notifyChange(uri, null);

		return count;
	}

	/* (non-Javadoc)
	 * @see android.content.ContentProvider#getType(android.net.Uri)
	 */
	
	@Override
	public String getType(Uri uri) {
		switch (sUriMatcher.match(uri)) {
		/*case DIARYS:
			return Diary.DiaryItem.CONTENT_TYPE;
		case DIARY_ID:
			return Diary.DiaryItem.CONTENT_ITEM_TYPE;*/
		case MEDICATIONSLIST:
			return MedicationList.MedicationItem.CONTENT_TYPE;
		case MEDICATIONSLIST_ID:
			return MedicationList.MedicationItem.CONTENT_ITEM_TYPE;
		case MEDICATIONS:
			return DailyMedication.MedicationItem.CONTENT_TYPE;
		case MEDICATIONS_ID:
			return DailyMedication.MedicationItem.CONTENT_ITEM_TYPE;
		case DBAVAILABLE_MEDICATIONS_ID:
			return AvailableMedicationInfoInDB.DBMedicationListItem.CONTENT_ITEM_TYPE;		
		case TB_GPS_LOCATION_ID:
			return LocationTrackingData.LocationPoint.CONTENT_ITEM_TYPE;
		case TB_LOCATION_DURATION_ID:
			return LocationTrackingDuration.TrackingDurationItem.CONTENT_ITEM_TYPE;
		case FORM_CONTENT_ID:
		    return FormContentData.FormContentItems.CONTENT_ITEM_TYPE;
		case DAILY_DIARY_ID:
			return DailyDiaryDB.DailyDiaryItem.CONTENT_ITEM_TYPE;
		case PEAK_FLOW_ID:
			return PeakFlowDB.PeakFlowItem.CONTENT_ITEM_TYPE;
		
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
	}

	/* (non-Javadoc)
	 * @see android.content.ContentProvider#insert(android.net.Uri, android.content.ContentValues)
	 */
	@Override
	public Uri insert(Uri uri, ContentValues values) {
		/* long rowId = mDAO.insertDiary(values);

        if (rowId > 0) {
            Uri diaryUri = ContentUris.withAppendedId(Diary.DiaryItem.CONTENT_ID_URI_BASE, rowId);
            getContext().getContentResolver().notifyChange(diaryUri, null);
            return diaryUri;
        }

        throw new SQLException("Failed to insert row into " + uri);*/

		Uri insertUri=null;
		long rowId;

		switch (sUriMatcher.match(uri)) {
		/*case DIARYS:
		case DIARY_ID:
			rowId = mDAO.insertDiary(values);
			if (rowId > 0) {
				insertUri = ContentUris.withAppendedId(Diary.DiaryItem.CONTENT_ID_URI_BASE, rowId);
				getContext().getContentResolver().notifyChange(insertUri, null);
				return insertUri;
			}*/
		case MEDICATIONS_ID:
		case MEDICATIONS:
			rowId = apsDao.insert(values,DailyMedication.MedicationItem.COLUMN_NAME_LASTTAKEN,DailyMedication.MedicationItem.TABLE_NAME);
			if (rowId > 0) {
				insertUri = ContentUris.withAppendedId(DailyMedication.MedicationItem.CONTENT_ID_URI_BASE, rowId);
				getContext().getContentResolver().notifyChange(insertUri, null);
				return insertUri;
			}
		case MEDICATIONSLIST:
		case MEDICATIONSLIST_ID:
			rowId = apsDao.insert(values,MedicationList.MedicationItem.COLUMN_NAME_ENTRYTIME,MedicationList.MedicationItem.TABLE_NAME);
			if (rowId > 0) {
				insertUri = ContentUris.withAppendedId(MedicationList.MedicationItem.CONTENT_ID_URI_BASE, rowId);
				getContext().getContentResolver().notifyChange(insertUri, null);
				return insertUri;
			}
		case DBAVAILABLE_MEDICATIONS:
		case DBAVAILABLE_MEDICATIONS_ID:
			rowId = apsDao.insert(values,AvailableMedicationInfoInDB.DBMedicationListItem.COLUMN_NAME_ENTRYTIME,AvailableMedicationInfoInDB.DBMedicationListItem.TABLE_NAME);
			if (rowId > 0) {
				insertUri = ContentUris.withAppendedId(AvailableMedicationInfoInDB.DBMedicationListItem.CONTENT_ID_URI_BASE, rowId);
				getContext().getContentResolver().notifyChange(insertUri, null);
				return insertUri;
			}
		case TB_GPS_LOCATION:
		case TB_GPS_LOCATION_ID:
			rowId = apsDao.insert(values,LocationTrackingData.LocationPoint.COLUMN_NAME_ENTRYTIME,LocationTrackingData.LocationPoint.TABLE_NAME);
			if (rowId > 0) {
				insertUri = ContentUris.withAppendedId(LocationTrackingData.LocationPoint.CONTENT_ID_URI_BASE, rowId);
				getContext().getContentResolver().notifyChange(insertUri, null);
				return insertUri;
			}
		case TB_LOCATION_DURATION:
		case TB_LOCATION_DURATION_ID:
			rowId = apsDao.insert(values,LocationTrackingDuration.TrackingDurationItem.COLUMN_NAME_ENTRYTIME,LocationTrackingDuration.TrackingDurationItem.TABLE_NAME);
			if (rowId > 0) {
				insertUri = ContentUris.withAppendedId(LocationTrackingDuration.TrackingDurationItem.CONTENT_ID_URI_BASE, rowId);
				getContext().getContentResolver().notifyChange(insertUri, null);
				return insertUri;
			}
		case FORM_CONTENT:
		case FORM_CONTENT_ID:
		    rowId = apsDao.insert(values,FormContentData.FormContentItems.COLUMN_NAME_LAST_COLLECTED_TIME,FormContentData.FormContentItems.TABLE_NAME);
		    if (rowId > 0) {
			insertUri = ContentUris.withAppendedId(FormContentData.FormContentItems.CONTENT_ID_URI_BASE, rowId);
			getContext().getContentResolver().notifyChange(insertUri, null);
			return insertUri;
		    }
		case DAILY_DIARY:
		case DAILY_DIARY_ID:
			rowId = apsDao.insert(values,DailyDiaryDB.DailyDiaryItem.COLUMN_NAME_ENTRYTIME,DailyDiaryDB.DailyDiaryItem.TABLE_NAME);
			if (rowId > 0) {
				insertUri = ContentUris.withAppendedId(DailyDiaryDB.DailyDiaryItem.CONTENT_ID_URI_BASE, rowId);
				getContext().getContentResolver().notifyChange(insertUri, null);
				return insertUri;
			}
		case PEAK_FLOW:
		case PEAK_FLOW_ID:
			rowId = apsDao.insert(values,PeakFlowDB.PeakFlowItem.COLUMN_NAME_ENTRYTIME,PeakFlowDB.PeakFlowItem.TABLE_NAME);
			if (rowId > 0) {
				insertUri = ContentUris.withAppendedId(PeakFlowDB.PeakFlowItem.CONTENT_ID_URI_BASE, rowId);
				getContext().getContentResolver().notifyChange(insertUri, null);
				return insertUri;
			}
		default:
			throw new SQLException("Failed to insert row into " + uri);
		}
	}

	/* (non-Javadoc)
	 * @see android.content.ContentProvider#onCreate()
	 */
	@Override
	public boolean onCreate() {
		//mDAO = new DAO(getContext());
		apsDao=new ApsDao(getContext());
		return true;
	}


	/* (non-Javadoc)
	 * @see android.content.ContentProvider#query(android.net.Uri, java.lang.String[], java.lang.String, java.lang.String[], java.lang.String)
	 */
	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		/*Cursor cursor = null;
       	   cursor = mDAO.queryDiarys(projection, selection, selectionArgs, sortOrder);
	       return cursor;*/
		Cursor cursor = null;
		switch (sUriMatcher.match(uri)) {
		/*case DIARYS:
		case DIARY_ID:
			cursor = mDAO.queryDiarys(projection, selection, selectionArgs, sortOrder);
			return cursor;*/
		case MEDICATIONS_ID:
		case MEDICATIONS:

			cursor = apsDao.query(projection, selection, selectionArgs, sortOrder,DailyMedication.MedicationItem.TABLE_NAME,DailyMedication.MedicationItem.DEFAULT_SORT_ORDER);
			return cursor;
		case MEDICATIONSLIST:
		case MEDICATIONSLIST_ID:

			cursor = apsDao.query(projection, selection, selectionArgs, sortOrder,MedicationList.MedicationItem.TABLE_NAME,MedicationList.MedicationItem.DEFAULT_SORT_ORDER);
			return cursor;
		case DBAVAILABLE_MEDICATIONS:
		case DBAVAILABLE_MEDICATIONS_ID:

			cursor = apsDao.query(projection, selection, selectionArgs, sortOrder,AvailableMedicationInfoInDB.DBMedicationListItem.TABLE_NAME,AvailableMedicationInfoInDB.DBMedicationListItem.DEFAULT_SORT_ORDER);
			return cursor;
		case TB_GPS_LOCATION:
		case TB_GPS_LOCATION_ID:

			cursor = apsDao.query(projection, selection, selectionArgs, sortOrder,LocationTrackingData.LocationPoint.TABLE_NAME,LocationTrackingData.LocationPoint.DEFAULT_SORT_ORDER);
			return cursor;
		case TB_LOCATION_DURATION:
		case TB_LOCATION_DURATION_ID:
			cursor = apsDao.query(projection, selection, selectionArgs, sortOrder,LocationTrackingDuration.TrackingDurationItem.TABLE_NAME,LocationTrackingDuration.TrackingDurationItem.DEFAULT_SORT_ORDER);
			return cursor;
		case FORM_CONTENT:
		case FORM_CONTENT_ID:
		    cursor = apsDao.query(projection, selection, selectionArgs, sortOrder,FormContentData.FormContentItems.TABLE_NAME,FormContentData.FormContentItems.DEFAULT_SORT_ORDER);
		    return cursor;
		case DAILY_DIARY:
		case DAILY_DIARY_ID:
		    cursor = apsDao.query(projection, selection, selectionArgs, sortOrder,DailyDiaryDB.DailyDiaryItem.TABLE_NAME,DailyDiaryDB.DailyDiaryItem.DEFAULT_SORT_ORDER);
		    return cursor;
		case PEAK_FLOW:
		case PEAK_FLOW_ID:
			cursor = apsDao.query(projection, selection, selectionArgs, sortOrder,PeakFlowDB.PeakFlowItem.TABLE_NAME,PeakFlowDB.PeakFlowItem.DEFAULT_SORT_ORDER);
			return cursor;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

	}

	/* (non-Javadoc)
	 * @see android.content.ContentProvider#update(android.net.Uri, android.content.ContentValues, java.lang.String, java.lang.String[])
	 */
	@Override
	public int update(Uri uri, ContentValues values, String where,
			String[] whereArgs) {
		int count;
		long id;

		switch (sUriMatcher.match(uri)) {
		/*case DIARYS:
			count = mDAO.updateDiarys(values, where, whereArgs);
			break;
		case DIARY_ID:
			id= ContentUris.parseId(uri);
			count = mDAO.updateDiaryById(id, values);
			break;*/
		case MEDICATIONS:
			count = apsDao.update(values, where, whereArgs,DailyMedication.MedicationItem.TABLE_NAME);
			break;
		case MEDICATIONS_ID:
			id = ContentUris.parseId(uri);
			count = apsDao.updateById(id, values,DailyMedication.MedicationItem.TABLE_NAME,BaseColumns._ID);
			break;
		case MEDICATIONSLIST:
			count = apsDao.update(values, where, whereArgs,MedicationList.MedicationItem.TABLE_NAME);
			break;
		case MEDICATIONSLIST_ID:
			id = ContentUris.parseId(uri);
			count = apsDao.updateById(id, values,MedicationList.MedicationItem.TABLE_NAME,BaseColumns._ID);
			break;
		case DBAVAILABLE_MEDICATIONS:
			count = apsDao.update(values, where, whereArgs,AvailableMedicationInfoInDB.DBMedicationListItem.TABLE_NAME);
			break;
		case DBAVAILABLE_MEDICATIONS_ID:
			id = ContentUris.parseId(uri);
			count = apsDao.updateById(id, values,AvailableMedicationInfoInDB.DBMedicationListItem.TABLE_NAME,BaseColumns._ID);
			break;
		case TB_GPS_LOCATION:
			count = apsDao.update(values, where, whereArgs,LocationTrackingData.LocationPoint.TABLE_NAME);
			break;
		case TB_GPS_LOCATION_ID:
			id = ContentUris.parseId(uri);
			count = apsDao.updateById(id, values,LocationTrackingData.LocationPoint.TABLE_NAME,BaseColumns._ID);
			break;
		case TB_LOCATION_DURATION:
			count = apsDao.update(values, where, whereArgs,LocationTrackingDuration.TrackingDurationItem.TABLE_NAME);
			break;
		case TB_LOCATION_DURATION_ID:
			id = ContentUris.parseId(uri);
			count = apsDao.updateById(id, values,LocationTrackingDuration.TrackingDurationItem.TABLE_NAME,BaseColumns._ID);
			break;
		case FORM_CONTENT:
			count = apsDao.update(values, where, whereArgs,FormContentData.FormContentItems.TABLE_NAME);
			break;
		case FORM_CONTENT_ID:
		    id = ContentUris.parseId(uri);
		    count = apsDao.updateById(id, values,FormContentData.FormContentItems.TABLE_NAME,BaseColumns._ID);
		    break;
		case DAILY_DIARY:
			count = apsDao.update(values, where, whereArgs,DailyDiaryDB.DailyDiaryItem.TABLE_NAME);
			break;
		case DAILY_DIARY_ID:
		    id = ContentUris.parseId(uri);
		    count = apsDao.updateById(id, values,DailyDiaryDB.DailyDiaryItem.TABLE_NAME,BaseColumns._ID);
		    break;
		case PEAK_FLOW:
			count = apsDao.update(values, where, whereArgs,PeakFlowDB.PeakFlowItem.TABLE_NAME);
			break;
		case PEAK_FLOW_ID:
			id = ContentUris.parseId(uri);
			count = apsDao.updateById(id, values,PeakFlowDB.PeakFlowItem.TABLE_NAME,BaseColumns._ID);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		getContext().getContentResolver().notifyChange(uri, null);

		return count;
	}

	/**
	 * Gets the db helper for test.
	 *
	 * @return the db helper for test
	 */
	public DatabaseHelper getDbHelperForTest() {
		return apsDao.getDbHelperForTest();
	}

	/**
	 * Check columns.
	 *
	 * @param projection the projection
	 * @param available the available
	 */
	private void checkColumns(String[] projection,String[] available) {
		/*String[] available = { LocTable.COLUMN_TIME,
				LocTable.COLUMN_LONGITUDE, LocTable.COLUMN_LATITUDE,
				LocTable.COLUMN_ID };*/
		if (projection != null) {
			HashSet<String> requestedColumns = new HashSet<String>(
					Arrays.asList(projection));
			HashSet<String> availableColumns = new HashSet<String>(
					Arrays.asList(available));
			// Check if all columns which are requested are available
			if (!availableColumns.containsAll(requestedColumns)) {
				throw new IllegalArgumentException(
						"Unknown columns in projection");
			}
		}
	}
}
