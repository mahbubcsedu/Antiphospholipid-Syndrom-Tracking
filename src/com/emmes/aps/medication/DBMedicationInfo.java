/* 
  * Copyright 2014 (C) The EMMES Corporation 
  *  
  * Created on : 02-06-2014
  * Last Update: Jun 18, 2014 9:07:31 AM
  * Author     : Mahbubur Rahman
  * Title      : Summer intern 2014 
  * Project    : Daily Diary Android Application
  * 
  */
package com.emmes.aps.medication;

// TODO: Auto-generated Javadoc
/**
 * The Class DBMedicationInfo is for handling the list item detail info. The
 * medication is dislayed in a list by the listactivity
 * {@linkplain com.emmes.aps.medication.MedicationListActivity}. The list item
 * is the object of type DBMedicationInfo.
 * 
 * @author Mahbubur Rahman 
 * @since 1.0
 */
public class DBMedicationInfo {

	/** The medication name. */
	String mMedicationName;

	/** The medication dosage. */
	String mMedicationDosage;

	/** The medication idin db. */
	String mMedicationIdinDB;

	/** The medication entry date. */
	String mMedicationEntryDate;

	/**
	 * Instantiates a new DB medication info.
	 */
	public DBMedicationInfo() {
		super();
	}

	/**
	 * Gets the medication name.
	 *
	 * @return the medicationName
	 */
	public String getMedicationName() {
		return mMedicationName;
	}

	/**
	 * Sets the medication name.
	 *
	 * @param medicationName the medicationName to set
	 */
	public void setMedicationName(String medicationName) {
		this.mMedicationName = medicationName;
	}

	/**
	 * Gets the medication dosage.
	 *
	 * @return the medicationDosage
	 */
	public String getMedicationDosage() {
		return mMedicationDosage;
	}

	/**
	 * Sets the medication dosage.
	 *
	 * @param medicationDosage the medicationDosage to set
	 */
	public void setMedicationDosage(String medicationDosage) {
		this.mMedicationDosage = medicationDosage;
	}

	/**
	 * Gets the medication idin db.
	 *
	 * @return the medicationIdinDB
	 */
	public String getMedicationIdinDB() {
		return mMedicationIdinDB;
	}

	/**
	 * Sets the medication idin db.
	 *
	 * @param medicationIdinDB the medicationIdinDB to set
	 */
	public void setMedicationIdinDB(String medicationIdinDB) {
		this.mMedicationIdinDB = medicationIdinDB;
	}

	/**
	 * Gets the medication entry date.
	 *
	 * @return the medicationEntryDate
	 */
	public String getMedicationEntryDate() {
		return mMedicationEntryDate;
	}

	/**
	 * Sets the medication entry date.
	 *
	 * @param medicationEntryDate the medicationEntryDate to set
	 */
	public void setMedicationEntryDate(String medicationEntryDate) {
		this.mMedicationEntryDate = medicationEntryDate;
	}

	/**
	 * Instantiates a new DB medication info.
	 *
	 * @param medicationName the medication name
	 * @param medicationDosage the medication dosage
	 * @param medicationIdinDB the medication idin db
	 * @param medicationEntryDate the medication entry date
	 */
	public DBMedicationInfo(String medicationName, String medicationDosage,
			String medicationIdinDB, String medicationEntryDate) {
		super();
		this.mMedicationName = medicationName;
		this.mMedicationDosage = medicationDosage;
		this.mMedicationIdinDB = medicationIdinDB;
		this.mMedicationEntryDate = medicationEntryDate;
	}

}
