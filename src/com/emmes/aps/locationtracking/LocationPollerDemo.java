/***
	Copyright (c) 2010 CommonsWare, LLC

	Licensed under the Apache License, Version 2.0 (the "License"); you may
	not use this file except in compliance with the License. You may obtain
	a copy of the License at
		http://www.apache.org/licenses/LICENSE-2.0
	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
 */

package com.emmes.aps.locationtracking;

import com.emmes.aps.R;
import com.emmes.aps.locpoller.LocationPoller;
import com.emmes.aps.locpoller.LocationPollerParameter;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Toast;


// TODO: Auto-generated Javadoc
/**
 * The Class LocationPollerDemo.
 */
public class LocationPollerDemo extends Activity {
	//private static final int PERIOD=1800000; 	// 30 minutes
	/** The Constant PERIOD. */
	private static final int PERIOD=5000; 	// 5 sec minutes
	
	/** The pi. */
	private PendingIntent pi=null;
	
	/** The mgr. */
	private AlarmManager mgr=null;

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		mgr=(AlarmManager)getSystemService(ALARM_SERVICE);

		Intent i=new Intent(this, LocationPoller.class);

		Bundle bundle = new Bundle();
		LocationPollerParameter parameter = new LocationPollerParameter(bundle);
		parameter.setIntentToBroadcastOnCompletion(new Intent(this, LocationReceiver.class));
		// try GPS and fall back to NETWORK_PROVIDER
		parameter.setProviders(new String[] {LocationManager.GPS_PROVIDER, LocationManager.NETWORK_PROVIDER});
		parameter.setTimeout(60000);
		i.putExtras(bundle);


		pi=PendingIntent.getBroadcast(this, 0, i, 0);
		mgr.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
				SystemClock.elapsedRealtime(),
				PERIOD,
				pi);

		Toast
		.makeText(this,
				"Location polling every 30 minutes begun",
				Toast.LENGTH_LONG)
				.show();
	}

	/**
	 * Omg please stop.
	 *
	 * @param v the v
	 */
	public void omgPleaseStop(View v) {
		mgr.cancel(pi);
		finish();
	}
}
