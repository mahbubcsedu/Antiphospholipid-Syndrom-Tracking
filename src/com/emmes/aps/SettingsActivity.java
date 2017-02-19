/* 
  * Copyright 2014 (C) The EMMES Corporation 
  *  
  * Created on : 02-06-2014
  * Last Update: Aug 14, 2014 4:00:34 PM
  * Author     : Mahbubur Rahman
  * Title      : Summer intern 2014 
  * Project    : Daily Diary Android Application
  * 
  */
package com.emmes.aps;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import com.emmes.aps.data.FormContentData;
import com.emmes.aps.locationtracking.LocationUtils;
import com.emmes.aps.util.CalendarUtils;
import com.emmes.aps.util.NotificationUtils;

import android.app.AlarmManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.util.Log;
//import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

// TODO: Auto-generated Javadoc
/**
 * A {@link PreferenceActivity} that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 * <p>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */
public class SettingsActivity extends NavigationActivity
{

    /** The Constant TAG. */
    private static final String TAG = "SettingsActivity";
    
    /** The show static. */
    private static boolean showStatic = false;
    
    /** The m alarms. */
    private static AlarmManager mAlarms;
    
    /** The s context. */
    static Context sContext;
    
    /** The m last loc prefs. */
    static SharedPreferences mLastLocPrefs;
    
    /** The m editor. */
    static SharedPreferences.Editor mEditor;
    
    /** The m prefs. */
    SharedPreferences mPrefs;
    /** The sharedpreferences object to deal with shared preferences. */
    SharedPreferences mApplicationPrefs;
    
    /**
     * mEditor is an instance of SharedPreference.Editor which is used for
     * editing the sharedpreferences in the application @see SharedPreferences.
     *
     * @param savedInstanceState the saved instance state
     */
   // SharedPreferences.Editor mEditor;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
	super.onCreate(savedInstanceState);

	sContext = this;
	mAlarms = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
	mPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
	if (mPrefs.getString(this.getResources().getString(R.string.spref_subject_id), "").equals("")
	        || mPrefs.getString(this.getResources().getString(R.string.spref_enrollment_code), "").equals("")
	        || mPrefs.getString(sContext.getResources().getString(R.string.device_date_issue), "").equals(""))
	    showStatic = true;

	// Display the fragment as the main content.
	getFragmentManager().beginTransaction().replace(android.R.id.content, new SettingsFragment()).commit();

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
	    Log.d(TAG, "application is in background  and goind to PIN activity on restart");

