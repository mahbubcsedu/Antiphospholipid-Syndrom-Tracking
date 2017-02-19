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


import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.emmes.aps.data.Diary;

import android.app.IntentService;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;

// TODO: Auto-generated Javadoc
/**
 * The Class SyncDataService.
 */
public class SyncDataService extends IntentService {

	/** The Constant TAG. */
	private static final String TAG="SyncService";

	/**
	 * Instantiates a new sync data service.
	 */
	public SyncDataService()
	{
		super("SyncDataService");
	}

	/* (non-Javadoc)
	 * @see android.app.IntentService#onHandleIntent(android.content.Intent)
	 */
	@Override
	protected void onHandleIntent(Intent intent) {

		Log.e(TAG, "Starting SyncService");

		if (intent.getData() == null) {
			intent.setData(Diary.DiaryItem.CONTENT_URI);
		} 		
		/**
		 * check diary column and projection list before runnning this code
		 */
		ContentResolver resolver =getContentResolver();
		Cursor cursor = resolver.query(
				intent.getData(), // Use the default content URI for the provider.
				Diary.DiaryItem.FULL_PROJECTION, //PROJECTION,                       // Return the note ID and title for each note.
				null,                             // No where clause, return all records.
				null,                             // No where clause, therefore no where column values.
				Diary.DiaryItem.DEFAULT_SORT_ORDER  // Use the default sort order.
				);

		Diary[] diarys =  Diary.Helper.getDiariesFromCursor(cursor);


		try{
			String jsonData = toJSON(diarys);
			Log.e(TAG, "After toJSON: " + jsonData);
			postToWebService(jsonData);
		}catch (JSONException e) {
			Log.e(TAG,"Failed to convert expenses to JSON",e);
		}  catch (Exception e) {
			Log.e(TAG,"Error communicating with service. Check the server is running" ,e);
		}        	

		if(null != cursor){
			cursor.close();
		}
		Log.i(TAG, "Processing finished");	
	}

	/**
	 * To json.
	 *
	 * @param diarys the diarys
	 * @return the string
	 * @throws JSONException the JSON exception
	 */
	private String toJSON(Diary[] diarys) throws JSONException {

		JSONArray jDiarys = Diary.Helper.diaryArrayToJSON(diarys);
		JSONObject jsonRoot = new JSONObject();
		jsonRoot.put("diary", jDiarys);
		return jsonRoot.toString();		


	}

	/**
	 * Post to web service.
	 *
	 * @param msgBody the msg body
	 * @throws URISyntaxException the URI syntax exception
	 * @throws ClientProtocolException the client protocol exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void postToWebService(String msgBody) throws URISyntaxException, ClientProtocolException, IOException  {  

		URI serviceUri = new URI("http://test.emmes.com/phrtest/diaryservice?diarydata=" + msgBody);			
		HttpPost postRequest = new HttpPost(serviceUri);
		postRequest.addHeader("content-type", "application/json");
		postRequest.setEntity(new StringEntity(msgBody));
		ResponseHandler<String> handler = new BasicResponseHandler();
		DefaultHttpClient httpclient = new DefaultHttpClient(); 
		String result = httpclient.execute(postRequest, handler);
		Log.i(TAG, "Put to Service. Result: " + result);
		httpclient.getConnectionManager().shutdown();


	}   

}
