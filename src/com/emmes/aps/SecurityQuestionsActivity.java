package com.emmes.aps;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.preference.SecurePreferences;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;

// TODO: Auto-generated Javadoc
/**
 * Activity which displays a login screen to the user, offering registration as
 * well.
 */
public class SecurityQuestionsActivity extends Activity {

	/**
	 * The default email to populate the email field with.
	 */
	public static final String EXTRA_EMAIL = "com.example.android.authenticatordemo.extra.EMAIL";

	// Values for security responses
	/** The m response1. */
	private String mResponse1;
	
	/** The m response2. */
	private String mResponse2;
	
	/** The m pref resp1. */
	private String mPrefResp1;
	
	/** The m pref resp2. */
	private String mPrefResp2;

	/** The Constant TAG. */
	private final static String TAG="SecurityQuestionsActivity";
	// UI references.
	/** The m response1 view. */
	private EditText mResponse1View;
	
	/** The m response2 view. */
	private EditText mResponse2View;
	
	/** The m security form view. */
	private View mSecurityFormView;
	
	/** The m security status view. */
	private View mSecurityStatusView;
	
	/** The m security status message view. */
	private TextView mSecurityStatusMessageView;
	 
 	/** The prefs. */
 	SharedPreferences prefs;
	    /** The sharedpreferences object to deal with shared preferences. */
	    SharedPreferences mApplicationPrefs;
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_security_questions);
		setupActionBar();
		
		// Retrieve security responses from preferences
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		SecurePreferences secprefs = new SecurePreferences();
		mPrefResp1 = secprefs.decrypt(prefs.getString("pref_response1", ""));
		mPrefResp2 = secprefs.decrypt(prefs.getString("pref_response2", ""));

		// Set up the login form.
		mResponse1 = getIntent().getStringExtra(EXTRA_EMAIL);
		mResponse1View = (EditText) findViewById(R.id.response1);
		mResponse1View.setText(mResponse1);

		mResponse2View = (EditText) findViewById(R.id.response2);
		mResponse2View
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView textView, int id,
							KeyEvent keyEvent) {
						if (id == R.id.login || id == EditorInfo.IME_NULL) {
							attemptLogin();
							return true;
						}
						return false;
					}
				});

		mSecurityFormView = findViewById(R.id.security_form);
		mSecurityStatusView = findViewById(R.id.security_status);
		mSecurityStatusMessageView = (TextView) findViewById(R.id.security_status_message);

		findViewById(R.id.sign_in_button).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						attemptLogin();
					}
				});
	}

	//@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	/**
	 * Setup action bar.
	 */
	private void setupActionBar() {
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.security_questions, menu);
		return true;
	}

	/**
	 * Attempt login.
	 */
	public void attemptLogin() {
		// Reset errors.
		mResponse1View.setError(null);
		mResponse2View.setError(null);

		// Store values at the time of the login attempt.
		mResponse1 = mResponse1View.getText().toString();
		mResponse2 = mResponse2View.getText().toString();

		boolean cancel = false;
		View focusView = null;

		if (TextUtils.isEmpty(mResponse1)) {
			mResponse1View.setError(getString(R.string.error_field_required));
			focusView = mResponse1View;
			cancel = true;
		}

		if (TextUtils.isEmpty(mResponse2)) {
			mResponse2View.setError(getString(R.string.error_field_required));
			focusView = mResponse2View;
			cancel = true;
		} 

		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		} else {
			// Show a progress spinner and kick off the user login attempt.
			mSecurityStatusMessageView.setText(R.string.login_progress_signing_in);
			showProgress(true);
			cancel = true;
			
			if (mPrefResp1.equals(mResponse1)) {
				if (mPrefResp2.equals(mResponse2)) {
					cancel = false;
					Intent launchingIntent = new Intent(this,SettingsActivity.class);
					startActivity(launchingIntent);
					finish();
				} else {
					showProgress(false);
					focusView = mResponse2View;
					mResponse2View.setError(getString(R.string.error_incorrect_response));
				}
			} else {
				showProgress(false);
				focusView = mResponse1View;
				mResponse1View.setError(getString(R.string.error_incorrect_response));
			}
			
			if (cancel) {
				showProgress(false);
				focusView.requestFocus();
			}
		}
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
	    
	/**
	 * Shows the progress UI and hides the login form.
	 *
	 * @param show the show
	 */
	private void showProgress(final boolean show) {
		int shortAnimTime = getResources().getInteger(
				android.R.integer.config_shortAnimTime);

		mSecurityStatusView.setVisibility(View.VISIBLE);
		mSecurityStatusView.animate().setDuration(shortAnimTime)
				.alpha(show ? 1 : 0)
				.setListener(new AnimatorListenerAdapter() {
					@Override
					public void onAnimationEnd(Animator animation) {
						mSecurityStatusView.setVisibility(show ? View.VISIBLE
								: View.GONE);
					}
				});

		mSecurityFormView.setVisibility(View.VISIBLE);
		mSecurityFormView.animate().setDuration(shortAnimTime)
				.alpha(show ? 0 : 1)
				.setListener(new AnimatorListenerAdapter() {
					@Override
					public void onAnimationEnd(Animator animation) {
						mSecurityFormView.setVisibility(show ? View.GONE
								: View.VISIBLE);
					}
				});
	}

}
