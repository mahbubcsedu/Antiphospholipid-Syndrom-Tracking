package com.emmes.aps.test;

import com.emmes.aps.R;
import com.emmes.aps.medication.ActivityDailyMedicationByDate;
import com.emmes.aps.medication.ActivityMedicationListView;
import com.emmes.aps.supplement.ActivityDailySupplementByDate;
import com.emmes.aps.supplement.ActivitySupplementListView;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class ActivityDummy extends Activity{

	Context context;
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		context=this;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dummyhome);
		
		Button btnGo=(Button) findViewById(R.id.btnGoMed);
		btnGo.setOnClickListener(goMedication);
		
		Button btnGoHist=(Button) findViewById(R.id.btnGoMedHist);
		btnGoHist.setOnClickListener(goMedicationHist);
		
		Button btnGos=(Button) findViewById(R.id.btnGoS);
		btnGos.setOnClickListener(goSuppliment);
		
		Button btnGosHist=(Button) findViewById(R.id.btnGoSHist);
		btnGosHist.setOnClickListener(goSupplimentHist);
	}
	private OnClickListener goMedication = new OnClickListener() {     
		public void onClick(View v) 
		{         // Do something in response to the click  
			startActivity(new Intent(context,ActivityMedicationListView.class));
		} 
	}; 
	private OnClickListener goMedicationHist = new OnClickListener() {     
		public void onClick(View v) 
		{         // Do something in response to the click  
			startActivity(new Intent(context,ActivityDailyMedicationByDate.class));
		} 
	}; 
	
	private OnClickListener goSuppliment = new OnClickListener() {     
		public void onClick(View v) 
		{         // Do something in response to the click  
			startActivity(new Intent(context,ActivitySupplementListView.class));
		} 
	}; 
	private OnClickListener goSupplimentHist = new OnClickListener() {     
		public void onClick(View v) 
		{         // Do something in response to the click  
			startActivity(new Intent(context,ActivityDailySupplementByDate.class));
		} 
	}; 
}
