package com.emmes.aps;

import java.util.List;

import com.emmes.aps.locationtracking.LocationUtils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.preference.SecurePreferences;

/**
 * Activity which displays a login screen to the user
 */

public class PINActivity extends Activity
{

    private static final String TAG = "PINActivity";
    private Handler timerHandler = new Handler();
    private String mPassword;
    private EditText mPasswordView;
    private View mLoginFormView;
    private View mLoginStatusView;
    private TextView mLoginStatusMessageView;
    private String mPin;
    private Long mTimeout;
    private boolean isRedirectedConsent;
    private boolean isRestarted;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
	super.onCreate(savedInstanceState);

	Log.i(TAG, "PINActivity onCreate");

	isRedirectedConsent = false;
	this.isRestarted=false;

	Bundle extras = getIntent().getExtras();
	if (extras != null)
	{
	    if (extras.containsKey(LocationUtils.DIRECTING_FROM_LOCATION_RECEIVER_FOR_CONSENT))
	    {
		boolean consent = extras.getBoolean(LocationUtils.DIRECTING_FROM_LOCATION_RECEIVER_FOR_CONSENT, false);

		// if(consent.toLowerCase().equals(new String("yes")))
		
		getIntent().putExtra(LocationUtils.DIRECTING_FROM_LOCATION_RECEIVER_FOR_CONSENT,"no");
		isRedirectedConsent = consent;
	    }
	}

	mTimeout = Long.parseLong(getString(R.string.inactive_time));
	setContentView(R.layout.activity_pin);

	mPasswordView = (EditText) findViewById(R.id.pin);
	mPasswordView.setFilters(new InputFilter[]{new InputFilter.LengthFilter(4)});
	mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
	    @Override
	    public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent)
	    {
		if (id == R.id.login || id == EditorInfo.IME_NULL)
		{
		    attemptLogin();
		    return true;
		}
		return false;
	    }
	});

	mLoginFormView = findViewById(R.id.login_form);
	mLoginStatusView = findViewById(R.id.login_status);
	mLoginStatusMessageView = (TextView) findViewById(R.id.login_status_message);

	// Retrieve PIN from preferences
	SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
	SecurePreferences secprefs = new SecurePreferences();
	try
	{
	    mPin = secprefs.decrypt(prefs.getString(this.getResources().getString(R.string.spref_pref_pin_encrypted), ""));
	    //mPin = SecureSharedpreference.decrypt(this.getResources().getString(R.string.EncryptionKey),prefs.getString(this.getResources().getString(R.string.spref_pref_pin), ""));
	} catch (Exception e)
	{
	    e.printStackTrace();
	}

	findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
	    @Override
	    public void onClick(View view)
	    {
		attemptLogin();
	    }
	});

	// Set notification alarms
	scheduleAlarm();
    }

   

    /* (non-Javadoc)
     * @see android.app.Activity#onDestroy()
     */
    @Override
    protected void onDestroy()
    {
	// TODO Auto-generated method stub
	super.onDestroy();
    }

    @Override
    protected void onResume()
    {
	mPasswordView.setText("");
	super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
	super.onCreateOptionsMenu(menu);
	getMenuInflater().inflate(R.menu.pin, menu);
	return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
	switch (item.getItemId())
	{
	case R.id.action_forgot_password:
	    Intent launchingIntent = new Intent(this, SecurityQuestionsActivity.class);
	    startActivity(launchingIntent);
	    return true;
	}
	return super.onOptionsItemSelected(item);
    }

    /**
     * Attempts to sign in. If there are form errors, the errors are presented
     * and no actual login attempt is made.
     */
    public void attemptLogin()
    {

	mPasswordView.setError(null);
	mPassword = mPasswordView.getText().toString();

	boolean cancel = false;
	View focusView = null;

	// Check for a valid pin.
	if (TextUtils.isEmpty(mPassword))
	{
	    mPasswordView.setError(getString(R.string.error_field_required));
	    focusView = mPasswordView;
	    cancel = true;
	}
	else if (mPassword.length() < 4)
	{
	    mPasswordView.setError(getString(R.string.error_invalid_password));
	    focusView = mPasswordView;
	    cancel = true;
	}

	if (cancel)
	{
	    // There was an error; don't attempt login and focus the first
	    // form field with an error.
	    focusView.requestFocus();
	}
	else
	{
	    // Show a progress spinner, and kick off the user login attempt.
	    mLoginStatusMessageView.setText(R.string.login_progress_signing_in);
	    showProgress(true);
	    cancel = true;
	    if (mPin.equals(mPassword))
	    {
		// postDelayed handler to exit if inactive
		timerHandler.postDelayed(updateTimerThread, mTimeout);

		cancel = false;

		if (isRedirectedConsent)
		{
		    isRedirectedConsent = false;
		    Intent intent = new Intent(this, SettingsActivity.class);
		    startActivity(intent);
		}
		else
		{
		    Intent launchingIntent = new Intent(this, DiaryActivity.class);
		    startActivity(launchingIntent);
		}
	    }

	    if (cancel)
	    {
		showProgress(false);
		mPasswordView.setError(getString(R.string.error_incorrect_password));
		mPasswordView.requestFocus();
	    }
	}
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    private void showProgress(final boolean show)
    {
	int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

	mLoginStatusView.setVisibility(View.VISIBLE);
	mLoginStatusView.animate().setDuration(shortAnimTime).alpha(show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
	    @Override
	    public void onAnimationEnd(Animator animation)
	    {
		mLoginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
	    }
	});

	mLoginFormView.setVisibility(View.VISIBLE);
	mLoginFormView.animate().setDuration(shortAnimTime).alpha(show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
	    @Override
	    public void onAnimationEnd(Animator animation)
	    {
		mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
	    }
	});
    }

    //
    private Runnable updateTimerThread = new Runnable() {
	@Override
	public void run()
	{
	    Log.i(TAG, "Timer running ...");
	    // Do not time out if on PIN screen
	    ActivityManager am = (ActivityManager) getApplicationContext().getSystemService("activity");
	    List<RunningTaskInfo> taskInfo = am.getRunningTasks(1);
	    ComponentName componentInfo = taskInfo.get(0).topActivity;
	    if (!this.getClass().getName().contains(componentInfo.getClassName()))
	    {

		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		Long lastAct = prefs.getLong("lastactiontime", 0);
		Long tempvar = System.currentTimeMillis();
		Log.i(TAG, tempvar.toString());
		Log.i(TAG, lastAct.toString());
		if (System.currentTimeMillis() - lastAct > mTimeout)
		{
		    Log.i(TAG, "Timing Out");
		    Intent launchingIntent = new Intent(getApplicationContext(), MainActivity.class);
		    startActivity(launchingIntent);
		    timerHandler.removeCallbacks(updateTimerThread);
		}
		else
		{
		    timerHandler.postDelayed(updateTimerThread, mTimeout);
		}
	    }
	}
    };

    private void scheduleAlarm()
    {
	Intent intentAlarm = new Intent(this, AlarmReciever.class);
	AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
	alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + mTimeout, mTimeout,
	        PendingIntent.getBroadcast(this, 1, intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT));
    }

}
