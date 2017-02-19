/* 
 * Copyright 2014 (C) The EMMES Corporation 
 *  
 * Created on : 02-06-2014
 * Last Update: Jul 2, 2014 1:10:34 PM
 * Author     : Mahbubur Rahman
 * Title      : Summer intern 2014 
 * Project    : Daily Diary Android Application
 * 
 */
package com.emmes.aps.supplement;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.emmes.aps.MainActivity;
import com.emmes.aps.PeakFlowActivity;
import com.emmes.aps.R;
import com.emmes.aps.data.DatabaseConstants;
import com.emmes.aps.data.DailyMedication;
import com.emmes.aps.data.MedicationList;
import com.emmes.aps.medication.AdapterDailyMedicationList;
import com.emmes.aps.medication.MedicationDetails;
import com.emmes.aps.util.DataTransferUtils;

// TODO: Auto-generated Javadoc
/**
 * The Class ActivitySupplementListView.
 * 
 * @author Mahbubur Rahman
 */
public class ActivitySupplementListView extends Activity
{

    /** The Constant TAG. */
    private static final String TAG = "ActivitySupplimentListView";

    /** The listview. */
    private ListView listview = null;

    /** The adapter. */
    AdapterDailyMedicationList adapter;

    /** The today medication uri. */
    private Uri mBaseUri, todayMedicationUri;

    /** The tv header. */
    TextView tvHeader;

    /** The context. */
    Context context;

    /** The btn medication removefrom list. */
    private Button btnMedicationSubmit;

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
	this.context = this;
	setContentView(R.layout.activity_daily_medication_list);

	tvHeader = (TextView) findViewById(R.id.tvmedheadertitle);
	tvHeader.setText(R.string.tvsupplimentlistheader);
	mBaseUri = MedicationList.MedicationItem.CONTENT_URI;
	todayMedicationUri = DailyMedication.MedicationItem.CONTENT_URI;

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
	{ // Do something in response to the click

	    MedicationDetails item = (MedicationDetails) adapter.getItem(position);
	    Intent intent = new Intent(context, ActivitySupplementDetailView.class);
	    intent.putExtra("id", item.getId());
	    intent.putExtra("med_name", item.getMedication_name());
	    intent.putExtra("dosage", item.getDosage());
	    intent.putExtra("freq", item.getFrequency());
	    startActivityForResult(intent, 0);

	}
    };

    /** The m send selected medication. */
    private OnClickListener mSendSelectedMedication = new OnClickListener() {
	public void onClick(View v)
	{ // Do something in response to the click
	    SendMedicationSelected();
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

    /*
     * (non-Javadoc)
     * 
     * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
	// Handle presses on the action bar items
	switch (item.getItemId())
	{
	case R.id.item_add:
	    Intent intent = new Intent(this, ActivitySupplementDetailView.class);
	    startActivityForResult(intent, 0);
	    // startActivity(i);

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

    /*
     * (non-Javadoc)
     * 
     * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
	// Inflate the menu items for use in the action bar
	MenuInflater inflater = getMenuInflater();
	inflater.inflate(R.menu.option_menu_medlist, menu);
	return super.onCreateOptionsMenu(menu);
    }

    /**
     * query out data for populating the list.
     * 
     * @return the array list
     */
    private ArrayList<MedicationDetails> retrieveDataFromDB()
    {

	/**
	 * define which columns to retrieve
	 */
	String[] projection_columns = new String[4];
	projection_columns[0] = MedicationList.MedicationItem._ID;
	projection_columns[1] = MedicationList.MedicationItem.COLUMN_NAME_MEDICATION_NAME;
	projection_columns[2] = MedicationList.MedicationItem.COLUMN_NAME_DOSAGE;
	projection_columns[3] = MedicationList.MedicationItem.COLUMN_NAME_FREQ;

	String selection = DailyMedication.MedicationItem.COLUMN_NAME_TYPE + "=?";
	String[] selArgs = { DatabaseConstants.ITEM_TYPE_SUPPLEMENT };

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

    /**
     * Cursor to list contents.
     * 
     * @param cursor
     *            the cursor
     * @return the list contents
     */
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
     * Gets the already selected medication for today.
     * 
     * @return the already selected medication for today
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
	String[] selArgs = { DatabaseConstants.ITEM_TYPE_SUPPLEMENT, DataTransferUtils.STATUS_DATA_TRANSFER_INCOMPLETE };

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

    /**
     * Send medication selected.
     */
    private void SendMedicationSelected()
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
		values.put(DailyMedication.MedicationItem.COLUMN_NAME_TYPE, DatabaseConstants.ITEM_TYPE_SUPPLEMENT);
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
			+ DatabaseConstants.ITEM_TYPE_SUPPLEMENT + "'", null);
	// long current_time=Calendar.getInstance().getTimeInMillis();

	SendMedicationSelected();

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
