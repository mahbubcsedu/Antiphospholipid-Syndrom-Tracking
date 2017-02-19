package com.emmes.aps;

import java.util.Calendar;
import java.util.TimeZone;

import com.emmes.aps.DiaryActivity.PEAK_FLOW_CATEGORY;
import com.emmes.aps.data.DatabaseConstants;
import com.emmes.aps.data.FormContentData;
import com.emmes.aps.data.PeakFlowDB;
import com.emmes.aps.util.CalendarUtils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
//import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
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
import android.widget.TextView;

public class PeakFlowActivity extends Activity
{

    /** The sharedpreferences object to deal with shared preferences. */
    SharedPreferences mApplicationPrefs;
    /**
     * mEditor is an instance of SharedPreference.Editor which is used for
     * editing the sharedpreferences in the application @see SharedPreferences.
     */
    SharedPreferences.Editor mEditor;

    private static final String TAG = "PeakFlowActivity";
    Context context;
    TextView peakflow;
    Button mPFAdd1;
    Button mPFAdd2;
    EditText mPF1;
    EditText mPF2;
    EditText mPF3;
    TextView mPFLabel1;
    TextView mPFLabel2;
    TextView mPFLabel3;
    int mTimetoprogress;
    boolean IS_REPEATED_VISIT_TO_PEAKFLOW_PAGE_THIS_WINDOW;
    DiaryActivity.PEAK_FLOW_CATEGORY peak;
    final Handler mHandler = new Handler();

    /* (non-Javadoc)
     * 
     * @see android.app.Activity#onDestroy() */
    @Override
    protected void onDestroy()
    {
	// TODO Auto-generated method stub
	mHandler.removeCallbacksAndMessages(null);
	super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
	super.onCreate(savedInstanceState);
	this.context = this;
	setContentView(R.layout.activity_peak_flow);
	IS_REPEATED_VISIT_TO_PEAKFLOW_PAGE_THIS_WINDOW = false;
	// DiaryActivity.PEAK_FLOW_CATEGORY=getIntent().getExtras("")
	Bundle extra = getIntent().getExtras();

	peak = (PEAK_FLOW_CATEGORY) extra.get("peakflow_category");

	peakflow = (TextView) findViewById(R.id.peakFlow);

	if (peak == DiaryActivity.PEAK_FLOW_CATEGORY.AM)
	    peakflow.setText(this.context.getResources().getString(R.string.peak_flow_screen_header_title_morning));
	else
	    peakflow.setText(this.context.getResources().getString(R.string.peak_flow_screen_header_title_evening));

	mPFAdd1 = (Button) findViewById(R.id.btnPfAdd1);
	mPFAdd2 = (Button) findViewById(R.id.btnPfAdd2);
	mPF1 = (EditText) findViewById(R.id.etPeakFlow01);
	mPF2 = (EditText) findViewById(R.id.etPeakFlow02);
	mPF3 = (EditText) findViewById(R.id.etPeakFlow03);
	mPFLabel1 = (TextView) findViewById(R.id.peakFlow01);
	mPFLabel2 = (TextView) findViewById(R.id.peakFlow02);
	mPFLabel3 = (TextView) findViewById(R.id.peakFlow03);

	mPFAdd2.setVisibility(View.GONE);
	mPF2.setVisibility(View.GONE);
	mPF3.setVisibility(View.GONE);
	mPFLabel2.setVisibility(View.GONE);
	mPFLabel3.setVisibility(View.GONE);

	mTimetoprogress = Integer.parseInt(PeakFlowActivity.this.getResources().getString(R.string.peakflow_waiting_time));

	mPFAdd1.setOnClickListener(mAdd1Listener1);
	mPFAdd2.setOnClickListener(mAdd1Listener2);
	loadTodayPeakFlow();
    }

