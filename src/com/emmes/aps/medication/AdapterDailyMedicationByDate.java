/* 
  * Copyright 2014 (C) The EMMES Corporation 
  *  
  * Created on : 02-06-2014
  * Last Update: Jun 18, 2014 9:07:16 AM
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
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class AdapterDailyMedicationByDate extends BaseExpandableListAdapter{

	Context context;
	ArrayList<HeaderInfo> headerinfo=new ArrayList<HeaderInfo>();

	/**
	 * @return the headerinfo
	 */
	public ArrayList<HeaderInfo> getHeaderinfo() {
		return headerinfo;
	}

	/**
	 * @param headerinfo the headerinfo to set
	 */
	public void setHeaderinfo(ArrayList<HeaderInfo> headerinfo) {
		this.headerinfo = headerinfo;
	}

	public AdapterDailyMedicationByDate(Context context,
			ArrayList<HeaderInfo> headerinfo) {
		super();
		this.context = context;
		this.headerinfo = headerinfo;
	}



	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return this.headerinfo.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		ArrayList<ChildDetailInfo> medicationList=this.headerinfo.get(groupPosition).getChildInfo();
		return medicationList.size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return this.headerinfo.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		ArrayList<ChildDetailInfo> childItemList = headerinfo.get(groupPosition).getChildInfo();
		return childItemList.get(childPosition);
		//return childPosition;
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub

		//long getChildId=this.headerinfo.get(groupPosition).getChildInfo().get(childPosition);
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		HeaderInfo headerInfo = (HeaderInfo) getGroup(groupPosition);
		if (convertView == null) {
			LayoutInflater inf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inf.inflate(R.layout.group_list_header, null);
		}

		TextView heading = (TextView) convertView.findViewById(R.id.heading);

		heading.setText(headerInfo.getMedicationDate().trim());

		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final ChildDetailInfo childInfo = (ChildDetailInfo) getChild(groupPosition, childPosition);
		if (convertView == null) {
			LayoutInflater inf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inf.inflate(R.layout.group_list_child, null);
		}

		TextView sequence = (TextView) convertView.findViewById(R.id.sequence);
		sequence.setText(childInfo.getMedSeq().trim() + ") ");
		TextView childItem = (TextView) convertView.findViewById(R.id.childItem);
		childItem.setText(childInfo.getMedicaitonName().trim()+"("+childInfo.getMedDosage()+","+childInfo.getFreq()+"x/day)");

		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override	
	public void onGroupCollapsed(int groupPosition) {

		super.onGroupCollapsed(groupPosition);

	}



	@Override	
	public void onGroupExpanded(int groupPosition) {

		super.onGroupExpanded(groupPosition);

	}



}
