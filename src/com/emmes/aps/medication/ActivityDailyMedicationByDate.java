/* 
  * Copyright 2014 (C) The EMMES Corporation 
  *  
  * Created on : 02-06-2014
  * Last Update: Jun 18, 2014 9:06:56 AM
  * Author     : Mahbubur Rahman
  * Title      : Summer intern 2014 
  * Project    : Daily Diary Android Application
  * 
  */
package com.emmes.aps.medication;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;



import com.emmes.aps.MainActivity;
import com.emmes.aps.PeakFlowActivity;
//import java.util.LinkedHashMap;
import com.emmes.aps.R;
import com.emmes.aps.data.DatabaseConstants;
import com.emmes.aps.data.DailyMedication;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.Toast;
/**
 * 
 * @author Mahbubur Rahman
 * <h1>Daily Medication Diary</h1>
 * <p>The class ActivityDailyMedicationByDate is responsible for retrieving user 
 * daily medication history and display them as expandable listview.
 * The header will define the medication time they have reported
 * and also this header is clickable to see the detail history of that particular time
 * reported</p>
 * <p>The detail of medication consists of only the medication name serially display of that particular 
 * time. This also include the dosage and frequency of that medication inside braked.</p>
 *
 */
public class ActivityDailyMedicationByDate extends Activity{

	Context context;
	//private LinkedHashMap<String, HeaderInfo> medicationDateMap = new LinkedHashMap<String, HeaderInfo>();
	private ArrayList<HeaderInfo> medicationDate = new ArrayList<HeaderInfo>();

	private  AdapterDailyMedicationByDate medicationAdapter;
	private ExpandableListView myList;
	private Uri todayMedicationUri;
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_daily_medications);
		todayMedicationUri=DailyMedication.MedicationItem.CONTENT_URI;

		context=this;
		//get reference to the ExpandableListView
		myList = (ExpandableListView) findViewById(R.id.myList);


		//create the adapter by passing your ArrayList data
		medicationDate=retrieveDataForList();
		medicationAdapter = new AdapterDailyMedicationByDate(context, medicationDate);



		//attach the adapter to the list
		myList.setAdapter(medicationAdapter);

		//expand all Groups
		expandAll();

		//listener for group heading click
		myList.setOnGroupClickListener(myListGroupClicked);
		//listener for child row click
		myList.setOnChildClickListener(myListItemClicked);


	}


	//method to expand all groups
	private void expandAll() {
		int count = medicationAdapter.getGroupCount();
		for (int i = 0; i < count; i++){
			myList.expandGroup(i);
		}
	}

	/*	//method to collapse all groups
	private void collapseAll() {
		int count = medicationAdapter.getGroupCount();
		for (int i = 0; i < count; i++){
			myList.collapseGroup(i);
		}
	}*/


	//our child listener
	private OnChildClickListener myListItemClicked =  new OnChildClickListener() {

		public boolean onChildClick(ExpandableListView parent, View v,
				int groupPosition, int childPosition, long id) {

			//get the group header
			HeaderInfo headerInfo = medicationDate.get(groupPosition);
			//get the child info
			ChildDetailInfo detailInfo =  headerInfo.getChildInfo().get(childPosition);
			//display it or do something with it
			Toast.makeText(getBaseContext(), "Clicked on Detail " + headerInfo.getMedicationDate() 
					+ "/" + detailInfo.getMedicaitonName(), Toast.LENGTH_SHORT).show();
			return false;
		}

	};

	//our group listener
	private OnGroupClickListener myListGroupClicked =  new OnGroupClickListener() {

		public boolean onGroupClick(ExpandableListView parent, View v,
				int groupPosition, long id) {

			//get the group header
			HeaderInfo headerInfo = medicationDate.get(groupPosition);
			//myList.expandGroup(groupPosition);
			//set the current group to be selected so that it becomes visible
			//myList.setSelectedGroup(groupPosition);
			//display it or do something with it
			Toast.makeText(getBaseContext(), "Child on Header " + headerInfo.getMedicationDate()+"with childsize"+headerInfo.getChildInfo().size(), 
					Toast.LENGTH_SHORT).show(); 

			return false;
		}

	};

	/**
	 * Feed data to header info from database
	 */

	@SuppressLint("SimpleDateFormat")
	private ArrayList<HeaderInfo> retrieveDataForList()
	{
		/**
		 * define which columns to retrieve
		 */
		String[] projection_columns=new String[5];

		projection_columns[0]=DailyMedication.MedicationItem._ID;
		projection_columns[1]=DailyMedication.MedicationItem.COLUMN_NAME_MEDICATION_NAME;
		projection_columns[2]=DailyMedication.MedicationItem.COLUMN_NAME_DOSAGE;
		projection_columns[3]=DailyMedication.MedicationItem.COLUMN_NAME_FREQ;
		projection_columns[4]=DailyMedication.MedicationItem.COLUMN_NAME_LASTTAKEN;
		String selection=DailyMedication.MedicationItem.COLUMN_NAME_TYPE+"=?";
		String[] selArgs={DatabaseConstants.ITEM_TYPE_MEDICATION};
		Cursor cursor = getContentResolver().query(todayMedicationUri,projection_columns,selection,selArgs,null);
		//ContentUris.parseId(newItemUri);

		ArrayList<HeaderInfo> medicationList = new ArrayList<HeaderInfo>();
		
		cursor.moveToFirst();

		if(cursor.getCount()>0)
		{// if returned data is not empty
			//ListContents contents=new ListContents();
			HeaderInfo headItem=new HeaderInfo();
			ChildDetailInfo childlistitem=new ChildDetailInfo();
			ArrayList<ChildDetailInfo> childList=new ArrayList<ChildDetailInfo>();

			String preDate="",curDate="";
			//new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
			
			
			while (!cursor.isAfterLast()) 
			{
				curDate=Long.toString(cursor.getLong(4));
				if(!preDate.equals(curDate))
				{
					if(!preDate.equals(""))
					{
						headItem.setChildInfo(childList);
						headItem.setMedicationDate(convertTime(Long.parseLong(preDate)));
						medicationList.add(headItem);
					}

					headItem=new HeaderInfo();
					childList=new ArrayList<ChildDetailInfo>();
				}
				childlistitem=new ChildDetailInfo();
				childlistitem.setMedSeq(Integer.toString(childList.size()+1));
				childlistitem.setMedicaitonName(cursor.getString(1));
				childlistitem.setMedDosage(cursor.getString(2));
				childlistitem.setFreq(cursor.getInt(3));
				childList.add(childlistitem);
				preDate=curDate;
				cursor.moveToNext();
			}
			// make sure to close the cursor
			cursor.close();
			headItem.setChildInfo(childList);
			headItem.setMedicationDate(convertTime(Long.parseLong(preDate)));

			medicationList.add(headItem);
		}
		return medicationList; 
	}
	@SuppressLint("SimpleDateFormat")
	public String convertTime(long time){
		Date date = new Date(time);
		Format format = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
		return format.format(date).toString();
	}
	
	 /* (non-Javadoc)
	     * 
	     * @see android.app.Activity#onRestart() */
	    @Override
	    protected void onRestart()
	    {
		// TODO Auto-generated method stub
		onBackPressed();
		super.onRestart();
	    }
	    public void onBackPressed()
	    {
		Intent intent = new Intent(this, MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra("EXIT", true);
		startActivity(intent);
	    }
}
