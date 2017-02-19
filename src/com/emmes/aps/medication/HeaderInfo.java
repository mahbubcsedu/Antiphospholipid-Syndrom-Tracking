/* 
  * Copyright 2014 (C) The EMMES Corporation 
  *  
  * Created on : 02-06-2014
  * Last Update: Jun 18, 2014 9:07:35 AM
  * Author     : Mahbubur Rahman
  * Title      : Summer intern 2014 
  * Project    : Daily Diary Android Application
  * 
  */
package com.emmes.aps.medication;

import java.util.ArrayList;

public class HeaderInfo {
	String mMedicationDate;
	ArrayList<ChildDetailInfo> mChildInfo=new ArrayList<ChildDetailInfo>();
	/**
	 * @return the medicationDate
	 */
	public String getMedicationDate() {
		return mMedicationDate;
	}
	/**
	 * @param medicationDate the medicationDate to set
	 */
	public void setMedicationDate(String medicationDate) {
		this.mMedicationDate = medicationDate;
	}
	/**
	 * @return the childInfo
	 */
	public ArrayList<ChildDetailInfo> getChildInfo() {
		return mChildInfo;
	}
	/**
	 * @param childInfo the childInfo to set
	 */
	public void setChildInfo(ArrayList<ChildDetailInfo> childInfo) {
		this.mChildInfo = childInfo;
	}
}
