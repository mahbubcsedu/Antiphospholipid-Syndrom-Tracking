/* 
 * Copyright 2014 (C) The EMMES Corporation 
 *  
 * Created on : 02-06-2014
 * Last Update: Aug 12, 2014 11:27:07 AM
 * Author     : Mahbubur Rahman
 * Title      : Summer intern 2014 
 * Project    : Daily Diary Android Application
 * 
 */
package com.emmes.aps;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.preference.TimePickerPreference;
import android.provider.BaseColumns;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
//import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.emmes.aps.data.DailyDiaryDB;
import com.emmes.aps.data.DailyMedication;
import com.emmes.aps.data.DatabaseConstants;
//import com.emmes.aps.data.Diary;
import com.emmes.aps.data.FormContentData;
import com.emmes.aps.data.LocationTrackingData;
import com.emmes.aps.data.PeakFlowDB;
//import com.emmes.aps.medication.ActivityMedicationListView;
import com.emmes.aps.medication.MedicationListActivity;
//import com.emmes.aps.supplement.ActivitySupplementListView;
import com.emmes.aps.supplement.SupplementListActivity;
import com.emmes.aps.sync.DataSyncRequest;
import com.emmes.aps.sync.DataSyncRequestReceiver;
import com.emmes.aps.sync.DiaryData;
import com.emmes.aps.sync.SyncUtils;
import com.emmes.aps.util.CalendarUtils;
import com.emmes.aps.util.DataTransferUtils;
import com.emmes.aps.util.HoursMinutesPickerDialog;
import com.emmes.aps.util.NetworkUtils;
import com.emmes.aps.util.NotificationUtils;
import com.emmes.aps.util.NumberPickerDialog;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

// TODO: Auto-generated Javadoc

/**
 * The APS application is all about collection of daily data from pregnant women
 * which is actually a daily diary. The <i>DiaryActivity<i> is thus the mother
 * of all other activities and main working placement of this app.
 * <p>
 * DiaryActivity has all the information entering entry points both primary and
 * secondary data. Most of the questions are related to yes or no or dropdown
 * selection. Secondary questions peakflow, medicaiton and supplement all are
 * automatically navigate to the secondary activity based on the selection.
 * 
 * 
 * @author Mahbubur Rahman *
 * @see AlertDialog
 * @see Activity
 * @see Calendar
 * @see Map
 * @since 1.0
 */

public class DiaryActivity extends NavigationActivity
{

    /**
     * This is a flag boolean variable to keep track of repeated visit to diary
     * page today(the day when the diary page open).
     */
    private boolean IS_REPEATED_VISIT_TO_DIARY_PAGE_TODAY = false;

    /**
     * This is a flag boolean variable to determine whether the user already
     * have submitted her diary today.
     */
    private boolean IS_DIARY_DONE_FOR_THE_DAY = false;

    /** This boolean flag variable keep track of visibility of am peak flow. */
    private boolean IS_AM_PEAK_FLOW_VISIBLE;

    /**
     * T This is a flag boolean variable to determine whether the PM Peak flow
     * should be displayed to user or not.
     */
    private boolean IS_PM_PEAK_FLOW_VISIBLE;

    /**
     * This is a flag boolean variable to determine whether the nitroxide
     * information should be displayed to user or not.
     */
    private boolean IS_NITRO_VISIBLE;

    /**
     * This boolean flag keep track of nitro information addressed by the user
     * or not
     */
    private boolean IS_NITRO_ENTRY_EMPTY;

    /** The Constant TAG for Logcat. */
    private static final String TAG = "DiaryActivity";

    /** The context of this activity. */
    Context mContext;
    // private Uri mBaseUri;
    /** mDiaryDate is the instance variable for keeping track of today's date */
    private TextView mDiaryDate;

    /** The m wheezing. */
    private RadioGroup mWheezing;

    /** The m coughing. */
    private RadioGroup mCoughing;

    /** The m short breath. */
    private RadioGroup mShortBreath;

    /** The m chest tightness. */
    private RadioGroup mChestTightness;

    /** The m chest pain. */
    private RadioGroup mChestPain;

    /** The m fever. */
    private RadioGroup mFever;

    /** The m nausea. */
    private RadioGroup mNausea;

    /** The m runny nose. */
    private RadioGroup mRunnyNose;

    /** The m other. */
    private RadioGroup mOther;

    /** The m et other. */
    private EditText mEtOther;

    /** The m miss school work. */
    private RadioGroup mMissSchoolWork;

    /** The m pescription. */
    private RadioGroup mPescription;

    /** The m otc. */
    private RadioGroup mOTC;
    // private Spinner mHrsSlept;
    // private Spinner mMinSlept;
    /** The m wake. */
    private RadioGroup mWake;

    /** The m times woke. */
    private Spinner mTimesWoke;
    // private Spinner mHrsSitting;
    // private Spinner mMinSitting;
    /** The m exercise. */
    private RadioGroup mExercise;
    // private Spinner mMinOutdoorsTimeSpent;
    // private Spinner mHrsOutdoorsTimeSpent;
    /** The m min indoors exercise. */
    private Spinner mMinIndoorsExercise;

    /** The m min outdoors exercise. */
    private Spinner mMinOutdoorsExercise;
    // private Spinner mHrsVehicle;
    // private Spinner mMinVehicle;
    /** The m smoke. */
    private RadioGroup mSmoke;

    /** The m num smoke. */
    private Spinner mNumSmoke;

    /** The m people smoking. */
    private RadioGroup mPeopleSmoking;

    /** The m caffein. */
    private RadioGroup mCaffein;

    /** The m stress. */
    private RadioGroup mStress;

    /** The m nitric oxide. */
    private EditText mNitricOxide;
    // private TextView mTimeSlept;
    /** The peak flow button. */
    private Button mBtnPeakFlow;

    /** The diary complete. */
    private Button mBtnDiaryComplete;

    /** The m duration slept. */
    private TextView mDurationSlept;

    /** The m duration sitting. */
    private TextView mDurationSitting;

    /** The m duration vehicle. */
    private TextView mDurationVehicle;

    /** The m duration spent outdoors. */
    private TextView mDurationSpentOutdoors;

    /** The sharedpreferences object to deal with shared preferences. */
    SharedPreferences mApplicationPrefs;
    /**
     * mEditor is an instance of SharedPreference.Editor which is used for
     * editing the SharedPreferences in the application @see SharedPreferences.
     */
    SharedPreferences.Editor mEditor;

    /**
     * The receiver is for sync the diary data with the server. The intent
     * service runs background and generate sync alarm. The receiver is
     * registered to capture sync receiver for both way communication from app
     * to server and server to app. @see IntentService
     */
    private DataSyncRequestReceiver mReceiver;

    /**
     * The Enum PEAK_FLOW_CATEGORY will be used to determine whether the current
     * time is under AM or PM peak flow range or none or the window.
     */
    public enum PEAK_FLOW_CATEGORY {

	/** The am. */
	AM,
	/** The pm. */
	PM,
	/** The none. */
	NONE
    }

    /**
     * The PEAK_FLOW_CATEGORY_NOW variable to keep current window category while
     * transition to peak flow activity.
     */
    private PEAK_FLOW_CATEGORY PEAK_FLOW_CATEGORY_NOW;

    /**
     * The Enum DiaryLoadMode.
     */
    private enum DiaryLoadMode {

	/** The new. */
	NEW,
	/** The edit. */
	EDIT
    };

    /* (non-Javadoc)
     * 
     * @see android.app.Activity#onCreate(android.os.Bundle) */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
	super.onCreate(savedInstanceState);
	this.mContext = this;
	IS_REPEATED_VISIT_TO_DIARY_PAGE_TODAY = false;
	this.IS_NITRO_ENTRY_EMPTY = true;
	this.PEAK_FLOW_CATEGORY_NOW = PEAK_FLOW_CATEGORY.NONE;
	// mode = DiaryLoadMode.NEW;

	DateFormat df = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
	String today = df.format(Calendar.getInstance().getTime());

	setContentView(R.layout.activity_diary);
	// Intent intent = getIntent();
	/* mBaseUri = intent.getData(); if (mBaseUri == null) { mBaseUri =
	 * Diary.DiaryItem.CONTENT_URI; } */
	Log.i(TAG, "starting diary");
	mDiaryDate = (TextView) findViewById(R.id.diarydate);
	mDiaryDate.append(today);
	mWheezing = (RadioGroup) findViewById(R.id.rgWheezing);
	mCoughing = (RadioGroup) findViewById(R.id.rgCoughing);
	mShortBreath = (RadioGroup) findViewById(R.id.rgShortBreath);
	mChestTightness = (RadioGroup) findViewById(R.id.rgChestTightness);
	mChestPain = (RadioGroup) findViewById(R.id.rgChestPain);
	mFever = (RadioGroup) findViewById(R.id.rgFever);
	mNausea = (RadioGroup) findViewById(R.id.rgNauseaVomiting);
	mRunnyNose = (RadioGroup) findViewById(R.id.rgRunnyNose);
	mOther = (RadioGroup) findViewById(R.id.rgOther);
	mMissSchoolWork = (RadioGroup) findViewById(R.id.rgMissSchoolWork);
	mPescription = (RadioGroup) findViewById(R.id.rgMedication);
	mOTC = (RadioGroup) findViewById(R.id.rgOTC);

	mWake = (RadioGroup) findViewById(R.id.rgBreathingCoughingMidNight);
	mTimesWoke = (Spinner) findViewById(R.id.spTimesWokeUp);

	mExercise = (RadioGroup) findViewById(R.id.rgExercise);

	mMinIndoorsExercise = (Spinner) findViewById(R.id.spExerciseIndoors);
	mMinOutdoorsExercise = (Spinner) findViewById(R.id.spExerciseOutdoors);

	mSmoke = (RadioGroup) findViewById(R.id.rgSmoke);
	mNumSmoke = (Spinner) findViewById(R.id.spNumberSmoked);
	mPeopleSmoking = (RadioGroup) findViewById(R.id.rgPeopleSmoking);
	mCaffein = (RadioGroup) findViewById(R.id.rgDrinkCaffein);
	mStress = (RadioGroup) findViewById(R.id.rgDailyStress);

	mEtOther = (EditText) findViewById(R.id.etOther);
	Log.i(TAG, "mid point diary");

