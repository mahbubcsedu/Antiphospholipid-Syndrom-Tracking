/* 
 * Copyright 2014 (C) The EMMES Corporation 
 *  
 * Created on : 02-06-2014
 * Last Update: Jul 14, 2014 12:05:25 PM
 * Author     : Mahbubur Rahman
 * Title      : Summer intern 2014 
 * Project    : Daily Diary Android Application
 * 
 */
package com.emmes.aps.sync;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.emmes.aps.data.DailyDiaryDB;
import com.emmes.aps.data.DailyMedication;
import com.emmes.aps.data.LocationTrackingData;
import com.emmes.aps.data.LocationTrackingDuration;
import com.emmes.aps.data.PeakFlowDB;
import com.emmes.aps.util.DataTransferUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
// TODO: Auto-generated Javadoc
import android.database.Cursor;
import android.provider.BaseColumns;
import android.util.Log;

// TODO: Auto-generated Javadoc
/**
 * The Class DataSyncRequestReceiver is a BroadCastReceiver
 * {@linkplain BroadCastReceiver} which broadcast while data is sync with the
 * server. Upon receiving the response from the server, this broadcast receiver,
 * process the data sent by the server. The main data here is the location
 * tracking duration data which should be set from the server. This will update
 * the location duration table if anything added to the server.
 * 
 * @author Mahbubur Rahman
 * @see BroadcastReceiver
 * @since 1.0
 */
public class DataSyncRequestReceiver extends BroadcastReceiver
{

	/** The context. */
	Context mContext;

	/** The Constant TAG. */
	private final static String TAG = "DataSyncRequestReceiver";
	/** The Constant PROCESS_RESPONSE. */
	public static final String PROCESS_RESPONSE = "com.emmes.aps.intent.action.PROCESS_RESPONSE";

	/* (non-Javadoc)
	 * 
	 * @see
	 * android.content.BroadcastReceiver#onReceive(android.content.Context,
	 * android.content.Intent) */
	@Override
	public void onReceive(Context context, Intent intent)
	{

		this.mContext = context;
		String response = null;
		String responseType = intent.getStringExtra(DataSyncRequest.IN_MSG);

		if (responseType.trim().equalsIgnoreCase(SyncUtils.SYNC_DATA_SERVICE_NAME_TAG))
		{
			// you can choose to implement another transaction here
			response = intent.getStringExtra(DataSyncRequest.OUT_MSG);
			syncLocalDatabaseWithServerResponse(response);
		}

	}

