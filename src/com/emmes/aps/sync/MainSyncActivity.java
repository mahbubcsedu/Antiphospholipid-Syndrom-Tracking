/* 
 * Copyright 2014 (C) The EMMES Corporation 
 *  
 * Created on : 02-06-2014
 * Last Update: Jul 2, 2014 1:11:31 PM
 * Author     : Mahbubur Rahman
 * Title      : Summer intern 2014 
 * Project    : Daily Diary Android Application
 * 
 */
package com.emmes.aps.sync;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.emmes.aps.R;
import com.emmes.aps.data.LocationTrackingDuration;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

// TODO: Auto-generated Javadoc
/**
 * The Class MainSyncActivity.
 */
public class MainSyncActivity extends Activity {

	/** The receiver. */
	private MyRequestReceiver receiver;

	/** The get info. */
	private Button getInfo;

	/** The country code. */
	private EditText countryCode;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mainsync);

		// Register your receiver so that the Activity can be notified
		// when the JSON response came back
		IntentFilter filter = new IntentFilter(MyRequestReceiver.PROCESS_RESPONSE);
		filter.addCategory(Intent.CATEGORY_DEFAULT);
		receiver = new MyRequestReceiver();
		registerReceiver(receiver, filter);

		countryCode = (EditText) findViewById(R.id.countryCode);

		// button that will trigger the http request
		getInfo = (Button) findViewById(R.id.getInfo);
		getInfo.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				getCountryInformation();
				// hide the soft keyboard
				InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				mgr.hideSoftInputFromWindow(countryCode.getWindowToken(), 0);

			}
		});

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		Log.v("AndroidJSONTutorialActivity", "onDestory");

		unregisterReceiver(receiver);
		super.onDestroy();
	}

	/**
	 * Gets the country information.
	 * 
	 * @return the country information
	 */
	private void getCountryInformation() {

		boolean internet = isNetworkAvailable(this);
		if (internet)
		{
			String code = countryCode.getText().toString();
			// if no country code was entered
			if (code.trim().isEmpty())
			{
				Toast toast = Toast.makeText(this, "Please enter a Country Code first!", Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.TOP, 105, 50);
				toast.show();
			}
			else
			{
				// pass the request to your service so that it can
				// run outside the scope of the main UI thread
				Intent msgIntent = new Intent(this, DataSyncRequest.class);
				msgIntent.putExtra(DataSyncRequest.IN_MSG, "getCountryInfo");
				msgIntent.putExtra("countryCode", code.trim());
				startService(msgIntent);
			}
		}
	}

	// parse and display JSON response
	/**
	 * Display country information.
	 * 
	 * @param response
	 *            the response
	 */
	private void displayCountryInformation(String response) {

		JSONObject responseObj = null;
		DecimalFormat df2 = new DecimalFormat("0.00##");

		// get references to your views
		TextView name = (TextView) findViewById(R.id.name);
		TextView continent = (TextView) findViewById(R.id.continent);
		TextView region = (TextView) findViewById(R.id.region);
		TextView lifeExpectancy = (TextView) findViewById(R.id.lifeExpectancy);
		TextView gnp = (TextView) findViewById(R.id.gnp);
		TextView surfaceArea = (TextView) findViewById(R.id.surfaceArea);
		TextView population = (TextView) findViewById(R.id.population);
		TextView errorMessage = (TextView) findViewById(R.id.errorMessage);

		try
		{
			// create JSON object from JSON string
			responseObj = new JSONObject(response);
			// get the success property
			boolean success = responseObj.getBoolean("success");
			if (success)
			{

				Gson gson = new Gson();
				// get the country information property
				String countryInfo = responseObj.getString("countryInfo");
				// create java object from the JSON object
				Country country = gson.fromJson(countryInfo, Country.class);

				// set values from your country java object
				name.setText(country.getName());
				continent.setText(country.getContinent());
				region.setText(country.getRegion());
				lifeExpectancy.setText(df2.format(country.getLifeExpectancy()));
				gnp.setText(df2.format(country.getGnp()));
				surfaceArea.setText(df2.format(country.getSurfaceArea()));
				population.setText(String.valueOf(country.getPopulation()));
				errorMessage.setText(" ");
			}
			else
			{

				name.setText(" ");
				continent.setText(" ");
				region.setText(" ");
				lifeExpectancy.setText(" ");
				gnp.setText(" ");
				surfaceArea.setText(" ");
				population.setText(" ");
				errorMessage.setText("Invalid Country Code!");

			}

		} catch (JSONException e)
		{
			e.printStackTrace();
		}

	}

	// parse and display JSON response
	/**
	 * Display duration information.
	 * 
	 * @param response
	 *            the response
	 */
	private void displayDurationInformation(String response) {

		JSONObject responseObj = null;
		DecimalFormat df2 = new DecimalFormat("0.00##");

		try
		{
			// create JSON object from JSON string
			responseObj = new JSONObject(response);
			// get the success property
			boolean success = responseObj.getBoolean("success");
			if (success)
			{

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

					if ((z = (LocationTrackingDurationFormat) dInfolist.get(i)) != null)
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

				/*
				 * gnp.setText(df2.format(country.getGnp()));
				 * surfaceArea.setText(df2.format(country.getSurfaceArea()));
				 * population.setText(String.valueOf(country.getPopulation()));
				 */

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

	/**
	 * Sync tracking duration.
	 * 
	 * @param durationList
	 *            the duration list
	 */
	private void syncTrackingDuration(ArrayList<LocationTrackingDurationFormat> durationList) {
		List<LocationTrackingDurationFormat> localLocList = new ArrayList<LocationTrackingDurationFormat>();
		LocationTrackingDurationFormat obj = new LocationTrackingDurationFormat();

		final String[] PROJECTION = new String[] { LocationTrackingDuration.TrackingDurationItem._ID,
				LocationTrackingDuration.TrackingDurationItem.COLUMN_NAME_ENTRYTIME,
				LocationTrackingDuration.TrackingDurationItem.COLUMN_NAME_START_TIME,
				LocationTrackingDuration.TrackingDurationItem.COLUMN_NAME_END_TIME, };

		// Constants representing column positions from PROJECTION.
		final int COLUMN_ID = 0;
		final int COLUMN_ENTRY_TIME = 1;
		final int COLUMN_START_TIME = 2;
		final int COLUMN_END_TIME = 3;
		Cursor cursor = getContentResolver().query(LocationTrackingDuration.TrackingDurationItem.CONTENT_URI,
				PROJECTION, null, null, null);
		ContentValues content = new ContentValues();
		if (cursor.getCount() > 0)
		{

			cursor.moveToFirst();
			while (!cursor.isAfterLast())
			{
				obj = new LocationTrackingDurationFormat();
				obj.setId(cursor.getString(COLUMN_ID));
				obj.setEntryTime(cursor.getString(COLUMN_ENTRY_TIME));
				obj.setStartDate(cursor.getString(COLUMN_START_TIME));
				obj.setEndDate(cursor.getString(COLUMN_END_TIME));
				localLocList.add(obj);
			}
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
				content.put(LocationTrackingDuration.TrackingDurationItem._ID, e.id);
				content.put(LocationTrackingDuration.TrackingDurationItem.COLUMN_NAME_ENTRYTIME, e.entryTime);
				content.put(LocationTrackingDuration.TrackingDurationItem.COLUMN_NAME_START_TIME, e.startDate);
				content.put(LocationTrackingDuration.TrackingDurationItem.COLUMN_NAME_END_TIME, e.endDate);
				getContentResolver().insert(LocationTrackingDuration.TrackingDurationItem.CONTENT_URI, content);
				content.clear();

			}
		}

	}

	// check if you have internet connection
	/**
	 * Checks if is network available.
	 * 
	 * @param context
	 *            the context
	 * @return true, if is network available
	 */
	private boolean isNetworkAvailable(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null)
		{
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null)
			{
				for (int i = 0; i < info.length; i++)
				{
					Log.w("INTERNET:", String.valueOf(i));
					if (info[i].getState() == NetworkInfo.State.CONNECTED)
					{
						Log.w("INTERNET:", "connected!");
						return true;
					}
				}
			}
		}
		return false;
	}

	// broadcast receiver to receive messages sent from the JSON IntentService
	/**
	 * The Class MyRequestReceiver.
	 */
	public class MyRequestReceiver extends BroadcastReceiver {

		/** The Constant PROCESS_RESPONSE. */
		public static final String PROCESS_RESPONSE = "com.as400samplecode.intent.action.PROCESS_RESPONSE";

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * android.content.BroadcastReceiver#onReceive(android.content.Context,
		 * android.content.Intent)
		 */
		@Override
		public void onReceive(Context context, Intent intent) {

			String response = null;
			String responseType = intent.getStringExtra(DataSyncRequest.IN_MSG);

			if (responseType.trim().equalsIgnoreCase("getCountryInfo"))
			{
				response = intent.getStringExtra(DataSyncRequest.OUT_MSG);
				displayDurationInformation(response);
				// displayCountryInformation(response);
			}
			else if (responseType.trim().equalsIgnoreCase("getSomethingElse"))
			{
				// you can choose to implement another transaction here
			}

		}
	}
}