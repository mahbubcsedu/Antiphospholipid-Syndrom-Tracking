package com.emmes.aps.medication.fragments;

import java.util.ArrayList;

import com.emmes.aps.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MedicationListAdapter extends BaseAdapter {
	Context context;
	ArrayList<String> medicationName;

	

	public MedicationListAdapter(Context context, ArrayList<String> medicationName) {
		super();
	
		this.context = context;
		this.medicationName = medicationName;
	}

	@Override
	public int getCount() {
		return medicationName.size();
	}

	@Override
	public Object getItem(int position) {
		return medicationName.get(position);
	}

	@Override
	public long getItemId(int id) {
		return id;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		String medicationInaPostion = (String) getItem(position);
		
		View parentView = convertView;
		
		if (parentView == null) {
			LayoutInflater vi = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			parentView = vi.inflate(R.layout.activity_medication_listview, null);
		}

		
		TextView medicationName = (TextView) parentView
				.findViewById(R.id.tvmed_nameinlist);
		medicationName.setText(medicationInaPostion);
		

		return parentView;
	}
}