	    onReturnFromHome();
	}
	// TODO Auto-generated method stub
	super.onRestart();
    }

    /**
     * On return from home.
     */
    public void onReturnFromHome()
    {
	Intent intent = new Intent(this, MainActivity.class);
	intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	intent.putExtra("EXIT", true);
	startActivity(intent);
    }

    /* (non-Javadoc)
     * @see com.emmes.aps.NavigationActivity#onOptionsItemSelected(android.view.MenuItem)
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
	switch (item.getItemId())
	{
	case android.R.id.home:
	    NavUtils.navigateUpFromSameTask(this);
	    return true;
	}
	return super.onOptionsItemSelected(item);
    }

    /* (non-Javadoc)
     * @see android.app.Activity#onBackPressed()
     */
    @Override
    public void onBackPressed()
    {
	if (nopin())
	{
	    Toast toast = Toast.makeText(this, SettingsActivity.this.getResources().getString(R.string.pin_enter_request_text), Toast.LENGTH_LONG);
	    toast.show();
	    Intent intent = new Intent(this, MainActivity.class);
	    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	    intent.putExtra("EXIT", true);
	    startActivity(intent);
	}
	Log.i(TAG, "pin");
	Intent intent = new Intent(this, MainActivity.class);
	intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	intent.putExtra("EXIT", false);
	intent.putExtra(LocationUtils.DIRECTING_FROM_LOCATION_RECEIVER_FOR_CONSENT, "no");
	startActivity(intent);
	// super.onBackPressed();
    }

    /**
     * Nopin.
     *
     * @return true, if successful
     */
    private boolean nopin()
    {
	boolean pinfound = true;
	SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
	if (prefs.getString(this.getResources().getString(R.string.spref_pref_pin), "").equals(""))
	    pinfound = false;
	return !pinfound;
    }

    /**
     * Finish settings.
     *
     * @param v the v
     */
    public void finishSettings(View v)
    {
	onBackPressed();
    }

    

    /**
     * A preference value change listener that updates the preference's summary
     * to reflect its new value.
     */
    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
	@Override
	public boolean onPreferenceChange(Preference preference, Object value)
	{
	    String stringValue = value.toString();
	    /* LocationAlarmReceiver aReceiver=new LocationAlarmReceiver();
	     * aReceiver.CancelRecurringAlarm(sContext);
	     * Log.d(TAG,"Recurring alarm cancelled"); */

	    /* if (stringValue.equals("Yes")) {
	     * 
	     * 
	     * } else { // stopRecurringAlarm(); } */
	    /* Just for testing purpose */
	    mLastLocPrefs = PreferenceManager.getDefaultSharedPreferences(SettingsActivity.sContext);
	    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm", Locale.US);
	    mEditor = mLastLocPrefs.edit();
	    Calendar testcal = Calendar.getInstance();
	    testcal.add(Calendar.MINUTE, 2);
	    mEditor.putString(sContext.getResources().getString(R.string.am_alarm_time), sdf.format(testcal.getTime()));
	    testcal.add(Calendar.MINUTE, 2);
	    mEditor.putString(sContext.getResources().getString(R.string.pm_alarm_time), sdf.format(testcal.getTime()));
	    mEditor.commit();
	    // update last collection to some previous day
	    ContentValues values = new ContentValues();

	    Calendar currenttime = Calendar.getInstance();
	    currenttime.add(Calendar.DAY_OF_YEAR, -1);
	    // currenttime.set(currenttime.DAY_OF_YEAR,currenttime.DAY_OF_YEAR-2);

	    // values.put(MedicationList.MedicationItem._ID, med_id);
	    values.put(FormContentData.FormContentItems.COLUMN_NAME_LAST_COLLECTED_TIME, CalendarUtils.calTimeToDateStringyyyymmddhhmmss(currenttime));

	    // String where = MedicationList.MedicationItem._ID+"="+med_id;
	    // String[] args = new String[] { Integer.toString(med_id) };
	    int numofrowdeleted = sContext.getContentResolver().update(FormContentData.FormContentItems.CONTENT_URI, values, null, null);
	    Log.d(TAG, numofrowdeleted + " rows updated");
	    /**
	     * commented out after development and testing done
	     */

	    if (preference.getKey().equals(SettingsActivity.sContext.getResources().getString(R.string.am_alarm_time)))
	    {
		ReminderNotification.setRepeatingReminder(SettingsActivity.sContext, NotificationUtils.REMINDER_NOTIFICATION_MORNING_CODE);
		Log.v(TAG, preference.getKey());
	    }
	    else if (preference.getKey().equals(SettingsActivity.sContext.getResources().getString(R.string.pm_alarm_time)))
	    {
		ReminderNotification.setRepeatingReminder(SettingsActivity.sContext, NotificationUtils.REMINDER_NOTIFICATION_EVENING_CODE);

		Log.v(TAG, preference.getKey());
	    }

	    if (preference instanceof ListPreference)
	    {
		// For list preferences, look up the correct display value in
		// the preference's 'entries' list.
		ListPreference listPreference = (ListPreference) preference;
		int index = listPreference.findIndexOfValue(stringValue);

		// Set the summary to reflect the new value.
		preference.setSummary(index >= 0 ? listPreference.getEntries()[index] : null);

	    }
	    else
	    {
		// For all other preferences, set the summary to the value's
		// simple string representation.
		preference.setSummary(stringValue);
	    }
	    return true;
	}
    };

    /* (non-Javadoc)
     * 
     * @see android.app.Activity#onDestroy() */
    @Override
    protected void onDestroy()
    {
	// TODO Auto-generated method stub
	super.onDestroy();
	Log.d(TAG, "Settings destroy");
    }

    /**
     * Binds a preference's summary to its value. More specifically, when the
     * preference's value is changed, its summary (line of text below the
     * preference title) is updated to reflect the value. The summary is also
     * immediately updated upon calling this method. The exact display format is
     * dependent on the type of preference.
     *
     * @param preference the preference
     * @see #sBindPreferenceSummaryToValueListener
     */
    private static void bindPreferenceSummaryToValue(Preference preference)
    {
	// Set the listener to watch for value changes.
	preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

	// Trigger the listener immediately with the preference's
	// current value.
	sBindPreferenceSummaryToValueListener.onPreferenceChange(preference, PreferenceManager.getDefaultSharedPreferences(preference.getContext())
	        .getString(preference.getKey(), ""));

    }

    /**
     * The Class SettingsFragment.
     */
    public static class SettingsFragment extends PreferenceFragment
    {

	/* (non-Javadoc)
	 * @see android.preference.PreferenceFragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
	    super.onCreate(savedInstanceState);

	    // Load the preferences from an XML resource
	    addPreferencesFromResource(R.xml.pref_general);
	    bindPreferenceSummaryToValue(findPreference(this.getResources().getString(R.string.spref_question_list1)));
	    bindPreferenceSummaryToValue(findPreference(this.getResources().getString(R.string.spref_question_list2)));
	    // bindPreferenceSummaryToValue(findPreference("alarm_morning"));
	    bindPreferenceSummaryToValue(findPreference(this.getResources().getString(R.string.am_alarm_time)));
	    bindPreferenceSummaryToValue(findPreference(this.getResources().getString(R.string.pm_alarm_time)));
	    // bindPreferenceSummaryToValue(findPreference("alarm_evening"));
	    bindPreferenceSummaryToValue(findPreference(this.getResources().getString(R.string.spref_pref_consent)));

	    if (showStatic)
	    {
		addPreferencesFromResource(R.xml.pref_static);
		bindPreferenceSummaryToValue(findPreference(this.getResources().getString(R.string.spref_subject_id)));
		bindPreferenceSummaryToValue(findPreference(this.getResources().getString(R.string.spref_enrollment_code)));
		bindPreferenceSummaryToValue(findPreference(this.getResources().getString(R.string.spref_incentive)));
		bindPreferenceSummaryToValue(findPreference(sContext.getResources().getString(R.string.device_date_issue)));

		Preference pref = findPreference(this.getResources().getString(R.string.spref_settings_continue));
		pref.setOnPreferenceClickListener(new OnPreferenceClickListener() {
		    @Override
		    public boolean onPreferenceClick(Preference preference)
		    {
			getActivity().onBackPressed();
			return false;
		    }
		});
	    }

	}

	/* (non-Javadoc)
	 * 
	 * @see android.preference.PreferenceFragment#onStart() */
	@Override
	public void onStart()
	{
	    // TODO Auto-generated method stub
	    super.onStart();
	    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(sContext);
	    if (prefs.getString(this.getResources().getString(R.string.spref_subject_id), "").equals("")
		    || prefs.getString(this.getResources().getString(R.string.spref_enrollment_code), "").equals("")
		    || prefs.getString(sContext.getResources().getString(R.string.device_date_issue), "").equals(""))
		showStatic = true;
	}

	/* (non-Javadoc)
	 * 
	 * @see android.app.Fragment#onResume() */
	@Override
	public void onResume()
	{
	    // TODO Auto-generated method stub
	    super.onResume();
	}

	/* (non-Javadoc)
	 * @see android.preference.PreferenceFragment#onDestroy()
	 */
	@Override
	public void onDestroy()
	{
	    // TODO Auto-generated method stub
	    Log.d(TAG, "Settings activity destroy");
	    super.onDestroy();
	}
	 
    }
    

}
