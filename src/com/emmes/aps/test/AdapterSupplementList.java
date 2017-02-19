package com.emmes.aps.test;

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
import android.widget.Toast;

import com.emmes.aps.R;
import com.emmes.aps.medication.MedicationDetails;

public class AdapterSupplementList extends BaseAdapter
{

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
    final static private String TAG = "AdapterMedicationList";
    private HashMap<Integer, Boolean> mSelection = new HashMap<Integer, Boolean>();
    CheckBox cb;
    int position;
    ViewGroup parent;
    private int nTaken;

    public AdapterSupplementList(Context context, ArrayList<MedicationDetails> listdata, ArrayList<Integer> existingIds) {
	this.context = context;
	this.medList = listdata;
	this.existingIds = existingIds;
	this.nTaken=0;
    }

    /**
     * @return the nTaken
     */
    public int getnTaken()
    {
        return nTaken;
    }

    /**
     * @param nTaken the nTaken to set
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
	return this.medList.size();
    }

    /* (non-Javadoc)
     * 
     * @see android.widget.Adapter#getItem(int) */
    @Override
    public Object getItem(int position)
    {
	// TODO Auto-generated method stub
	return this.medList.get(position);
    }

    /* (non-Javadoc)
     * 
     * @see android.widget.Adapter#getItemId(int) */
    @Override
    public long getItemId(int position)
    {
	// TODO Auto-generated method stub
	return this.medList.get(position).getId();
    }

