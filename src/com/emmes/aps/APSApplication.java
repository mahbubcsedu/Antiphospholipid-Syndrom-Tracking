package com.emmes.aps;

import com.moki.manage.api.MokiManage;

import android.app.Activity;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

public class APSApplication extends Application implements Application.ActivityLifecycleCallbacks
{

    private static final String TAG = "APSApplication";

    private int resumed;
    private int paused;
    private int started;
    private int stopped;

    private MokiManage mmanage;
    private int activeActivityCount = 0;
    private final boolean enableASM = false;
    private final boolean enableAEM = true;
    private final boolean enableCompliance = false;
    private final String appKey = "2022a502-3681-4cc4-8c3b-22832ca34abb";
    private final String appID = "BWM Diary";
    private static final String TENANT_ID = "f7e06b01-52b9-40f9-b3ea-97f3f2fb9366";
    /** The sharedpreferences object to deal with shared preferences. */
    SharedPreferences mApplicationPrefs;
    /**
     * mEditor is an instance of SharedPreference.Editor which is used for
     * editing the sharedpreferences in the application @see SharedPreferences.
     */
    SharedPreferences.Editor mEditor;

    BroadcastReceiver mokiManageReciever = new BroadcastReceiver() {

	@Override
	public void onReceive(Context context, Intent intent)
	{
	    if (intent.getAction().equals(MokiManage.REGISTRATION_COMPLETED_NOTIFICATION))
	    {
// TODO: handle this as needed
	    }
	    else if (intent.getAction().equals(MokiManage.UNREGISTRATION_COMPLETED_NOTIFICATION))
	    {
// TODO: handle this as needed
	    }
	    else if (intent.getAction().equals(MokiManage.REGISTER_TO_A_NEW_TENANT_COMPLETED_NOTIFICATION))
	    {
// TODO: handle this as needed
	    }
	    else if (intent.getAction().equals(MokiManage.SETTINGS_PULL_COMPLETED_NOTIFICATION))
	    {
// TODO: handle this as needed
	    }
	    else if (intent.getAction().equals(MokiManage.SETTINGS_PUSH_COMPLETED_NOTIFICATION))
	    {
// TODO: handle this as needed
	    }
	}
    };

    @Override
    public void onCreate()
    {
	super.onCreate();
	Context context = this;
	//mmanage = MokiManage.sharedInstance(appKey, appID, context, enableASM, enableAEM, enableCompliance);
	registerActivityLifecycleCallbacks(this);
	// instance = this;
	//mmanage = MokiManage.sharedInstance(appKey, appID, context, enableASM, enableAEM, enableCompliance);
	registerActivityLifecycleCallbacks(this);
	/*registerReceiver(mokiManageReciever, new IntentFilter(MokiManage.REGISTRATION_COMPLETED_NOTIFICATION));
	registerReceiver(mokiManageReciever, new IntentFilter(MokiManage.UNREGISTRATION_COMPLETED_NOTIFICATION));
	registerReceiver(mokiManageReciever, new IntentFilter(MokiManage.REGISTER_TO_A_NEW_TENANT_COMPLETED_NOTIFICATION));
	registerReceiver(mokiManageReciever, new IntentFilter(MokiManage.SETTINGS_PULL_COMPLETED_NOTIFICATION));
	registerReceiver(mokiManageReciever, new IntentFilter(MokiManage.SETTINGS_PUSH_COMPLETED_NOTIFICATION));*/

	mApplicationPrefs=PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
	mEditor = mApplicationPrefs.edit();
	mEditor.putBoolean(getResources().getString(R.string.spref_is_application_background), false);
	mEditor.commit();

	//if (!mmanage.isRegistered())
	//{
	 //   mmanage.silentlyRegisterDevice(TENANT_ID);
	//}
    }

// public static MokiManageApplication instance(){
// return instance;
// }

    public MokiManage mokiManage()
    {
	return mmanage;
    }

    @Override
    public void onActivityStarted(Activity activity)
    {
	++started;
	activeActivityCount++;
	if (activeActivityCount == 1)
	{
	   // mmanage.resume();
	}
    }

    @Override
    public void onActivityStopped(Activity activity)
    {
	++stopped;
	android.util.Log.w("test", "application is visible: " + (started > stopped));
	activeActivityCount--;
	mApplicationPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

	mEditor = mApplicationPrefs.edit();
	mEditor.putBoolean(getResources().getString(R.string.spref_is_application_background), started <= stopped);
	mEditor.commit();
	Log.d(TAG, "application is in background  set to " + (started <= stopped));

	if (activeActivityCount == 0)
	{
	    //mmanage.pause();
	}
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState)
    {
    }

    @Override
    public void onActivityDestroyed(Activity activity)
    {
    }

    @Override
    public void onActivityPaused(Activity activity)
    {
	++paused;
	
	android.util.Log.w("test", "application is in foreground: " + (resumed > paused));	
	mApplicationPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
	mEditor = mApplicationPrefs.edit();
	mEditor.putBoolean(getResources().getString(R.string.spref_is_application_background), (resumed <= paused));
	mEditor.commit();
	Log.d(TAG, "application is in background  set to " + (resumed <= paused));
    }

    @Override
    public void onActivityResumed(Activity activity)
    {
	++resumed;
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState)
    {
    }
}