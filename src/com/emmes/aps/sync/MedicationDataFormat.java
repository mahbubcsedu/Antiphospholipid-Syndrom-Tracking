/* 
  * Copyright 2014 (C) The EMMES Corporation 
  *  
  * Created on : 02-06-2014
  * Last Update: Jul 2, 2014 1:11:31 PM
  * Author     : Mahbubur Rahman
  * Title      : Summer intern 2014 
  * Project    : Daily Diary Android Application
  * 
  */
package com.emmes.aps.sync;

// TODO: Auto-generated Javadoc
/**
 * The Class MedicationDataFormat.
 */
public class MedicationDataFormat {
	
	/** The medication name. */
	String medicationName;
	
	/** The Medication dose. */
	String MedicationDose;
	
	/** The how many times. */
	int howManyTimes;
	
	/** The when taken. */
	long whenTaken;
	
	/** The Medication type. */
	String MedicationType;
	
	/** The db id. */
	int dbId;



	/**
	 * Instantiates a new medication data format.
	 *
	 * @param medicationName the medication name
	 * @param medicationDose the medication dose
	 * @param howManyTimes the how many times
	 * @param whenTaken the when taken
	 * @param medicationType the medication type
	 * @param dbId the db id
	 */
	public MedicationDataFormat(String medicationName, String medicationDose,
			int howManyTimes, long whenTaken, String medicationType, int dbId) {
		super();
		this.medicationName = medicationName;
		MedicationDose = medicationDose;
		this.howManyTimes = howManyTimes;
		this.whenTaken = whenTaken;
		MedicationType = medicationType;
		this.dbId = dbId;
	}


	/**
	 * Instantiates a new medication data format.
	 */
	public MedicationDataFormat() {
		// TODO Auto-generated constructor stub
		this.medicationName="";
		this.MedicationDose="";
		this.howManyTimes=-1;
		this.whenTaken=0;
		this.MedicationType="";
		this.dbId=-1;
	}


	/**
	 * Gets the db id.
	 *
	 * @return the db id
	 */
	public int getDbId() {
		return dbId;
	}
	
	/**
	 * Sets the db id.
	 *
	 * @param dbId the new db id
	 */
	public void setDbId(int dbId) {
		this.dbId = dbId;
	}

	/**
	 * Gets the medication name.
	 *
	 * @return the medication name
	 */
	public String getMedicationName() {
		return medicationName;
	}
	
	/**
	 * Sets the medication name.
	 *
	 * @param medicationName the new medication name
	 */
	public void setMedicationName(String medicationName) {
		this.medicationName = medicationName;
	}
	
	/**
	 * Gets the medication dose.
	 *
	 * @return the medication dose
	 */
	public String getMedicationDose() {
		return MedicationDose;
	}
	
	/**
	 * Sets the medication dose.
	 *
	 * @param medicationDose the new medication dose
	 */
	public void setMedicationDose(String medicationDose) {
		MedicationDose = medicationDose;
	}
	
	/**
	 * Gets the how many times.
	 *
	 * @return the how many times
	 */
	public int getHowManyTimes() {
		return howManyTimes;
	}
	
	/**
	 * Sets the how many times.
	 *
	 * @param howManyTimes the new how many times
	 */
	public void setHowManyTimes(int howManyTimes) {
		this.howManyTimes = howManyTimes;
	}
	
	/**
	 * Gets the when taken.
	 *
	 * @return the when taken
	 */
	public long getWhenTaken() {
		return whenTaken;
	}
	
	/**
	 * Sets the when taken.
	 *
	 * @param whenTaken the new when taken
	 */
	public void setWhenTaken(long whenTaken) {
		this.whenTaken = whenTaken;
	}
	
	/**
	 * Gets the medication type.
	 *
	 * @return the medication type
	 */
	public String getMedicationType() {
		return MedicationType;
	}
	
	/**
	 * Sets the medication type.
	 *
	 * @param medicationType the new medication type
	 */
	public void setMedicationType(String medicationType) {
		MedicationType = medicationType;
	}
}
