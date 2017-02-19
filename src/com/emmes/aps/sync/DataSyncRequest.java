/* 
 * Copyright 2014 (C) The EMMES Corporation 
 *  
 * Created on : 02-06-2014
 * Last Update: Jul 24, 2014 3:27:48 PM
 * Author     : Mahbubur Rahman
 * Title      : Summer intern 2014 
 * Project    : Daily Diary Android Application
 * 
 */
package com.emmes.aps.sync;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.IntentService;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.provider.BaseColumns;
import android.util.Log;

import com.emmes.aps.data.DailyDiaryDB;
import com.emmes.aps.data.DailyMedication;
import com.emmes.aps.data.LocationTrackingData;
import com.emmes.aps.data.PeakFlowDB;
import com.emmes.aps.medication.MedicationDetails;
import com.emmes.aps.util.CalendarUtils;
import com.emmes.aps.util.DataTransferUtils;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

// TODO: Auto-generated Javadoc
/**
 * The Class DataSyncRequest {@link android.app.IntentService} is an
 * IntentService which accumulate all the transferable data from the application
 * and transfer them to the server working background. This also collections
 * settings that has been changed or updated to the server for example, tracking
 * duration setup.
 * <p>
 * <b>Outbound data: </b> The APS application have four different types of data
 * to be transfered to the server.
 * <ul>
 * <li><b>Diary Data:</b>The main diary data are collected for the diary page
 * itself from the database.</li>
 * <li><b>Medication Data:</b>The secondary medication data is the selected list
 * of prescribed medications by the patient each day.</li>
 * <li><b>Supplement Data:</b>The secondary supplement data is the selected list
 * of supplement or other medication by the patient each day.</li>
 * <li><b>Location Data:</b>The precise GPS location data is being captured by
 * the application if user give the consent and if the current time fall inside
 * the tracking duration set from the server side.</li>
 * <li><b>Peakflow Data:</b>The peak flow data is the three different data
 * points or measured data each day.</li>
 * <li><b>Settings Data:</b>User settings which are stored as sharedpreference
 * may be send to the server. This is not implemented here yet.</li>
 * </ul>
 * 
 * <b>Inbound data: </b> The APS application have some control that will be set
 * later after providing the patient with the smart device and application. Some
 * settings will always be changed.
 * <ul>
 * <li><b>Form content settings:</b>The form content or local settings of the
 * application for different types of data will be updated later from the server
 * side.
 * <table Border = "1" CellPadding = "10" CellSpacing = "2" Align="center">
 * <tr >
 * <td ><b>Form name</b></td>
 * <td ><b>Frequency</b></td>
 * <td ><b>Start window</b></td>
 * <td ><b>End Window</b></td>
 * <td ><b>Last collected time</b></td>
 * </tr>
 * <tr >
 * <td ><b>Diary</b></td>
 * <td ><b>1</b></td>
 * <td ><b>00:00</b></td>
 * <td ><b>23:59</b></td>
 * <td ><b>2014-07-23 13:00:00</b></td>
 * </tr>
 * <tr >
 * <td ><b>Nitro</b></td>
 * <td ><b>2</b></td>
 * <td ><b>16:00</b></td>
 * <td ><b>20:00</b></td>
 * <td ><b>2014-07-23 13:00:00</b></td>
 * </tr>
 * <tr >
 * <td ><b>AM_PEAK</b></td>
 * <td ><b>1</b></td>
 * <td ><b>8:00</b></td>
 * <td ><b>10:00</b></td>
 * <td ><b>2014-07-23 13:00:00</b></td>
 * </tr>
 * <tr >
 * <td ><b>PM_PEAK</b></td>
 * <td ><b>1</b></td>
 * <td ><b>16:00</b></td>
 * <td ><b>20:00</b></td>
 * <td ><b>2014-07-23 13:00:00</b></td>
 * </tr>
 * 
 * </table>
 * </li>
 * <li><b>Tracking duration Data:</b>For tracking the location of the user, the
 * system asked for consent and also there is start time and end time and that
 * will also be decided later. So this type of data should be downloaded from
 * server to application. The tracking duration could be more than once.</li>
 * 
 * </ul>
 * <p>
 * This service start the process of of collecting data,send these to server,
 * create broadcast request for tracking the response, process the response and
 * if anything new, store them to the local database tables.
 * 
 * @author Mahbubur Rahman *
 * @see IntentService
 * @see android.content.ContextWrapper#sendBroadcast(Intent intent)
 * @see Gson
 * @see ArrayList
 * @see org.apache.http.client.methods.HttpPost
 * @see android.content.ContentResolver
 * @since 1.0
 */
