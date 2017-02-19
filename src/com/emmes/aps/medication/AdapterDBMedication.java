/* 
 * Copyright 2014 (C) The EMMES Corporation 
 *  
 * Created on : 02-06-2014
 * Last Update: Aug 18, 2014 3:46:37 PM
 * Author     : Mahbubur Rahman
 * Title      : Summer intern 2014 
 * Project    : Daily Diary Android Application
 * 
 */
package com.emmes.aps.medication;

import java.util.ArrayList;
import com.emmes.aps.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

// TODO: Auto-generated Javadoc
/**
 * The Class AdapterDBMedication is the observed data behind the list of
 * medication name that enables medication autocomplete textview to select items
 * from the list.
 * <p>
 * The adapter takes two sets of items, one is the all items which are in
 * database and another set is the filter set which is generated filtering out
 * the allsets based on the condition set by the user from the activity. For
 * example, if single character filtration is set, then after typing single
 * character, this adapter will filter out start with the typed character.
 * 
 * @author Mahbubur Rahman
 * @see ArrayList
 * @see ArrayAdapter
 * @since 1.0
 */
public class AdapterDBMedication extends ArrayAdapter<DBMedicationInfo>
{

    /** The context of the class which calls this adapter class. */
    Context mContext;

    /** The medication list arraylist. */
    ArrayList<DBMedicationInfo> mMedicationList;

    /** The view resource id. */
    private int mViewResourceId;

    /** The my debug tag. */
    private final String TAG = "AdapterDBMedication";
    // private ArrayList<DBMedicationInfo> items;
    /** The items all which keeps all the items retrieved from database. */
    private ArrayList<DBMedicationInfo> mItemsAll;

    /**
     * The suggestions is the arrayList consists of filtered out data based on
     * character typed and threshold.
     */
    private ArrayList<DBMedicationInfo> mSuggestions;

    /** The suggestonsize is the length of suggested medication. */
    int mSuggestonsize;

    /**
     * The mMinItemtoBeDisplayed stores what minimum number of item should be in
     * the suggested list to be displayed.
     */
    int mMinItemtoBeDisplayed;

    /**
     * Instantiates a new adapter both for medication or supplement list to
     * display. The constructor takes two different types of list, one is the
     * list of user added medications or supplement and another is the selected
     * items for today.
     * 
     * @author Mahbubur Rahman
     * @param context
     *            the context from where the constructor called
     * @param viewResourceId
     *            which resource will be used for this adapter to generate view.
     * @param medicationList
     *            this of the medication that will be retrieved and set for
     *            binding to the view.
     * @return none
     * @since 1.0
     */
    public AdapterDBMedication(Context context, int viewResourceId, ArrayList<DBMedicationInfo> medicationList) {
	super(context, viewResourceId, medicationList);
	this.mContext = context;

	this.mMedicationList = medicationList;
	this.mItemsAll = (ArrayList<DBMedicationInfo>) medicationList.clone();
	this.mSuggestions = new ArrayList<DBMedicationInfo>();

	this.mMedicationList = medicationList;
	this.mViewResourceId = viewResourceId;
	this.mSuggestonsize = 0;
	this.mMinItemtoBeDisplayed = 2000;
    }

    /**
     * Gets the medication list.
     * 
     * @return the medicationList
     */
    public ArrayList<DBMedicationInfo> getMedicationList()
    {
	return mMedicationList;
    }

    /**
     * Gets the suggestonsize.
     * 
     * @return the suggestonsize
     */
    public int getSuggestonsize()
    {
	return mSuggestonsize;
    }

    /**
     * Sets the suggestonsize.
     * 
     * @param suggestonsize
     *            the suggestonsize to set
     */
    public void setSuggestonsize(int suggestonsize)
    {
	this.mSuggestonsize = suggestonsize;
    }

    /* (non-Javadoc)
     * 
     * @see android.widget.ArrayAdapter#getCount() */
    @Override
    public int getCount()
    {
	// TODO Auto-generated method stub
	return this.mMedicationList.size();
    }

    /* (non-Javadoc)
     * 
     * @see android.widget.ArrayAdapter#getItem(int) */
    @Override
    public DBMedicationInfo getItem(int position)
    {
	// TODO Auto-generated method stub
	return this.mMedicationList.get(position);
    }

    /* (non-Javadoc)
     * 
     * @see android.widget.ArrayAdapter#getItemId(int) */
    @Override
    public long getItemId(int position)
    {
	// TODO Auto-generated method stub
	return position;
    }

    /**
     * Gets the min itemto be displayed.
     * 
     * @return the minItemtoBeDisplayed
     */
    public int getMinItemtoBeDisplayed()
    {
	return mMinItemtoBeDisplayed;
    }

    /**
     * Sets the min itemto be displayed.
     * 
     * @param minItemtoBeDisplayed
     *            the minItemtoBeDisplayed to set
     */
    public void setMinItemtoBeDisplayed(int minItemtoBeDisplayed)
    {
	this.mMinItemtoBeDisplayed = minItemtoBeDisplayed;
    }

    /* (non-Javadoc)
     * 
     * @see android.widget.ArrayAdapter#getView(int, android.view.View,
     * android.view.ViewGroup) */
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
	// TODO Auto-generated method stub
	DBMedicationInfo medication = this.mMedicationList.get(position);

	View v = convertView;
	if (v == null)
	{
	    LayoutInflater vi = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    v = vi.inflate(mViewResourceId, null);
	}
	// DBMedicationInfo customer = items.get(position);
	if (medication != null)
	{
	    TextView customerNameLabel = (TextView) v.findViewById(R.id.tvSimpleTextView);
	    if (customerNameLabel != null)
	    {
		// Log.i(MY_DEBUG_TAG,
// "getView Customer Name:"+customer.getName());
		customerNameLabel.setText(medication.getMedicationName());
	    }
	}
	return v;

    }

    /* (non-Javadoc)
     * 
     * @see android.widget.ArrayAdapter#getFilter() */
    @Override
    public Filter getFilter()
    {
	return mNameFilter;
    }

    /** The name filter. */
    Filter mNameFilter = new Filter() {
	@Override
	public String convertResultToString(Object resultValue)
	{
	    String str = ((DBMedicationInfo) (resultValue)).getMedicationName();
	    return str;
	}

	/* (non-Javadoc)
	 * 
	 * @see FilterResults android.widget.Filter#performFiltering */
	@Override
	protected FilterResults performFiltering(CharSequence constraint)
	{
	    if (constraint != null)
	    {
		mSuggestions.clear();
		for (DBMedicationInfo medication : mItemsAll)
		{
		    if (medication.getMedicationName().toLowerCase().startsWith(constraint.toString().toLowerCase()))
		    {
			mSuggestions.add(medication);
		    }
		}

		FilterResults filterResults = new FilterResults();
		if (mSuggestions.size() >= mMinItemtoBeDisplayed)
		{
		    filterResults.values = mSuggestions;
		    filterResults.count = mSuggestions.size();
		}
		mSuggestonsize = filterResults.count;
		return filterResults;
	    }
	    else
	    {
		mSuggestonsize = 0;
		return new FilterResults();
	    }
	}

	@Override
	protected void publishResults(CharSequence constraint, FilterResults results)
	{
	    ArrayList<DBMedicationInfo> filteredList = (ArrayList<DBMedicationInfo>) results.values;
	    if (results != null && results.count > 0)
	    {
		clear();
		for (DBMedicationInfo c : filteredList)
		{
		    add(c);
		}
		notifyDataSetChanged();
	    }
	}
    };
}
