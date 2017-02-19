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
import java.util.HashSet;

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

import com.emmes.aps.MainActivity;
import com.emmes.aps.R;
import com.emmes.aps.data.AvailableMedicationInfoInDB;
import com.emmes.aps.data.DatabaseConstants;
import com.emmes.aps.data.DailyMedication;
import com.emmes.aps.data.MedicationList;
import com.emmes.aps.medication.AdapterDBMedication;
import com.emmes.aps.medication.DBMedicationInfo;

// TODO: Auto-generated Javadoc
/**
 * The activity ActivitySupplementDetailView is for adding new medication to the
 * list or removing existing one from the list. When the user select ADD or EDIT
 * from the listview medication list activity
 * {@linkplain com.emmes.aps.medication.MedicationListActivity} the app transite
 * the user to this activity.
 * 
 * <p>
 * <b>ADD:</b> If this activity opens as a result of add new item, the activity
 * will display empty autocomplete EditText for supplement name, dosage and
 * frequency. supplement name should be typed by the user. While the user typed
 * first character or character that is set from the settings, the autocomplete
 * list will appear which was derived from the database. Once the focused of the
 * supplement changed which means typing or selecting supplement name complete,
 * the dosage EditText will be reset with autocomplete list of dosage which is
 * available to the database for that particular selected supplements. The
 * frequency is free hand text which could be once or more per day, or per hour,
 * or per weekend.
 * 
 * <p>
 * <b>EDIT:</b> If the activity starts as a result of edit selection from the
 * list activity, The supplement name will be disabled for edit as supplement
 * name can not be changed after entering the supplement. If they really want to
 * do that they can just delete the supplement and add new one. The autocomplete
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
public class ActivitySupplementDetailView extends Activity
{
    /** The context. */
    Context context;
    private static final String TAG = "ActivityMedicationDetailView";
    /** The db medication list uri. */
    private Uri mBaseUri, dbMedicationListUri;

    /** The medication name. */
    AutoCompleteTextView medicationName;

    /** The medication dosage. */
    AutoCompleteTextView medicationDosage;

    /** The medication freq. */
    EditText medicationFreq;

    /** The sbmedication submit. */
    Button sbmedicationSubmit;

    /** The sbmedication remove. */
    // Button sbmedicationRemove;

    /** The tvmed_medicationlabel. */
    TextView tvmed_medicationlabel;

    /** The tvmed_dosagelabel. */
    TextView tvmed_dosagelabel;

    /** The tvmed_freqlabel. */
    TextView tvmed_freqlabel;

    /** The med_id. */
    int med_id;

    /** The adapter. */
    ArrayAdapter<String> adapter;

    /** The customer adapter. */
    AdapterDBMedication customerAdapter;

    /** The sharedpreferences object to deal with shared preferences. */
    SharedPreferences mApplicationPrefs;
    /**
     * mEditor is an instance of SharedPreference.Editor which is used for
     * editing the sharedpreferences in the application @see SharedPreferences.
     */
    SharedPreferences.Editor mEditor;

    /**
     * The Enum operationType.
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
	private int value;

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
	    return value;
	}

	/**
	 * Sets the value.
	 * 
	 * @param value
	 *            the new value
	 */
	public void setValue(int value)
	{
	    this.value = value;
	}

    };

    /** The op mode. */
    operationType opMode;

    /* (non-Javadoc)
     * 
     * @see android.app.Activity#onCreate(android.os.Bundle) */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_medicaitondetail_view);
	context = this;
	opMode = operationType.ADD;

	Bundle extras = getIntent().getExtras();

	mBaseUri = MedicationList.MedicationItem.CONTENT_URI;
	dbMedicationListUri = AvailableMedicationInfoInDB.DBMedicationListItem.CONTENT_URI;

	medicationName = (AutoCompleteTextView) findViewById(R.id.actvmed_medicationname);
	medicationDosage = (AutoCompleteTextView) findViewById(R.id.actvmed_dosage);
	medicationFreq = (EditText) findViewById(R.id.edtmed_freq);
	// cbmedicationTaken=(CheckBox) findViewById(R.id.cbmed_taken);

	tvmed_medicationlabel = (TextView) findViewById(R.id.tvmedlabel_medname);
	tvmed_dosagelabel = (TextView) findViewById(R.id.tvmedlabel_dosage);
	tvmed_freqlabel = (TextView) findViewById(R.id.tvmedlabel_freq);

	sbmedicationSubmit = (Button) findViewById(R.id.btnmed_submit);
	sbmedicationSubmit.setOnClickListener(mAddtoListHandler);

	// sbmedicationRemove = (Button) findViewById(R.id.btn_med_remove);
	// sbmedicationRemove.setOnClickListener(mRemovefromListHandler);

	if (extras != null)
	{
	    opMode = operationType.ITEMSELECTED;
	    String medname = extras.getString("med_name");
	    medicationName.setAdapter(null);
	    medicationName.setText(medname);
	    String dosage = extras.getString("dosage");
	    medicationDosage.setText(dosage);
	    // medicationDosage.requestFocus();
	    String freq = extras.getString("freq");
	    medicationFreq.setText(freq);
	    this.med_id = extras.getInt("id");
	    medicationName.setEnabled(false);
	    sbmedicationSubmit.setText(this.getResources().getString(R.string.btnmed_update_title));
	    // sbmedicationRemove.setVisibility(View.VISIBLE);

	    medicationDosage.setAdapter(null);
	    medicationDosage.setText(dosage);
	    ArrayList<String> dosageList = retrieveDosageFromDB(Integer.toString(this.med_id));
	    adapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, dosageList);
	    medicationDosage.setAdapter(adapter);
	    medicationDosage.setThreshold(1);
	}
	else
	{

	    opMode = operationType.ADD;
	    sbmedicationSubmit.setText("Add to List");
	    ArrayList<DBMedicationInfo> list = retrieveUserSupplementListDataFromDB();
	    // MedicationInformation minfo=new MedicationInformation();
	    customerAdapter = new AdapterDBMedication(this, R.layout.simple_text_view, list);
	    // ArrayAdapter<DBMedicationInfo> adapter = new
	    // ArrayAdapter<DBMedicationInfo>(this,android.R.layout.select_dialog_item,list);

	    // Getting the instance of AutoCompleteTextView

	    customerAdapter.setMinItemtoBeDisplayed(1);
	    medicationName.setThreshold(1);// will start working from first
		                           // character
	    medicationName.setAdapter(customerAdapter);// setting the adapter
		                                       // data into the
		                                       // AutoCompleteTextView

	    medicationName.setTextColor(Color.BLUE);
	    medicationName.setOnItemClickListener(mAutoCompleteItemListener);
	    medicationName.setOnFocusChangeListener(mFinishedTypingEventLister);

	    ArrayList<String> dosageList = retrieveDosageFromDB("");
	    adapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, dosageList);
	    medicationDosage.setAdapter(adapter);
	    medicationDosage.setThreshold(1);

	    // medicationDosage.setOnFocusChangeListener(mDoseFocusChanged);

	    // sbmedicationRemove.setVisibility(View.GONE);
	}

    }

    /**
     * The mFinishedTypingEventLister listener
     * {@linkplain OnFocusChangeListener} trigger while the user complete typing
     * for the supplement name and changed focus to another view other than this
     * one. Based on this trigger the autocomplete adapter of dosage is updated
     */
    private OnFocusChangeListener mFinishedTypingEventLister = new OnFocusChangeListener() {
	@Override
	public void onFocusChange(View v, boolean hasFocus)
	{
	    // TODO Auto-generated method stub
	    // customerAdapter.getSuggestonsize()
	    if (!hasFocus && customerAdapter.getSuggestonsize() == 0)
	    {
		// validateInput(v);

		ArrayList<String> dosageList = retrieveDosageFromDB("");
		adapter = new ArrayAdapter<String>(context, android.R.layout.select_dialog_item, dosageList);
		medicationDosage.setAdapter(adapter);
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
	    String dbIdoftheselectedItem = customerAdapter.getMedicationList().get(position).getMedicationIdinDB();
	    ArrayList<String> dosageList = retrieveDosageFromDB(dbIdoftheselectedItem);
	    adapter = new ArrayAdapter<String>(context, android.R.layout.select_dialog_item, dosageList);
	    medicationDosage.setAdapter(adapter);
	    // adapter.notifyDataSetChanged();
	    Log.d(TAG, "Item pressed");//, Toast.LENGTH_SHORT).show();
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

	    if (opMode.equals(operationType.ADD))
	    {
		ContentValues values = new ContentValues();
		// Long now = Long.valueOf(System.currentTimeMillis());
		String addedMedName = medicationName.getText().toString();
		String addedMedDosage = medicationDosage.getText().toString();
		String addedMedFreq = medicationFreq.getText().toString();
		if (addedMedName.length() == 0)
		{
		    Toast.makeText(getBaseContext(), getApplicationContext().getResources().getString(R.string.add_med_warn_medname_missing_message), Toast.LENGTH_SHORT).show();
		    return;
		}
		if (addedMedDosage.length() == 0)
		{
		    Toast.makeText(getBaseContext(), getApplicationContext().getResources().getString(R.string.add_med_warn_dosage_missing_message), Toast.LENGTH_SHORT).show();
		    return;
		}
		if (addedMedFreq.length() == 0)
		{
		    Toast.makeText(getBaseContext(), getApplicationContext().getResources().getString(R.string.add_med_warn_freq_missing_message), Toast.LENGTH_SHORT).show();
		    return;
		}
		values.put(MedicationList.MedicationItem.COLUMN_NAME_ENTRYTIME, Calendar.getInstance().getTime().toString());
		values.put(MedicationList.MedicationItem.COLUMN_NAME_MEDICATION_NAME, medicationName.getText().toString());
		values.put(MedicationList.MedicationItem.COLUMN_NAME_DOSAGE, medicationDosage.getText().toString());
		values.put(MedicationList.MedicationItem.COLUMN_NAME_FREQ, medicationFreq.getText().toString());
		values.put(MedicationList.MedicationItem.COLUMN_NAME_TYPE, DatabaseConstants.ITEM_TYPE_SUPPLEMENT);
		// values.put(Diary.DiaryItem.COLUMN_NAME_, value);
		Uri newItemUri = getContentResolver().insert(mBaseUri, values);
		ContentUris.parseId(newItemUri);

		Log.d(TAG, "You successfully added");

	    }
	    else if (opMode.equals(operationType.ITEMSELECTED))
	    {
		String addedMedName = medicationName.getText().toString();
		String addedMedDosage = medicationDosage.getText().toString();
		String addedMedFreq = medicationFreq.getText().toString();

		if (addedMedName.length() == 0)
		{
		    Toast.makeText(getBaseContext(), getApplicationContext().getResources().getString(R.string.add_med_warn_medname_missing_message), Toast.LENGTH_SHORT).show();
		    return;
		}
		if (addedMedDosage.length() == 0)
		{
		    Toast.makeText(getBaseContext(), getApplicationContext().getResources().getString(R.string.add_med_warn_dosage_missing_message), Toast.LENGTH_SHORT).show();
		    return;
		}
		if (addedMedFreq.length() == 0)
		{
		    Toast.makeText(getBaseContext(), getApplicationContext().getResources().getString(R.string.add_med_warn_freq_missing_message), Toast.LENGTH_SHORT).show();
		    return;
		}

		ContentValues values = new ContentValues();
		// values.put(MedicationList.MedicationItem._ID, med_id);
		values.put(MedicationList.MedicationItem.COLUMN_NAME_DOSAGE, medicationDosage.getText().toString());
		values.put(MedicationList.MedicationItem.COLUMN_NAME_FREQ, medicationFreq.getText().toString());
		String where = BaseColumns._ID + "=" + med_id;
		// String[] args = new String[] { Integer.toString(med_id) };
		int numofrowdeleted = getContentResolver().update(mBaseUri, values, where, null);

		Log.d(TAG,"You updated" + numofrowdeleted);

	    }
	    Intent parentintent = NavUtils.getParentActivityIntent(ActivitySupplementDetailView.this);
	    // parentintent.putExtra("medication", true);
	    NavUtils.navigateUpTo(ActivitySupplementDetailView.this, parentintent);
	    // Toast.makeText(getBaseContext(), text, duration)
	}
    };

   /* *//** The m removefrom list handler. *//*
    private OnClickListener mRemovefromListHandler = new OnClickListener() {
	public void onClick(View v)
	{
	    AlertDialog diaBox = AskOption();
	    diaBox.show();
	    // finish();
	}
    };*/

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
     * The method retrieveUserSupplementListDataFromDB() is for retrieving the
     * Supplement list from the database and then create an array list of type
     * Supplement to bind to the listview in listactivity
     * 
     * @param none
     * @return ArrayList<DBMedicationInfo> listofMedications
     * 
     * @throws none
     * @since 1.0
     * @author Mahbubur Rahman
     */
    private ArrayList<DBMedicationInfo> retrieveUserSupplementListDataFromDB()
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
	String[] selArgs = { DatabaseConstants.ITEM_TYPE_SUPPLEMENT };
	Cursor cursor = getContentResolver().query(dbMedicationListUri, projection_columns, selection, selArgs, null);
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
	    String[] selArgs = { DatabaseConstants.ITEM_TYPE_SUPPLEMENT };
	    cursor = getContentResolver().query(dbMedicationListUri, projection_columns, selection, selArgs, null);
	}
	else
	{
	    String selection = BaseColumns._ID + "=? AND " + DailyMedication.MedicationItem.COLUMN_NAME_TYPE
		    + "=?";
	    String[] selectArg = { id, DatabaseConstants.ITEM_TYPE_SUPPLEMENT };
	    cursor = getContentResolver().query(dbMedicationListUri, projection_columns, selection, selectArg, null);
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
	AlertDialog myQuittingDialogBox = new AlertDialog.Builder(context)
	// set message, title, and icon
	.setTitle(getApplicationContext().getResources().getString(R.string.listmed_warn_delete_selected_medication_title)).setMessage(getApplicationContext().getResources().getString(R.string.dialog_delete_confirm))
        .setPositiveButton(getApplicationContext().getResources().getString(R.string.pop_up_delete_positive_text), new DialogInterface.OnClickListener() {

		    @Override
			public void onClick(DialogInterface dialog, int whichButton)
		    {
		        // Do something in response to the click
		        ContentValues values = new ContentValues();
		        values.put(BaseColumns._ID, med_id);
		        String where = BaseColumns._ID + "=" + med_id;
		        // String[] args = new String[] {
		        // Integer.toString(med_id) };
		        int numofrowdeleted = getContentResolver().delete(mBaseUri, where, null);
		        Toast.makeText(context, "Removed" + numofrowdeleted + "row(s)", Toast.LENGTH_SHORT).show();

		        dialog.dismiss();
		        finish();
		    }

	        })

	        .setNegativeButton(getApplicationContext().getResources().getString(R.string.pop_up_cancel_text), new DialogInterface.OnClickListener() {
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
