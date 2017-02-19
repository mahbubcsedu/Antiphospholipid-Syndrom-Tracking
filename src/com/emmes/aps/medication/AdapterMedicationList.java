/* 
 * Copyright 2014 (C) The EMMES Corporation 
 *  
 * Created on : 02-06-2014
 * Last Update: Aug 18, 2014 12:57:32 PM
 * Author     : Mahbubur Rahman
 * Title      : Summer intern 2014 
 * Project    : Daily Diary Android Application
 * 
 */
package com.emmes.aps.medication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.emmes.aps.R;

// TODO: Auto-generated Javadoc
/**
 * The Class AdapterMedicationList is the observed data behind the list of
 * medication list which will be displayed with the activity
 * {@linkplain com.emmes.aps.medication.MedicationListActivity}.
 * 
 * @author Mahbubur Rahman
 * @see ArrayList
 * @see HashMap
 * @since 1.0
 */
public class AdapterMedicationList extends BaseAdapter
{

    /** The context is to keep the current class context. */
    Context mContext;
    /**
     * The medList is the list of medications or supplements. For list contents
     * in detail {@link com.emmes.aps.medication.MedicationDetails}
     */
    public ArrayList<MedicationDetails> mMedList;

    /**
     * existingIds is the list of integer which contains the list of Id's of
     * item which are already been selected by the user for today.
     */
    public ArrayList<Integer> mExistingIds;

    /**
     * Instantiates a new adapter daily medication list.
     * 
     */
    final static private String TAG = "AdapterMedicationList";

    /**
     * The mselection Hashmap controls which item has been added, which deleted
     * by storing boolean variable in eache position.
     */
    private HashMap<Integer, Boolean> mSelection = new HashMap<Integer, Boolean>();

    /**
     * The mCb is the beckbox enlisted to the list item of the listview of
     * medicaiton or supplement.
     */
    CheckBox mCb;

    /** The position of the current highlighted item to the adapter. */
    int mPosition;

    /** The parent of the selected view */
    ViewGroup mParent;

    /** The n taken. */
    private int nTaken;

    /**
     * Instantiates a new adapter both for medication or supplement list to
     * display. The constructor takes two different types of list, one is the
     * list of user added medications or supplement and another is the selected
     * items for today.
     * 
     * @param context
     *            the context from where the constructor called
     * @param listdata
     *            the listdata is the user added list of medication or
     *            supplement to the database.
     * @param existingIds
     *            the existing ids is the selected ids of medications or
     *            supplement today.
     * @param none
     * @return none
     * 
     * @throws none
     * @since 1.0
     * @author Mahbubur Rahman
     */
    public AdapterMedicationList(Context context, ArrayList<MedicationDetails> listdata, ArrayList<Integer> existingIds) {
	this.mContext = context;
	this.mMedList = listdata;
	this.mExistingIds = existingIds;
	this.nTaken = 0;
    }

    /**
     * Gets the n taken.
     * 
     * @return the nTaken
     */
    public int getnTaken()
    {
	return nTaken;
    }

    /**
     * Sets the n taken.
     * 
     * @param nTaken
     *            the nTaken to set
     */
    public void setnTaken(int nTaken)
    {
	this.nTaken = nTaken;
    }

    /* (non-Javadoc)
     * 
     * @see android.widget.Adapter#getCount() */
    @Override
    public int getCount()
    {
	// TODO Auto-generated method stub
	return this.mMedList.size();
    }

    /* (non-Javadoc)
     * 
     * @see android.widget.Adapter#getItem(int) */
    @Override
    public Object getItem(int position)
    {
	// TODO Auto-generated method stub
	return this.mMedList.get(position);
    }

    /* (non-Javadoc)
     * 
     * @see android.widget.Adapter#getItemId(int) */
    @Override
    public long getItemId(int position)
    {
	// TODO Auto-generated method stub
	return this.mMedList.get(position).getId();
    }