public class DataSyncRequest extends IntentService
{

	private final static String TAG = "DataSyncRequest";
	/** The in message. */
	private String mInMessage;

	/** The Constant IN_MSG. */
	public static final String IN_MSG = "requestType";

	/** The Constant OUT_MSG. */
	public static final String OUT_MSG = "outputMessage";

	/**
	 * Instantiates a new data sync request.
	 */
	public DataSyncRequest() {
		super("DataSyncRequest");
	}

	/* (non-Javadoc)
	 * 
	 * @see android.app.IntentService#onHandleIntent(android.content.Intent) */
	@Override
	protected void onHandleIntent(Intent intent)
	{

		// Get Intent extras that were passed
		mInMessage = intent.getStringExtra(IN_MSG);
		if (mInMessage.trim().equalsIgnoreCase("getCountryInfo"))
		{
			String countryCode = intent.getStringExtra("countryCode");
			getTransferrableData(countryCode);
			// getTrasferrableData();

		}
		else if (mInMessage.trim().equalsIgnoreCase(SyncUtils.SYNC_DATA_SERVICE_NAME_TAG))
		{
			// you can choose to implement another transaction here
			getTrasferrableData();
		}

	}

	/**
	 * This is to test using single information which is taken here country info
	 * and test the servlet. There is no direct relation with the project.
	 * 
	 * @deprecated
	 * @param countryCode
	 *            the country code
	 * @return the country info
	 */
	@Deprecated
	private void getCountryInfo(String countryCode)
	{

		// prepare to make Http request
		// String url = "http://10.0.7.5:8080/apsdatasync" + "/CountryServlet";
		String url = "http://10.0.7.5:8080/apsdatasync" + "/DataSyncServlet";
		// TestClassJson jobj=new TestClassJson("name","value");
		Gson gson = new Gson();

		JsonObject myObj = createJSONDataToTransfer();

		// add name value pair for the country code
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
		nameValuePairs.add(new BasicNameValuePair("countryCode", countryCode));
		nameValuePairs.add(new BasicNameValuePair("testcode", myObj.toString()));
		String response = sendHttpRequest(url, nameValuePairs);

		// broadcast message that we have received the response
		// from the WEB Service
		Intent broadcastIntent = new Intent();
		broadcastIntent.setAction(DataSyncRequestReceiver.PROCESS_RESPONSE);
		broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
		broadcastIntent.putExtra(IN_MSG, mInMessage);
		broadcastIntent.putExtra(OUT_MSG, response);
		sendBroadcast(broadcastIntent);
	}

	/**
	 * This getTransferrableData method.
	 * 
	 * @deprecated
	 * @param countryCode
	 *            the country code
	 * @return the trasferrable data
	 */
	@Deprecated
	private void getTransferrableData(String countryCode)
	{

		// prepare to make Http request
		// String url = "http://10.0.7.5:8080/apsdatasync" + "/CountryServlet";
		// String url = "http://10.0.7.5:8080/apsdatasync" + "/DataSyncServlet";
		String url = SyncUtils.SYNC_SERVLET_URL;
		// Gson gson = new Gson();

		JsonObject myObj = createJSONDataToTransfer();

		// add name value pair for the country code
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
		nameValuePairs.add(new BasicNameValuePair("countryCode", countryCode));
		nameValuePairs.add(new BasicNameValuePair("testcode", myObj.toString()));
		String response = sendHttpRequest(url, nameValuePairs);

		// broadcast message that we have received the response
		// from the WEB Service
		Intent broadcastIntent = new Intent();
		broadcastIntent.setAction(DataSyncRequestReceiver.PROCESS_RESPONSE);
		broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
		broadcastIntent.putExtra(IN_MSG, mInMessage);
		broadcastIntent.putExtra(OUT_MSG, response);
		sendBroadcast(broadcastIntent);
	}

