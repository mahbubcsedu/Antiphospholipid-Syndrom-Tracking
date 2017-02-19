/* 
  * Copyright 2014 (C) The EMMES Corporation 
  *  
  * Created on : 02-06-2014
  * Last Update: Aug 14, 2014 4:10:29 PM
  * Author     : Mahbubur Rahman
  * Title      : Summer intern 2014 
  * Project    : Daily Diary Android Application
  * 
  */
package com.emmes.aps;

import com.emmes.aps.data.DatabaseConstants;
import com.emmes.aps.data.FormContentData;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
//import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

// TODO: Auto-generated Javadoc
/**
 * The Class TestActivity.
 */
public class TestActivity extends Activity
{

    /** The Constant TAG. */
    private static final String TAG = "PeakFlowActivity";
    
    /** The context. */
    Context context;
    
    /** The nitrolast. */
    EditText nitrolast;
    
    /** The diarylast. */
    EditText diarylast;
    
    /** The amlast. */
    EditText amlast;
    
    /** The pmlast. */
    EditText pmlast;
    
    /** The startdiary. */
    EditText startdiary;
    
    /** The enddiary. */
    EditText enddiary;
    
    /** The startnitro. */
    EditText startnitro;
    
    /** The endnitro. */
    EditText endnitro;
    
    /** The startam. */
    EditText startam;
    
    /** The endam. */
    EditText endam;
    
    /** The startpm. */
    EditText startpm;
    
    /** The endpm. */
    EditText endpm;

    /** The btndlast. */
    Button btndlast;
    
    /** The btnamlast. */
    Button btnamlast;
    
    /** The btnpmlast. */
    Button btnpmlast;
    
    /** The btnnitrolast. */
    Button btnnitrolast;
    
    /** The btnnitrowindow. */
    Button btnnitrowindow;
    
    /** The btndiarywindow. */
    Button btndiarywindow;
    
    /** The btnamwindow. */
    Button btnamwindow;
    
    /** The btnpmwindow. */
    Button btnpmwindow;
    
    /** The values. */
    ContentValues values = new ContentValues();
    