    private OnClickListener mAdd1Listener1 = new OnClickListener() {

	int timerforprogressbar = mTimetoprogress;

	@Override
	public void onClick(View v)
	{
	    onWaitingFewSeconds(1);
	    /* showWorkingDialog(); Log.d(TAG, "waiting time =" +
	     * timetoprogress); new Handler().postDelayed(new Runnable() {
	     * 
	     * @Override public void run() { removeWorkingDialog();
	     * mPFAdd2.setVisibility(View.VISIBLE);
	     * mPF2.setVisibility(View.VISIBLE); mPF2.requestFocus();
	     * mPFLabel2.setVisibility(View.VISIBLE); }
	     * 
	     * }, timerforprogressbar); */
	    // TODO Auto-generated method stub

	}
    };
    private OnClickListener mAdd1Listener2 = new OnClickListener() {
	int timerforprogressbar = mTimetoprogress;

	@Override
	public void onClick(View v)
	{

	    onWaitingFewSeconds(2);
	    /* // final ProgressBar pbar = (ProgressBar) findViewById(R.id.bar);
	     * // // Final so we can access it from the other thread //
	     * pbar.setVisibility(View.VISIBLE);
	     * 
	     * // Create a Handler instance on the main thread
	     * 
	     * showWorkingDialog(); // Create and start a new Thread new
	     * Thread(new Runnable() { public void run() { try {
	     * Thread.sleep(timerforprogressbar); } catch (Exception e) { } //
	     * Just catch the InterruptedException
	     * 
	     * // Now we use the Handler to post back to the main thread
	     * handler.post(new Runnable() { public void run() { // Set the
	     * View's visibility back on the main UI // Thread
	     * removeWorkingDialog(); mPF3.setVisibility(View.VISIBLE);
	     * mPF3.requestFocus(); mPFLabel3.setVisibility(View.VISIBLE); } });
	     * } }).start(); */
	    /* // TODO Auto-generated method stub showWorkingDialog(); new
	     * Handler().postDelayed(new Runnable() {
	     * 
	     * @Override public void run() { removeWorkingDialog();
	     * mPF3.setVisibility(View.VISIBLE); mPF3.requestFocus();
	     * mPFLabel3.setVisibility(View.VISIBLE); }
	     * 
	     * }, timerforprogressbar); */

	}
    };
    private ProgressDialog working_dialog;

    /**
     * @deprecated showWorkingDialog void
     */
    @Deprecated
	private void showWorkingDialog()
    {
	working_dialog = ProgressDialog.show(context, this.getResources().getString(R.string.peak_flow_wating_title),
	        this.getResources().getString(R.string.peak_flow_waiting_message), true);
    }
    /**
     * @deprecated showWorkingDialog void
     */
    @Deprecated
	private void removeWorkingDialog()
    {
	if (working_dialog != null)
	{
	    working_dialog.dismiss();
	    working_dialog = null;
	}
    }

    @Override
    protected void onPause()
    {
	// TODO Auto-generated method stub
	super.onPause();
	savePeakFlow();
    }