	/**
	 * This syncTrackingDuration method is for updating the tracking duration
	 * data based on the response from the server. If there any different
	 * between server settings of tracking duration with the local table, the
	 * locat table is get updated.
	 * <p>
	 * <b>Algorithm: </b>
	 * <p>
	 * <ul>
	 * <li>The process first retrieve information from local database about
	 * tracking duration.</li>
	 * <li>Then it create a HashMap {@linkplain HashMap} using the data.</li>
	 * <li>The response from the server is compared to the HashMap. So if any new
	 * data is there, it will added.</li>
	 * <li>Finally the update HashMap is saved to the database tracking duration
	 * table.</li>
	 * </ul>
	 * 
	 * @param durationList
	 *            the list containing the tracking duration data.
	 * @return none
	 * @throws none
	 * @since 1.0
	 * @author Mahbubur Rahman
	 */
	private void syncTrackingDuration(ArrayList<LocationTrackingDurationFormat> durationList)
	{
		List<LocationTrackingDurationFormat> localLocList = new ArrayList<LocationTrackingDurationFormat>();
		LocationTrackingDurationFormat obj = new LocationTrackingDurationFormat();

		final String[] PROJECTION = new String[] { BaseColumns._ID,
				LocationTrackingDuration.TrackingDurationItem.COLUMN_NAME_ENTRYTIME,
				LocationTrackingDuration.TrackingDurationItem.COLUMN_NAME_START_TIME,
				LocationTrackingDuration.TrackingDurationItem.COLUMN_NAME_END_TIME, };

		// Constants representing column positions from PROJECTION.
		final int COLUMN_ID = 0;
		final int COLUMN_ENTRY_TIME = 1;
		final int COLUMN_START_TIME = 2;
		final int COLUMN_END_TIME = 3;
		Cursor cursor = this.mContext.getContentResolver().query(LocationTrackingDuration.TrackingDurationItem.CONTENT_URI, PROJECTION, null, null,
				null);
		ContentValues content = new ContentValues();
		if (cursor.getCount() > 0)
		{

			cursor.moveToFirst();
			do
			{
				obj = new LocationTrackingDurationFormat();
				obj.setId(cursor.getString(COLUMN_ID));
				obj.setEntryTime(cursor.getString(COLUMN_ENTRY_TIME));
				obj.setStartDate(cursor.getString(COLUMN_START_TIME));
				obj.setEndDate(cursor.getString(COLUMN_END_TIME));
				localLocList.add(obj);
			} while (cursor.moveToNext());
		}
		// make sure to close the cursor
		cursor.close();

		HashMap<String, LocationTrackingDurationFormat> entryMap = new HashMap<String, LocationTrackingDurationFormat>();
		for (LocationTrackingDurationFormat e : localLocList)
		{
			entryMap.put(e.id, e);
		}

		/**
		 * if any new value exists then insert to local database
		 */
		for (LocationTrackingDurationFormat e : durationList)
		{
			if (!entryMap.containsKey(e.id))
			{
				content = new ContentValues();
				content.put(BaseColumns._ID, e.id);
				content.put(LocationTrackingDuration.TrackingDurationItem.COLUMN_NAME_ENTRYTIME, e.entryTime);
				content.put(LocationTrackingDuration.TrackingDurationItem.COLUMN_NAME_START_TIME, e.startDate);
				content.put(LocationTrackingDuration.TrackingDurationItem.COLUMN_NAME_END_TIME, e.endDate);
				this.mContext.getContentResolver().insert(LocationTrackingDuration.TrackingDurationItem.CONTENT_URI, content);
				content.clear();

			}
		}

	}

	/**
	 * This statusUpdateToComplete updates the database table data which were in progress with transferring.
	 * If the data successfully transfered, then the status is updated as transfere complete. 
	 * @param none
	 * @return none
	 * @since 1.0
	 * @author Mahbubur Rahman
	 */
	public void statusUpdateToComplete()
	{
		ContentValues values = new ContentValues();

		values.put(DailyMedication.MedicationItem.COLUMN_NAME_STATUS, DataTransferUtils.STATUS_DATA_TRANSFER_COMPLETE);
		String where = DailyMedication.MedicationItem.COLUMN_NAME_STATUS + "='" + DataTransferUtils.STATUS_DATA_TRANSFER_IN_QUEUE + "'";
		// String[] args = new String[] { Integer.toString(med_id) };
		int numofrowupdated = this.mContext.getContentResolver().update(DailyMedication.MedicationItem.CONTENT_URI, values, where, null);
		Log.d(TAG, numofrowupdated + " rows of DailyMedication updated with status C");

		values.clear();
		values.put(LocationTrackingData.LocationPoint.COLUMN_NAME_STATUS, DataTransferUtils.STATUS_DATA_TRANSFER_COMPLETE);
		where = LocationTrackingData.LocationPoint.COLUMN_NAME_STATUS + "='" + DataTransferUtils.STATUS_DATA_TRANSFER_IN_QUEUE + "'";
		// String[] args = new String[] { Integer.toString(med_id) };
		numofrowupdated = this.mContext.getContentResolver().update(LocationTrackingData.LocationPoint.CONTENT_URI, values, where, null);
		Log.d(TAG, numofrowupdated + " rows of LocationTrackingData updated with status C");

		values.clear();
		values.put(DailyDiaryDB.DailyDiaryItem.COLUMN_NAME_STATUS, DataTransferUtils.STATUS_DATA_TRANSFER_COMPLETE);
		where = DailyDiaryDB.DailyDiaryItem.COLUMN_NAME_STATUS + "='" + DataTransferUtils.STATUS_DATA_TRANSFER_IN_QUEUE + "'";
		// String[] args = new String[] { Integer.toString(med_id) };
		numofrowupdated = this.mContext.getContentResolver().update(DailyDiaryDB.DailyDiaryItem.CONTENT_URI, values, where, null);
		Log.d(TAG, numofrowupdated + " rows of DailyDiaryDB.DailyDiaryItem updated with status C");

		values.clear();
		values.put(PeakFlowDB.PeakFlowItem.COLUMN_NAME_STATUS, DataTransferUtils.STATUS_DATA_TRANSFER_COMPLETE);
		where = PeakFlowDB.PeakFlowItem.COLUMN_NAME_STATUS + "='" + DataTransferUtils.STATUS_DATA_TRANSFER_IN_QUEUE + "'";
		// String[] args = new String[] { Integer.toString(med_id) };
		numofrowupdated = this.mContext.getContentResolver().update(PeakFlowDB.PeakFlowItem.CONTENT_URI, values, where, null);
		Log.d(TAG, numofrowupdated + " rows of DailyDiaryDB.DailyDiaryItem updated with status C");
	}