    /* (non-Javadoc)
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
	super.onCreate(savedInstanceState);
	this.context = this;
	setContentView(R.layout.activity_test);
	nitrolast = (EditText) findViewById(R.id.etnitro);
	diarylast = (EditText) findViewById(R.id.etd);
	amlast = (EditText) findViewById(R.id.etam);
	pmlast = (EditText) findViewById(R.id.etpm);
	startdiary = (EditText) findViewById(R.id.startdiary);
	enddiary = (EditText) findViewById(R.id.enddiary);
	startnitro = (EditText) findViewById(R.id.startnitro);
	endnitro = (EditText) findViewById(R.id.endnitro);
	startam = (EditText) findViewById(R.id.startam);
	endam = (EditText) findViewById(R.id.endam);
	startpm = (EditText) findViewById(R.id.startpm);
	endpm = (EditText) findViewById(R.id.endpm);

	btndlast = (Button) findViewById(R.id.btnd);
	
	btndlast.setOnClickListener(new OnClickListener() {

	   

	    @Override
	    public void onClick(View v)
	    {
		// TODO Auto-generated method stub
		values.clear();
		values.put(FormContentData.FormContentItems.COLUMN_NAME_LAST_COLLECTED_TIME, diarylast.getText().toString() + ".787+0000");
		String where = FormContentData.FormContentItems.COLUMN_NAME_FORM_NAME + "='" + DatabaseConstants.FORM_CONTENT_ID_DIARY + "'";
		int numofrowdeleted = getContentResolver().update(FormContentData.FormContentItems.CONTENT_URI, values, where, null);
		Log.d(TAG, numofrowdeleted + " rows updated with " + diarylast.getText().toString() + ".787+0000");
		Toast.makeText(context, numofrowdeleted + " rows updated with " + diarylast.getText().toString() + ".787+0000", Toast.LENGTH_SHORT).show();
	    }
	});
	
	
	btnamlast = (Button) findViewById(R.id.btnam);
	btnamlast.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v)
	    {
		// TODO Auto-generated method stub
		values.clear();
		values.put(FormContentData.FormContentItems.COLUMN_NAME_LAST_COLLECTED_TIME, amlast.getText().toString() + ".787+0000");
		String where = FormContentData.FormContentItems.COLUMN_NAME_FORM_NAME + "='" + DatabaseConstants.FORM_CONTENT_ID_AM_FLOW + "'";
		int numofrowdeleted = getContentResolver().update(FormContentData.FormContentItems.CONTENT_URI, values, where, null);
		Log.d(TAG, numofrowdeleted + " rows updated with " + amlast.getText().toString() + ".787+0000");
		Toast.makeText(context, numofrowdeleted + " rows updated with " + amlast.getText().toString() + ".787+0000", Toast.LENGTH_SHORT).show();
	   

	    }
	});
	btnpmlast = (Button) findViewById(R.id.btnpm);
	btnpmlast.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v)
	    {
		// TODO Auto-generated method stub
		values.clear();
		values.put(FormContentData.FormContentItems.COLUMN_NAME_LAST_COLLECTED_TIME, pmlast.getText().toString() + ".787+0000");
		String where = FormContentData.FormContentItems.COLUMN_NAME_FORM_NAME + "='" + DatabaseConstants.FORM_CONTENT_ID_PM_FLOW + "'";
		int numofrowdeleted = getContentResolver().update(FormContentData.FormContentItems.CONTENT_URI, values, where, null);
		Log.d(TAG, numofrowdeleted + " rows updated with " + pmlast.getText().toString() + ".787+0000");
		Toast.makeText(context, numofrowdeleted + " rows updated with " + pmlast.getText().toString() + ".787+0000", Toast.LENGTH_SHORT).show();
	    }
	});
	btnnitrolast = (Button) findViewById(R.id.btnnitro);
	btnnitrolast.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v)
	    {
		// TODO Auto-generated method stub
		values.clear();
		values.put(FormContentData.FormContentItems.COLUMN_NAME_LAST_COLLECTED_TIME, nitrolast.getText().toString() + ".787+0000");
		String where = FormContentData.FormContentItems.COLUMN_NAME_FORM_NAME + "='" + DatabaseConstants.FORM_CONTENT_ID_NITRO + "'";
		int numofrowdeleted = getContentResolver().update(FormContentData.FormContentItems.CONTENT_URI, values, where, null);
		Log.d(TAG, numofrowdeleted + " rows updated with " + nitrolast.getText().toString() + ".787+0000");
		Toast.makeText(context, numofrowdeleted + " rows updated with " + nitrolast.getText().toString() + ".787+0000", Toast.LENGTH_SHORT).show();
	    }
	});
	
	btnnitrowindow = (Button) findViewById(R.id.nitrowindow);
	btnnitrowindow.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v)
	    {
		// TODO Auto-generated method stub
		values.clear();
		values.put(FormContentData.FormContentItems.COLUMN_NAME_WINDOW_START, startnitro.getText().toString());
		values.put(FormContentData.FormContentItems.COLUMN_NAME_WINDOW_END, endnitro.getText().toString());
		String where = FormContentData.FormContentItems.COLUMN_NAME_FORM_NAME + "='" + DatabaseConstants.FORM_CONTENT_ID_NITRO + "'";
		int numofrowdeleted = getContentResolver().update(FormContentData.FormContentItems.CONTENT_URI, values, where, null);
		Log.d(TAG, numofrowdeleted + " rows updated with " + nitrolast.getText().toString() + ".787+0000");
		Toast.makeText(context, numofrowdeleted + " rows updated with " + startnitro.getText().toString(), Toast.LENGTH_SHORT).show();
	    }
	});
	btndiarywindow = (Button) findViewById(R.id.diarywindow);
	btndiarywindow.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v)
	    {
		// TODO Auto-generated method stub
		values.clear();
		values.put(FormContentData.FormContentItems.COLUMN_NAME_WINDOW_START, startdiary.getText().toString());
		values.put(FormContentData.FormContentItems.COLUMN_NAME_WINDOW_END, enddiary.getText().toString());
		String where = FormContentData.FormContentItems.COLUMN_NAME_FORM_NAME + "='" + DatabaseConstants.FORM_CONTENT_ID_DIARY + "'";
		int numofrowdeleted = getContentResolver().update(FormContentData.FormContentItems.CONTENT_URI, values, where, null);
		Log.d(TAG, numofrowdeleted + " rows updated with " + nitrolast.getText().toString() + ".787+0000");
		Toast.makeText(context, numofrowdeleted + " rows updated with " + startdiary.getText().toString(), Toast.LENGTH_SHORT).show();
	    }
	});
	btnamwindow = (Button) findViewById(R.id.amwindow);
	btnamwindow.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v)
	    {
		// TODO Auto-generated method stub
		values.clear();
		values.put(FormContentData.FormContentItems.COLUMN_NAME_WINDOW_START, startam.getText().toString());
		values.put(FormContentData.FormContentItems.COLUMN_NAME_WINDOW_END, endam.getText().toString());
		String where = FormContentData.FormContentItems.COLUMN_NAME_FORM_NAME + "='" + DatabaseConstants.FORM_CONTENT_ID_AM_FLOW + "'";
		int numofrowdeleted = getContentResolver().update(FormContentData.FormContentItems.CONTENT_URI, values, where, null);
		Log.d(TAG, numofrowdeleted + " rows updated with " + nitrolast.getText().toString() + ".787+0000");
		Toast.makeText(context, numofrowdeleted + " rows updated with " + startam.getText().toString(), Toast.LENGTH_SHORT).show();
	    }
	});
	btnpmwindow = (Button) findViewById(R.id.pmwindow);
	btnpmwindow.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v)
	    {
		// TODO Auto-generated method stub
		values.clear();
		values.put(FormContentData.FormContentItems.COLUMN_NAME_WINDOW_START, startpm.getText().toString());
		values.put(FormContentData.FormContentItems.COLUMN_NAME_WINDOW_END, endpm.getText().toString());
		String where = FormContentData.FormContentItems.COLUMN_NAME_FORM_NAME + "='" + DatabaseConstants.FORM_CONTENT_ID_PM_FLOW + "'";
		int numofrowdeleted = getContentResolver().update(FormContentData.FormContentItems.CONTENT_URI, values, where, null);
		Log.d(TAG, numofrowdeleted + " rows updated with " + nitrolast.getText().toString() + ".787+0000");
		Toast.makeText(context, numofrowdeleted + " rows updated with " + startpm.getText().toString(), Toast.LENGTH_SHORT).show();
	    }
	});

    }

}
