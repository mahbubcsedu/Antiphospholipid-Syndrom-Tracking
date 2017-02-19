/* 
 * Copyright 2014 (C) The EMMES Corporation 
 *  
 * Created on : 02-06-2014
 * Last Update: Aug 18, 2014 11:18:44 AM
 * Author     : Mahbubur Rahman
 * Title      : Summer intern 2014 
 * Project    : Daily Diary Android Application
 * 
 */
package com.emmes.aps.medication;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;

import com.emmes.aps.MainActivity;
import com.emmes.aps.R;
import com.emmes.aps.data.AvailableMedicationInfoInDB;
import com.emmes.aps.data.DatabaseConstants;
import com.emmes.aps.data.DailyMedication;
import com.emmes.aps.data.MedicationList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.BaseColumns;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

// TODO: Auto-generated Javadoc
/**
 * The activity ActivityMedicationDetailView is for adding new medication to the
 * list or removing existing one from the list. When the user select ADD or EDIT
 * from the listview medication list activity
 * {@linkplain com.emmes.aps.medication.MedicationListActivity} the app transite
 * the user to this activity.
 * 
 * <p>
 * <b>ADD:</b> If this activity opens as a result of add new item, the activity
 * will display empty autocomplete EditText for medication name, dosage and
 * frequency. Medication name should be typed by the user. While the user typed
 * first character or character that is set from the settings, the autocomplete
 * list will appear which was derived from the database. Once the focused of the
 * medicaiton changed which means typing or selecting medication name complete,
 * the dosage EditText will be reset with autocomplete list of dosage which is
 * available to the database for that particular selected medications. The
 * frequency is free hand text which could be once or more per day, or per hour,
 * or per weekend.
 * 
 * <p>
 * <b>EDIT:</b> If the activity starts as a result of edit selection from the
 * list activity, The medication name will be disabled for edit as medication
 * name can not be changed after entering the medication. If they really want to
 * do that they can just delete the medication and add new one. The autocomplete
 * editText will work for dosage only.
 * 
 * @author Mahbubur Rahman
 * @see ArrayList
 * @see SharedPreferences
 * @see android.view.View.OnFocusChangeListener
 * @see AutoCompleteTextView
 * @see ListView
 * @since 1.0
 */
public class ActivityMedicationDetailView extends Activity
{

    /** The context of ActivityMedicationDetailView class. */
    Context mContext;
    /**
     * The constants TAG for logging activities.
     */
    private static final String TAG = "ActivityMedicationDetailView";
    /**
     * The mBaseUri is for uri of the database table which keeps the user
     * entered medication list. The mDbMedicationListUri is the Uri for the
     * database table which keeps the medication list entered from the server
     * system. This is actually the list of all available medications that the
     * user may take.
     */
    private Uri mBaseUri, mDbMedicationListUri;

    /** The autocompleteTextview for medication. */
    AutoCompleteTextView mMedicationName;

    /** The autocomplete textview for dosage. */
    AutoCompleteTextView mMedicationDosage;

    /** The EditText for frequency. */
    EditText mMedicationFreq;

    /** Button which control the submit process both for add or edit. */
    Button mSbmedicationSubmit;

    /**
     * The tvmed_medicationlabel is the textview for keeping lable of
     * medication.
     */
    TextView mTvmed_medicationlabel;

    /** The tvmed_dosagelabel. */
    TextView mTvmed_dosagelabel;

    /** The tvmed_freqlabel. */
    TextView mTvmed_freqlabel;

    /** The med_id. */
    int mMed_id;

    /**
     * The adapter mAdapter is the adapter which control the autocomplete
     * textview of dosage providing data and control. .
     */
    ArrayAdapter<String> mAdapter;

    /**
     * The adaptermAutoMedicationAdapter is the adapter which control the
     * autocomplete textview of medication name providing data and control
     */
    AdapterDBMedication mAutoMedicationAdapter;

    /** The sharedpreferences object to deal with shared preferences. */
    SharedPreferences mApplicationPrefs;
    /**
     * mEditor is an instance of SharedPreference.Editor which is used for
     * editing the sharedpreferences in the application @see SharedPreferences.
     */
    SharedPreferences.Editor mEditor;

