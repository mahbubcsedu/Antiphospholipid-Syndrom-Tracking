/* 
 * Copyright 2014 (C) The EMMES Corporation 
 *  
 * Created on : 02-06-2014
 * Last Update: Jun 18, 2014 9:07:21 AM
 * Author     : Mahbubur Rahman
 * Title      : Summer intern 2014 
 * Project    : Daily Diary Android Application
 * 
 */
package com.emmes.aps.medication;

import java.util.ArrayList;
import com.emmes.aps.R;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

// TODO: Auto-generated Javadoc
/**
 * The Class AdapterDailyMedicationList is used in the APS project to handle the
 * medication and supplement list.
 * <p>
 * The list (both medication and supplement) is the list of medication and
 * supplement that user is currently taking. And this comes if the user added
 * the one or more medication or supplement to their list with dosage and
 * frequency. The list of all medication or supplement they are currently using
 * will be displayed as list using this adapter. The list is constitutes of
 * medication or supplement together with their dosage and frequency in first
 * bracket and a checkbox.
 * <p>
 * The check box will determine whether that particular list item is selected
 * already by the user today. If the user come here for the first time then all
 * check box will remain unchecked. But if they check some of that items and
 * leave and then again come back, then they will see the items selected.
 * <p>
 * The adapter will have two types of list, one is the list of medications or
 * supplement from the database and another is the list of ids which the user
 * already have selected today in his/her previous visit to this page.
 */
public class AdapterDailyMedicationList extends BaseAdapter {
    /** The context is to keep the current class context. */
    Context context;
    /**
     * The medList is the list of medications or supplements. For list contents
     * in detail @link {com.emmes.aps.medication.MedicationDetails}
     */
    public ArrayList<MedicationDetails> medList;
    /**
     * existingIds is the list of integer which contains the list of Id's of
     * item which are already been selected by the user for today
     */
    public ArrayList<Integer> existingIds;

    /**
     * Instantiates a new adapter daily medication list.
     * 
     * @param context
     *            is context of adapter class
     * @param listdata
     *            is the list of medication or supplement
     * @param existingIds
     *            is the list of id's that the user selected already for today
     */
    public AdapterDailyMedicationList(Context context, ArrayList<MedicationDetails> listdata,
	    ArrayList<Integer> existingIds) {
	this.context = context;
	this.medList = listdata;
	this.existingIds = existingIds;
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.widget.Adapter#getCount()
     */
    @Override
    public int getCount() {
	// TODO Auto-generated method stub
	return this.medList.size();
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.widget.Adapter#getItem(int)
     */
    @Override
    public Object getItem(int position) {
	// TODO Auto-generated method stub
	return this.medList.get(position);
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.widget.Adapter#getItemId(int)
     */
    @Override
    public long getItemId(int position) {
	// TODO Auto-generated method stub
	return this.medList.get(position).getId();
    }

    /**
     * Gets the med list.
     * 
     * @return the medList
     */
    public ArrayList<MedicationDetails> getMedList() {
	return medList;
    }

    /**
     * Sets the med list.
     * 
     * @param medList
     *            the medList to set
     */
    public void setMedList(ArrayList<MedicationDetails> medList) {
	this.medList = medList;
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.widget.Adapter#getView(int, android.view.View,
     * android.view.ViewGroup)
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
	MedicationDetails medicationInaPostion = (MedicationDetails) this.medList.get(position);
	ViewHolder holder = null;
	Log.v("ConvertView", String.valueOf(position));
	// View parentView = convertView;
	if (convertView == null)
	{
	    LayoutInflater vi = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    convertView = vi.inflate(R.layout.activity_medication_listview, null);
	    /**
	     * implementing view holder for controlling the list items
	     */
	    holder = new ViewHolder();
	    holder.tvmedname = (TextView) convertView.findViewById(R.id.tvmed_nameinlist);
	    holder.tvmedname.setText(medicationInaPostion.getMedication_name());
	    // holder.tvmedname.setTag(medicationInaPostion.getId());
	    holder.cb_med_taken = (CheckBox) convertView.findViewById(R.id.cbmed_taken);
	    if (this.existingIds.contains(medicationInaPostion.getId()))
	    {
		holder.cb_med_taken.setChecked(true);
		for (MedicationDetails lc : this.medList)
		{
		    if (lc.getId() == medicationInaPostion.getId())
			lc.isSelected = true;
		}
	    }
	    // holder.cb_med_taken.setText(medicationInaPostion.getDosage());
	    holder.tvmeddosage = (TextView) convertView.findViewById(R.id.tvMedListDosage);
	    holder.tvmeddosage.setText(medicationInaPostion.getDosage());
	    holder.tvmedfreq = (TextView) convertView.findViewById(R.id.tvMedListFreq);
	    holder.tvmedfreq.setText(medicationInaPostion.getFrequency());
	    holder.cb_med_taken.setOnClickListener(new View.OnClickListener() {
		public void onClick(View v) {
		    CheckBox cb = (CheckBox) v;
		    MedicationDetails listcontent = (MedicationDetails) cb.getTag();
		    Toast.makeText(context, "Clicked on Checkbox: " + cb.getText() + " is " + cb.isChecked(),
			    Toast.LENGTH_SHORT).show();
		    listcontent.setSelected(cb.isChecked());
		}
	    });
	    convertView.setTag(holder);
	}
	else
	{
	    holder = (ViewHolder) convertView.getTag();
	}
	// ListContents country = countryList.get(position);
	holder.tvmedname.setText(medicationInaPostion.getMedication_name() + " (" + medicationInaPostion.getDosage()
		+ "," + medicationInaPostion.getFrequency() + ")");
	holder.tvmeddosage.setText(medicationInaPostion.getDosage());
	holder.cb_med_taken.setChecked(medicationInaPostion.isSelected());
	holder.tvmedfreq.setText(medicationInaPostion.getFrequency());
	holder.cb_med_taken.setTag(medicationInaPostion);
	return convertView;
    }

    /**
     * The Class ViewHolder.
     */
    private class ViewHolder {
	/** The tvmedfreq. */
	TextView tvmedname, tvmeddosage, tvmedfreq;
	/** The cb_med_taken. */
	CheckBox cb_med_taken;
    }
}
