/* 
  * Copyright 2014 (C) The EMMES Corporation 
  *  
  * Created on : 02-06-2014
  * Last Update: Aug 18, 2014 4:25:00 PM
  * Author     : Mahbubur Rahman
  * Title      : Summer intern 2014 
  * Project    : Daily Diary Android Application
  * 
  */
package com.emmes.aps.medication;

// TODO: Auto-generated Javadoc
/**
 * The Class MedicationDetails is for handling the list item detail info and is used to generate custom view of the list item.
 * {@linkplain com.emmes.aps.medication.AdapterMedicationList#getView(int, android.view.View, android.view.ViewGroup)}.
 * 
 * @author Mahbubur Rahman 
 * @since 1.0
 */
public class MedicationDetails
{
    
    /** The m medication_name. */
    String mMedication_name;
    
    /** The m dosage. */
    String mDosage;
    
    /** The m frequency. */
    String mFrequency;
    
    /** The m id. */
    int mId;
    
    /** The is selected. */
    public boolean isSelected;

    // String status;
    /**
     * Instantiates a new medication details.
     */
    public MedicationDetails() {
	this.mMedication_name = "";
	this.mDosage = "";
	this.mFrequency = "";

    }

    /**
     * Instantiates a new medication details.
     *
     * @param id the id
     * @param medName the med name
     * @param dosage the dosage
     * @param freq the freq
     * @param isSelected the is selected
     */
    public MedicationDetails(int id, String medName, String dosage, String freq, boolean isSelected) {
	this.mId = id;
	this.mMedication_name = medName;
	this.mDosage = dosage;
	this.mFrequency = freq;
	this.isSelected = isSelected;
	// this.status=status;

    }

    /* public String getStatus() { return status; } public void setStatus(String
     * status) { this.status = status; } */
    /**
     * Checks if is selected.
     *
     * @return the isSelected
     */
    public boolean isSelected()
    {
	return isSelected;
    }

    /**
     * Sets the selected.
     *
     * @param isSelected            the isSelected to set
     */
    public void setSelected(boolean isSelected)
    {
	this.isSelected = isSelected;
    }

    /**
     * Gets the id.
     *
     * @return the id
     */
    public int getId()
    {
	return mId;
    }

    /**
     * Sets the id.
     *
     * @param id            the id to set
     */
    public void setId(int id)
    {
	this.mId = id;
    }

    /**
     * Gets the medication_name.
     *
     * @return the medication_name
     */
    public String getMedication_name()
    {
	return mMedication_name;
    }

    /**
     * Sets the medication_name.
     *
     * @param medication_name            the medication_name to set
     */
    public void setMedication_name(String medication_name)
    {
	this.mMedication_name = medication_name;
    }

    /**
     * Gets the dosage.
     *
     * @return the dosage
     */
    public String getDosage()
    {
	return mDosage;
    }

    /**
     * Sets the dosage.
     *
     * @param dosage            the dosage to set
     */
    public void setDosage(String dosage)
    {
	this.mDosage = dosage;
    }

    /**
     * Gets the frequency.
     *
     * @return the frequency
     */
    public String getFrequency()
    {
	return mFrequency;
    }

    /**
     * Sets the frequency.
     *
     * @param frequency            the frequency to set
     */
    public void setFrequency(String frequency)
    {
	this.mFrequency = frequency;
    }

}