    public ArrayList<Integer> getSelectedListIds()    
    {
	ArrayList<Integer> listIds=new ArrayList<Integer>();
	try
	{
	    Iterator<Entry<Integer, Boolean>> it = mSelection.entrySet().iterator();
	    while (it.hasNext())
	    {
		Map.Entry<Integer, Boolean> pairs = (Map.Entry<Integer, Boolean>) it.next();
		// System.out.println(pairs.getKey() + " = " +
                // pairs.getValue());
		listIds.add(this.medList.get(pairs.getKey()).getId());
		
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
	return medList;
    }

    /**
     * Sets the med list.
     * 
     * @param medList
     *            the medList to set
     */
    public void setMedList(ArrayList<MedicationDetails> medList)
    {
	this.medList = medList;
    }

    public void setNewSelection(int position, boolean value)
    {
	mSelection.put(position, value);
	notifyDataSetChanged();
    }

    public boolean isPositionChecked(int position)
    {
	Boolean result = mSelection.get(position);
	return result == null ? false : result;
    }

    public boolean isAnyPositionChecked()
    {
	Boolean result=false;
	try
	{
	    Iterator<Entry<Integer, Boolean>> it = mSelection.entrySet().iterator();
	    while (it.hasNext())
	    {
		Map.Entry<Integer, Boolean> pairs = (Map.Entry<Integer, Boolean>) it.next();
		// System.out.println(pairs.getKey() + " = " +
                // pairs.getValue());
		result=result || pairs.getValue();
		
		it.remove(); // avoids a ConcurrentModificationException
			    }
	} catch (Exception e)
	{
	    e.printStackTrace();
	}
	
	return result;
    }
    public Set<Integer> getCurrentCheckedPosition()
    {
	return mSelection.keySet();
    }

    public int getSingleCurrentCheckPosition()
    {
	int positionSelected = -1;
	int count = 0;
	try
	{
	    Iterator<Entry<Integer, Boolean>> it = mSelection.entrySet().iterator();
	    while (it.hasNext())
	    {
		Map.Entry<Integer, Boolean> pairs = (Map.Entry<Integer, Boolean>) it.next();
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

    public void removeSelection(int position)
    {
	mSelection.remove(position);
	notifyDataSetChanged();
    }

    public void clearSelection()
    {
	mSelection = new HashMap<Integer, Boolean>();
	notifyDataSetChanged();
	this.nTaken=0;
	
    }

    /* (non-Javadoc)
     * 
     * @see android.widget.Adapter#getView(int, android.view.View,
     * android.view.ViewGroup) */
    @Override
    public View getView(final int position, View convertView, final ViewGroup parent)
    {
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
	    holder.cb_med_taken.setTag(medicationInaPostion);
	    if (this.existingIds.contains(medicationInaPostion.getId()))
	    {
		//holder.cb_med_taken.setChecked(true);
		if(!isPositionChecked(position)){
		((ListView) parent).setItemChecked(position, true);
		}
		for (MedicationDetails lc : this.medList)
		{
		    if (lc.getId() == medicationInaPostion.getId()){
			lc.isSelected = true;
			this.nTaken++;
		    }
		}
	    }
	    // holder.cb_med_taken.setText(medicationInaPostion.getDosage());
	    holder.tvmeddosage = (TextView) convertView.findViewById(R.id.tvMedListDosage);
	    holder.tvmeddosage.setText(medicationInaPostion.getDosage());
	    holder.tvmedfreq = (TextView) convertView.findViewById(R.id.tvMedListFreq);
	    holder.tvmedfreq.setText(medicationInaPostion.getFrequency());

	    this.parent = parent;
	    this.position = position;
	    holder.cb_med_taken.setOnClickListener(new View.OnClickListener() {
		public void onClick(View v)
		{
		    CheckBox cb = (CheckBox) v;
		    MedicationDetails listcontent = (MedicationDetails) cb.getTag();
		    Toast.makeText(context, "Clicked on Checkbox: " + cb.getText() + " is " + cb.isChecked(), Toast.LENGTH_SHORT).show();
		    listcontent.setSelected(cb.isChecked());
		    if (cb.isChecked())
		    {
			((ListView) parent).setItemChecked(position, true);
		    }
		    else
		    {
			((ListView) parent).setItemChecked(position, false);
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
	holder.tvmedname.setText(medicationInaPostion.getMedication_name() + " (" + medicationInaPostion.getDosage() + ","
	        + medicationInaPostion.getFrequency() + ")");
	holder.tvmeddosage.setText(medicationInaPostion.getDosage());
	holder.cb_med_taken.setChecked(medicationInaPostion.isSelected());
	convertView.setBackgroundColor(this.context.getResources().getColor(android.R.color.background_light)); // default
	if (mSelection.get(position) != null)
	{
	    convertView.setBackgroundColor(this.context.getResources().getColor(android.R.color.holo_blue_light));

	    holder.cb_med_taken.setChecked(true);

	    // this is a selected position so make it red
	}

	return convertView;
    }

    /**
     * The Class ViewHolder.
     */
    private class ViewHolder
    {
	/** The tvmedfreq. */
	TextView tvmedname, tvmeddosage, tvmedfreq;
	/** The cb_med_taken. */
	CheckBox cb_med_taken;
    }

    // -------------------------------new add finished.............

    /* ArrayList<String> data; // Context context;
     * 
     * public AdapterMedicationList(Context context, int resource, int
     * textViewResourceId, ArrayList<String> objects) { super(context, resource,
     * textViewResourceId, objects); this.data = objects; this.context =
     * context; }
     * 
     * public void setNewSelection(int position, boolean value) {
     * mSelection.put(position, value); notifyDataSetChanged(); }
     * 
     * public boolean isPositionChecked(int position) { Boolean result =
     * mSelection.get(position); return result == null ? false : result; }
     * 
     * public Set<Integer> getCurrentCheckedPosition() { return
     * mSelection.keySet(); }
     * 
     * public void removeSelection(int position) { mSelection.remove(position);
     * notifyDataSetChanged(); }
     * 
     * public void clearSelection() { mSelection = new HashMap<Integer,
     * Boolean>(); notifyDataSetChanged(); }
     * 
     * if (row == null) { LayoutInflater vi = (LayoutInflater)
     * this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE); row =
     * vi.inflate(R.layout.row_list_item, null); holder = new ViewHolder(); //
     * and set its textView field to the proper value holder.cb_med_taken =
     * (CheckBox) row.findViewById(R.id.cbmed_taken);
     * holder.cb_med_taken.setChecked(false); // and store it as the 'tag' of
     * our view row.setTag(holder); } else { holder = (ViewHolder) row.getTag();
     * }
     * 
     * row.setBackgroundColor(this.context.getResources().getColor(android.R.color
     * .background_light)); // default
     * 
     * if (mSelection.get(position) != null) {
     * row.setBackgroundColor(this.context
     * .getResources().getColor(android.R.color.holo_blue_light));
     * 
     * holder.cb_med_taken.setChecked(true);
     * 
     * // this is a selected position so make it red } this.parent = parent;
     * this.position = position; Log.d(TAG, position + " postion created view");
     * 
     * holder.cb_med_taken.setOnClickListener(new OnClickListener() {
     * 
     * @Override public void onClick(View v) { // TODO Auto-generated method
     * stub CheckBox cb = (CheckBox) v; if (cb.isChecked()) { ((ListView)
     * parent).setItemChecked(position, true); } else { ((ListView)
     * parent).setItemChecked(position, false); } Log.d(TAG, position +
     * " position selected"); } });
     * 
     * return row; } */

}