    private void savePeakFlow()
    {
	ContentValues values = new ContentValues();

	Calendar now_time = Calendar.getInstance();
	// now_time.setTimeZone(TimeZone.getDefault());
	// Date date = now_time.getTime();
	String formattedDate = CalendarUtils.calTimeToDateStringyyyymmdd(now_time);
	// new
	// SimpleDateFormat("yyyy-MM-dd").format(date);

	if (IS_REPEATED_VISIT_TO_PEAKFLOW_PAGE_THIS_WINDOW)
	{
	    values.clear();
	    values.put(PeakFlowDB.PeakFlowItem.COLUMN_NAME_PEAK_FLOW1, mPF1.getText().toString());
	    values.put(PeakFlowDB.PeakFlowItem.COLUMN_NAME_PEAK_FLOW2, mPF2.getText().toString());
	    values.put(PeakFlowDB.PeakFlowItem.COLUMN_NAME_PEAK_FLOW3, mPF3.getText().toString());
	    values.put(PeakFlowDB.PeakFlowItem.COLUMN_NAME_PEAK_FLOW_CATEGORY, peak.toString());

	    String where = PeakFlowDB.PeakFlowItem.COLUMN_NAME_ENTRYTIME + "=?";
	    String[] selectionArgs = { formattedDate };
	    getContentResolver().update(PeakFlowDB.PeakFlowItem.CONTENT_URI, values, where, selectionArgs);
	}
	else
	{
	    values.clear();
	    values.put(PeakFlowDB.PeakFlowItem.COLUMN_NAME_PEAK_FLOW1, mPF1.getText().toString());
	    values.put(PeakFlowDB.PeakFlowItem.COLUMN_NAME_PEAK_FLOW2, mPF2.getText().toString());
	    values.put(PeakFlowDB.PeakFlowItem.COLUMN_NAME_PEAK_FLOW3, mPF3.getText().toString());
	    values.put(PeakFlowDB.PeakFlowItem.COLUMN_NAME_ENTRYTIME, formattedDate);
	    values.put(PeakFlowDB.PeakFlowItem.COLUMN_NAME_PEAK_FLOW_CATEGORY, peak.toString());
	    getContentResolver().insert(PeakFlowDB.PeakFlowItem.CONTENT_URI, values);
	}
	// if all peak flow measurements are entered then we will take this as
	// collection
	if ((mPF1.getText().length() != 0) && (mPF2.getText().length() != 0) && (mPF3.getText().length() != 0))
	{
	    values.clear();
	    values.put(FormContentData.FormContentItems.COLUMN_NAME_LAST_COLLECTED_TIME, CalendarUtils.calTimeToDateStringyyyymmddhhmmss(now_time));

	    String where = "";
	    if (peak == DiaryActivity.PEAK_FLOW_CATEGORY.AM)
		where = FormContentData.FormContentItems.COLUMN_NAME_FORM_NAME + "='" + DatabaseConstants.FORM_CONTENT_ID_AM_FLOW + "'";
	    else if (peak == DiaryActivity.PEAK_FLOW_CATEGORY.PM)
		where = FormContentData.FormContentItems.COLUMN_NAME_FORM_NAME + "='" + DatabaseConstants.FORM_CONTENT_ID_PM_FLOW + "'";

	    // String[] args = new String[] { Integer.toString(med_id) };
	    int numofrowdeleted = this.getContentResolver().update(FormContentData.FormContentItems.CONTENT_URI, values, where, null);
	    Log.d(TAG, numofrowdeleted + " rows updated which is peakflow row");
	}
    }

    private void loadTodayPeakFlow()
    {
	Calendar now_time = Calendar.getInstance();
	now_time.setTimeZone(TimeZone.getDefault());
	String formattedDate = CalendarUtils.calTimeToDateStringyyyymmdd(now_time);// new
	                                                                           // SimpleDateFormat("yyyy-MM-dd").format(date);

	String[] PROJECTION_COLUMS = { PeakFlowDB.PeakFlowItem.COLUMN_NAME_PEAK_FLOW1, PeakFlowDB.PeakFlowItem.COLUMN_NAME_PEAK_FLOW2,
	        PeakFlowDB.PeakFlowItem.COLUMN_NAME_PEAK_FLOW3, PeakFlowDB.PeakFlowItem.COLUMN_NAME_PEAK_FLOW_CATEGORY };
	String selection = PeakFlowDB.PeakFlowItem.COLUMN_NAME_ENTRYTIME + "=?";
	String[] selectionArgs = { formattedDate };
	// ContentValues contents = new ContentValues();
	// contents.put(key, value);
	Cursor cursor = getContentResolver().query(PeakFlowDB.PeakFlowItem.CONTENT_URI, PROJECTION_COLUMS, selection, selectionArgs,
	        PeakFlowDB.PeakFlowItem.DEFAULT_SORT_ORDER);

	if (cursor.getCount() > 0)
	{
	    cursor.moveToFirst();
	    IS_REPEATED_VISIT_TO_PEAKFLOW_PAGE_THIS_WINDOW = true;
	    String flow1 = cursor.getString(0);
	    String flow2 = cursor.getString(1);
	    String flow3 = cursor.getString(2);
	    String flow_category = cursor.getString(3);
	    cursor.close();
	    mPF1.setText(flow1);
	    mPF2.setText(flow2);
	    mPF3.setText(flow3);

	    if (!flow1.equals(""))
	    {
		mPF1.setVisibility(View.VISIBLE);// .setText(flow1);
		mPFAdd1.setVisibility(View.VISIBLE);
		mPFLabel1.setVisibility(View.VISIBLE);

	    }
	    if (!flow2.equals(""))
	    {
		mPF2.setVisibility(View.VISIBLE);// .setText(flow1);
		mPFAdd2.setVisibility(View.VISIBLE);
		mPFLabel2.setVisibility(View.VISIBLE);
	    }
	    if (!flow3.equals(""))
	    {
		mPF3.setVisibility(View.VISIBLE);// .setText(flow1);
		mPFLabel3.setVisibility(View.VISIBLE);

	    }

	    /* mPFAdd2.setVisibility(View.VISIBLE);
	     * mPF2.setVisibility(View.VISIBLE);
	     * mPF3.setVisibility(View.VISIBLE);
	     * mPFLabel2.setVisibility(View.VISIBLE);
	     * mPFLabel3.setVisibility(View.VISIBLE); */
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

	    onBackPressed();
	}
	// TODO Auto-generated method stub
	super.onRestart();
    }

