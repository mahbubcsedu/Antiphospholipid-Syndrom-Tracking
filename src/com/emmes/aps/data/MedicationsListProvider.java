/*
 * @author  Mahbubur Rahman
 * IT Intern Summer 2014
 * EMMES Corporation
 * Copyright (c) to EMMES Corporation 2014
 */
package com.emmes.aps.data;


import com.emmes.aps.dao.MedicationListDAO;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;

// TODO: Auto-generated Javadoc
/**
 * The Class MedicationsListProvider.
 */
public class MedicationsListProvider extends ContentProvider {

	/** The m dao. */
	private MedicationListDAO mDAO;

	/** The Constant sUriMatcher. */
	private static final UriMatcher sUriMatcher;

	/** The Constant MEDICATIONSLIST. */
	private static final int MEDICATIONSLIST=5;

	/** The Constant MEDICATIONSLIST_ID. */
	private static final int MEDICATIONSLIST_ID=6;

	static {
		sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

		sUriMatcher.addURI(MedicationList.AUTHORITY, "medicationslist", MEDICATIONSLIST);
		sUriMatcher.addURI(MedicationList.AUTHORITY, "medicationslist/#", MEDICATIONSLIST_ID);
	}

	/* (non-Javadoc)
	 * @see android.content.ContentProvider#delete(android.net.Uri, java.lang.String, java.lang.String[])
	 */
	@Override
	public int delete(Uri uri, String where, String[] whereArgs) {
		int count;

		switch (sUriMatcher.match(uri)) {

		case MEDICATIONSLIST:
			count = mDAO.deleteMedicationList(where, whereArgs);
			break;
		case MEDICATIONSLIST_ID:
			int medicationsId = Integer.parseInt(uri.getPathSegments().get(MedicationList.MedicationItem.MEDICATIONS_ID_PATH_POSITION));
			count = mDAO.deleteMedicationListById(medicationsId);
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

		case MEDICATIONSLIST:
			return MedicationList.MedicationItem.CONTENT_TYPE;
		case MEDICATIONSLIST_ID:
			return MedicationList.MedicationItem.CONTENT_ITEM_TYPE;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
	}

	/* (non-Javadoc)
	 * @see android.content.ContentProvider#insert(android.net.Uri, android.content.ContentValues)
	 */
	@Override
	public Uri insert(Uri uri, ContentValues values) {
		long rowId = mDAO.insertMedicationList(values);

		if (rowId > 0) {
			Uri medicationsUri = ContentUris.withAppendedId(MedicationList.MedicationItem.CONTENT_ID_URI_BASE, rowId);
			getContext().getContentResolver().notifyChange(medicationsUri, null);
			return medicationsUri;
		}

		throw new SQLException("Failed to insert row into " + uri);
	}

	/* (non-Javadoc)
	 * @see android.content.ContentProvider#onCreate()
	 */
	@Override
	public boolean onCreate() {
		mDAO = new MedicationListDAO(getContext());
		return true;
	}


	/* (non-Javadoc)
	 * @see android.content.ContentProvider#query(android.net.Uri, java.lang.String[], java.lang.String, java.lang.String[], java.lang.String)
	 */
	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		Cursor cursor = null;
		cursor = mDAO.queryMedicationList(projection, selection, selectionArgs, sortOrder);
		return cursor;
	}

	/* (non-Javadoc)
	 * @see android.content.ContentProvider#update(android.net.Uri, android.content.ContentValues, java.lang.String, java.lang.String[])
	 */
	@Override
	public int update(Uri uri, ContentValues values, String where,
			String[] whereArgs) {
		int count;

		switch (sUriMatcher.match(uri)) {
		case MEDICATIONSLIST:
			count = mDAO.updateMedicationList(values, where, whereArgs);
			break;
		case MEDICATIONSLIST_ID:
			long medicationId = ContentUris.parseId(uri);
			count = mDAO.updateMedicationListById(medicationId, values);
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
		return mDAO.getDbHelperForTest();
	}
}