	/**
	 * This syncLocalDatabaseWithServerResponse method receives the server response. 
	 * If the response contains success message, then it updated the database as successfully transfered the data to the server otherwise, it does not change the database entry using
	 * the method {@link com.emmes.aps.sync#statusUpdateToComplete}.
	 * <p>This method also receives the response as JSon object of the tracking duration data. It then call the method {@link com.emmes.aps.sync#syncTrackingDuration} and update the database.
	 * 
	 * @param response
	 *            response from the server.
	 * @return none
	 * @throws JSONException 
	 * @since 1.0
	 * @author Mahbubur Rahman
	 */
	private void syncLocalDatabaseWithServerResponse(String response)
	{

		JSONObject responseObj = null;
		try
		{
			// create JSON object from JSON string
			responseObj = new JSONObject(response);
			// get the success property
			boolean success = responseObj.getBoolean("success");
			if (success)
			{

				statusUpdateToComplete();

				Gson gson = new Gson();
				// get the country information property
				String durationinfo = responseObj.getString("durationinfo");
				// create java object from the JSON object
				// LocationTrackingDurationFormat country =
				// gson.fromJson(durationinfo,
				// LocationTrackingDurationFormat.class);
				ArrayList<LocationTrackingDurationFormat> dInfolist = gson.fromJson(durationinfo,
						new TypeToken<ArrayList<LocationTrackingDurationFormat>>() {
				}.getType());
				// set values from your country java object

				for (int i = 0; i < dInfolist.size(); i++)
				{

					LocationTrackingDurationFormat z;

					if ((z = dInfolist.get(i)) != null)
					{
						// TODO have to write function for insert to database
						// and also have to check for failure.
						// as we are using here multiple tables, we have to make
						// sure every data has been saved, if one failed, undo
						// the insertion.
						System.out.println(z.getEntryTime() + "," + z.getStartDate());
						// name.setText(z.getId());
						// continent.setText(z.getEntryTime());
						// region.setText(z.getStartDate());
						// lifeExpectancy.setText(df2.format(z.getEndDate()));
					}

				}

				syncTrackingDuration(dInfolist);

				/* gnp.setText(df2.format(country.getGnp()));
				 * surfaceArea.setText(df2.format(country.getSurfaceArea()));
				 * population.setText(String.valueOf(country.getPopulation())); */

			}
			else
			{

				// TODO have to write or do some processing for failure like
				// have to avoid deleting from database.

			}

		} catch (JSONException e)
		{
			e.printStackTrace();
		}

	}

}