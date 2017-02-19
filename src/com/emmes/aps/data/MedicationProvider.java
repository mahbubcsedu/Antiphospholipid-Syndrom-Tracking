package com.emmes.aps.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;

public class MedicationProvider extends ContentProvider {
	
	private DAO mDAO;
    private static final UriMatcher sUriMatcher;
    private static final int MEDS = 1;
    private static final int MED_ID = 2;
    	
    
    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(Diary.AUTHORITY, "meds", MEDS);
        sUriMatcher.addURI(Diary.AUTHORITY, "meds/#", MED_ID);      
    }
        
	@Override
	public int delete(Uri uri, String where, String[] whereArgs) {
        int count;

        switch (sUriMatcher.match(uri)) {
            case MEDS:
            	count = mDAO.deleteMeds(where, whereArgs);
            	break;
            case MED_ID:
            	int medId = Integer.parseInt(uri.getPathSegments().get(Medication.MedItem.MED_ID_PATH_POSITION));
            	count = mDAO.deleteMedsById(medId);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return count;
	}

	@Override
	public String getType(Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case MEDS:
                return Medication.MedItem.CONTENT_TYPE;
            case MED_ID:
                return Medication.MedItem.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
        long rowId = mDAO.insertMed(values);

        if (rowId > 0) {
            Uri medUri = ContentUris.withAppendedId(Medication.MedItem.CONTENT_ID_URI_BASE, rowId);
            getContext().getContentResolver().notifyChange(medUri, null);
            return medUri;
        }

        throw new SQLException("Failed to insert row into " + uri);
	}

	@Override
	public boolean onCreate() {
		mDAO = new DAO(getContext());
		return true;
	}


	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		   Cursor cursor = null;
       	   cursor = mDAO.queryMeds(projection, selection, selectionArgs, sortOrder);
	       return cursor;
	}

	@Override
	public int update(Uri uri, ContentValues values, String where,
			String[] whereArgs) {
        int count;

        switch (sUriMatcher.match(uri)) {
            case MEDS:
                count = mDAO.updateMeds(values, where, whereArgs);
                break;
            case MED_ID:
	             long diaryId = ContentUris.parseId(uri);
	             count = mDAO.updateMedById(diaryId, values);
	             break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return count;
	}

    public DatabaseHelper getDbHelperForTest() {
        return mDAO.getDbHelperForTest();
    }
}