    /**
     * Gets all the ids that the user have selected today.
     * 
     * @return the selected list of ids.
     * @param none
     * 
     * 
     * @throws none
     * @since 1.0
     * @author Mahbubur Rahman
     */
    public ArrayList<Integer> getSelectedListIds()
    {
	ArrayList<Integer> listIds = new ArrayList<Integer>();
	try
	{
	    Iterator<Entry<Integer, Boolean>> it = mSelection.entrySet().iterator();
	    while (it.hasNext())
	    {
		Map.Entry<Integer, Boolean> pairs = it.next();
		// System.out.println(pairs.getKey() + " = " +
		// pairs.getValue());
		listIds.add(this.mMedList.get(pairs.getKey()).getId());

		it.remove(); // avoids a ConcurrentModificationException

	    }
	} catch (Exception e)
	{
	    e.printStackTrace();
	}

	return listIds;

    }

    /**
     * Gets the med list.
     * 
     * @return the medList
     */
    public ArrayList<MedicationDetails> getMedList()
    {
	return mMedList;
    }

    /**
     * Sets the med list.
     * 
     * @param medList
     *            the medList to set
     */
    public void setMedList(ArrayList<MedicationDetails> medList)
    {
	this.mMedList = medList;
    }

    /**
     * Sets the new selection.
     * 
     * @param position
     *            the position of in the adapter
     * @param value
     *            the value at adapter to given position.
     */
    public void setNewSelection(int position, boolean value)
    {
	mSelection.put(position, value);
	// this.nTaken++;
	notifyDataSetChanged();
    }

    /**
     * Checks if is position checked.
     * 
     * @param position
     *            the position of in the adapter
     * @return true, if is position checked
     */
    public boolean isPositionChecked(int position)
    {
	Boolean result = mSelection.get(position);
	return result == null ? false : result;
    }

    /**
     * Checks if any position is checked.
     * 
     * @return true, if is any position checked
     */
    public boolean isAnyPositionChecked()
    {
	Boolean result = false;
	try
	{
	    Iterator<Entry<Integer, Boolean>> it = mSelection.entrySet().iterator();
	    while (it.hasNext())
	    {
		Map.Entry<Integer, Boolean> pairs = it.next();
		// System.out.println(pairs.getKey() + " = " +
		// pairs.getValue());
		result = result || pairs.getValue();

		it.remove(); // avoids a ConcurrentModificationException
	    }
	} catch (Exception e)
	{
	    e.printStackTrace();
	}

	return result;
    }

    /**
     * Gets the current checked position.
     * 
     * @return the current checked position
     */
    public Set<Integer> getCurrentCheckedPosition()
    {
	return mSelection.keySet();
    }

    /**
     * Gets the single current check position. If not then return -1;
     * 
     * @return the single current checked position
     * @throws IllegalStateException
     *             ()
     */
    public int getSingleCurrentCheckPosition()
    {
	int positionSelected = -1;
	int count = 0;
	try
	{
	    Iterator<Entry<Integer, Boolean>> it = mSelection.entrySet().iterator();
	    while (it.hasNext())
	    {
		Map.Entry<Integer, Boolean> pairs = it.next();
		// System.out.println(pairs.getKey() + " = " +
		// pairs.getValue());
		positionSelected = pairs.getKey();
		count++;
		it.remove(); // avoids a ConcurrentModificationException
		if (count > 1)
		{

		    positionSelected = -1;
		    throw new IllegalStateException();
		}
	    }
	} catch (Exception e)
	{
	    e.printStackTrace();
	}

	return positionSelected;
    }

    /**
     * Removes the selection.
     * 
     * @param position
     *            the selected position
     */
    public void removeSelection(int position)
    {
	mSelection.remove(position);
	// this.nTaken--;
	notifyDataSetChanged();
    }

    /**
     * Clear all selection.
     */
    public void clearSelection()
    {
	mSelection = new HashMap<Integer, Boolean>();
	notifyDataSetChanged();
	this.nTaken = 0;

    }