	/**
	 * This getTrasferrableData method set the servlet and collect data from the
	 * local database to be sent to the server. This method call
	 * {@link com.emmes.aps.sync.DataSyncRequest#createJSONDataToTransfer} to
	 * get all accumulated data into a single JSON object.
	 * 
	 * @param none
	 * @return none
	 * @throws none
	 * @since 1.0
	 * @author Mahbubur Rahman
	 */
	private void getTrasferrableData()
	{

		// prepare to make Http request
		String url = SyncUtils.SYNC_SERVLET_URL;
		// Gson gson = new Gson();

		JsonObject myObj = createJSONDataToTransfer();

		// add name value pair for the country code
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
		// nameValuePairs.add(new BasicNameValuePair("countryCode",
		// countryCode));
		nameValuePairs.add(new BasicNameValuePair(SyncUtils.SYNC_DATA_JSON_TAG_NAME, myObj.toString()));
		String response = sendHttpRequest(url, nameValuePairs);

		// broadcast message that we have received the response
		// from the WEB Service
		Intent broadcastIntent = new Intent();
		broadcastIntent.setAction(DataSyncRequestReceiver.PROCESS_RESPONSE);
		broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
		broadcastIntent.putExtra(IN_MSG, mInMessage);
		broadcastIntent.putExtra(OUT_MSG, response);
		sendBroadcast(broadcastIntent);
	}

	/**
	 * This createJSONDataToTransfer method calls
	 * {@link com.emmes.aps.DataSyncRequest.sync#retrieveLocationDataFromDB} for
	 * location and transform it to JSON object, calls
	 * {@link com.emmes.aps.DataSyncRequest.sync#retrieveDiaryData} for diary
	 * and covert to diary JSON object, call
	 * {@link com.emmes.aps.DataSyncRequest.sync#retrievePeakFlowData} for peak
	 * flow and convert to peak flow object, cakks
	 * {@link com.emmes.aps.DataSyncRequest.sync#retrieveMedicationDataFromDB}
	 * for medication and supplement and convert it to medication JSON object.
	 * Finally all are integrated to single JSON object.
	 * 
	 * @param none
	 * @throws none
	 * @since 1.0
	 * @author Mahbubur Rahman
	 * @return jO- a single JsonObject containing all types of information that
	 *         has to be sent to the server.
	 */
	private JsonObject createJSONDataToTransfer()
	{
		Gson gsobj = new Gson();
		/**
		 * Location Data
		 */
		ArrayList<LocationDataFormat> locData = retrieveLocationDataFromDB();

		// String gsLoc = gsobj.toJson(locData);
		JsonElement gsLoc = gsobj.toJsonTree(locData);
		Log.d(TAG, "#" + gsLoc + "#");

		JsonObject jO = new JsonObject();

		jO.add("location", gsLoc);

		ArrayList<MedicationDataFormat> mList = retrieveMedicationDataFromDB();

		JsonElement gsMed = gsobj.toJsonTree(mList);
		Log.d(TAG, "#" + gsMed + "#");
		jO.add("medication", gsMed);

		ArrayList<DiaryData> dList = retrieveDiaryData();

		gsMed = gsobj.toJsonTree(dList);

		jO.add("diary", gsMed);

		ArrayList<PeakFlowData> pfList = retrievePeakFlowData();

		gsMed = gsobj.toJsonTree(pfList);

		jO.add("peakflow", gsMed);

		return jO;
	}

