/* 
 * Copyright 2014 (C) The EMMES Corporation 
 *  
 * Created on : 02-06-2014
 * Last Update: Jun 18, 2014 9:07:12 AM
 * Author     : Mahbubur Rahman
 * Title      : Summer intern 2014 
 * Project    : Daily Diary Android Application
 * 
 */
package com.emmes.aps.medication;

import java.util.ArrayList;
import java.util.Calendar;

import com.emmes.aps.MainActivity;
import com.emmes.aps.PeakFlowActivity;
import com.emmes.aps.R;
import com.emmes.aps.data.DatabaseConstants;
import com.emmes.aps.data.DailyMedication;
import com.emmes.aps.data.MedicationList;
import com.emmes.aps.util.DataTransferUtils;

import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

/**
 * @author Mahbubur Rahman
 * 
 *
 */
/**
 * Define an activity of the APS app displaying medications the user is taking
 * currently.
 * 
 * <p>
 * This class is inherited from {@link Activity}. This class is responsible for
 * creating the view to display the user added medications with option to select
 * each day.
 * 
 * <p>
 * The system calls onPerformSync() via an RPC call through the IBinder object
 * supplied by SyncService.
 */
public class ActivityMedicationListView extends Activity
{
    private static final String TAG = "ActivityMedicationListView";
    private ListView listview = null;
    AdapterDailyMedicationList adapter;
    private Uri mBaseUri, todayMedicationUri;
    Context context;

    
    /** The btn medication removefrom list. */
    /* private Button btnMedicationSubmit; */
    /*
     * (non-Javadoc)
     * 
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
	getActionBar().setDisplayHomeAsUpEnabled(true);
	this.context = this;
	
	setContentView(R.layout.activity_daily_medication_list);
	mBaseUri = MedicationList.MedicationItem.CONTENT_URI;
	todayMedicationUri = DailyMedication.MedicationItem.CONTENT_URI;
	/**
	 * removed button as the data are updated onPause
	 */
	/*
	 * btnMedicationSubmit=(Button) findViewById(R.id.btnMedSubmit);
	 * btnMedicationSubmit.setOnClickListener(mSendSelectedMedication);
	 */
	listview = (ListView) findViewById(R.id.listView1);
	// View header = (View) inflater.inflate(R.layout.listheader,null);
	// ls.addHeaderView(header);
	// MedicationInformation medinfo=new MedicationInformation();
	adapter = new AdapterDailyMedicationList(this, retrieveDataFromDB(), getAlreadySelectedMedicationForToday());
	listview.setAdapter(adapter);
	// listview.addHeaderView(header);
	// listview.setAdapter(new MedicationListAdapter(getActivity(),
	// MedicationInformation.getArrayList()));
	listview.setOnItemClickListener(mMessageClickedHandler);
	// btnMedicationAddtoList.setOnClickListener(mAddtoListHandler);
    }

    // Create a message handling object as an anonymous class.
    /** The m message clicked handler. */
    private OnItemClickListener mMessageClickedHandler = new OnItemClickListener() {
	public void onItemClick(AdapterView parent, View v, int position, long id)
	{ // Do
	  // something
	  // in
	  // response
	  // to
	  // the
	  // click
	    MedicationDetails item = (MedicationDetails) adapter.getItem(position);
	    Intent intent = new Intent(context, ActivityMedicationDetailView.class);
	    intent.putExtra("id", item.getId());
	    intent.putExtra("med_name", item.getMedication_name());
	    intent.putExtra("dosage", item.getDosage());
	    intent.putExtra("freq", item.getFrequency());
	    startActivityForResult(intent, 0);
	}
    };
    private OnClickListener mSendSelectedMedication = new OnClickListener() {
	public void onClick(View v)
	{ // Do something in response to the click
	    sendMedicationSelected();
	}
    };

    /*
     * (non-Javadoc)
     * 
     * @see android.app.Activity#onStart()
     */
    @Override
    protected void onStart()
    {
	// TODO Auto-generated method stub
	super.onStart();
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.app.Activity#onResume()
     */
    @Override
    protected void onResume()
    {
	// TODO Auto-generated method stub
	// if(adapter!=null)
	// adapter.notifyDataSetChanged();
	super.onResume();
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.app.Activity#onPause()
     */
    @Override
    protected void onPause()
    {
	// TODO Auto-generated method stub
	super.onPause();
	Toast.makeText(getApplicationContext(), "paused called", Toast.LENGTH_LONG).show();
	updateMedicationData();
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.app.Activity#onDestroy()
     */
    @Override
    protected void onDestroy()
    {
	// TODO Auto-generated method stub
	super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
	// Handle presses on the action bar items
	switch (item.getItemId())
	{
	case R.id.item_add:
	    Intent intent = new Intent(this, ActivityMedicationDetailView.class);
	    startActivityForResult(intent, 0);
	    // startActivity(i);
	    return true;
	case android.R.id.home:
	    //NavUtils.navigateUpFromSameTask(this);
	    Intent parentintent=NavUtils.getParentActivityIntent(this);
	   // parentintent.putExtra("medication", true);
	    NavUtils.navigateUpTo(this,parentintent);
	    return true;
	default:
	    return super.onOptionsItemSelected(item);
	}
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.app.Activity#onActivityResult(int, int,
     * android.content.Intent)
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
	// TODO Auto-generated method stub
	// super.onActivityResult(requestCode, resultCode, data);
	adapter = new AdapterDailyMedicationList(this, retrieveDataFromDB(), getAlreadySelectedMedicationForToday());
	listview.setAdapter(adapter);
	// adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
	// Inflate the menu items for use in the action bar
	MenuInflater inflater = getMenuInflater();
	inflater.inflate(R.menu.option_menu_medlist, menu);
	return super.onCreateOptionsMenu(menu);
    }

    /**
     * query out data for populating the list
     */
    private ArrayList<MedicationDetails> retrieveDataFromDB()
    {
	/**
	 * define which columns to retrieve
	 */
	String[] projection_columns = new String[5];
	projection_columns[0] = MedicationList.MedicationItem._ID;
	projection_columns[1] = MedicationList.MedicationItem.COLUMN_NAME_MEDICATION_NAME;
	projection_columns[2] = MedicationList.MedicationItem.COLUMN_NAME_DOSAGE;
	projection_columns[3] = MedicationList.MedicationItem.COLUMN_NAME_FREQ;
	String selection = DailyMedication.MedicationItem.COLUMN_NAME_TYPE + "=?";
	String[] selArgs = { DatabaseConstants.ITEM_TYPE_MEDICATION };
	Cursor cursor = getContentResolver().query(mBaseUri, projection_columns, selection, selArgs, null);
	// ContentUris.parseId(newItemUri);
	ArrayList<MedicationDetails> medicationList = new ArrayList<MedicationDetails>();
	// ListContents contents=new ListContents();
	if (cursor.getCount() > 0)
	{
	    cursor.moveToFirst();
	    while (!cursor.isAfterLast())
	    {
		MedicationDetails comment = cursorToListContents(cursor);
		medicationList.add(comment);
		cursor.moveToNext();
	    }
	}
	// make sure to close the cursor
	cursor.close();
	return medicationList;
    }

    private MedicationDetails cursorToListContents(Cursor cursor)
    {
	MedicationDetails medication = new MedicationDetails();
	medication.setId(cursor.getInt(0));
	medication.setMedication_name(cursor.getString(1));
	medication.setDosage(cursor.getString(2));
	medication.setFrequency(cursor.getString(3));
	return medication;
    }

    /**
     * <p>
     * This method is for finding out from the local database about what already
     * have the user selected today. This case arise while the user previously
     * visited this activity and have selected medication. But later s/he may
     * see what he has selected and if anything wrong, s/he can update the list.
     * </p>
     * 
     * @return selected medicationlist
     */
    private ArrayList<Integer> getAlreadySelectedMedicationForToday()
    {
	ArrayList<Integer> med_ids = new ArrayList<Integer>();
	String[] projection_columns = new String[3];
	projection_columns[0] = DailyMedication.MedicationItem._ID;
	projection_columns[1] = DailyMedication.MedicationItem.COLUMN_NAME_LASTTAKEN;
	projection_columns[2] = DailyMedication.MedicationItem.COLUMN_NAME_MEDICATION_ID;

	int ID = 0, LAST_TAKEN = 1, MED_ID = 2;
	String selection = DailyMedication.MedicationItem.COLUMN_NAME_TYPE + "=? AND "
		+ DailyMedication.MedicationItem.COLUMN_NAME_STATUS + "=?";
	String[] selArgs = { DatabaseConstants.ITEM_TYPE_MEDICATION, DataTransferUtils.STATUS_DATA_TRANSFER_INCOMPLETE };
	Cursor cursor = getContentResolver().query(DailyMedication.MedicationItem.CONTENT_URI, projection_columns,
		selection, selArgs, null);
	Calendar d = Calendar.getInstance();
	Calendar dcurrent = Calendar.getInstance();
	boolean sameDay = false;
	if (cursor.getCount() > 0)
	{
	    cursor.moveToFirst();
	    while (!cursor.isAfterLast())
	    {
		d.setTimeInMillis(cursor.getLong(LAST_TAKEN));
		sameDay = d.get(Calendar.YEAR) == dcurrent.get(Calendar.YEAR)
			&& d.get(Calendar.DAY_OF_YEAR) == dcurrent.get(Calendar.DAY_OF_YEAR);
		if (sameDay)
		{
		    med_ids.add(cursor.getInt(MED_ID));
		    Log.d(TAG, "same day data exists" + cursor.getInt(MED_ID));
		}
		cursor.moveToNext();
	    }
	}
	// make sure to close the cursor
	cursor.close();
	return med_ids;
    }

    private void sendMedicationSelected()
    {
	StringBuffer responseText = new StringBuffer();
	responseText.append("You have selected the following medicaitons...\n");
	Long now = Long.valueOf(System.currentTimeMillis());
	int selectedcount = 0;
	ArrayList<MedicationDetails> medList = adapter.medList;
	for (int i = 0; i < medList.size(); i++)
	{
	    MedicationDetails medication = medList.get(i);
	    if (medication.isSelected())
	    {
		selectedcount++;
		responseText.append("\n" + medication.getMedication_name());
		ContentValues values = new ContentValues();
		// Long now = Long.valueOf(System.currentTimeMillis());
		values.put(DailyMedication.MedicationItem.COLUMN_NAME_LASTTAKEN, now.toString());
		values.put(DailyMedication.MedicationItem.COLUMN_NAME_MEDICATION_NAME, medication.getMedication_name());
		values.put(DailyMedication.MedicationItem.COLUMN_NAME_DOSAGE, medication.getDosage());
		values.put(DailyMedication.MedicationItem.COLUMN_NAME_FREQ, medication.getFrequency());
		values.put(DailyMedication.MedicationItem.COLUMN_NAME_TYPE, DatabaseConstants.ITEM_TYPE_MEDICATION);
		values.put(DailyMedication.MedicationItem.COLUMN_NAME_MEDICATION_ID, medication.getId());

		// values.put(Diary.DiaryItem.COLUMN_NAME_, value);
		Uri newItemUri = getContentResolver().insert(todayMedicationUri, values);
		ContentUris.parseId(newItemUri);
	    }
	}
	if (selectedcount == 0)
	{
	    Toast.makeText(getApplicationContext(), "Please select at least a single medication from the list",
		    Toast.LENGTH_LONG).show();
	}
	else
	{
	    Toast.makeText(getApplicationContext(), responseText, Toast.LENGTH_LONG).show();
	}
    }

    /**
     * The update process is for capturing the user data whether it is complete
     * or incomplete. The process starts by first query the data with status
     * I=incomplete. If any data exists then the process will update the data,
     * if not then create(insert data to database without changing the status as
     * the default status is I) The medication answered could be multiple and
     * most of the case it is true.
     * 
     * Best way is to delete the existing if any and insert all of them. "
     * delete from medication where status==I;
     */
    public void updateMedicationData()
    {
	/**
	 * Project used when querying content provider. Returns all known
	 * fields.
	 */
	getContentResolver().delete(
		DailyMedication.MedicationItem.CONTENT_URI,
		DailyMedication.MedicationItem.COLUMN_NAME_STATUS + "='"
			+ DataTransferUtils.STATUS_DATA_TRANSFER_INCOMPLETE + "' AND "
			+ DailyMedication.MedicationItem.COLUMN_NAME_TYPE + "='"
			+ DatabaseConstants.ITEM_TYPE_MEDICATION + "'", null);
	// long current_time=Calendar.getInstance().getTimeInMillis();
	sendMedicationSelected();
    }
    /* (non-Javadoc)
     * 
     * @see android.app.Activity#onRestart() */
    @Override
    protected void onRestart()
    {
	// TODO Auto-generated method stub
	onBackPressed();
	super.onRestart();
    }
    public void onBackPressed()
    {
	Intent intent = new Intent(this, MainActivity.class);
	intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	intent.putExtra("EXIT", true);
	startActivity(intent);
    }
}
