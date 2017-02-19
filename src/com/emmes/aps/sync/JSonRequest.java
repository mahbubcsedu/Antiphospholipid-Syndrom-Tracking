/* 
  * Copyright 2014 (C) The EMMES Corporation 
  *  
  * Created on : 02-06-2014
  * Last Update: Jul 2, 2014 1:11:30 PM
  * Author     : Mahbubur Rahman
  * Title      : Summer intern 2014 
  * Project    : Daily Diary Android Application
  * 
  */
package com.emmes.aps.sync;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
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
 


import com.emmes.aps.sync.MainSyncActivity.MyRequestReceiver;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
 
// TODO: Auto-generated Javadoc
/**
 * The Class JSonRequest.
 */
public class JSonRequest extends IntentService{
 
    /** The in message. */
    private String inMessage;
    
    /** The Constant IN_MSG. */
    public static final String IN_MSG = "requestType";
    
    /** The Constant OUT_MSG. */
    public static final String OUT_MSG = "outputMessage";
 
    /**
     * Instantiates a new j son request.
     */
    public JSonRequest() {
        super("JSonRequest");
    }
 
    /* (non-Javadoc)
     * @see android.app.IntentService#onHandleIntent(android.content.Intent)
     */
    @Override
    protected void onHandleIntent(Intent intent) {
 
        //Get Intent extras that were passed
        inMessage = intent.getStringExtra(IN_MSG);
        if(inMessage.trim().equalsIgnoreCase("getCountryInfo")){
            String countryCode = intent.getStringExtra("countryCode");
            getCountryInfo(countryCode);
        }
        else if(inMessage.trim().equalsIgnoreCase("getSomethingElse")){
            //you can choose to implement another transaction here
        }
 
    }
 
    /**
     * Gets the country info.
     *
     * @param countryCode the country code
     * @return the country info
     */
    private void getCountryInfo(String countryCode) {
 
        //prepare to make Http request
        //String url = "http://10.0.7.5:8080/apsdatasync" + "/CountryServlet";
    	String url = "http://10.0.7.5:8080/apsdatasync" + "/DataSyncServlet";
       // TestClassJson jobj=new TestClassJson("name","value");
        Gson gson = new Gson();
        //creates json from country object
      //  JsonElement testobj = gson.toJsonTree(jobj);

        //create a new JSON object
       // JsonObject myObj = new JsonObject();
        //myObj.add("testinfo", testobj);
        JsonObject myObj = createJSONDataToTransfer();
        //myObj.add("testinfo", testobj);
        
        //add name value pair for the country code
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
        nameValuePairs.add(new BasicNameValuePair("countryCode",countryCode));
        nameValuePairs.add(new BasicNameValuePair("testcode",myObj.toString()));
        String response = sendHttpRequest(url,nameValuePairs);
 
        //broadcast message that we have received the response
        //from the WEB Service
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(MyRequestReceiver.PROCESS_RESPONSE);
        broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
        broadcastIntent.putExtra(IN_MSG, inMessage);
        broadcastIntent.putExtra(OUT_MSG, response);
        sendBroadcast(broadcastIntent);
    }
 
    /**
     * Creates the json data to transfer.
     *
     * @return the json object
     */
    private JsonObject createJSONDataToTransfer()
    {
    	Gson gsobj=new Gson();
    	/**
    	 * Location Data
    	 */
    	ArrayList<LocationDataFormat> locData=new ArrayList<LocationDataFormat>();
    	LocationDataFormat loc=new LocationDataFormat(23.3,-90.5,12343);
    	locData.add(loc);
    	loc=new LocationDataFormat(23.3,-91.5,13343);
    	locData.add(loc);
    	
    	//String gsLoc = gsobj.toJson(locData);
    	JsonElement gsLoc = gsobj.toJsonTree(locData);
		System.out.println("#"+gsLoc+"#");


		JsonObject jO = new JsonObject();

		jO.add("location", gsLoc);
		
		
		ArrayList<MedicationDataFormat> mList=new ArrayList<MedicationDataFormat>();
		MedicationDataFormat med=new MedicationDataFormat("medication1","5mg",3,12434,"medication",12321312);
		mList.add(med);
		med=new MedicationDataFormat("medication2","50mg",3,12634,"supplement",123123);
    	mList.add(med);
    	JsonElement gsMed = gsobj.toJsonTree(mList);
		System.out.println("#"+gsMed+"#");
		jO.add("medication", gsMed);
		
		DiaryData dData=new DiaryData();
		dData.setCaffein("tea");
		JsonElement gsDiary = gsobj.toJsonTree(dData);
		jO.add("diary", gsDiary);

    	return jO;
    }
    
    /**
     * Send http request.
     *
     * @param url the url
     * @param nameValuePairs the name value pairs
     * @return the string
     */
    private String sendHttpRequest(String url, List<NameValuePair> nameValuePairs) {
 
        int REGISTRATION_TIMEOUT = 15 * 1000;
        int WAIT_TIMEOUT = 60 * 1000;
        HttpClient httpclient = new DefaultHttpClient();
        HttpParams params = httpclient.getParams();
        HttpResponse response;
        String content =  "";
        
        try {
 
            //http request parameters
            HttpConnectionParams.setConnectionTimeout(params, REGISTRATION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(params, WAIT_TIMEOUT);
            ConnManagerParams.setTimeout(params, WAIT_TIMEOUT);
 
            //http POST
            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
 
            //send the request and receive the repponse
            response = httpclient.execute(httpPost);
 
            StatusLine statusLine = response.getStatusLine();
            if(statusLine.getStatusCode() == HttpStatus.SC_OK){
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                response.getEntity().writeTo(out);
                out.close();
                content = out.toString();
            }
 
            else{
                //Closes the connection.
                Log.w("HTTP1:",statusLine.getReasonPhrase());
                response.getEntity().getContent().close();
                throw new IOException(statusLine.getReasonPhrase());
            }
 
        } catch (ClientProtocolException e) {
            Log.w("HTTP2:",e );
        } catch (IOException e) {
            Log.w("HTTP3:",e );
        }catch (Exception e) {
            Log.w("HTTP4:",e );
        }
 
        //send back the JSON response String
        return content;
 
    }
 
    
}