	mNitricOxide = (EditText) findViewById(R.id.etNitricOxide);
	mBtnPeakFlow = (Button) findViewById(R.id.peak_flow_button);
	mBtnDiaryComplete = (Button) findViewById(R.id.btnSubmitDiary);
	mDurationSlept = (TextView) findViewById(R.id.tvPickTimeSlept);
	mDurationSitting = (TextView) findViewById(R.id.tvPickTimeSitting);
	mDurationVehicle = (TextView) findViewById(R.id.tvPickDurationVehicle);
	mDurationSpentOutdoors = (TextView) findViewById(R.id.tvPickOutdoorsTimeSpent);
	// mMedication = (RadioGroup) findViewById(R.id.rgMedication);
	addListenerMedicationGroup();
	addListenerSupplementGroup();
	addListenerSmokingGroup();
	addListenerOtherGroup();
	addListenerExersizeGroup();
	addListenerPeakFlow();
	addListenerDiaryComplete();
	addListenSleptTime();
	// addListenNitroOxide();
	addListenerWokeupGroup();
	addListenSpentOutdoorsTime();
	addListenSittingTime();
	addListenVehicleTime();
	Log.i(TAG, "done onCreate");
	loadTodayDiaryData();
    }

    /**
     * The method addListenSleptTime is for integrating the Radio group actions
     * for sleeping time.
     * 
     * @param none
     * @return void
     * @see andorid.view.View#OnClickListener
     * @throws none
     * @since 1.0
     * @author Mahbubur Rahman
     */

    public void addListenSleptTime()
    {
	mDurationSlept.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v)
	    {
		// TODO Auto-generated method stub
		// Calendar mcurrentTime = Calendar.getInstance();
		int hour = Integer.parseInt(getResources().getString(R.string.default_time_duration_hrs));// mcurrentTime.get(Calendar.HOUR_OF_DAY);
		int minute = Integer.parseInt(getResources().getString(R.string.default_time_duration_mins));// mcurrentTime.get(Calendar.MINUTE);
		HoursMinutesPickerDialog mTimePicker;
		mTimePicker = new HoursMinutesPickerDialog(DiaryActivity.this, new HoursMinutesPickerDialog.OnTimeSetListener() {
		    @Override
		    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute)
		    {
			mDurationSlept.setText(CalendarUtils.getTimeHHMM(Integer.toString(selectedHour), Integer.toString(selectedMinute)));
		    }

		}, hour, minute, true);// Yes 24 hour time
		mTimePicker.setTitle(getApplicationContext().getResources().getString(R.string.diary_slept_Time_popup_title));
		mTimePicker.show();

	    }
	});
    }

    /**
     * The method addListenSleptTime is for integrating the Radio group actions
     * for duration of vehicle spent time.
     * 
     * @param none
     * @return void
     * @see andorid.view.View#OnClickListener
     * @throws none
     * @since 1.0
     * @author Mahbubur Rahman ,Glenn Tucker
     */
    public void addListenVehicleTime()
    {
	mDurationVehicle.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v)
	    {
		// TODO Auto-generated method stub
		// Calendar mcurrentTime = Calendar.getInstance();
		int hour = Integer.parseInt(getResources().getString(R.string.default_time_duration_hrs));// mcurrentTime.get(Calendar.HOUR_OF_DAY);
		int minute = Integer.parseInt(getResources().getString(R.string.default_time_duration_mins));
		HoursMinutesPickerDialog mTimePicker;
		mTimePicker = new HoursMinutesPickerDialog(DiaryActivity.this, new HoursMinutesPickerDialog.OnTimeSetListener() {
		    @Override
		    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute)
		    {
			mDurationVehicle.setText(CalendarUtils.getTimeHHMM(Integer.toString(selectedHour), Integer.toString(selectedMinute)));
		    }

		}, hour, minute, true);// Yes 24 hour time
		mTimePicker.setTitle(getApplicationContext().getResources().getString(R.string.diary_vehicle_Time_popup_title));
		mTimePicker.show();

	    }
	});
    }

    /**
     * The method addListenSleptTime is for integrating the Radio group actions
     * for duration of sitting outside time.
     * 
     * @param none
     * @return void
     * @see andorid.view.View#OnClickListener
     * @throws none
     * @since 1.0
     * @author Mahbubur Rahman ,Glenn Tucker
     */
    public void addListenSittingTime()
    {
	mDurationSitting.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v)
	    {
		// TODO Auto-generated method stub
		// Calendar mcurrentTime = Calendar.getInstance();
		int hour = Integer.parseInt(getResources().getString(R.string.default_time_duration_hrs));// mcurrentTime.get(Calendar.HOUR_OF_DAY);
		int minute = Integer.parseInt(getResources().getString(R.string.default_time_duration_mins));
		HoursMinutesPickerDialog mTimePicker;
		mTimePicker = new HoursMinutesPickerDialog(DiaryActivity.this, new HoursMinutesPickerDialog.OnTimeSetListener() {
		    @Override
		    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute)
		    {
			mDurationSitting.setText(CalendarUtils.getTimeHHMM(Integer.toString(selectedHour), Integer.toString(selectedMinute)));
		    }

		}, hour, minute, true);// Yes 24 hour time
		mTimePicker.setTitle(getApplicationContext().getResources().getString(R.string.diary_sitting_outside_Time_popup_title));
		mTimePicker.show();

	    }
	});
    }

    /**
     * The method addListenSleptTime is for integrating the Radio group actions
     * for duration of outside spent time.
     * 
     * @param none
     * @return void
     * @see andorid.view.View#OnClickListener
     * @throws none
     * @since 1.0
     * @author Mahbubur Rahman ,Glenn Tucker
     */
    public void addListenSpentOutdoorsTime()
    {
	mDurationSpentOutdoors.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v)
	    {
		// TODO Auto-generated method stub
		// Calendar mcurrentTime = Calendar.getInstance();
		int hour = Integer.parseInt(getResources().getString(R.string.default_time_duration_hrs));// mcurrentTime.get(Calendar.HOUR_OF_DAY);
		int minute = Integer.parseInt(getResources().getString(R.string.default_time_duration_mins));
		HoursMinutesPickerDialog mTimePicker;
		mTimePicker = new HoursMinutesPickerDialog(DiaryActivity.this, new HoursMinutesPickerDialog.OnTimeSetListener() {
		    @Override
		    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute)
		    {
			mDurationSpentOutdoors.setText(CalendarUtils.getTimeHHMM(Integer.toString(selectedHour), Integer.toString(selectedMinute)));
		    }

		}, hour, minute, true);// Yes 24 hour time
		mTimePicker.setTitle(getApplicationContext().getResources().getString(R.string.diary_spent_outdoor_Time_popup_title));
		mTimePicker.show();

	    }
	});
    }

    /**
     * The method addListenSleptTime is for integrating the Radio group actions
     * for duration of vehicle spent time.
     * 
     * @deprecated this is not used in the current version of the application
     *             instead only edit text has been used with normal behaviour.
     * @param none
     * @return void
     * @see andorid.view.View#OnClickListener
     * @throws none
     * @since 1.0
     * @author Mahbubur Rahman
     * 
     */
    @Deprecated
	public void addListenNitroOxide()
    {
	mNitricOxide.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v)
	    {
		int defaultNum = 0;
		int minNum = 0;
		int maxNum = 100;
		int dialogId = 234;
		int units = R.string.nitro_unit;
		NumberPickerDialog mPicker = new NumberPickerDialog(DiaryActivity.this, new NumberPickerDialog.OnNumberSetListener() {

		    @Override
		    public void onNumberSet(int dialogId, int number)
		    {
			// TODO Auto-generated method stub
			mNitricOxide.setText(String.valueOf(number));
		    }
		}, defaultNum, minNum, maxNum, R.string.nitro_picker_title, units, dialogId);
		mPicker.show();
		// TODO Auto-generated method stub
		// return false;
	    }
	});
    }

    /**
     * The method addListenerPeakFlow is for integrating the peak flow activity
     * to diary using the peak flow button.
     * 
     * @param none
     * @return void
     * @see andorid.view.View#OnClickListener
     * @throws none
     * @since 1.0
     * @author Mahbubur Rahman
     */
    public void addListenerPeakFlow()
    {
	mBtnPeakFlow.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v)
	    {
		Intent launchingIntent = new Intent(DiaryActivity.this, PeakFlowActivity.class);
		launchingIntent.putExtra("peakflow_category", PEAK_FLOW_CATEGORY_NOW);
		startActivity(launchingIntent);
	    }
	});

    }

    /**
     * The method addListenerDiaryComplete is for integrating the diary
     * completion button to the diary page. This add the functionality of
     * complete button to diary and work thus so complete all the post process
     * after user agreed to submit the entered data to the system.
     * 
     * @param none
     * @return void
     * @see andorid.view.View#OnClickListener {@link android.view.View#GONE}
     *      {@link andorid.view.View#OnCheckChangedListener}
     * @throws none
     * @since 1.0
     * @author Mahbubur Rahman
     */
    public void addListenerDiaryComplete()
    {

	mBtnDiaryComplete.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v)
	    {
		statusUpdatetoReady();
		ContentValues values = new ContentValues();
		Long now = Long.valueOf(System.currentTimeMillis());
		// values.put(MedicationList.MedicationItem._ID, med_id);
		values.put(FormContentData.FormContentItems.COLUMN_NAME_LAST_COLLECTED_TIME,
		        CalendarUtils.calTimeToDateStringyyyymmdd(Calendar.getInstance()));

		String where = FormContentData.FormContentItems.COLUMN_NAME_FORM_NAME + "='" + DatabaseConstants.FORM_CONTENT_ID_DIARY + "'";
		// String[] args = new String[] { Integer.toString(med_id) };
		int numrowupdated = getContentResolver().update(FormContentData.FormContentItems.CONTENT_URI, values, where, null);

		ReminderNotification rnote = new ReminderNotification();
		rnote.setNotificationAlarm(DiaryActivity.this, NotificationUtils.DIARY_NOTIFICATION_REQUEST_CODE, now);
		Log.d(TAG, "Diary submitted=" + numrowupdated);
		// finish();
		Intent intent = new Intent(DiaryActivity.this, DiaryActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	    }
	});

    }

    /**
     * The method addListenerMedicationGroup is for integrating the user
     * prescribed medication taking information to the diary page. The method
     * triggers for navigating to secondary medication selection activity based
     * on the database current status, form logic and selection of radio button.
     * <p>
     * If the current selection for the day of use is zero and user select yes,
     * then this will navigate to secondary medication page but if selection is
     * not zero which means user already have visited the selection activity for
     * the day of use and have selected at least one medication from the list.
     * If so, then "yes" radio button is already selected, and this will also
     * make the "edit" interface visible for user. So, if user decides to see
     * what she has selected, she can go by tapping "Edit". If the user wants to
     * select "No" and nothing is selected then nothing will be done. But if she
     * already selected at least one, and tries to select no now then a pop up
     * will appear and will ask for confirmation whether she really wants to
     * select "No". Because, if "No" is selected, then the underline database of
     * medication list which user already selected will be deleted by the system
     * to keep the stability and validity of data.
     * 
     * @param none
     * @return void
     * @see andorid.view.View#OnClickListener {@link android.view.View#GONE}
     *      {@link andorid.view.View#OnCheckChangedListener}
     * @throws none
     * @since 1.0
     * @author Mahbubur Rahman
     */
    public void addListenerMedicationGroup()
    {

	RadioGroup mMedication = (RadioGroup) findViewById(R.id.rgMedication);
	mMedication.setOnCheckedChangeListener(new OnCheckedChangeListener() {

	    @Override
	    public void onCheckedChanged(RadioGroup _Medication_Group, int checkedId)
	    {
		// TODO Auto-generated method stub
		final ArrayList<Integer> medlist = getSelectedMedicationForToday();
		Log.i(TAG, "Medication back ");
		String value = ((RadioButton) findViewById(_Medication_Group.getCheckedRadioButtonId())).getText().toString();
		Log.i(TAG, value);
		LinearLayout llMedicationBack = (LinearLayout) findViewById(R.id.llSYMedicationBack);

		if (value.equalsIgnoreCase(DiaryActivity.this.getResources().getString(R.string.quest_yes)) && medlist.size() == 0)
		{
		    llMedicationBack.setVisibility(android.view.View.GONE);
		    Intent launchingIntent = new Intent(DiaryActivity.this, MedicationListActivity.class);
		    startActivity(launchingIntent);
		}
		else if (value.equalsIgnoreCase(DiaryActivity.this.getResources().getString(R.string.quest_yes)) && medlist.size() != 0)
		{
		    // if the case is no
		    llMedicationBack.setVisibility(android.view.View.VISIBLE);
		    Button goMedication = (Button) findViewById(R.id.btnMedicationBack);
		    goMedication.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v)
			{
			    // TODO Auto-generated method stub
			    Intent launchingIntent = new Intent(DiaryActivity.this, MedicationListActivity.class);
			    startActivity(launchingIntent);
			}
		    });
		    // other.setText("");
		}
		else if (value.equalsIgnoreCase(DiaryActivity.this.getResources().getString(R.string.quest_no)) && medlist.size() != 0)
		{
		    // warnDeleteSelectedMedication();
		    AlertDialog dialog = warnDeleteSelectedMedication();
		    dialog.show();

		    // rbMedNo.setSelected(false);
		}
		else
		{
		    llMedicationBack.setVisibility(android.view.View.GONE);
		}

	    }
	});
    }

    /**
     * The method addListenerSupplementGroup is for integrating the user
     * prescribed supplement taking information to the diary page. The method
     * triggers for navigating to secondary supplement selection activity based
     * on the database current status, form logic and selection of radio button.
     * <p>
     * If the current selection for the day of use is zero and user select yes,
     * then this will navigate to secondary supplement page but if selection is
     * not zero which means user already have visited the selection activity for
     * the day of use and have selected at least one supplement from the list.
     * If so, then "yes" radio button is already selected, and this will also
     * make the "edit" interface visible for user. So, if user decides to see
     * what she has selected, she can go by tapping "Edit". If the user wants to
     * select "No" and nothing is selected then nothing will be done. But if she
     * already selected at least one, and tries to select no now then a pop up
     * will appear and will ask for confirmation whether she really wants to
     * select "No". Because, if "No" is selected, then the underline database of
     * supplement list which user already selected will be deleted by the system
     * to keep the stability and validity of data.
     * 
     * @param none
     * @return void
     * @see {@link android.view.View#GONE} {@link android.widget.RadioGroup}
     *      {@link android.widget.RadioButton} {@linkplain java.util.ArrayList}
     * @throws none
     * @since 1.0
     * @author Mahbubur Rahman
     */
    public void addListenerSupplementGroup()
    {

	RadioGroup mMedication = (RadioGroup) findViewById(R.id.rgOTC);
	mMedication.setOnCheckedChangeListener(new OnCheckedChangeListener() {

	    @Override
	    public void onCheckedChanged(RadioGroup _Medication_Group, int checkedId)
	    {
		// TODO Auto-generated method stub
		final ArrayList<Integer> supplist = getSelectedSupplementForToday();
		Log.i(TAG, "Supplement back ");
		String value = ((RadioButton) findViewById(_Medication_Group.getCheckedRadioButtonId())).getText().toString();
		Log.i(TAG, value);
		LinearLayout llMedicationBack = (LinearLayout) findViewById(R.id.llSYOTCBack);

		if (value.equalsIgnoreCase(DiaryActivity.this.getResources().getString(R.string.quest_yes)) && supplist.size() == 0)
		{
		    llMedicationBack.setVisibility(android.view.View.GONE);
		    Intent launchingIntent = new Intent(DiaryActivity.this, SupplementListActivity.class);
		    startActivity(launchingIntent);
		}
		else if (value.equalsIgnoreCase(DiaryActivity.this.getResources().getString(R.string.quest_yes)) && supplist.size() != 0)
		{
		    // if the case is no
		    llMedicationBack.setVisibility(android.view.View.VISIBLE);
		    Button goMedication = (Button) findViewById(R.id.btnSYOTCBack);
		    goMedication.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v)
			{
			    // TODO Auto-generated method stub
			    Intent launchingIntent = new Intent(DiaryActivity.this, SupplementListActivity.class);
			    startActivity(launchingIntent);
			}
		    });
		    // other.setText("");
		}
		else if (value.equalsIgnoreCase(DiaryActivity.this.getResources().getString(R.string.quest_no)) && supplist.size() != 0)
		{
		    // warnDeleteSelectedMedication();
		    AlertDialog dialog = warnDeleteSelectedSupplement();
		    dialog.show();

		    // rbMedNo.setSelected(false);
		}
		else
		{
		    llMedicationBack.setVisibility(android.view.View.GONE);
		}

	    }
	});

    }

    /**
     * The method addListenerExersizeGroup is to integrated the functionality of
     * extra information that the user wants to provide and is not listed to
     * diary activity form.
     * 
     * @param none
     * @return void
     * @see andorid.view.View#OnClickListener
     * @throws none
     * @since 1.0
     * @author Mahbubur Rahman
     */
    public void addListenerOtherGroup()
    {

	// RadioGroup radioOtherGroup = (RadioGroup) findViewById(R.id.rgOther);
	// RadioButton rbOTCYes = (RadioButton) findViewById(R.id.rbOTC0);

	mOther.setOnCheckedChangeListener(new OnCheckedChangeListener() {

	    @Override
	    public void onCheckedChanged(RadioGroup _otherGroup, int arg1)
	    {
		Log.i(TAG, "OtherGroup");
		String value = ((RadioButton) findViewById(_otherGroup.getCheckedRadioButtonId())).getText().toString();
		Log.i(TAG, value);
		LinearLayout symptomsOther = (LinearLayout) findViewById(R.id.llEtOther);
		if (value.equalsIgnoreCase(DiaryActivity.this.getResources().getString(R.string.quest_yes)))
		{
		    symptomsOther.setVisibility(android.view.View.VISIBLE);
		}
		else
		{
		    // if the case is no
		    symptomsOther.setVisibility(android.view.View.GONE);
		    EditText other = (EditText) findViewById(R.id.etOther);
		    other.setText("");
		}
	    }

	});

    }

    /**
     * The method addListenerExersizeGroup is to integrated the functionality of
     * exercising information selection radio button group button.
     * 
     * @param none
     * @return void
     * @see andorid.view.View#OnClickListener
     * @throws none
     * @since 1.0
     * @author Mahbubur Rahman
     */
    public void addListenerExersizeGroup()
    {

	mExercise.setOnCheckedChangeListener(new OnCheckedChangeListener() {

	    @Override
	    public void onCheckedChanged(RadioGroup _otherGroup, int arg1)
	    {
		Log.i(TAG, "ExersizeGroup");
		String value = ((RadioButton) findViewById(_otherGroup.getCheckedRadioButtonId())).getText().toString();
		Log.i(TAG, value);
		LinearLayout ExerciseIndoors = (LinearLayout) findViewById(R.id.llSYExerciseIndoor);
		LinearLayout ExerciseOutdoors = (LinearLayout) findViewById(R.id.llExerciseOutdoors);
		if (value.equalsIgnoreCase(DiaryActivity.this.getResources().getString(R.string.quest_yes)))
		{
		    ExerciseIndoors.setVisibility(android.view.View.VISIBLE);
		    ExerciseOutdoors.setVisibility(android.view.View.VISIBLE);
		}
		else
		{
		    ExerciseIndoors.setVisibility(android.view.View.GONE);
		    ExerciseOutdoors.setVisibility(android.view.View.GONE);
		    Spinner spinner = (Spinner) findViewById(R.id.spExerciseIndoors);
		    spinner.setSelection(0);
		    spinner = (Spinner) findViewById(R.id.spExerciseOutdoors);
		    spinner.setSelection(0);
		}
	    }

	});

    }

    /**
     * The method addListenerSmokingGroup is to integrated the functionality of
     * smoking information selection radio button group button. Just like all
     * the simple group button this activity, this group also just add
     * functionality of yes and no selection.
     * 
     * @param none
     * @return void
     * @see andorid.view.View#OnClickListener
     * @throws none
     * @since 1.0
     * @author Mahbubur Rahman
     */
    public void addListenerSmokingGroup()
    {

	// RadioGroup radioSmokingGroup = (RadioGroup)
	// findViewById(R.id.rgSmoke);
	// RadioButton rbSmokingYes = (RadioButton) findViewById(R.id.rbSmoke0);

	mSmoke.setOnCheckedChangeListener(new OnCheckedChangeListener() {

	    @Override
	    public void onCheckedChanged(RadioGroup _smokingGroup, int arg1)
	    {
		Log.i(TAG, "Smoking");
		String value = ((RadioButton) findViewById(_smokingGroup.getCheckedRadioButtonId())).getText().toString();
		Log.i(TAG, value);
		LinearLayout numSmoke = (LinearLayout) findViewById(R.id.llSYNumberOfSmoke);
		if (value.equalsIgnoreCase(DiaryActivity.this.getResources().getString(R.string.quest_yes)))
		{
		    numSmoke.setVisibility(android.view.View.VISIBLE);
		}
		else
		{
		    numSmoke.setVisibility(android.view.View.GONE);
		    Spinner smoked = (Spinner) findViewById(R.id.spNumberSmoked);
		    smoked.setSelection(0);
		}
	    }

	});

    }

    /**
     * The method addListenerWokeupGroup is to integrated the functionality of
     * wake up information selection radio button group button. Just like all
     * the simple group button this activity, this group also just add
     * functionality of yes and no selection.
     * 
     * @param none
     * @return void
     * @see andorid.view.View#OnClickListener
     * @throws none
     * @since 1.0
     * @author Mahbubur Rahman
     */
    public void addListenerWokeupGroup()
    {

	// RadioGroup radioSmokingGroup = (RadioGroup)
	// findViewById(R.id.rgSmoke);
	// RadioButton rbSmokingYes = (RadioButton) findViewById(R.id.rbSmoke0);

	mWake.setOnCheckedChangeListener(new OnCheckedChangeListener() {

	    @Override
	    public void onCheckedChanged(RadioGroup _smokingGroup, int arg1)
	    {
		Log.i(TAG, "Woke up");
		String value = ((RadioButton) findViewById(_smokingGroup.getCheckedRadioButtonId())).getText().toString();
		Log.i(TAG, value);
		LinearLayout wokeup = (LinearLayout) findViewById(R.id.llSYTimeWokeUp);
		if (value.equalsIgnoreCase(DiaryActivity.this.getResources().getString(R.string.quest_yes)))
		{
		    wokeup.setVisibility(android.view.View.VISIBLE);
		}
		else
		{
		    wokeup.setVisibility(android.view.View.GONE);
		    Spinner sworkup = (Spinner) findViewById(R.id.spTimesWokeUp);
		    sworkup.setSelection(0);
		}
	    }

	});

    }

    /* (non-Javadoc)
     * 
     * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu) */
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
	// Inflate the menu; this adds items to the action bar if it is present.
	getMenuInflater().inflate(R.menu.diary, menu);
	return true;
    }

    /* (non-Javadoc)
     * 
     * @see
     * com.emmes.aps.NavigationActivity#onOptionsItemSelected(android.view.
     * MenuItem) */
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
	// return super.onOptionsItemSelected(item);
	switch (item.getItemId())
	{
	case R.id.action_settings:
	    Intent launchingIntent = new Intent(this, SettingsActivity.class);
	    startActivity(launchingIntent);
	    return true;
// actio_test is only for settings form settings which is time and by settings
// that I can check different timing implementation.
	case R.id.action_test:
	    Intent testintent = new Intent(this, TestActivity.class);
	    startActivity(testintent);
	    return true;
	case android.R.id.home:
	    NavUtils.navigateUpFromSameTask(this);
	    return true;
	default:
	    return super.onOptionsItemSelected(item);
	}

    }

    /* (non-Javadoc)
     * 
     * @see android.app.Activity#onResume() */
    @Override
    protected void onResume()
    {
	// TODO Auto-generated method stub
	super.onResume();
	syncDiaryData();
	loadTodayDiaryData();

    }

    /* (non-Javadoc)
     * 
     * @see android.app.Activity#onDestroy() */
    @Override
    protected void onDestroy()
    {
	Log.v(TAG, "onDestory");

	unregisterReceiver(mReceiver);
	super.onDestroy();
    }

    /**
     * The method syncDiaryData() is for sync the diary data which are ready
     * before entering to the next diary to insert and also check the server
     * side configuration and if needed update the local settings based on the
     * server side information like update the duration of location tracking.
     * This method calls the intentServer for doing the sync process background.
     * <p>
     * This method first create and register a receiver with response process id
     * and check the network before calling the intentService. If every thing
     * alright then it call for the service to do the sync process.
     * 
     * @param none
     * @return the country information
     * @see {@link android.content.IntentFilter}
     *      {@link com.emmes.aps.sync.DataSyncRequest}
     * @throws none
     * @since 1.0
     * @author Mahbubur Rahman
     */
    private void syncDiaryData()
    {

	// Register your receiver so that the Activity can be notified
	// when the JSON response came back
	IntentFilter filter = new IntentFilter(DataSyncRequestReceiver.PROCESS_RESPONSE);
	filter.addCategory(Intent.CATEGORY_DEFAULT);
	mReceiver = new DataSyncRequestReceiver();
	registerReceiver(mReceiver, filter);
	boolean internet = NetworkUtils.isNetworkAvailable(this);
	if (internet)
	{
	    Intent msgIntent = new Intent(this, DataSyncRequest.class);
	    msgIntent.putExtra(DataSyncRequest.IN_MSG, SyncUtils.SYNC_DATA_SERVICE_NAME_TAG);
	    // msgIntent.putExtra("countryCode", code.trim());
	    startService(msgIntent);

	}
    }

    /* *//**
     * Load diary data from cursor.
     */
    /* private void loadDiaryDataFromCursor() { if (mCursor != null) { if
     * (mCursor.moveToFirst()) { mDiaryDate.setText(mCursor.getString(0)); } } } */

    /* (non-Javadoc)
     * 
     * @see com.emmes.aps.NavigationActivity#onPause() */
    @Override
    protected void onPause()
    {
	super.onPause();
	saveDiaryData();
	Log.i(TAG, "Saving Diary");

    }

    /**
     * The method saveDiaryData() is to save data of diary to local database
     * name tb_dailydiary
     * {@linkplain com.emmes.aps.data.DailyDiaryDB#DialyDiaryTableManage}.
     * <p>
     * The diary data are stored as a single string of JSON
     * {@linkplain com.google.gson}. A class named DiaryData
     * {@linkplain com.emmes.aps.sync.DiaryData} has been used to create java
     * object of diary information and then transform it to JSON object. Then
     * this JSON object is stored to the database.
     * <p>
     * As the data can be save at onPause of the activity and several times of
     * the day, data can be saved multiple times with updated or without
     * updated. Every time the diary page load, the JSON object is loaded from
     * the database and based on the user current selection update and if the
     * savings is done for the fist time then new JSON object is created and
     * saved to database.
     * <p>
     * As only one single data will be for each day, the data will only be
     * inserted once a day and will be updated all other times. This class
     * maintain several boolean flag to keep track of data being updated or
     * insert. For example if {@code if (IS_REPEATED_VISIT_TO_DIARY_PAGE_TODAY)}
     * is true which means this is updated data not new and will be updated but
     * if false then the data is saving for the first time and will be entered
     * to database as new entry.
     * <p>
     * For the nitroxide case, the nitroxide input edittext will appear based on
     * the form content. It could appear once every other day. But the diary
     * could be save without filling this field or may be accidentally paused or
     * some other things may happen which will give this field that the data has
     * been entered and nitro last collection time will be updated. So, to
     * handle this situation the system maintain another flag which will see if
     * the nitro data is actually entered or not and also if the nitro is
     * visible today or not. For example if
     * {@code if (this.IS_NITRO_VISIBLE && !this.IS_NITRO_ENTRY_EMPTY)} is true
     * which means the nitro value has been entered and the last time of
     * collection for nitro will be updated otherwise, it will not be updated.
     * 
     * @param none
     * @return the country information
     * @see {@link android.content.IntentFilter}
     *      {@link com.emmes.aps.sync.DataSyncRequest}
     * @throws none
     * @since 1.0
     * @author Mahbubur Rahman
     */
    private void saveDiaryData()
    {
	Log.i(TAG, "TO BE DONE->saveDiaryItem");
	DiaryData ddata = new DiaryData();

	/* collect user input from diary page */
	String sWheezing = "", sCoughing = "", sShortBreath = "", sChestTightness = "", sChestPain = "", sFever = "", sNausea = "", sRunnyNose = "", sOther = "", sMissSchoolWork = "", sPescription = "", sOTC = "", sWake = "", sExercise = "", sSmoke = "", sPeopleSmoking = "", sCaffein = "", sStress = "";

	Object value = null;

	sWheezing = (value = (findViewById(mWheezing.getCheckedRadioButtonId()))) != null ? (((RadioButton) value).getText().toString())
	        : "";
	ddata.setWheezing(sWheezing);

	sCoughing = (value = (findViewById(mCoughing.getCheckedRadioButtonId()))) != null ? (((RadioButton) value).getText().toString())
	        : "";

	ddata.setCoughing(sCoughing);

	sShortBreath = (value = (findViewById(mShortBreath.getCheckedRadioButtonId()))) != null ? (((RadioButton) value).getText()
	        .toString()) : "";

	ddata.setShortBreath(sShortBreath);

	sChestTightness = (value = (findViewById(mChestTightness.getCheckedRadioButtonId()))) != null ? (((RadioButton) value)
	        .getText().toString()) : "";

	ddata.setChestTightness(sChestTightness);

	sChestPain = (value = (findViewById(mChestPain.getCheckedRadioButtonId()))) != null ? (((RadioButton) value).getText()
	        .toString()) : "";

	ddata.setChestPain(sChestPain);
	sFever = (value = (findViewById(mFever.getCheckedRadioButtonId()))) != null ? (((RadioButton) value).getText().toString()) : "";

	ddata.setFever(sFever);
	sNausea = (value = (findViewById(mNausea.getCheckedRadioButtonId()))) != null ? (((RadioButton) value).getText().toString())
	        : "";

	ddata.setNausea(sNausea);
	sRunnyNose = (value = (findViewById(mRunnyNose.getCheckedRadioButtonId()))) != null ? (((RadioButton) value).getText()
	        .toString()) : "";

	ddata.setRunnyNose(sRunnyNose);
	sOther = (value = (findViewById(mOther.getCheckedRadioButtonId()))) != null ? (((RadioButton) value).getText().toString()) : "";

	ddata.setOther(sOther);
	sMissSchoolWork = (value = (findViewById(mMissSchoolWork.getCheckedRadioButtonId()))) != null ? (((RadioButton) value)
	        .getText().toString()) : "";

	ddata.setMissSchoolWork(sMissSchoolWork);

	sPescription = (value = (findViewById(mPescription.getCheckedRadioButtonId()))) != null ? (((RadioButton) value).getText()
	        .toString()) : "";

	ddata.setPescription(sPescription);

	sOTC = (value = (findViewById(mOTC.getCheckedRadioButtonId()))) != null ? (((RadioButton) value).getText().toString()) : "";

	ddata.setOTC(sOTC);
	sWake = (value = (findViewById(mWake.getCheckedRadioButtonId()))) != null ? (((RadioButton) value).getText().toString()) : "";

	ddata.setWake(sWake);

	sExercise = (value = (findViewById(mExercise.getCheckedRadioButtonId()))) != null ? (((RadioButton) value).getText().toString())
	        : "";

	ddata.setExercise(sExercise);

	sSmoke = (value = (findViewById(mSmoke.getCheckedRadioButtonId()))) != null ? (((RadioButton) value).getText().toString()) : "";

	ddata.setSmoke(sSmoke);

	sPeopleSmoking = (value = (findViewById(mPeopleSmoking.getCheckedRadioButtonId()))) != null ? (((RadioButton) value).getText()
	        .toString()) : "";

	ddata.setPeopleSmoking(sPeopleSmoking);
	sCaffein = (value = (findViewById(mCaffein.getCheckedRadioButtonId()))) != null ? (((RadioButton) value).getText().toString())
	        : "";

	ddata.setCaffein(sCaffein);
	sStress = (value = (findViewById(mStress.getCheckedRadioButtonId()))) != null ? (((RadioButton) value).getText().toString())
	        : "";

	ddata.setStress(sStress);
	/* Health section: extra */

	/* Daily Activities */
	ddata.setHrsSlept(mDurationSlept.getText().toString() != "" ? CalendarUtils.getHour(mDurationSlept.getText().toString()) : "");
	ddata.setMinSlept(mDurationSlept.getText().toString() != "" ? CalendarUtils.getMinute(mDurationSlept.getText().toString()) : "");

	ddata.setTimesWoke(mTimesWoke.getSelectedItem().toString());

	ddata.setHrsSitting(mDurationSitting.getText().toString() != "" ? CalendarUtils.getHour(mDurationSitting.getText().toString()) : "");
	ddata.setMinSitting(mDurationSitting.getText().toString() != "" ? CalendarUtils.getMinute(mDurationSitting.getText().toString()) : "");

	ddata.setMinIndoorsExercise(mMinIndoorsExercise.getSelectedItem().toString());
	ddata.setMinOutdoorsExercise(mMinOutdoorsExercise.getSelectedItem().toString());

	ddata.setMinOutdoorsTimeSpent(mDurationSpentOutdoors.getText().toString() != "" ? CalendarUtils.getHour(mDurationSpentOutdoors.getText()
	        .toString()) : "");
	ddata.setHrsOutdoorsTimeSpent(mDurationSpentOutdoors.getText().toString() != "" ? CalendarUtils.getMinute(mDurationSpentOutdoors.getText()
	        .toString()) : "");

	ddata.setHrsVehicle(mDurationVehicle.getText().toString() != "" ? CalendarUtils.getHour(mDurationVehicle.getText().toString()) : "");
	ddata.setMinVehicle(mDurationVehicle.getText().toString() != "" ? CalendarUtils.getMinute(mDurationVehicle.getText().toString()) : "");

	ddata.setNumSmoke(mNumSmoke.getSelectedItem().toString());

	ddata.setNitricOxide(mNitricOxide.getText().toString());

	if (!mNitricOxide.getText().toString().isEmpty())
	    this.IS_NITRO_ENTRY_EMPTY = false;

	ddata.setEtOther(mEtOther.getText().toString());

	Gson gson = new Gson();
	JsonObject jo = new JsonObject();
	JsonElement diarytree = gson.toJsonTree(ddata);
	jo.add("diary", diarytree);
	ContentValues values = new ContentValues();

	Calendar now_time = Calendar.getInstance();
	// now_time.setTimeZone(TimeZone.getDefault());
	// Date date = now_time.getTime();
	String formattedDate = CalendarUtils.calTimeToDateStringyyyymmdd(now_time);// new
	                                                                           // SimpleDateFormat("yyyy-MM-dd").format(date);

	if (IS_REPEATED_VISIT_TO_DIARY_PAGE_TODAY)
	{
	    values.clear();
	    values.put(DailyDiaryDB.DailyDiaryItem.COLUMN_NAME_DIARY_CONTENT, jo.toString());
	    String where = DailyDiaryDB.DailyDiaryItem.COLUMN_NAME_ENTRYTIME + "=?";
	    String[] selectionArgs = { formattedDate };
	    getContentResolver().update(DailyDiaryDB.DailyDiaryItem.CONTENT_URI, values, where, selectionArgs);
	}
	else
	{
	    values.clear();
	    values.put(DailyDiaryDB.DailyDiaryItem.COLUMN_NAME_DIARY_CONTENT, jo.toString());
	    values.put(DailyDiaryDB.DailyDiaryItem.COLUMN_NAME_ENTRYTIME, formattedDate);
	    getContentResolver().insert(DailyDiaryDB.DailyDiaryItem.CONTENT_URI, values);
	}

	values.clear();
	values.put(FormContentData.FormContentItems.COLUMN_NAME_LAST_COLLECTED_TIME, CalendarUtils.calTimeToDateStringyyyymmddhhmmss(now_time));

	String where = FormContentData.FormContentItems.COLUMN_NAME_FORM_NAME + "='" + DatabaseConstants.FORM_CONTENT_ID_DIARY + "'";

	// String[] args = new String[] { Integer.toString(med_id) };
	int numofrowdeleted = this.getContentResolver().update(FormContentData.FormContentItems.CONTENT_URI, values, where, null);
	Log.d(TAG, numofrowdeleted + " rows updated which is peakflow row");

	if (this.IS_NITRO_VISIBLE && !this.IS_NITRO_ENTRY_EMPTY)
	{
	    where = FormContentData.FormContentItems.COLUMN_NAME_FORM_NAME + "='" + DatabaseConstants.FORM_CONTENT_ID_NITRO + "'";
	    numofrowdeleted = this.getContentResolver().update(FormContentData.FormContentItems.CONTENT_URI, values, where, null);
	    Log.d(TAG, numofrowdeleted + " rows updated which is nitro row");
	}

    }

    /**
     * The method setDiaryElementsToInvisible() is to control the display of
     * diary activity. Some views should be displayed and some should not be
     * based on the user usage and form contents local settings. For example, if
     * diary data is complete by the user, the diary related information will
     * not be displayed together with nitro info. As the medication selection is
     * also linked to diary, so medication will also loose the link to go. The
     * peak flow will be controlled by itself. If three peak flow are entered,
     * peak flow button will be vanished.
     * <p>
     * Peakflow button will be visible if one of the AM or PM peak flow have not
     * been filled up and this should also be in the peakflow time window.
     * 
     * <pre>
     * {@code if (this.IS_AM_PEAK_FLOW_VISIBLE || this.IS_PM_PEAK_FLOW_VISIBLE)
     * 		llPeakFlow.setVisibility(View.VISIBLE);
     * 	    else
     * 		llPeakFlow.setVisibility(View.GONE);}
     * </pre>
     * <p>
     * If all the information are entered or user agreed that all information
     * entered, then there is nothing to show to the diary activity and simply
     * will display a thank you message to the user for inviting next day diary
     * information.
     * 
     * <pre>
     * {@code if (this.IS_DIARY_DONE_FOR_THE_DAY && !this.IS_AM_PEAK_FLOW_VISIBLE && !this.IS_PM_PEAK_FLOW_VISIBLE)
     * 	    showThanksDialog();}
     * </pre>
     * 
     * @param none
     * @return the country information
     * 
     * @throws none
     * @since 1.0
     * @author Mahbubur Rahman
     */
    private void setDiaryElementsToInvisible()
    {

	/**
	 * Listing of all layout views
	 */
	LinearLayout llSymptomsTitle = (LinearLayout) findViewById(R.id.llSymptomsTitle);
	LinearLayout llPeakFlow = (LinearLayout) findViewById(R.id.llpeakflow);
	LinearLayout llSymptomsSummary = (LinearLayout) findViewById(R.id.llSymtomsSummary);
	LinearLayout llWheezing = (LinearLayout) findViewById(R.id.llwheezing);
	LinearLayout llCouphing = (LinearLayout) findViewById(R.id.llSYCouphing);
	LinearLayout llSYShortBreath = (LinearLayout) findViewById(R.id.llSYShortBreath);
	LinearLayout llSYChestTightness = (LinearLayout) findViewById(R.id.llSYChestTightness);
	LinearLayout llSYChestPain = (LinearLayout) findViewById(R.id.llSYChestPain);
	LinearLayout llSYFever = (LinearLayout) findViewById(R.id.llSYFever);
	LinearLayout llSVNauseaVomitting = (LinearLayout) findViewById(R.id.llSVNauseaVomitting);
	LinearLayout llRunnyNose = (LinearLayout) findViewById(R.id.llRunnyNose);
	LinearLayout llSVOther = (LinearLayout) findViewById(R.id.llSVOther);
	LinearLayout llEtOther = (LinearLayout) findViewById(R.id.llEtOther);
	LinearLayout llSYMissSchoolWork = (LinearLayout) findViewById(R.id.llSYMissSchoolWork);
	LinearLayout llSYMedication = (LinearLayout) findViewById(R.id.llSYMedication);
	LinearLayout llSYOTC = (LinearLayout) findViewById(R.id.llSYOTC);
	LinearLayout llSYOTCBack = (LinearLayout) findViewById(R.id.llSYOTCBack);
	LinearLayout llDailyActivitiesTitle = (LinearLayout) findViewById(R.id.llDailyActivitiesTitle);
	LinearLayout llSYMedicationBack = (LinearLayout) findViewById(R.id.llSYMedicationBack);
	LinearLayout llSYTimeSlept = (LinearLayout) findViewById(R.id.llSYTimeSlept);
	LinearLayout llSYBreathingCaoughingMidnight = (LinearLayout) findViewById(R.id.llSYBreathingCaoughingMidnight);
	LinearLayout llSYTimeWokeUp = (LinearLayout) findViewById(R.id.llSYTimeWokeUp);
	LinearLayout llSYTimeSitting = (LinearLayout) findViewById(R.id.llSYTimeSitting);
	LinearLayout llSYExercise = (LinearLayout) findViewById(R.id.llSYExercise);
	LinearLayout llSYExerciseIndoor = (LinearLayout) findViewById(R.id.llSYExerciseIndoor);
	LinearLayout llExerciseOutdoors = (LinearLayout) findViewById(R.id.llExerciseOutdoors);
	LinearLayout llSYTimeSpent = (LinearLayout) findViewById(R.id.llSYTimeSpent);
	LinearLayout llSYTimeSpentOutdoor = (LinearLayout) findViewById(R.id.llSYTimeSpentOutdoor);
	LinearLayout llSYVehicle = (LinearLayout) findViewById(R.id.llSYVehicle);
	LinearLayout llSYSmoke = (LinearLayout) findViewById(R.id.llSYSmoke);
	LinearLayout llSYNumberOfSmoke = (LinearLayout) findViewById(R.id.llSYNumberOfSmoke);
	LinearLayout llSYPeopleSmoking = (LinearLayout) findViewById(R.id.llSYPeopleSmoking);
	LinearLayout llSYDrinkCaffein = (LinearLayout) findViewById(R.id.llSYDrinkCaffein);
	LinearLayout llSYDailyStress = (LinearLayout) findViewById(R.id.llSYDailyStress);
	LinearLayout llSYNitricOxide = (LinearLayout) findViewById(R.id.llSYNitricOxide);
	LinearLayout lldiaryComplete = (LinearLayout) findViewById(R.id.lldiaryComplete);

	if (this.IS_DIARY_DONE_FOR_THE_DAY)
	{

	    llSymptomsTitle.setVisibility(View.GONE);
	    llSymptomsSummary.setVisibility(View.GONE);
	    llWheezing.setVisibility(View.GONE);
	    llCouphing.setVisibility(View.GONE);
	    llSYShortBreath.setVisibility(View.GONE);
	    llSYChestTightness.setVisibility(View.GONE);
	    llSYChestPain.setVisibility(View.GONE);
	    llSYFever.setVisibility(View.GONE);
	    llSVNauseaVomitting.setVisibility(View.GONE);
	    llRunnyNose.setVisibility(View.GONE);
	    llSVOther.setVisibility(View.GONE);
	    llEtOther.setVisibility(View.GONE);
	    llSYMissSchoolWork.setVisibility(View.GONE);
	    llSYMedication.setVisibility(View.GONE);
	    llSYOTC.setVisibility(View.GONE);
	    llDailyActivitiesTitle.setVisibility(View.GONE);
	    llSYMedicationBack.setVisibility(View.GONE);
	    llSYTimeSlept.setVisibility(View.GONE);
	    llSYBreathingCaoughingMidnight.setVisibility(View.GONE);
	    llSYTimeWokeUp.setVisibility(View.GONE);
	    llSYTimeSitting.setVisibility(View.GONE);
	    llSYExercise.setVisibility(View.GONE);
	    llSYExerciseIndoor.setVisibility(View.GONE);
	    llExerciseOutdoors.setVisibility(View.GONE);
	    llSYTimeSpent.setVisibility(View.GONE);
	    llSYTimeSpentOutdoor.setVisibility(View.GONE);
	    llSYVehicle.setVisibility(View.GONE);
	    llSYSmoke.setVisibility(View.GONE);
	    llSYNumberOfSmoke.setVisibility(View.GONE);
	    llSYPeopleSmoking.setVisibility(View.GONE);
	    llSYDrinkCaffein.setVisibility(View.GONE);
	    llSYDailyStress.setVisibility(View.GONE);
	    llSYNitricOxide.setVisibility(View.GONE);
	    lldiaryComplete.setVisibility(View.GONE);
	    Log.d(TAG, "Making diary views invisible");

	    if (this.IS_AM_PEAK_FLOW_VISIBLE || this.IS_PM_PEAK_FLOW_VISIBLE)
		llPeakFlow.setVisibility(View.VISIBLE);
	    else
		llPeakFlow.setVisibility(View.GONE);
	    // peakFlowButton.setVisibility(View.GONE);
	    Log.i(TAG, "Visibility GONE");
	}
	else
	{
	    Log.d(TAG, "Making diary views visible");

	    llSymptomsTitle.setVisibility(View.VISIBLE);
	    llSymptomsSummary.setVisibility(View.VISIBLE);
	    llWheezing.setVisibility(View.VISIBLE);
	    llCouphing.setVisibility(View.VISIBLE);
	    llSYShortBreath.setVisibility(View.VISIBLE);
	    llSYChestTightness.setVisibility(View.VISIBLE);
	    llSYChestPain.setVisibility(View.VISIBLE);
	    llSYFever.setVisibility(View.VISIBLE);
	    llSVNauseaVomitting.setVisibility(View.VISIBLE);
	    llRunnyNose.setVisibility(View.VISIBLE);
	    llSVOther.setVisibility(View.VISIBLE);

	    llEtOther.setVisibility(llEtOther.getVisibility());

	    llSYMissSchoolWork.setVisibility(View.VISIBLE);
	    llSYMedication.setVisibility(View.VISIBLE);
	    llSYOTC.setVisibility(View.VISIBLE);
	    llDailyActivitiesTitle.setVisibility(View.VISIBLE);
	    llSYMedicationBack.setVisibility(llSYMedicationBack.getVisibility());
	    llSYOTCBack.setVisibility(llSYOTCBack.getVisibility());

	    llSYTimeSlept.setVisibility(View.VISIBLE);
	    llSYBreathingCaoughingMidnight.setVisibility(View.VISIBLE);
	    llSYTimeWokeUp.setVisibility(llSYTimeWokeUp.getVisibility());
	    llSYTimeSitting.setVisibility(View.VISIBLE);
	    llSYExercise.setVisibility(View.VISIBLE);
	    llSYExerciseIndoor.setVisibility(llSYExerciseIndoor.getVisibility());
	    llExerciseOutdoors.setVisibility(llExerciseOutdoors.getVisibility());
	    llSYTimeSpent.setVisibility(View.VISIBLE);
	    llSYTimeSpentOutdoor.setVisibility(View.VISIBLE);
	    llSYVehicle.setVisibility(View.VISIBLE);
	    llSYSmoke.setVisibility(View.VISIBLE);

	    llSYNumberOfSmoke.setVisibility(llSYNumberOfSmoke.getVisibility());

	    llSYPeopleSmoking.setVisibility(View.VISIBLE);
	    llSYDrinkCaffein.setVisibility(View.VISIBLE);
	    llSYDailyStress.setVisibility(View.VISIBLE);

	    if (this.IS_NITRO_VISIBLE)
	    {
		llSYNitricOxide.setVisibility(View.VISIBLE);
	    }
	    else
	    {
		llSYNitricOxide.setVisibility(View.GONE);
	    }

	    lldiaryComplete.setVisibility(View.VISIBLE);

	    // TODO check the condition again

	    if (this.IS_AM_PEAK_FLOW_VISIBLE || this.IS_PM_PEAK_FLOW_VISIBLE)
		llPeakFlow.setVisibility(View.VISIBLE);
	    else
		llPeakFlow.setVisibility(View.GONE);
	    Log.i(TAG, "Visibility BACK");
	}

	if (this.IS_DIARY_DONE_FOR_THE_DAY && !this.IS_AM_PEAK_FLOW_VISIBLE && !this.IS_PM_PEAK_FLOW_VISIBLE)
	    showThanksDialog();
    }

    /**
     * The method loadTodayDiaryData() loads the data from the database if exist
     * something for that particular day.
     * <p>
     * The method first call {@link com.emmes.aps.DiaryActivity#isVisibleNow()}
     * to determine what to display to the diary and what views should be hide
     * from the usr. Then its query out information from the database which fill
     * two conditions
     * <ul>
     * <li>The data has been entered today to the database.</li>
     * <li>The status is incomplete means the user has not agreed to submit yet.
     * </li>
     * </ul>
     * <p>
     * This method then based on the data, determined if this is repeated visit
     * of the page today or is visiting for the first time. If the diary is not
     * completed for today and data exists then it binds the data to the views
     * by calling {@link com.emmes.aps.DiaryActivity#bindExistingDataToViews()}
     * <p>
     * If every decision was made, it call then
     * {@linkplain com.emmes.aps.DiaryActivity#setDiaryElementsToInvisible()} to
     * control the visibility of views.
     * 
     * @param none
     * @return the country information
     * 
     * @throws none
     * @since 1.0
     * @author Mahbubur Rahman
     */
    private void loadTodayDiaryData()
    {

	isVisibleNow();
	Calendar now_time = Calendar.getInstance();
	// now_time.setTimeZone(TimeZone.getDefault());
	// Date date = now_time.getTime();

	String formattedDate = CalendarUtils.calTimeToDateStringyyyymmdd(now_time);// new
	                                                                           // SimpleDateFormat("yyyy-MM-dd").format(date);

	String[] PROJECTION_COLUMS = { DailyDiaryDB.DailyDiaryItem.COLUMN_NAME_DIARY_CONTENT, DailyDiaryDB.DailyDiaryItem.COLUMN_NAME_STATUS };
	String selection = DailyDiaryDB.DailyDiaryItem.COLUMN_NAME_ENTRYTIME + "=?";
	String[] selectionArgs = { formattedDate };
	// ContentValues contents = new ContentValues();
	// contents.put(key, value);
	Cursor cursor = getContentResolver().query(DailyDiaryDB.DailyDiaryItem.CONTENT_URI, PROJECTION_COLUMS, selection, selectionArgs,
	        DailyDiaryDB.DailyDiaryItem.DEFAULT_SORT_ORDER);

	String diaryString = "";
	if (cursor.getCount() > 0)
	{// some data exist for today
	    cursor.moveToFirst();
	    IS_REPEATED_VISIT_TO_DIARY_PAGE_TODAY = true;
	    // if the diary status for today is complete
	    if (!cursor.getString(1).equalsIgnoreCase(DataTransferUtils.STATUS_DATA_TRANSFER_INCOMPLETE))
	    {
		IS_DIARY_DONE_FOR_THE_DAY = true;
		// TODO hide all views in diary page
	    }
	    else
	    {
		IS_DIARY_DONE_FOR_THE_DAY = false;
		diaryString = cursor.getString(0);
		bindExistingDataToViews(diaryString);
		// setPeakFlowToVisible();
	    }

	}

	setDiaryElementsToInvisible();
	// setPeakFlowToVisible();
	cursor.close();

	// }//end of if

    }

    /**
     * The method bindExistingDataToViews(String diaryString) is to bind the
     * data if exists already to the database. Like if the diary page is loaded
     * at least for second time, there there will be data to the database for
     * that day and if the data status is incomplete and day is not expired then
     * the data should be binded with the views. Most of the binding is radio
     * group radio button with the selection of data.
     * <p>
     * Almost all the data bound are related to yes or no answers and some are
     * selected value for dropdown list. If the selected drop down list contains
     * value other than default then its set as it is. The only exceptional
     * thing here done is the binding data to selection of medication and
     * suppliment. As these are secondary activities selections, if user
     * selected previously yes and then now they are selecting no, these means,
     * they have selected medication but now they don't want to keep. To make
     * sure of their response, if there is already selection of medication or
     * supplement, and they are trying to select no, then a pop up message
     * showed up and asked for consent. If they want to remove the selected
     * medication, then can or they can cancle it and leave the selection as it
     * is.
     * 
     * @param diaryString JSON string object containing data related to diary page.
     * @return none
     * @throws none
     * @since 1.0
     * @author Mahbubur Rahman
     */
    private void bindExistingDataToViews(String diaryString)
    {
	Gson gson = new Gson();
	new JsonObject();
	JSONObject requestObj = null;
	String todaydiary;
	DiaryData ddata;
	// mode = DiaryLoadMode.EDIT;
	try
	{
	    requestObj = new JSONObject(diaryString);
	    todaydiary = requestObj.get("diary").toString();
	    ddata = gson.fromJson(todaydiary, DiaryData.class);

	    if (ddata.getWheezing().equalsIgnoreCase(this.getResources().getString(R.string.quest_yes)))
	    {
		((RadioButton) findViewById(R.id.rbWheezing0)).setChecked(true);

	    }
	    else if (ddata.getWheezing().equalsIgnoreCase(this.getResources().getString(R.string.quest_no)))
	    {
		((RadioButton) findViewById(R.id.rbWheezing1)).setChecked(true);

	    }

	    if (ddata.getCoughing().equalsIgnoreCase(this.getResources().getString(R.string.quest_yes)))
	    {
		((RadioButton) findViewById(R.id.rbCoughing0)).setChecked(true);

	    }
	    else if (ddata.getCoughing().equalsIgnoreCase(this.getResources().getString(R.string.quest_no)))
	    {
		((RadioButton) findViewById(R.id.rbCoughing1)).setChecked(true);

	    }

	    if (ddata.getShortBreath().equalsIgnoreCase(this.getResources().getString(R.string.quest_yes)))
	    {
		((RadioButton) findViewById(R.id.rbShortBreath0)).setChecked(true);

	    }
	    else if (ddata.getShortBreath().equalsIgnoreCase(this.getResources().getString(R.string.quest_no)))
	    {
		((RadioButton) findViewById(R.id.rbShortBreath1)).setChecked(true);

	    }

	    if (ddata.getChestTightness().equalsIgnoreCase(this.getResources().getString(R.string.quest_yes)))
	    {
		((RadioButton) findViewById(R.id.rbChestTightness0)).setChecked(true);

	    }
	    else if (ddata.getChestTightness().equalsIgnoreCase(this.getResources().getString(R.string.quest_no)))
	    {
		((RadioButton) findViewById(R.id.rbChestTightness1)).setChecked(true);

	    }

	    if (ddata.getChestPain().equalsIgnoreCase(this.getResources().getString(R.string.quest_yes)))
	    {
		((RadioButton) findViewById(R.id.rbChestPain0)).setChecked(true);

	    }
	    else if (ddata.getChestPain().equalsIgnoreCase(this.getResources().getString(R.string.quest_no)))
	    {
		((RadioButton) findViewById(R.id.rbChestPain1)).setChecked(true);

	    }
	    if (ddata.getFever().equalsIgnoreCase(this.getResources().getString(R.string.quest_yes)))
	    {
		((RadioButton) findViewById(R.id.rbFever0)).setChecked(true);

	    }

	    else if (ddata.getFever().equalsIgnoreCase(this.getResources().getString(R.string.quest_no)))
	    {
		((RadioButton) findViewById(R.id.rbFever1)).setChecked(true);

	    }
	    if (ddata.getNausea().equalsIgnoreCase(this.getResources().getString(R.string.quest_yes)))
	    {
		((RadioButton) findViewById(R.id.rbNauseaVomiting0)).setChecked(true);

	    }

	    else if (ddata.getNausea().equalsIgnoreCase(this.getResources().getString(R.string.quest_no)))
	    {
		((RadioButton) findViewById(R.id.rbNauseaVomiting1)).setChecked(true);

	    }

	    if (ddata.getRunnyNose().equalsIgnoreCase(this.getResources().getString(R.string.quest_yes)))
	    {
		((RadioButton) findViewById(R.id.rbRunnyNose0)).setChecked(true);

	    }
	    else if (ddata.getRunnyNose().equalsIgnoreCase(this.getResources().getString(R.string.quest_no)))
	    {
		((RadioButton) findViewById(R.id.rbRunnyNose1)).setChecked(true);

	    }

	    if (ddata.getOther().equalsIgnoreCase(this.getResources().getString(R.string.quest_yes)))
	    {
		((RadioButton) findViewById(R.id.rbOther0)).setChecked(true);

	    }
	    else if (ddata.getOther().equalsIgnoreCase(this.getResources().getString(R.string.quest_no)))
	    {
		((RadioButton) findViewById(R.id.rbOther1)).setChecked(true);

	    }

	    if (ddata.getMissSchoolWork().equalsIgnoreCase(this.getResources().getString(R.string.quest_yes)))
	    {
		((RadioButton) findViewById(R.id.rbMissSchoolWork0)).setChecked(true);

	    }
	    else if (ddata.getMissSchoolWork().equalsIgnoreCase(this.getResources().getString(R.string.quest_no)))
	    {
		((RadioButton) findViewById(R.id.rbMissSchoolWork1)).setChecked(true);

	    }

	    if (ddata.getWake().equalsIgnoreCase(this.getResources().getString(R.string.quest_yes)))
	    {
		((RadioButton) findViewById(R.id.rbBreathingCoughingMidNight0)).setChecked(true);

	    }
	    else if (ddata.getWake().equalsIgnoreCase(this.getResources().getString(R.string.quest_no)))
	    {
		((RadioButton) findViewById(R.id.rbBreathingCoughingMidNight1)).setChecked(true);

	    }
	    if (ddata.getExercise().equalsIgnoreCase(this.getResources().getString(R.string.quest_yes)))
	    {
		((RadioButton) findViewById(R.id.rbExercise0)).setChecked(true);

	    }
	    else if (ddata.getExercise().equalsIgnoreCase(this.getResources().getString(R.string.quest_no)))
	    {
		((RadioButton) findViewById(R.id.rbExercise1)).setChecked(true);

	    }
	    if (ddata.getSmoke().equalsIgnoreCase(this.getResources().getString(R.string.quest_yes)))
	    {
		((RadioButton) findViewById(R.id.rbSmoke0)).setChecked(true);

	    }
	    else if (ddata.getSmoke().equalsIgnoreCase(this.getResources().getString(R.string.quest_no)))
	    {
		((RadioButton) findViewById(R.id.rbSmoke1)).setChecked(true);

	    }
	    if (ddata.getPeopleSmoking().equalsIgnoreCase(this.getResources().getString(R.string.quest_yes)))
	    {
		((RadioButton) findViewById(R.id.rbPeopleSmoking0)).setChecked(true);

	    }
	    else if (ddata.getPeopleSmoking().equalsIgnoreCase(this.getResources().getString(R.string.quest_no)))
	    {
		((RadioButton) findViewById(R.id.rbPeopleSmoking1)).setChecked(true);

	    }
	    if (ddata.getCaffein().equalsIgnoreCase(this.getResources().getString(R.string.quest_yes)))
	    {
		((RadioButton) findViewById(R.id.rbDrinkCaffein0)).setChecked(true);

	    }
	    else if (ddata.getCaffein().equalsIgnoreCase(this.getResources().getString(R.string.quest_no)))
	    {
		((RadioButton) findViewById(R.id.rbDrinkCaffein1)).setChecked(true);

	    }
	    if (ddata.getStress().equalsIgnoreCase(this.getResources().getString(R.string.BDD_QST_Stress1)))
	    {
		((RadioButton) findViewById(R.id.rbDailyStress0)).setChecked(true);

	    }
	    else if (ddata.getStress().equalsIgnoreCase(this.getResources().getString(R.string.BDD_QST_Stress2)))
	    {
		((RadioButton) findViewById(R.id.rbDailyStress1)).setChecked(true);

	    }
	    else if (ddata.getStress().equalsIgnoreCase(this.getResources().getString(R.string.BDD_QST_Stress3)))
	    {
		((RadioButton) findViewById(R.id.rbDailyStress2)).setChecked(true);

	    }

	    // Get the medication list selected by the user for today
	    ArrayList<Integer> medlist = getSelectedMedicationForToday();
	    if (medlist.size() == 0)
	    {
		((RadioButton) findViewById(R.id.rbMedication0)).setChecked(false);
		((RadioButton) findViewById(R.id.rbMedication1)).setChecked(false);
	    }
	    else
	    {
		if (ddata.getPescription().equalsIgnoreCase(this.getResources().getString(R.string.quest_yes)))
		{
		    ((RadioButton) findViewById(R.id.rbMedication0)).setChecked(true);

		}
		else if (ddata.getPescription().equalsIgnoreCase(this.getResources().getString(R.string.quest_no)))
		{
		    ((RadioButton) findViewById(R.id.rbMedication1)).setChecked(true);

		}
	    }
	    // Get the medication list selected by the user for today
	    ArrayList<Integer> suppList = getSelectedSupplementForToday();
	    if (suppList.size() == 0)
	    {
		((RadioButton) findViewById(R.id.rbOTC0)).setChecked(false);
		((RadioButton) findViewById(R.id.rbOTC1)).setChecked(false);
	    }
	    else
	    {
		if (ddata.getOTC().equalsIgnoreCase(this.getResources().getString(R.string.quest_yes)))
		{
		    ((RadioButton) findViewById(R.id.rbOTC0)).setChecked(true);

		}
		else if (ddata.getOTC().equalsIgnoreCase(this.getResources().getString(R.string.quest_no)))
		{
		    ((RadioButton) findViewById(R.id.rbOTC1)).setChecked(true);

		}
	    }

	    mEtOther.setText(ddata.getEtOther());
	    selectSpinnerItemByValue(mTimesWoke, ddata.getTimesWoke());
	    selectSpinnerItemByValue(mMinIndoorsExercise, ddata.getMinIndoorsExercise());
	    selectSpinnerItemByValue(mMinOutdoorsExercise, ddata.getMinOutdoorsExercise());
	    selectSpinnerItemByValue(mNumSmoke, ddata.getNumSmoke());
	    mDurationSlept.setText(CalendarUtils.getTimeHHMM(ddata.getHrsSlept(), ddata.getMinSlept()));
	    mDurationSitting.setText(CalendarUtils.getTimeHHMM(ddata.getHrsSitting(), ddata.getMinSlept()));
	    mDurationVehicle.setText(CalendarUtils.getTimeHHMM(ddata.getHrsVehicle(), ddata.getMinVehicle()));
	    mDurationSpentOutdoors.setText(CalendarUtils.getTimeHHMM(ddata.getHrsOutdoorsTimeSpent(), ddata.getMinOutdoorsTimeSpent()));

	    if (this.IS_NITRO_VISIBLE)
		mNitricOxide.setText(ddata.getNitricOxide());

	} catch (JSONException e)
	{
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

    
    /**
     * The static method selectSpinnerItemByValue(Spinner spnr, String value) helps to find the index from the spinner adapter for the current selection.
     * As the data the is retrieved from the database is not index but the value and in spinner data are handled by index, this function take
     * the value and find out the index from the adapter and based on that, it selects that index to bind the spinner view. 
     * 
     * @param spnr Spinner object {@link android.Widget#Spinner} for which the method need to find the index.
     * @param value is the value that is retrieved from the database as selected in previous entry. The index of this value have to be found out by this mehtod.
     * @return none
     * @throws none
     * @since 1.0
     * @author Mahbubur Rahman
     */
    public static void selectSpinnerItemByValue(Spinner spnr, String value)
    {
	@SuppressWarnings("unchecked")
	ArrayAdapter<String> adapter = (ArrayAdapter<String>) spnr.getAdapter();
	for (int position = 0; position < adapter.getCount(); position++)
	{
	    if (adapter.getItem(position).equals(value))
	    // if(adapter.getItemId(position) == value)
	    {
		spnr.setSelection(position);
		return;
	    }
	}
    }

    /**
     * viewControl method will control what to show and what not to show of the
     * diary page
     * 
     * <p>
     * Diary Data: if data is present with status I for today then nothing to
     * do, view all and have access to all items of diary if data is present but
     * status is not I, then hide all the views of the diary page.
     * 
     * if data is not present today, then calculate lasttimecollection+frequency
     * of data collection*day. if the time is today and current time is within
     * the window set in form_content settings, then make visible all the views.
     * 
     * <p>
     * Peak Flow: all the procedure is same as above and the peak flow button
     * will be set visible or hide but this will not be affected by complete
     * button.
     * <p>
     * Nitro information: same as before but this will also not be affected by
     * the submit button.
     * 
     * @param none
     * @return none
     * 
     * @throws none
     * @since 1.0
     * @author Mahbubur Rahman
     */
    private void isVisibleNow()
    {

	String[] PROJECTION_COLUMNS = { FormContentData.FormContentItems.COLUMN_NAME_FORM_NAME, FormContentData.FormContentItems.COLUMN_NAME_FREQ,
	        FormContentData.FormContentItems.COLUMN_NAME_LAST_COLLECTED_TIME, FormContentData.FormContentItems.COLUMN_NAME_WINDOW_START,
	        FormContentData.FormContentItems.COLUMN_NAME_WINDOW_END, FormContentData.FormContentItems.COLUMN_NAME_TIME_CATEGORY };
	int FORM_NAME = 0, FREQ = 1, LAST_COLLECTION = 2, WINDOW_START = 3, WINDOW_END = 4;
	Cursor cursor = this.getContentResolver().query(FormContentData.FormContentItems.CONTENT_URI, PROJECTION_COLUMNS, null, null, null);
	int freq = 0;
	Calendar cur_time = Calendar.getInstance();
	Calendar lastcollected = Calendar.getInstance();
	Calendar starttime = Calendar.getInstance();
	Calendar endtime = Calendar.getInstance();
	if (cursor != null)
	{
	    cursor.moveToFirst();
	    while (!cursor.isAfterLast())
	    {
		lastcollected = Calendar.getInstance();
		freq = cursor.getInt(FREQ);
		if (freq < 0)
		{
		    Log.d(TAG, "frequency is neqative");
		    throw new IllegalArgumentException();
		}

		// lastcollected.setTimeInMillis(cursor.getLong(LAST_COLLECTION));
		lastcollected = CalendarUtils.dateString2Calendaryyyymmddhhmmss(cursor.getString(LAST_COLLECTION));
		// add number of day*frequency for repeated alarm
		lastcollected.add(Calendar.DAY_OF_YEAR, freq);
		starttime.set(Calendar.HOUR_OF_DAY, TimePickerPreference.getHour(cursor.getString(WINDOW_START)));
		starttime.set(Calendar.MINUTE, TimePickerPreference.getMinute(cursor.getString(WINDOW_START)));
		starttime.set(Calendar.SECOND, 0);
		endtime.set(Calendar.HOUR_OF_DAY, TimePickerPreference.getHour(cursor.getString(WINDOW_END)));
		endtime.set(Calendar.MINUTE, TimePickerPreference.getMinute(cursor.getString(WINDOW_END)));
		endtime.set(Calendar.SECOND, 0);

		if (cursor.getString(FORM_NAME).equals(DatabaseConstants.FORM_CONTENT_ID_DIARY))
		{
		    if ((CalendarUtils.isSameDay(lastcollected, cur_time) || CalendarUtils.isEarlier(lastcollected, cur_time))
			    && CalendarUtils.isInTimeWindow(starttime, endtime))
		    {

		    }
		}
		else if (cursor.getString(FORM_NAME).equals(DatabaseConstants.FORM_CONTENT_ID_NITRO))
		{
		    if ((CalendarUtils.isSameDay(lastcollected, cur_time) || CalendarUtils.isEarlier(lastcollected, cur_time))
			    && CalendarUtils.isInTimeWindow(starttime, endtime))
		    {
			this.IS_NITRO_VISIBLE = true;

		    }
		}
		else if (cursor.getString(FORM_NAME).equals(DatabaseConstants.FORM_CONTENT_ID_AM_FLOW))
		{
		    if ((CalendarUtils.isSameDay(lastcollected, cur_time) || CalendarUtils.isEarlier(lastcollected, cur_time))
			    && CalendarUtils.isInTimeWindow(starttime, endtime))
		    {
			this.IS_AM_PEAK_FLOW_VISIBLE = true;
			this.PEAK_FLOW_CATEGORY_NOW = PEAK_FLOW_CATEGORY.AM;

		    }
		}
		else if (cursor.getString(FORM_NAME).equals(DatabaseConstants.FORM_CONTENT_ID_PM_FLOW))
		{
		    if ((CalendarUtils.isSameDay(lastcollected, cur_time) || CalendarUtils.isEarlier(lastcollected, cur_time))
			    && CalendarUtils.isInTimeWindow(starttime, endtime))
		    {
			this.IS_AM_PEAK_FLOW_VISIBLE = true;
			this.PEAK_FLOW_CATEGORY_NOW = PEAK_FLOW_CATEGORY.PM;

		    }
		}
		cursor.moveToNext();
	    }
	}
	cursor.close();
	// this.notificationMsg = notificationMessage;
	// return notifyDiary || notifyNitro || notifyAMFlow || notifyPMFlow;
    }

    /**
     * The method statusUpdatetoReady() helps to update the data status in database. The sync process {@link com.emmes.aps.DiaryActivity#syncDiaryData} try to sync
     * data which does not need to be changed or there is no option for the user to change. This means that the usr already have agreed to submit the data
     * or the day already have passed. This time, this function will be called to update the database data status from incomplete to ready state. This will
     * enable the sync process to consider these data for transfer to server.
     * 
     * @param none
     * @return none
     * @throws none
     * @since 1.0
     * @author Mahbubur Rahman
     */
    public void statusUpdatetoReady()
    {
	ContentValues values = new ContentValues();

	values.clear();
	values.put(DailyDiaryDB.DailyDiaryItem.COLUMN_NAME_STATUS, DataTransferUtils.STATUS_DATA_TRANSFER_READY);
	String where = DailyDiaryDB.DailyDiaryItem.COLUMN_NAME_STATUS + "='" + DataTransferUtils.STATUS_DATA_TRANSFER_INCOMPLETE + "'";
	// String[] args = new String[] { Integer.toString(med_id) };
	int numofrowupdated = getContentResolver().update(DailyDiaryDB.DailyDiaryItem.CONTENT_URI, values, where, null);
	Log.d(TAG, numofrowupdated + " rows of daily diary updated with status S");

	values.clear();
	values.put(DailyMedication.MedicationItem.COLUMN_NAME_STATUS, DataTransferUtils.STATUS_DATA_TRANSFER_READY);
	where = DailyMedication.MedicationItem.COLUMN_NAME_STATUS + "='" + DataTransferUtils.STATUS_DATA_TRANSFER_INCOMPLETE + "'";
	// String[] args = new String[] { Integer.toString(med_id) };
	numofrowupdated = getContentResolver().update(DailyMedication.MedicationItem.CONTENT_URI, values, where, null);
	Log.d(TAG, numofrowupdated + " rows of DailyMedication updated with status S");

	values.clear();
	values.put(PeakFlowDB.PeakFlowItem.COLUMN_NAME_STATUS, DataTransferUtils.STATUS_DATA_TRANSFER_READY);
	where = PeakFlowDB.PeakFlowItem.COLUMN_NAME_STATUS + "='" + DataTransferUtils.STATUS_DATA_TRANSFER_INCOMPLETE + "'";
	// String[] args = new String[] { Integer.toString(med_id) };
	numofrowupdated = getContentResolver().update(PeakFlowDB.PeakFlowItem.CONTENT_URI, values, where, null);
	Log.d(TAG, numofrowupdated + " rows of peakflow updated with status S");

	values.clear();
	values.put(LocationTrackingData.LocationPoint.COLUMN_NAME_STATUS, DataTransferUtils.STATUS_DATA_TRANSFER_READY);
	where = LocationTrackingData.LocationPoint.COLUMN_NAME_STATUS + "='" + DataTransferUtils.STATUS_DATA_TRANSFER_INCOMPLETE + "'";
	// String[] args = new String[] { Integer.toString(med_id) };
	numofrowupdated = getContentResolver().update(LocationTrackingData.LocationPoint.CONTENT_URI, values, where, null);
	Log.d(TAG, numofrowupdated + " rows of LocationTrackingData updated with status S");
    }

    /**
     * <p>
     * This method is for tracking the diary entry. If the user already have
     * submittend all the records or information and then again come back to the
     * diary, they should be keep away from doing this and inform the user
     * explaining the cause.This method create a {@line AlertDialog} which shows
     * the user with custom message of thank you for today and let them invite
     * for next day data entry.
     * </p>
     * 
     * @param none
     * @return the country information
     * 
     * @throws none
     * @since 1.0
     * @author Mahbubur Rahman
     * 
     */
    public void showThanksDialog()
    {
	AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DiaryActivity.this);

	// set title
	alertDialogBuilder.setTitle(this.getResources().getString(R.string.Thanks_msg_title));

	// set dialog message
	alertDialogBuilder.setMessage(this.getResources().getString(R.string.Thanks_msg_body)).setCancelable(false)
	        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
		    @Override
			public void onClick(DialogInterface dialog, int id)
		    {
		        // if this button is clicked, close
		        // current activity
		        Intent intent = new Intent(DiaryActivity.this, MainActivity.class);
		        startActivity(intent);
		        // DiaryActivity.this.finish();
		    }
	        });

	// create alert dialog
	AlertDialog alertDialog = alertDialogBuilder.create();

	// show it
	alertDialog.show();
    }

    /**
     * <p>
     * This method is for finding out from the local database about what already
     * have the user selected today. This case arise while the user previously
     * visited this activity and have selected medication. But later s/he may
     * see what he has selected and if anything wrong, s/he can update the list.
     * </p>
     * 
     * @param none
     * 
     * 
     * @throws none
     * @since 1.0
     * @author Mahbubur Rahman
     * @return selected medicationlist
     */
    private ArrayList<Integer> getSelectedSupplementForToday()
    {
	ArrayList<Integer> med_ids = new ArrayList<Integer>();
	String[] projection_columns = new String[3];
	projection_columns[0] = BaseColumns._ID;
	projection_columns[1] = DailyMedication.MedicationItem.COLUMN_NAME_LASTTAKEN;
	projection_columns[2] = DailyMedication.MedicationItem.COLUMN_NAME_MEDICATION_ID;

	int MED_ID = 2;
	String selection = DailyMedication.MedicationItem.COLUMN_NAME_TYPE + "=? AND " + DailyMedication.MedicationItem.COLUMN_NAME_LASTTAKEN + "=?";
	String[] selArgs = { DatabaseConstants.ITEM_TYPE_SUPPLEMENT, CalendarUtils.calTimeToDateStringyyyymmdd(Calendar.getInstance()) };
	Cursor cursor = getContentResolver().query(DailyMedication.MedicationItem.CONTENT_URI, projection_columns, selection, selArgs, null);
	// Calendar d = Calendar.getInstance();
	// Calendar dcurrent = Calendar.getInstance();
	// boolean sameDay = false;
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
     * This method is for finding out from the local database about what already
     * have the user selected today. This case arise while the user previously
     * visited this activity and have selected medication. But later s/he may
     * see what he has selected and if anything wrong, s/he can update the list.
     * </p>
     * @param none
     * 
     * @throws none
     * @since 1.0
     * @author Mahbubur Rahman
     * @return selected medicationlist
     */
    private ArrayList<Integer> getSelectedMedicationForToday()
    {
	ArrayList<Integer> med_ids = new ArrayList<Integer>();
	String[] projection_columns = new String[3];
	projection_columns[0] = BaseColumns._ID;
	projection_columns[1] = DailyMedication.MedicationItem.COLUMN_NAME_LASTTAKEN;
	projection_columns[2] = DailyMedication.MedicationItem.COLUMN_NAME_MEDICATION_ID;

	int MED_ID = 2;
	String selection = DailyMedication.MedicationItem.COLUMN_NAME_TYPE + "=? AND " + DailyMedication.MedicationItem.COLUMN_NAME_LASTTAKEN + "=?";
	String[] selArgs = { DatabaseConstants.ITEM_TYPE_MEDICATION, CalendarUtils.calTimeToDateStringyyyymmdd(Calendar.getInstance()) };
	Cursor cursor = getContentResolver().query(DailyMedication.MedicationItem.CONTENT_URI, projection_columns, selection, selArgs, null);
	// Calendar d = Calendar.getInstance();
	// Calendar dcurrent = Calendar.getInstance();
	// boolean sameDay = false;
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

    /* (non-Javadoc)
     * 
     * @see android.app.Activity#onRestart() */
    @Override
    protected void onRestart()
    {

	mApplicationPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
	if (mApplicationPrefs.getBoolean(this.getResources().getString(R.string.spref_is_application_background), false))
	{
	    Log.d(TAG, "application is in background and going to PIN activity on restart");

	    onBackPressed();
	}
	// TODO Auto-generated method stub
	super.onRestart();
    }

    /**
     * <p>
     * This method is for creating dialog when the user trying to delete
     * medication list by selecting no from diary page. This occurs when the
     * user first select yes for medication and go to the medication section.
     * There they have selected the medication but later dicides that they have
     * not taken any medicatin and want to modify the list. So for being sure,
     * the system asks, whether the user really want to delete or its just a
     * mistake to press the no button.
     * 
     * @param none
     * @throws none
     * @since 1.0
     * @author Mahbubur Rahman
     * @return the alert dialog
     */
    public AlertDialog warnDeleteSelectedMedication()
    {
	AlertDialog myQuittingDialogBox = new AlertDialog.Builder(this.mContext)
	        // set message, title, and icon
	        .setTitle(getApplicationContext().getResources().getString(R.string.diary_warn_delete_selected_medication_title))
	        .setMessage(getApplicationContext().getResources().getString(R.string.diary_warn_delete_selected_medication_message))
	        .setPositiveButton(getApplicationContext().getResources().getString(R.string.pop_up_delete_positive_text),
	                new DialogInterface.OnClickListener() {

		            @Override
					public void onClick(DialogInterface dialog, int whichButton)
		            {
		                // Do something in response to the click
		                getContentResolver().delete(
		                        DailyMedication.MedicationItem.CONTENT_URI,
		                        DailyMedication.MedicationItem.COLUMN_NAME_TYPE + "='" + DatabaseConstants.ITEM_TYPE_MEDICATION + "' AND "
		                                + DailyMedication.MedicationItem.COLUMN_NAME_LASTTAKEN + "='"
		                                + CalendarUtils.calTimeToDateStringyyyymmdd(Calendar.getInstance()) + "'", null);
		                Log.d(TAG, "Removed all medication today");

		                LinearLayout llMedicationBack = (LinearLayout) findViewById(R.id.llSYMedicationBack);
		                llMedicationBack.setVisibility(android.view.View.GONE);
		                // rbMedNo.setSelected(true);
		                dialog.dismiss();
		                // finish();
		            }

	                })

	        .setNegativeButton(getApplicationContext().getResources().getString(R.string.pop_up_cancel_text),
	                new DialogInterface.OnClickListener() {
		            @Override
					public void onClick(DialogInterface dialog, int which)
		            {

		                RadioButton rbMedYes = (RadioButton) findViewById(R.id.rbMedication0);
		                rbMedYes.setChecked(true);
		                dialog.dismiss();

		            }
	                }).create();
	return myQuittingDialogBox;
    }

    /**
     * <p>
     * This method is for creating dialog when the user trying to delete
     * supplement list by selecting no from diary page. This occurs when the
     * user first select yes for supplement and go to the supplement section.
     * There they have selected the supplement but later decides that they have
     * not taken any supplement and want to modify the list. So for being sure,
     * the system asks, whether the user really want to delete or its just a
     * mistake to press the no button.
     * 
     * @param none
     * @throws none
     * @since 1.0
     * @author Mahbubur Rahman
     * @return the alert dialog
     */
    public AlertDialog warnDeleteSelectedSupplement()
    {
	AlertDialog myQuittingDialogBox = new AlertDialog.Builder(this.mContext)
	        // set message, title, and icon
	        .setTitle(getApplicationContext().getResources().getString(R.string.diary_warn_delete_selected_supplement_title))
	        .setMessage(getApplicationContext().getResources().getString(R.string.diary_warn_delete_selected_supplement_message))
	        .setPositiveButton(getApplicationContext().getResources().getString(R.string.pop_up_delete_positive_text),
	                new DialogInterface.OnClickListener() {

		            @Override
					public void onClick(DialogInterface dialog, int whichButton)
		            {
		                // Do something in response to the click
		                getContentResolver().delete(
		                        DailyMedication.MedicationItem.CONTENT_URI,
		                        DailyMedication.MedicationItem.COLUMN_NAME_TYPE + "='" + DatabaseConstants.ITEM_TYPE_SUPPLEMENT + "' AND "
		                                + DailyMedication.MedicationItem.COLUMN_NAME_LASTTAKEN + "='"
		                                + CalendarUtils.calTimeToDateStringyyyymmdd(Calendar.getInstance()) + "'", null);
		                Log.d(TAG, "Removed all medication today");

		                LinearLayout llMedicationBack = (LinearLayout) findViewById(R.id.llSYOTCBack);
		                llMedicationBack.setVisibility(android.view.View.GONE);
		                // rbMedNo.setSelected(true);
		                dialog.dismiss();
		                // finish();
		            }

	                })

	        .setNegativeButton(getApplicationContext().getResources().getString(R.string.pop_up_cancel_text),
	                new DialogInterface.OnClickListener() {
		            @Override
					public void onClick(DialogInterface dialog, int which)
		            {

		                RadioButton rbSupYes = (RadioButton) findViewById(R.id.rbOTC0);
		                rbSupYes.setChecked(true);
		                dialog.dismiss();

		            }
	                }).create();
	return myQuittingDialogBox;
    }

    /* (non-Javadoc)
     * 
     * @see android.app.Activity#onBackPressed() */
    @Override
	public void onBackPressed()
    {
	Intent intent = new Intent(DiaryActivity.this, MainActivity.class);
	intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	intent.putExtra("EXIT", true);
	startActivity(intent);
    }
}
