/* 
 * Copyright 2014 (C) The EMMES Corporation 
 *  
 * Created on : 02-06-2014
 * Last Update: Aug 18, 2014 10:29:23 AM
 * Author     : Mahbubur Rahman
 * Title      : Summer intern 2014 
 * Project    : Daily Diary Android Application
 * 
 */
package com.emmes.aps.medication;

import java.util.ArrayList;
import java.util.Calendar;

import com.emmes.aps.MainActivity;
import com.emmes.aps.R;
import com.emmes.aps.data.DailyMedication;
import com.emmes.aps.data.DatabaseConstants;
import com.emmes.aps.data.MedicationList;
import com.emmes.aps.util.CalendarUtils;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.BaseColumns;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.AbsListView;
import android.widget.AbsListView.MultiChoiceModeListener;

// TODO: Auto-generated Javadoc

/**
 * The activity MedicationListActivity is responsible for displaying the user
 * medication list which they have selected as they are taking currently. At the
 * first, this list is empty and there is nothing to display to the list but
 * will display only the add action bar button ADD which will
 * {@linkplain android.app.ActionBar} enable the user to add the medication name
 * which they are taking. This activity will display the added medication as
 * list with checkbox. The user will be able to select medication by tapping the
 * checkbox of each item on the list.
 * 
 * <p>
 * If the user come to this activity for the second time or later and have
 * selected any item previously then it will display the list with selected item
 * and also based on selection, the action bar will be displayed. <br>
 * If only one item have been selected, then the action bar will display both
 * the EDIT and DELETE option but if multiple items selected then only DELETE
 * option will be selected. The user can deselect the previously selected item
 * or can select more item. If they want to add more item to the list, then they
 * have to deselect the selected item first, then action bar will give the
 * option to add more items to the list. So everyday when the user will visit
 * this activity, then will get the option ADD, and after selecting at list one,
 * they will get the option EDIT or DELETE or both based on selection. But for
 * delete something will ask the user for consent. If consent is given then the
 * item will be deleted from the list.
 * 
 * <p>
 * After selection has been done, DONE button on action bar is enough to save
 * the list to database and they will immediately moved to diary page.
 * 
 * @author Mahbubur Rahman *
 * @see ArrayList
 * @see SharedPreferences
 * @see android.app.ListActivity#getListView()
 * @see MultiChoiceModeListener
 * @see ListView
 * @since 1.0
 */
public class MedicationListActivity extends ListActivity
{

    /** The data. */
    private ArrayList<String> mData = new ArrayList<String>();

    /** The context instance of this class. */
    Context mContext;

    /** The Constant TAG for logging. */
    private final static String TAG = "MedicationListActivity";

    /**
     * The adapter mAdapeter is the data organization behind the list displayed
     * to the activity.
     */
    AdapterMedicationList mAdapter;

    /**
     * The mBaseUri is the Uri for medication list database table Uri while the
     * mTodayMedicationUri is the Uri of the table medications which stored the
     * daily medication selected by user.
     */
    private Uri mBaseUri, mTodayMedicationUri;

    /** The sharedpreferences object to deal with shared preferences. */
    SharedPreferences mApplicationPrefs;
    /**
     * mEditor is an instance of SharedPreference.Editor which is used for
     * editing the sharedpreferences in the application @see SharedPreferences.
     */
    SharedPreferences.Editor mEditor;

    /* (non-Javadoc)
     * 
     * @see android.app.Activity#onCreate(android.os.Bundle) */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
	getActionBar().setDisplayHomeAsUpEnabled(true);
	this.mContext = this;

	// setContentView(R.layout.activity_daily_medication_list);
	setContentView(R.layout.activitylist_test);
	mBaseUri = MedicationList.MedicationItem.CONTENT_URI;
	mTodayMedicationUri = DailyMedication.MedicationItem.CONTENT_URI;
	/**
	 * removed button as the data are updated onPause
	 */
	LayoutInflater lf;
	View headerView;
	lf = this.getLayoutInflater();

	headerView = lf.inflate(R.layout.list_row_header, getListView(), false);

	// View view = View.inflate(this, R.layout.list_row_header, null);
	getListView().addHeaderView(headerView, null, false);
	mAdapter = new AdapterMedicationList(this, retrieveUserMedicationListFromDB(), getAlreadySelectedMedicationForToday());
	setListAdapter(mAdapter);