    /**
     * This getView is Override method to give custom control over the views.
     * While creating view from the data, the existing items are tracked and if
     * any data listed as existing then items checkbox is selected and also
     * triggered as set item checked to display the action bar and also the
     * control logic of data selection.
     * <p>
     * This custom view builder also set action for checkbox selection. The
     * checkbox is integrated with selection listener. If user select or
     * deselect items in the list, the checkbox listener will work based on
     * selection. <br>
     * The view also characterize based on selection. If item/s selected, the
     * color is changed to specified color for whole items in the list.<br>
     * (non-Javadoc)
     * 
     * @see android.widget.Adapter#getView(int, android.view.View,
     *      android.view.ViewGroup)
     */
    @Override
    public View getView(final int position, View convertView, final ViewGroup parent)
    {
	MedicationDetails medicationInaPostion = this.mMedList.get(position);

	ViewHolder holder = null;
	Log.v("ConvertView", String.valueOf(position));
	// View parentView = convertView;
	if (convertView == null)
	{
	    LayoutInflater vi = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    convertView = vi.inflate(R.layout.activity_medication_listview, null);
	    /**
	     * implementing view holder for controlling the list items
	     */
	    holder = new ViewHolder();
	    holder.mTvmedname = (TextView) convertView.findViewById(R.id.tvmed_nameinlist);
	    holder.mTvmedname.setText(medicationInaPostion.getMedication_name());
	    // holder.tvmedname.setTag(medicationInaPostion.getId());
	    holder.mCb_med_taken = (CheckBox) convertView.findViewById(R.id.cbmed_taken);
	    holder.mCb_med_taken.setTag(medicationInaPostion);
	    if (this.mExistingIds.contains(medicationInaPostion.getId()))
	    {

		for (MedicationDetails lc : this.mMedList)
		{
		    if (lc.getId() == medicationInaPostion.getId())
		    {
			lc.isSelected = true;
			// holder.cb_med_taken.setChecked(true);
			if (!isPositionChecked(position))
			{
			    ((ListView) parent).setItemChecked(position, true);
			    Log.d(TAG, "Existing selected position checked");
			}
			// this.nTaken++;
		    }
		}
	    }
	    // holder.cb_med_taken.setText(medicationInaPostion.getDosage());
	    holder.mTvmeddosage = (TextView) convertView.findViewById(R.id.tvMedListDosage);
	    holder.mTvmeddosage.setText(medicationInaPostion.getDosage());
	    holder.mTvmedfreq = (TextView) convertView.findViewById(R.id.tvMedListFreq);
	    holder.mTvmedfreq.setText(medicationInaPostion.getFrequency());

	    this.mParent = parent;
	    this.mPosition = position;
	    holder.mCb_med_taken.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View v)
		{
		    CheckBox cb = (CheckBox) v;
		    MedicationDetails listcontent = (MedicationDetails) cb.getTag();
		    Log.d(TAG, "Clicked on Checkbox: " + cb.getText() + " is " + cb.isChecked());
		    listcontent.setSelected(cb.isChecked());
		    if (cb.isChecked())
		    {
			((ListView) parent).setItemChecked(position, true);
			Log.d(TAG, "One position is checked");
			// nTaken++;
		    }
		    else
		    {
			((ListView) parent).setItemChecked(position, false);
			Log.d(TAG, "One position is unchecked");
			// nTaken--;
		    }

		}
	    });
	    convertView.setTag(holder);
	}
	else
	{
	    holder = (ViewHolder) convertView.getTag();
	}
	// ListContents country = countryList.get(position);
	holder.mTvmedname.setText(medicationInaPostion.getMedication_name() + " (" + medicationInaPostion.getDosage() + ","
	        + medicationInaPostion.getFrequency() + ")");
	holder.mTvmeddosage.setText(medicationInaPostion.getDosage());
	holder.mCb_med_taken.setChecked(medicationInaPostion.isSelected());
	convertView.setBackgroundColor(this.mContext.getResources().getColor(android.R.color.background_light)); // default
	if (mSelection.get(position) != null)
	{
	    convertView.setBackgroundColor(this.mContext.getResources().getColor(android.R.color.holo_blue_light));

	    holder.mCb_med_taken.setChecked(true);

	    // this is a selected position so make it red
	}

	return convertView;
    }

    /**
     * The Class ViewHolder which is used to hold the view temporarily while
     * building the views from data.
     */
    private class ViewHolder
    {
	/** The tvmedfreq. */
	TextView mTvmedname, mTvmeddosage, mTvmedfreq;
	/** The cb_med_taken. */
	CheckBox mCb_med_taken;
    }

    // -------------------------------new add finished.............

}