	/**
	 * This retrieveMedicationDataFromDB() method retrieve Medication data which
	 * are ready to be transfered to server. This is determined by observing the
	 * status of data. If some data failed previously to transfer because of
	 * network or other server related problem, then this will also be included
	 * . So finally it will retrieve data with status READY or IN_QUEUE or data entered earliar than today .
	 * 
	 * @return the array list of type of
	 *         {@link com.emmes.aps.sync#MedicationDataFormat}
	 * @param none
	 * @throws none
	 * @since 1.0
	 * @author Mahbubur Rahman
	 */
	private ArrayList<MedicationDataFormat> retrieveMedicationDataFromDB()
	{



		ArrayList<MedicationDataFormat> medicationList = new ArrayList<MedicationDataFormat>();
		Calendar now_time = Calendar.getInstance();
		// now_time.setTimeZone(TimeZone.getDefault());
		// Date date = now_time.getTime();

		String formattedDate = CalendarUtils.calTimeToDateStringyyyymmdd(now_time);// new
		// SimpleDateFormat("yyyy-MM-dd").format(date);

		/**
		 * define which columns to retrieve and both supplement and medication
		 */
		String[] PROJECTION_COLUMS = new String[6];
		PROJECTION_COLUMS[0] = BaseColumns._ID;
		PROJECTION_COLUMS[1] = DailyMedication.MedicationItem.COLUMN_NAME_MEDICATION_NAME;
		PROJECTION_COLUMS[2] = DailyMedication.MedicationItem.COLUMN_NAME_DOSAGE;
		PROJECTION_COLUMS[3] = DailyMedication.MedicationItem.COLUMN_NAME_FREQ;
		PROJECTION_COLUMS[4] = DailyMedication.MedicationItem.COLUMN_NAME_LASTTAKEN;
		PROJECTION_COLUMS[5] = DailyMedication.MedicationItem.COLUMN_NAME_STATUS;


		String selection = DailyMedication.MedicationItem.COLUMN_NAME_LASTTAKEN + "<? OR " + DailyMedication.MedicationItem.COLUMN_NAME_STATUS + "=? OR "
				+ DailyMedication.MedicationItem.COLUMN_NAME_STATUS + "=?";
		String[] selectionArgs = { formattedDate, DataTransferUtils.STATUS_DATA_TRANSFER_READY, DataTransferUtils.STATUS_DATA_TRANSFER_IN_QUEUE };
		// ContentValues contents = new ContentValues();
		// contents.put(key, value);
		Cursor medicationCursor = getContentResolver().query(DailyMedication.MedicationItem.CONTENT_URI, PROJECTION_COLUMS, selection, selectionArgs,
				DailyMedication.MedicationItem.DEFAULT_SORT_ORDER);

		//PeakFlowData pfData = new PeakFlowData();


		if (medicationCursor.getCount() > 0)
		{// some data exist for today
			medicationCursor.moveToFirst();

			while (!medicationCursor.isAfterLast())
			{

				MedicationDataFormat medication = cursorToListContents(medicationCursor);
				medicationList.add(medication);

				/*pfData.setPeakflow1(cursor.getString(1));
		pfData.setPeakflow2(cursor.getString(2));
		pfData.setPeakflow3(cursor.getString(3));
		pfData.setRecordDate(cursor.getString(0));
		pfList.add(pfData);*/

				medicationCursor.moveToNext();
			}

		}

		medicationCursor.close();

		ContentResolver cResolver = getContentResolver();
		ContentValues contents = new ContentValues();
		contents.put(DailyMedication.MedicationItem.COLUMN_NAME_STATUS, DataTransferUtils.STATUS_DATA_TRANSFER_IN_QUEUE);
		int updatedrow = cResolver.update(DailyMedication.MedicationItem.CONTENT_URI, contents, selection, selectionArgs);
		Log.d(TAG, updatedrow + " Medication updated");
		return medicationList;
		///.............................





	}