    /**
     * The Enum operationType determine which mode this activity loaded. Based
     * on this mode, the system decide and load the data and bind them as well
     * with appropriate fasion.
     * 
     */
    public enum operationType {

	/** The add. */
	ADD(0),
	/** The delete. */
	DELETE(1),
	/** The view. */
	VIEW(2),
	/** The itemselected. */
	ITEMSELECTED(3),
	/** The update. */
	UPDATE(4);

	/** The value. */
	private int mValue;

	/**
	 * Instantiates a new operation type.
	 * 
	 * @param value
	 *            the value
	 */
	private operationType(int value) {
	    this.setValue(value);
	}

	/**
	 * Gets the value.
	 * 
	 * @return the value
	 */
	public int getValue()
	{
	    return mValue;
	}

	/**
	 * Sets the value.
	 * 
	 * @param value
	 *            the new value
	 */
	public void setValue(int value)
	{
	    this.mValue = value;
	}

    };

    /** The op mode. */
    operationType mOpMode;

    /* (non-Javadoc)
     * 
     * @see android.app.Activity#onCreate(android.os.Bundle) */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_medicaitondetail_view);
	mContext = this;
	mOpMode = operationType.ADD;

	Bundle extras = getIntent().getExtras();

	mBaseUri = MedicationList.MedicationItem.CONTENT_URI;
	mDbMedicationListUri = AvailableMedicationInfoInDB.DBMedicationListItem.CONTENT_URI;

	mMedicationName = (AutoCompleteTextView) findViewById(R.id.actvmed_medicationname);
	mMedicationDosage = (AutoCompleteTextView) findViewById(R.id.actvmed_dosage);
	mMedicationFreq = (EditText) findViewById(R.id.edtmed_freq);
	// cbmedicationTaken=(CheckBox) findViewById(R.id.cbmed_taken);

	mTvmed_medicationlabel = (TextView) findViewById(R.id.tvmedlabel_medname);
	mTvmed_dosagelabel = (TextView) findViewById(R.id.tvmedlabel_dosage);
	mTvmed_freqlabel = (TextView) findViewById(R.id.tvmedlabel_freq);

	mSbmedicationSubmit = (Button) findViewById(R.id.btnmed_submit);
	mSbmedicationSubmit.setOnClickListener(mAddtoListHandler);

	// sbmedicationRemove = (Button) findViewById(R.id.btn_med_remove);
	// sbmedicationRemove.setOnClickListener(mRemovefromListHandler);

	if (extras != null)
	{
	    mOpMode = operationType.ITEMSELECTED;
	    String medname = extras.getString("med_name");
	    mMedicationName.setAdapter(null);
	    mMedicationName.setText(medname);
	    String dosage = extras.getString("dosage");
	    mMedicationDosage.setText(dosage);
	    // medicationDosage.requestFocus();
	    String freq = extras.getString("freq");
	    mMedicationFreq.setText(freq);
	    this.mMed_id = extras.getInt("id");
	    mMedicationName.setEnabled(false);
	    mSbmedicationSubmit.setText(this.getResources().getString(R.string.btnmed_update_title));
	    // sbmedicationRemove.setVisibility(View.VISIBLE);

	    mMedicationDosage.setAdapter(null);
	    mMedicationDosage.setText(dosage);
	    ArrayList<String> dosageList = retrieveDosageFromDB(Integer.toString(this.mMed_id));
	    mAdapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, dosageList);
	    mMedicationDosage.setAdapter(mAdapter);
	    mMedicationDosage.setThreshold(1);
	}
	else
	{

	    mOpMode = operationType.ADD;
	    mSbmedicationSubmit.setText("Add to List");
	    ArrayList<DBMedicationInfo> list = retrieveUserMedicationListFromDB();
	    // MedicationInformation minfo=new MedicationInformation();
	    mAutoMedicationAdapter = new AdapterDBMedication(this, R.layout.simple_text_view, list);
	    // ArrayAdapter<DBMedicationInfo> adapter = new
	    // ArrayAdapter<DBMedicationInfo>(this,android.R.layout.select_dialog_item,list);

	    // Getting the instance of AutoCompleteTextView

	    mAutoMedicationAdapter.setMinItemtoBeDisplayed(1);
	    mMedicationName.setThreshold(1);// will start working from first
		                            // character
	    mMedicationName.setAdapter(mAutoMedicationAdapter);// setting the
// adapter
	    // data into the
	    // AutoCompleteTextView

	    mMedicationName.setTextColor(Color.BLUE);
	    mMedicationName.setOnItemClickListener(mAutoCompleteItemListener);
	    mMedicationName.setOnFocusChangeListener(mFinishedTypingEventLister);
	    /* medicationName.setOnFocusChangeListener(new
	     * OnFocusChangeListener(){
	     * 
	     * @Override public void onFocusChange(View v, boolean hasFocus) {
	     * // TODO Auto-generated method stub
	     * //customerAdapter.getSuggestonsize() if (!hasFocus &&
	     * customerAdapter.getSuggestonsize()==0) { //validateInput(v);
	     * 
	     * ArrayList<String> dosageList=retrieveDosageFromDB("");
	     * adapter=new
	     * ArrayAdapter<String>(context,android.R.layout.select_dialog_item
	     * ,dosageList); medicationDosage.setAdapter(adapter);
	     * Toast.makeText(getApplicationContext(),
	     * "You have finished typing", Toast.LENGTH_SHORT).show(); } }}); */
	    // medicationName.setOnEditorActionListener(mFinishedTypingEventLister);

	    ArrayList<String> dosageList = retrieveDosageFromDB("");
	    mAdapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, dosageList);
	    mMedicationDosage.setAdapter(mAdapter);
	    mMedicationDosage.setThreshold(1);
	    // medicationDosage.setOnFocusChangeListener(mDoseFocusChanged);

	    // sbmedicationRemove.setVisibility(View.GONE);
	}

    }

    /**
     * The mFinishedTypingEventLister listener
     * {@linkplain OnFocusChangeListener} trigger while the user complete typing
     * for the medication name and changed focus to another view other than this
     * one. Based on this trigger the autocomplete adapter of dosage is updated
     */
    private OnFocusChangeListener mFinishedTypingEventLister = new OnFocusChangeListener() {
	@Override
	public void onFocusChange(View v, boolean hasFocus)
	{
	    // TODO Auto-generated method stub
	    // customerAdapter.getSuggestonsize()
	    if (!hasFocus && mAutoMedicationAdapter.getSuggestonsize() == 0)
	    {
		// validateInput(v);

		ArrayList<String> dosageList = retrieveDosageFromDB("");
		mAdapter = new ArrayAdapter<String>(mContext, android.R.layout.select_dialog_item, dosageList);
		mMedicationDosage.setAdapter(mAdapter);
		Log.d(TAG, "You have finished typing");
	    }
	}
    };

    /**
     * The mAutoCompleteItemListener listener {@linkplain OnFocusChangeListener}
     * trigger while the user select item from the autocomplete list. Based on
     * this trigger the autocomplete adapter of dosage is updated
     */
    private OnItemClickListener mAutoCompleteItemListener = new OnItemClickListener() {
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	{
	    // TODO Auto-generated method stub
	    String dbIdoftheselectedItem = mAutoMedicationAdapter.getMedicationList().get(position).getMedicationIdinDB();
	    ArrayList<String> dosageList = retrieveDosageFromDB(dbIdoftheselectedItem);
	    mAdapter = new ArrayAdapter<String>(mContext, android.R.layout.select_dialog_item, dosageList);
	    mMedicationDosage.setAdapter(mAdapter);
	    // adapter.notifyDataSetChanged();
	    Log.d(TAG, "Item pressed");// , Toast.LENGTH_SHORT).show();
	}

    };

    /**
     * The mAddtoListHandler listener {@linkplain OnFocusChangeListener} trigger
     * while the user tapped the add to list button. If triggered, it store the
     * information of the new medication or update the information of the
     * selected medication to the database.
     */
    private OnClickListener mAddtoListHandler = new OnClickListener() {
	@Override
	public void onClick(View v)
	{ // Do something in response to the click

	    if (mOpMode.equals(operationType.ADD))
	    {
		ContentValues values = new ContentValues();
		// Long now = Long.valueOf(System.currentTimeMillis());
		String addedMedName = mMedicationName.getText().toString();
		String addedMedDosage = mMedicationDosage.getText().toString();
		String addedMedFreq = mMedicationFreq.getText().toString();
		if (addedMedName.length() == 0)
		{
		    Toast.makeText(getBaseContext(), getApplicationContext().getResources().getString(R.string.add_med_warn_medname_missing_message),
			    Toast.LENGTH_SHORT).show();
		    return;
		}
		if (addedMedDosage.length() == 0)
		{
		    Toast.makeText(getBaseContext(), getApplicationContext().getResources().getString(R.string.add_med_warn_dosage_missing_message),
			    Toast.LENGTH_SHORT).show();
		    return;
		}
		if (addedMedFreq.length() == 0)
		{
		    Toast.makeText(getBaseContext(), getApplicationContext().getResources().getString(R.string.add_med_warn_freq_missing_message),
			    Toast.LENGTH_SHORT).show();
		    return;
		}
		values.put(MedicationList.MedicationItem.COLUMN_NAME_ENTRYTIME, Calendar.getInstance().getTime().toString());
		values.put(MedicationList.MedicationItem.COLUMN_NAME_MEDICATION_NAME, mMedicationName.getText().toString());
		values.put(MedicationList.MedicationItem.COLUMN_NAME_DOSAGE, mMedicationDosage.getText().toString());
		values.put(MedicationList.MedicationItem.COLUMN_NAME_FREQ, mMedicationFreq.getText().toString());
		values.put(MedicationList.MedicationItem.COLUMN_NAME_TYPE, DatabaseConstants.ITEM_TYPE_MEDICATION);
		// values.put(Diary.DiaryItem.COLUMN_NAME_, value);
		Uri newItemUri = getContentResolver().insert(mBaseUri, values);
		ContentUris.parseId(newItemUri);

		Log.d(TAG, "You successfully added");// ,
// Toast.LENGTH_SHORT).show();

	    }
	    else if (mOpMode.equals(operationType.ITEMSELECTED))
	    {
		String addedMedName = mMedicationName.getText().toString();
		String addedMedDosage = mMedicationDosage.getText().toString();
		String addedMedFreq = mMedicationFreq.getText().toString();

		if (addedMedName.length() == 0)
		{
		    Toast.makeText(getBaseContext(), getApplicationContext().getResources().getString(R.string.add_med_warn_medname_missing_message),
			    Toast.LENGTH_SHORT).show();
		    return;
		}
		if (addedMedDosage.length() == 0)
		{
		    Toast.makeText(getBaseContext(), getApplicationContext().getResources().getString(R.string.add_med_warn_dosage_missing_message),
			    Toast.LENGTH_SHORT).show();
		    return;
		}
		if (addedMedFreq.length() == 0)
		{
		    Toast.makeText(getBaseContext(), getApplicationContext().getResources().getString(R.string.add_med_warn_freq_missing_message),
			    Toast.LENGTH_SHORT).show();
		    return;
		}

		ContentValues values = new ContentValues();
		// values.put(MedicationList.MedicationItem._ID, med_id);
		values.put(MedicationList.MedicationItem.COLUMN_NAME_DOSAGE, mMedicationDosage.getText().toString());
		values.put(MedicationList.MedicationItem.COLUMN_NAME_FREQ, mMedicationFreq.getText().toString());
		String where = BaseColumns._ID + "=" + mMed_id;
		// String[] args = new String[] { Integer.toString(med_id) };
		int numofrowdeleted = getContentResolver().update(mBaseUri, values, where, null);

		// Toast.makeText(getBaseContext(), "You updated" +
// numofrowdeleted, Toast.LENGTH_SHORT).show();
		Log.d(TAG, "You updated" + numofrowdeleted);
	    }
	    Intent parentintent = NavUtils.getParentActivityIntent(ActivityMedicationDetailView.this);
	    // parentintent.putExtra("medication", true);
	    NavUtils.navigateUpTo(ActivityMedicationDetailView.this, parentintent);
	    // finish();
	    // Toast.makeText(getBaseContext(), text, duration)
	}
    };

    /** The m removefrom list handler. */
    /* private OnClickListener mRemovefromListHandler = new OnClickListener() {
     * public void onClick(View v) { AlertDialog diaBox = AskOption();
     * diaBox.show(); // finish(); } }; */

    /* (non-Javadoc)
     * 
     * @see android.app.Activity#onStart() */
    @Override
    protected void onStart()
    {
	// TODO Auto-generated method stub
	super.onStart();
    }

    /* (non-Javadoc)
     * 
     * @see android.app.Activity#onResume() */
    @Override
    protected void onResume()
    {
	// TODO Auto-generated method stub
	super.onResume();
    }

    /* (non-Javadoc)
     * 
     * @see android.app.Activity#onPause() */
    @Override
    protected void onPause()
    {
	// TODO Auto-generated method stub
	super.onPause();
    }

    /* (non-Javadoc)
     * 
     * @see android.app.Activity#onDestroy() */
    @Override
    protected void onDestroy()
    {
	// TODO Auto-generated method stub
	super.onDestroy();
    }

    /**
     * The method retrieveUserMedicationListFromDB() is for retrieving the
     * medication list from the database and then create an array list of type
     * medication to bind to the listview in listactivity
     * 
     * @param none
     * @return ArrayList<DBMedicationInfo> listofMedications
     * 
     * @throws none
     * @since 1.0
     * @author Mahbubur Rahman
     */
    private ArrayList<DBMedicationInfo> retrieveUserMedicationListFromDB()
    {

	/**
	 * define which columns to retrieve
	 */
	String[] projection_columns = new String[4];
	projection_columns[0] = BaseColumns._ID;
	projection_columns[1] = AvailableMedicationInfoInDB.DBMedicationListItem.COLUMN_NAME_MEDICATION_NAME;
	projection_columns[2] = AvailableMedicationInfoInDB.DBMedicationListItem.COLUMN_NAME_DOSAGE;
	projection_columns[3] = AvailableMedicationInfoInDB.DBMedicationListItem.COLUMN_NAME_ENTRYTIME;

	String selection = DailyMedication.MedicationItem.COLUMN_NAME_TYPE + "=?";
	String[] selArgs = { DatabaseConstants.ITEM_TYPE_MEDICATION };
	Cursor cursor = getContentResolver().query(mDbMedicationListUri, projection_columns, selection, selArgs, null);
	// ContentUris.parseId(newItemUri);

	ArrayList<DBMedicationInfo> medicationList = new ArrayList<DBMedicationInfo>();

	// ListContents contents=new ListContents();

	if (cursor.getCount() > 0)
	{
	    cursor.moveToFirst();
	    while (!cursor.isAfterLast())
	    {
		DBMedicationInfo medicationinfo = cursorToDBMedicationInfo(cursor);
		medicationList.add(medicationinfo);
		cursor.moveToNext();
	    }
	}
	// make sure to close the cursor
	cursor.close();

	return medicationList;

    }

    /**
     * The method retrieveDosageFromDB() is for retrieving the dosage list
     * from the database and then create an array list to
     * bind to the autocomplete textview adapter of dosage.
     * 
     * @param none
     * @return ArrayList<MedicationDetails> listofMedications
     * 
     * @throws none
     * @since 1.0
     * @author Mahbubur Rahman
     */
    private ArrayList<String> retrieveDosageFromDB(String id)
    {
	/**
	 * define which columns to retrieve
	 */
	String[] projection_columns = new String[4];
	projection_columns[0] = BaseColumns._ID;
	// projection_columns[1]=AvailableMedicationInfoInDB.DBMedicationListItem.COLUMN_NAME_MEDICATION_NAME;
	projection_columns[1] = AvailableMedicationInfoInDB.DBMedicationListItem.COLUMN_NAME_DOSAGE;
	// projection_columns[3]=AvailableMedicationInfoInDB.DBMedicationListItem.COLUMN_NAME_ENTRYTIME;

	Cursor cursor = null;
	if (id.length() == 0)
	{
	    String selection = DailyMedication.MedicationItem.COLUMN_NAME_TYPE + "=?";
	    String[] selArgs = { DatabaseConstants.ITEM_TYPE_MEDICATION };
	    cursor = getContentResolver().query(mDbMedicationListUri, projection_columns, selection, selArgs, null);
	}
	else
	{
	    String selection = BaseColumns._ID + "=? AND " + DailyMedication.MedicationItem.COLUMN_NAME_TYPE
		    + "=?";
	    String[] selectArg = { id, DatabaseConstants.ITEM_TYPE_MEDICATION };
	    cursor = getContentResolver().query(mDbMedicationListUri, projection_columns, selection, selectArg, null);
	}
	// ContentUris.parseId(newItemUri);

	ArrayList<String> medicationList = new ArrayList<String>();

	// ListContents contents=new ListContents();

	if (cursor.getCount() > 0)
	{
	    cursor.moveToFirst();
	    while (!cursor.isAfterLast())
	    {
		String medicationinfo = cursor.getString(1);
		medicationList.add(medicationinfo);
		cursor.moveToNext();
	    }
	}
	// make sure to close the cursor
	cursor.close();

	// ArrayList al = new ArrayList();
	// add elements to al, including duplicates
	HashSet<String> hs = new HashSet<String>();
	hs.addAll(medicationList);
	medicationList.clear();
	medicationList.addAll(hs);

	return medicationList;
    }

    /**
     * The method cursorToDBMedicationInfo() helps to processed the cursor data
     * into DBMedicationInfo type which is then added to medication arrayList.
     * 
     * @param cursor
     *            {@link Cursor} contains medications data from database.
     * @return medication an object of type MedicationDetails
     *         {@linkplain DBMedicationInfo}
     * 
     * @throws none
     * @since 1.0
     * @author Mahbubur Rahman
     */
    private DBMedicationInfo cursorToDBMedicationInfo(Cursor cursor)
    {
	DBMedicationInfo medication = new DBMedicationInfo();
	medication.setMedicationIdinDB(Integer.toString(cursor.getInt(0)));
	medication.setMedicationName(cursor.getString(1));
	medication.setMedicationDosage(cursor.getString(2));
	medication.setMedicationEntryDate(Long.toString(cursor.getLong(3)));

	return medication;
    }

    /**
     * detele operation is removed from the detail view.
     * 
     * @deprecated
     * @return AskOption AlertDialog
     */
    @Deprecated
	private AlertDialog AskOption()
    {
	AlertDialog myQuittingDialogBox = new AlertDialog.Builder(mContext)
	        // set message, title, and icon
	        .setTitle(getApplicationContext().getResources().getString(R.string.listmed_warn_delete_selected_medication_title))
	        .setMessage(getApplicationContext().getResources().getString(R.string.listmed_warn_delete_selected_medication_message))
	        .setPositiveButton(getApplicationContext().getResources().getString(R.string.pop_up_delete_positive_text),
	                new DialogInterface.OnClickListener() {

		            @Override
					public void onClick(DialogInterface dialog, int whichButton)
		            {
		                // Do something in response to the click
		                ContentValues values = new ContentValues();
		                values.put(BaseColumns._ID, mMed_id);
		                String where = BaseColumns._ID + "=" + mMed_id;
		                // String[] args = new String[] {
		                // Integer.toString(med_id) };
		                int numofrowdeleted = getContentResolver().delete(mBaseUri, where, null);
		                Log.d(TAG, "Removed" + numofrowdeleted + "row(s)");// ,
// Toast.LENGTH_SHORT).show();

		                dialog.dismiss();
		                finish();
		            }

	                })

	        .setNegativeButton(getApplicationContext().getResources().getString(R.string.pop_up_delete_positive_text),
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
	    Log.d(TAG, "application is in background  and goind to PIN activity on restart");

	    onBackPressed();
	}
	// TODO Auto-generated method stub
	super.onRestart();
    }

    @Override
	public void onBackPressed()
    {
	Intent intent = new Intent(this, MainActivity.class);
	intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	intent.putExtra("EXIT", true);
	startActivity(intent);
    }
}
