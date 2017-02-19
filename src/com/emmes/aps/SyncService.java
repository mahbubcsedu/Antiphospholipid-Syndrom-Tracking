package com.emmes.aps;


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

public class SyncService extends IntentService {
	private static final String TAG="SyncService";
	
	public SyncService()
	{
		super("SyncService");
	}
	
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
	
	private String toJSON(Diary[] diarys) throws JSONException {

			JSONArray jDiarys = Diary.Helper.diaryArrayToJSON(diarys);
			JSONObject jsonRoot = new JSONObject();
			jsonRoot.put("diary", jDiarys);
			return jsonRoot.toString();		
			
		 
	}
    
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