	/**
	 * This retrieveLocationDataFromDB() method retrieve location data which are
	 * ready to be transfered to server. This is determined by observing the
	 * status of data. If some data failed previously to transfer because of
	 * network or other server related problem, then this will also be included
	 * . So finally it will retrieve data with status READY and IN_QUEUE .
	 * 
	 * @return the array list of type of
	 *         {@link com.emmes.aps.sync#LocationDataFormat}
	 * @param none
	 * @throws none
	 * @since 1.0
	 * @author Mahbubur Rahman
	 */
	private ArrayList<LocationDataFormat> retrieveLocationDataFromDB()
	{

		ArrayList<LocationDataFormat> locList = new ArrayList<LocationDataFormat>();
		//ContentResolver cResolver = getContentResolver();

		/**
		 * define which columns to retrieve 
		 */
		String[] projection_columns = new String[5];
		projection_columns[0] = BaseColumns._ID;
		projection_columns[1] = LocationTrackingData.LocationPoint.GPS_LATITUDE;
		projection_columns[2] = LocationTrackingData.LocationPoint.GPS_LONGITUDE;
		projection_columns[3] = LocationTrackingData.LocationPoint.COLUMN_NAME_ENTRYTIME;
		projection_columns[4] = LocationTrackingData.LocationPoint.COLUMN_NAME_STATUS;

		Calendar now_time = Calendar.getInstance();
		// now_time.setTimeZone(TimeZone.getDefault());
		// Date date = now_time.getTime();

		String formattedDate = CalendarUtils.calTimeToDateStringyyyymmdd(now_time);// new
		// SimpleDateFormat("yyyy-MM-dd").format(date);
		String selection = LocationTrackingData.LocationPoint.COLUMN_NAME_ENTRYTIME + "<? OR " + LocationTrackingData.LocationPoint.COLUMN_NAME_STATUS + "=? OR "
				+ LocationTrackingData.LocationPoint.COLUMN_NAME_STATUS + "=?";
		String[] selectionArgs = { formattedDate, DataTransferUtils.STATUS_DATA_TRANSFER_READY, DataTransferUtils.STATUS_DATA_TRANSFER_IN_QUEUE };
		// ContentValues contents = new ContentValues();
		// contents.put(key, value);
		Cursor locCursor = getContentResolver().query(LocationTrackingData.LocationPoint.CONTENT_URI, projection_columns, selection, selectionArgs,
				LocationTrackingData.LocationPoint.DEFAULT_SORT_ORDER);

		//PeakFlowData pfData = new PeakFlowData();


		if (locCursor.getCount() > 0)
		{// some data exist for today
			locCursor.moveToFirst();

			while (!locCursor.isAfterLast())
			{

				LocationDataFormat location = cursorToLocation(locCursor);
				locList.add(location);

				locCursor.moveToNext();
			}

		}

		locCursor.close();
		ContentResolver cResolver = getContentResolver();
		ContentValues contents = new ContentValues();
		contents.put(LocationTrackingData.LocationPoint.COLUMN_NAME_STATUS, DataTransferUtils.STATUS_DATA_TRANSFER_IN_QUEUE);
		int updatedrow = cResolver.update(LocationTrackingData.LocationPoint.CONTENT_URI, contents, selection, selectionArgs);
		Log.d(TAG, updatedrow + " Medication updated");
		return locList;

		
	}

	/**
	 * The method cursorToLocation() helps to processed the cursor data into
	 * LocationDataFormat type which is then added to location arrayList.
	 * 
	 * @param cursor
	 *            {@link Cursor} contains location data from database.
	 * @return loc -an object of type LocationDataFormat
	 *         {@linkplain LocationDataFormat}
	 * 
	 * @throws none
	 * @since 1.0
	 * @author Mahbubur Rahman
	 */
	private LocationDataFormat cursorToLocation(Cursor cursor)
	{
		LocationDataFormat loc = new LocationDataFormat();
		loc.setDbId(cursor.getInt(0));
		loc.setLatitude(cursor.getLong(1));
		loc.setLongitude(cursor.getLong(2));
		loc.setTimeofcollection(cursor.getInt(3));

		return loc;
	}

	/**
	 * The method cursorToListContents() helps to processed the cursor data into
	 * MedicationDetails type which is then added to medication arrayList.
	 * 
	 * @param cursor
	 *            {@link Cursor} contains medications data from database.
	 * @return medication an object of type MedicationDetails
	 *         {@linkplain MedicationDetails}
	 * 
	 * @throws none
	 * @since 1.0
	 * @author Mahbubur Rahman
	 */
	private MedicationDataFormat cursorToListContents(Cursor cursor)
	{
		MedicationDataFormat medication = new MedicationDataFormat();
		medication.setDbId(cursor.getInt(0));
		medication.setMedicationName(cursor.getString(1));
		medication.setMedicationDose(cursor.getString(2));
		medication.setHowManyTimes(cursor.getInt(3));
		medication.setWhenTaken(cursor.getLong(4));

		return medication;
	}

