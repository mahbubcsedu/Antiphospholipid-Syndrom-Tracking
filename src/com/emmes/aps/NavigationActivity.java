package com.emmes.aps;

import android.app.Activity;
import android.view.MenuItem;

/*
 * This class extends Activity to provide a single common source 
 * for actions to be triggered on any activity that extends this
 * throughout the application.
 * 
 */

public class NavigationActivity extends Activity {
    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Navigation.registerAction(getApplicationContext());
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onPause() {
        super.onPause();
        Navigation.registerAction(getApplicationContext());
    }

	/* (non-Javadoc)
	 * @see android.app.Activity#onRestart()
	 */
        @Override
        protected void onRestart()
        {
	    // TODO Auto-generated method stub
	    super.onRestart();
        }
}