	// mAdapter = new SelectionAdapter(this, R.layout.row_list_item,
// R.id.textView1, data);
	// setListAdapter(mAdapter);
	getListView().setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);

	getListView().setMultiChoiceModeListener(new MultiChoiceModeListener() {

	    private int nr = 0;

	    @Override
	    public boolean onPrepareActionMode(ActionMode mode, Menu menu)
	    {
		// TODO Auto-generated method stub
		if (nr == 1)
		{
		    MenuItem item = menu.findItem(R.id.item_edit);
		    item.setVisible(true);
		    return true;
		}
		else
		{
		    MenuItem item = menu.findItem(R.id.item_edit);
		    item.setVisible(false);
		    return true;
		}
		// return false;
	    }

	    @Override
	    public void onDestroyActionMode(ActionMode mode)
	    {
		// TODO Auto-generated method stub
		mAdapter.clearSelection();
		mode = null;
		if (nr != 0)
		{
		    Intent parentintent = NavUtils.getParentActivityIntent(MedicationListActivity.this);
		    parentintent.putExtra("medication", true);
		    NavUtils.navigateUpTo(MedicationListActivity.this, parentintent);
		}

	    }

	    @Override
	    public boolean onCreateActionMode(ActionMode mode, Menu menu)
	    {
		// TODO Auto-generated method stub

		nr = 0;
		mAdapter.setnTaken(0);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.cab_menu_diary, menu);
		return true;
	    }

	    @Override
	    public boolean onActionItemClicked(ActionMode mode, MenuItem item)
	    {
		// TODO Auto-generated method stub
		switch (item.getItemId())
		{

		case R.id.item_delete:
		    // will ask for confirmation.
		    AlertDialog dialog = AskOptionToDelete();
		    dialog.show();
		    /* nr = 0; adapter.clearSelection(); */
		    mAdapter.notifyDataSetChanged();

		    // mode.finish();
		    break;
		case R.id.item_edit:
		    MedicationDetails med = (MedicationDetails) mAdapter.getItem(mAdapter.getSingleCurrentCheckPosition());
		    Intent intent = new Intent(mContext, ActivityMedicationDetailView.class);
		    intent.putExtra("id", med.getId());
		    intent.putExtra("med_name", med.getMedication_name());
		    intent.putExtra("dosage", med.getDosage());
		    intent.putExtra("freq", med.getFrequency());
		    startActivityForResult(intent, 0);
		    nr = 0;
		    mAdapter.setnTaken(0);
		    // adapter.clearSelection();
		    mode = null;
		    break;
		}
		return false;
	    }

	    @Override
	    public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked)
	    {
		// TODO Auto-generated method stub
		if (checked)
		{
		    nr++;
		    mAdapter.setNewSelection(position, checked);
		}
		else
		{
		    nr--;
		    mAdapter.removeSelection(position);
		}
		mode.setTitle(nr + " " + getApplicationContext().getResources().getString(R.string.list_med_action_bar_message));
		// to invalidate cab menu in runtime
		mode.invalidate();
	    }
	});

    }

    /* (non-Javadoc)
     * 
     * @see android.app.Activity#onPause() */
    @Override
    protected void onPause()
    {
	// TODO Auto-generated method stub
	super.onPause();
	// Toast.makeText(getApplicationContext(), "paused called",
// Toast.LENGTH_LONG).show();
	Log.d(TAG, "onPaused called to save data");
	updateMedicationData();
    }

    /* (non-Javadoc)
     * 
     * @see android.app.Activity#onContextMenuClosed(android.view.Menu) */
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
	// Handle presses on the action bar items
	switch (item.getItemId())
	{
	case R.id.item_add:
	    Intent intent = new Intent(this, ActivityMedicationDetailView.class);
	    startActivityForResult(intent, 0);

	    return true;
	case android.R.id.home:
	    // NavUtils.navigateUpFromSameTask(this);
	    Intent parentintent = NavUtils.getParentActivityIntent(this);
	    parentintent.putExtra("medication", true);
	    NavUtils.navigateUpTo(this, parentintent);
	    return true;
	default:
	    return super.onOptionsItemSelected(item);
	}
    }

    /* (non-Javadoc)
     * 
     * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu) */
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
	// Inflate the menu items for use in the action bar
	MenuInflater inflater = getMenuInflater();
	inflater.inflate(R.menu.option_menu_medlist, menu);
	return super.onCreateOptionsMenu(menu);
    }

    /* (non-Javadoc)
     * 
     * @see android.app.Activity#onActivityResult(int, int,
     * android.content.Intent) */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
	// TODO Auto-generated method stub
	// super.onActivityResult(requestCode, resultCode, data);
	mAdapter = new AdapterMedicationList(this, retrieveUserMedicationListFromDB(), getAlreadySelectedMedicationForToday());
	setListAdapter(mAdapter);
	// adapter.notifyDataSetChanged();
    }

    /**
     * The method retrieveUserMedicationListFromDB() is for retrieving the
     * medication list from the database and then create an array list of type
     * medication to bind to the listview in listactivity
     * 
     * @param none
     * @return ArrayList<MedicationDetails> listofMedications
     * 
     * @throws none
     * @since 1.0
     * @author Mahbubur Rahman
     */
    private ArrayList<MedicationDetails> retrieveUserMedicationListFromDB()
    {
	/**
	 * define which columns to retrieve
	 */
	String[] projection_columns = new String[5];
	projection_columns[0] = BaseColumns._ID;
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

    /**
     * The method cursorToListContents() helps to processed the cursor data into MedicationDetails type which is then added to medication arrayList.
     * 
     * @param cursor {@link Cursor} contains medications data from database.
     * @return medication an object of type MedicationDetails {@linkplain MedicationDetails}
     * 
     * @throws none
     * @since 1.0
     * @author Mahbubur Rahman
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
     * <p>
     * This method is for finding out from the local database about what already
     * have the user selected today. This case arise while the user previously
     * visited this activity and have selected medication. But later s/he may
     * see what he has selected and if anything wrong, s/he can update the list.
     * </p>
     * @param none
     * @throws none
     * @since 1.0
     * @author Mahbubur Rahman
     * @return selected medicationlist
     */
    private ArrayList<Integer> getAlreadySelectedMedicationForToday()
    {
	ArrayList<Integer> med_ids = new ArrayList<Integer>();
	String[] projection_columns = new String[3];
	projection_columns[0] = BaseColumns._ID;
	projection_columns[1] = DailyMedication.MedicationItem.COLUMN_NAME_LASTTAKEN;
	projection_columns[2] = DailyMedication.MedicationItem.COLUMN_NAME_MEDICATION_ID;

	int ID = 0, LAST_TAKEN = 1, MED_ID = 2;
	String selection = DailyMedication.MedicationItem.COLUMN_NAME_TYPE + "=? AND " + DailyMedication.MedicationItem.COLUMN_NAME_LASTTAKEN + "=?";
	String[] selArgs = { DatabaseConstants.ITEM_TYPE_MEDICATION, CalendarUtils.calTimeToDateStringyyyymmdd(Calendar.getInstance()) };
	Cursor cursor = getContentResolver().query(DailyMedication.MedicationItem.CONTENT_URI, projection_columns, selection, selArgs, null);
	Calendar d = Calendar.getInstance();
	Calendar dcurrent = Calendar.getInstance();
	boolean sameDay = false;
	if (cursor.getCount() > 0)
	{
	    cursor.moveToFirst();
	    while (!cursor.isAfterLast())
	    {
		/* d.setTimeInMillis(cursor.getLong(LAST_TAKEN)); sameDay =
	         * d.get(Calendar.YEAR) == dcurrent.get(Calendar.YEAR) &&
	         * d.get(Calendar.DAY_OF_YEAR) ==
	         * dcurrent.get(Calendar.DAY_OF_YEAR); if (sameDay) { */
		med_ids.add(cursor.getInt(MED_ID));
		Log.d(TAG, "same day data exists" + cursor.getInt(MED_ID));
		// }
		cursor.moveToNext();
	    }
	}
	// make sure to close the cursor
	cursor.close();
	return med_ids;
    }

    /**
     * <p>
     * This method is for collecting selected medication information from the list activity and store to the database.
     * </p>
     * @param none
     * @throws none
     * @since 1.0
     * @author Mahbubur Rahman
     * @return none
     */
    private void sendMedicationSelected()
    {
	StringBuffer responseText = new StringBuffer();
	responseText.append("You have selected the following medicaitons...\n");
	// Long now = Long.valueOf(System.currentTimeMillis());
	int selectedcount = 0;
	ArrayList<MedicationDetails> medList = mAdapter.mMedList;
	for (int i = 0; i < medList.size(); i++)
	{
	    MedicationDetails medication = medList.get(i);
	    if (medication.isSelected())
	    {
		selectedcount++;
		responseText.append("\n" + medication.getMedication_name());
		ContentValues values = new ContentValues();
		// Long now = Long.valueOf(System.currentTimeMillis());
		values.put(DailyMedication.MedicationItem.COLUMN_NAME_LASTTAKEN, CalendarUtils.calTimeToDateStringyyyymmdd(Calendar.getInstance()));
		values.put(DailyMedication.MedicationItem.COLUMN_NAME_MEDICATION_NAME, medication.getMedication_name());
		values.put(DailyMedication.MedicationItem.COLUMN_NAME_DOSAGE, medication.getDosage());
		values.put(DailyMedication.MedicationItem.COLUMN_NAME_FREQ, medication.getFrequency());
		values.put(DailyMedication.MedicationItem.COLUMN_NAME_TYPE, DatabaseConstants.ITEM_TYPE_MEDICATION);
		values.put(DailyMedication.MedicationItem.COLUMN_NAME_MEDICATION_ID, medication.getId());

		// values.put(Diary.DiaryItem.COLUMN_NAME_, value);
		Uri newItemUri = getContentResolver().insert(mTodayMedicationUri, values);
		ContentUris.parseId(newItemUri);
	    }
	}
	if (selectedcount == 0)
	{
	    // Toast.makeText(getApplicationContext(),
	    // "Please select at least a single medication from the list",
	    // Toast.LENGTH_LONG).show();
	    Log.d(TAG, "No medication have been selected this time");
	}
	else
	{
	    // Toast.makeText(getApplicationContext(), responseText,
	    // Toast.LENGTH_LONG).show();
	    Log.d(TAG, responseText.toString());
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
     * @param none
     * @throws none
     * @since 1.0
     * @author Mahbubur Rahman
     * @return none
     */
    public void updateMedicationData()
    {
	/**
	 * Project used when querying content provider. Returns all known
	 * fields.
	 */
	getContentResolver().delete(
	        DailyMedication.MedicationItem.CONTENT_URI,
	        DailyMedication.MedicationItem.COLUMN_NAME_TYPE + "='" + DatabaseConstants.ITEM_TYPE_MEDICATION + "' AND "
	                + DailyMedication.MedicationItem.COLUMN_NAME_LASTTAKEN + "='"
	                + CalendarUtils.calTimeToDateStringyyyymmdd(Calendar.getInstance()) + "'", null);
	// long current_time=Calendar.getInstance().getTimeInMillis();
	sendMedicationSelected();
    }

    /**
     * This AskOptionToDelete() method is for creating alert dialog for user confirmation of delete item from medication list.
     * @param none
     * @throws none
     * @since 1.0
     * @author Mahbubur Rahman
     * @return myQuittingDialogBox return {@linkplain AlertDialog} type object for delete confirmaiton of medication.
     */
    private AlertDialog AskOptionToDelete()
    {
	AlertDialog myQuittingDialogBox = new AlertDialog.Builder(this.mContext)
	        // set message, title, and icon
	        .setTitle(getApplicationContext().getResources().getString(R.string.listmed_warn_delete_selected_medication_title))
	        .setMessage(getApplicationContext().getResources().getString(R.string.dialog_delete_confirm))
	        .setPositiveButton(getApplicationContext().getResources().getString(R.string.pop_up_delete_positive_text),
	                new DialogInterface.OnClickListener() {

		            @Override
					public void onClick(DialogInterface dialog, int whichButton)
		            {
		                // Do something in response to the click
		                ContentValues values = new ContentValues();
		                ArrayList<Integer> selectedMedIds = mAdapter.getSelectedListIds();
		                String selections = "";
		                for (int ids : selectedMedIds)
		                {
			            selections += Integer.toString(ids) + ",";

		                }
		                selections = selections.substring(0, selections.length() - 1);
		                // values.put(MedicationList.MedicationItem._ID,
		                // med_id);
		                String where = BaseColumns._ID + " IN (" + selections + " )";
		                // String[] args = new String[] {
		                // Integer.toString(med_id) };
		                int numofrowdeleted = getContentResolver().delete(mBaseUri, where, null);
		                Log.d(TAG, "Removed" + numofrowdeleted + "row(s)");
		                mAdapter.notifyDataSetChanged();

		                dialog.dismiss();
		                // getListView().invalidateViews();
		                Intent intent = getIntent();
		                finish();
		                startActivity(intent);
		            }

	                })

	        .setNegativeButton(getApplicationContext().getResources().getString(R.string.pop_up_cancel_text),
	                new DialogInterface.OnClickListener() {
		            @Override
					public void onClick(DialogInterface dialog, int which)
		            {

		                dialog.dismiss();

		            }
	                }).create();
	return myQuittingDialogBox;

    }

    /* (non-Javadoc)
     * 
     * @see android.app.Activity#onRestart() */
    @Override
    protected void onRestart()
    {
	// mEditor = mApplicationPrefs.edit();
	// mEditor.putBoolean(getResources().getString(R.string.spref_is_application_background),
	// started <= stopped);
	mApplicationPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
	if (mApplicationPrefs.getBoolean(this.getResources().getString(R.string.spref_is_application_background), false))
	{
	    Log.d(TAG, "application is in background and going to PIN activity on restart");

	    onBackPressed();
	}
	// TODO Auto-generated method stub
	super.onRestart();
    }

    /* (non-Javadoc)
     * 
     * @see android.app.Activity#onBackPressed() */
    @Override
	public void onBackPressed()
    {
	Intent intent = new Intent(this, MainActivity.class);
	intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	intent.putExtra("EXIT", true);
	startActivity(intent);
    }
}