	/**
	 * this sendHttpRequest method is for communicating with the server using
	 * http.
	 * 
	 * @param url
	 *            the url of ther servlet
	 * @param nameValuePairs
	 *            the name value pairs for data to be strasfered
	 * @return the string that contains the confirmation message together with
	 *         the message set from server
	 */
	private String sendHttpRequest(String url, List<NameValuePair> nameValuePairs)
	{

		int REGISTRATION_TIMEOUT = 15 * 1000;
		int WAIT_TIMEOUT = 60 * 1000;
		HttpClient httpclient = new DefaultHttpClient();
		HttpParams params = httpclient.getParams();
		HttpResponse response;
		String content = "";

		try
		{

			// http request parameters
			HttpConnectionParams.setConnectionTimeout(params, REGISTRATION_TIMEOUT);
			HttpConnectionParams.setSoTimeout(params, WAIT_TIMEOUT);
			ConnManagerParams.setTimeout(params, WAIT_TIMEOUT);

			// http POST
			HttpPost httpPost = new HttpPost(url);
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			// send the request and receive the repponse
			response = httpclient.execute(httpPost);

			StatusLine statusLine = response.getStatusLine();
			if (statusLine.getStatusCode() == HttpStatus.SC_OK)
			{
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				response.getEntity().writeTo(out);
				out.close();
				content = out.toString();
			}

			else
			{
				// Closes the connection.
				Log.w("HTTP1:", statusLine.getReasonPhrase());
				response.getEntity().getContent().close();
				throw new IOException(statusLine.getReasonPhrase());
			}

		} catch (ClientProtocolException e)
		{
			Log.w("HTTP2:", e);
		} catch (IOException e)
		{
			Log.w("HTTP3:", e);
		} catch (Exception e)
		{
			Log.w("HTTP4:", e);
		}

		// send back the JSON response String
		return content;

	}