    @Override
	public void onBackPressed()
    {
	Intent intent = new Intent(PeakFlowActivity.this, MainActivity.class);
	intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	intent.putExtra("EXIT", true);
	startActivity(intent);
    }

    public void onWaitingFewSeconds(final int whichPeakFlow)
    {
	/* if (calculatedValue.equals(NOT_YET_CALCULATED)) { */
	// show progress dialog
	final ProgressDialog progress = ProgressDialog.show(context, this.getResources().getString(R.string.peak_flow_wating_title), this
	        .getResources().getString(R.string.peak_flow_waiting_message), true);

	AsyncTask<Void, Void, Boolean> waitForCompletion = new AsyncTask<Void, Void, Boolean>() {
	    @Override
	    protected Boolean doInBackground(Void... params)
	    {
		long timeStarted = System.currentTimeMillis();
		while (System.currentTimeMillis() - timeStarted < mTimetoprogress)
		{
		    // wait for 1.5 ms
		    try
		    {
			Thread.sleep(100);
		    } catch (InterruptedException e)
		    {
			Log.e(TAG, "thread interrupted", e);
		    }
		}
		progress.dismiss();
		return true;
	    };

	    @Override
	    protected void onPostExecute(Boolean result)
	    {

		if (result == true)
		{
		    if (whichPeakFlow == 1)
		    {
			progress.dismiss();
			mPFAdd2.setVisibility(View.VISIBLE);
			mPF2.setVisibility(View.VISIBLE);
			mPF2.requestFocus();
			mPFLabel2.setVisibility(View.VISIBLE);
		    }
		    else if (whichPeakFlow == 2)
		    {
			mPF3.setVisibility(View.VISIBLE);
			mPF3.requestFocus();
			mPFLabel3.setVisibility(View.VISIBLE);
		    }
		    /* AlertDialog dialog = new
	             * AlertDialog.Builder(PeakFlowActivity.this).create();
	             * dialog.setTitle(R.string.not_yet_calulated_alert_title);
	             * dialog.setMessage(getResources().getString(R.string.
	             * not_yet_calulated_alert_message)); dialog.show(); */
		}
		/* else { i.putExtra(Constants.AMOUNT,
	         * Integer.parseInt(calculatedValue)); startActivity(i); } */
	    }
	};
	waitForCompletion.execute(null, null, null);
	// }
	/* else { i.putExtra(Constants.AMOUNT,
	 * Integer.parseInt(calculatedValue)); startActivity(i); } */
    }
}