	/**
	 * This retrieveDiaryData() method retrieve diary data which are ready to be
	 * transfered to server. This is determined by observing the status of data.
	 * If some data failed previously to transfer because of network or other
	 * server related problem, then this will also be included . So finally it
	 * will retrieve data with status READY and IN_QUEUE .
	 * 
	 * @return the array list of type of {@link com.emmes.aps.sync#DiaryData}
	 * @param none
	 * 
	 * @throws none
	 * @since 1.0
	 * @author Mahbubur Rahman
	 */
	private ArrayList<DiaryData> retrieveDiaryData()
	{

		ArrayList<DiaryData> diaryList = new ArrayList<DiaryData>();
		Calendar now_time = Calendar.getInstance();
		// now_time.setTimeZone(TimeZone.getDefault());
		// Date date = now_time.getTime();

		String formattedDate = CalendarUtils.calTimeToDateStringyyyymmdd(now_time);// new
		// SimpleDateFormat("yyyy-MM-dd").format(date);

		String[] PROJECTION_COLUMS = { DailyDiaryDB.DailyDiaryItem.COLUMN_NAME_DIARY_CONTENT, DailyDiaryDB.DailyDiaryItem.COLUMN_NAME_STATUS };
		String selection = DailyDiaryDB.DailyDiaryItem.COLUMN_NAME_ENTRYTIME + "<? OR " + DailyDiaryDB.DailyDiaryItem.COLUMN_NAME_STATUS + "=? OR "
				+ DailyDiaryDB.DailyDiaryItem.COLUMN_NAME_STATUS + "=?";
		String[] selectionArgs = { formattedDate, DataTransferUtils.STATUS_DATA_TRANSFER_READY, DataTransferUtils.STATUS_DATA_TRANSFER_IN_QUEUE };
		// ContentValues contents = new ContentValues();
		// contents.put(key, value);
		Cursor cursor = getContentResolver().query(DailyDiaryDB.DailyDiaryItem.CONTENT_URI, PROJECTION_COLUMS, selection, selectionArgs,
				DailyDiaryDB.DailyDiaryItem.DEFAULT_SORT_ORDER);

		String diaryString = "";
		Gson gson = new Gson();

		JSONObject requestObj = null;
		String todaydiary;
		DiaryData ddata;

		if (cursor.getCount() > 0)
		{// some data exist for today
			cursor.moveToFirst();

			while (!cursor.isAfterLast())
			{

				diaryString = cursor.getString(0);

				try
				{
					requestObj = new JSONObject(diaryString);
					todaydiary = requestObj.get("diary").toString();
					ddata = gson.fromJson(todaydiary, DiaryData.class);
					diaryList.add(ddata);
				} catch (JSONException e)
				{
					Log.d(TAG, e.getMessage());
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				cursor.moveToNext();
			}

		}

		cursor.close();
		ContentResolver cResolver = getContentResolver();
		ContentValues contents = new ContentValues();
		contents.put(DailyDiaryDB.DailyDiaryItem.COLUMN_NAME_STATUS, DataTransferUtils.STATUS_DATA_TRANSFER_IN_QUEUE);
		int updatedrow = cResolver.update(DailyDiaryDB.DailyDiaryItem.CONTENT_URI, contents, selection, selectionArgs);
		Log.d(TAG, updatedrow + " diary row updated");
		// }//end of if
		return diaryList;
	}

	/**
	 * This retrievePeakFlowData() method retrieve peak flow data which are
	 * ready to be transfered to server. This is determined by observing the
	 * status of data. If some data failed previously to transfer because of
	 * network or other server related problem, then this will also be included
	 * .So finally it will retrieve data with status READY and IN_QUEUE .
	 * 
	 * @return the array list of type of {@link com.emmes.aps.sync#PeakFlowData}
	 * @param none
	 * 
	 * @throws none
	 * @since 1.0
	 * @author Mahbubur Rahman
	 */
	private ArrayList<PeakFlowData> retrievePeakFlowData()
	{

		ArrayList<PeakFlowData> pfList = new ArrayList<PeakFlowData>();
		Calendar now_time = Calendar.getInstance();
		// now_time.setTimeZone(TimeZone.getDefault());
		// Date date = now_time.getTime();

		String formattedDate = CalendarUtils.calTimeToDateStringyyyymmdd(now_time);// new
		// SimpleDateFormat("yyyy-MM-dd").format(date);

		String[] PROJECTION_COLUMS = { PeakFlowDB.PeakFlowItem.COLUMN_NAME_ENTRYTIME, PeakFlowDB.PeakFlowItem.COLUMN_NAME_PEAK_FLOW1,
				PeakFlowDB.PeakFlowItem.COLUMN_NAME_PEAK_FLOW2, PeakFlowDB.PeakFlowItem.COLUMN_NAME_PEAK_FLOW3,
				PeakFlowDB.PeakFlowItem.COLUMN_NAME_STATUS };
		String selection = PeakFlowDB.PeakFlowItem.COLUMN_NAME_ENTRYTIME + "<? OR " + PeakFlowDB.PeakFlowItem.COLUMN_NAME_STATUS + "=? OR "
				+ PeakFlowDB.PeakFlowItem.COLUMN_NAME_STATUS + "=?";
		String[] selectionArgs = { formattedDate, DataTransferUtils.STATUS_DATA_TRANSFER_READY, DataTransferUtils.STATUS_DATA_TRANSFER_IN_QUEUE };
		// ContentValues contents = new ContentValues();
		// contents.put(key, value);
		Cursor cursor = getContentResolver().query(PeakFlowDB.PeakFlowItem.CONTENT_URI, PROJECTION_COLUMS, selection, selectionArgs,
				PeakFlowDB.PeakFlowItem.DEFAULT_SORT_ORDER);

		PeakFlowData pfData = new PeakFlowData();


		if (cursor.getCount() > 0)
		{// some data exist for today
			cursor.moveToFirst();

			while (!cursor.isAfterLast())
			{

				pfData.setPeakflow1(cursor.getString(1));
				pfData.setPeakflow2(cursor.getString(2));
				pfData.setPeakflow3(cursor.getString(3));
				pfData.setRecordDate(cursor.getString(0));
				pfList.add(pfData);

				cursor.moveToNext();
			}

		}

		cursor.close();

		ContentResolver cResolver = getContentResolver();
		ContentValues contents = new ContentValues();
		contents.put(PeakFlowDB.PeakFlowItem.COLUMN_NAME_STATUS, DataTransferUtils.STATUS_DATA_TRANSFER_IN_QUEUE);
		int updatedrow = cResolver.update(PeakFlowDB.PeakFlowItem.CONTENT_URI, contents, selection, selectionArgs);
		Log.d(TAG, updatedrow + " peak flow row updated");
		return pfList;
	}

}
