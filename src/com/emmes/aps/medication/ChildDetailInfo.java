/* 
 * Copyright 2014 (C) The EMMES Corporation 
 *  
 * Created on : 02-06-2014
 * Last Update: Aug 18, 2014 4:07:41 PM
 * Author     : Mahbubur Rahman
 * Title      : Summer intern 2014 
 * Project    : Daily Diary Android Application
 * 
 */
package com.emmes.aps.medication;

// TODO: Auto-generated Javadoc
/**
 * The Class ChildDetailInfo is for handling the list item detail info. The
 * medication is dislayed in a list by the listactivity
 * {@linkplain com.emmes.aps.medication.MedicationListActivity}. The list item
 * is the object of type ChildDetailsInfo.
 * 
 * @author Mahbubur Rahman 
 * @since 1.0
 */
public class ChildDetailInfo
{

    /** The medicaiton name. */
    String medicaitonName;

    /** The med dosage. */
    String medDosage;

    /** The freq. */
    Integer freq;

    /** The med seq. */
    String medSeq;

    /**
     * Gets the medicaiton name.
     * 
     * @return the medicaitonName
     */
    public String getMedicaitonName()
    {
	return medicaitonName;
    }

    /**
     * Sets the medicaiton name.
     * 
     * @param medicaitonName
     *            the medicaitonName to set
     */
    public void setMedicaitonName(String medicaitonName)
    {
	this.medicaitonName = medicaitonName;
    }

    /**
     * Gets the med dosage.
     * 
     * @return the medDosage
     */
    public String getMedDosage()
    {
	return medDosage;
    }

    /**
     * Sets the med dosage.
     * 
     * @param medDosage
     *            the medDosage to set
     */
    public void setMedDosage(String medDosage)
    {
	this.medDosage = medDosage;
    }

    /**
     * Gets the freq.
     * 
     * @return the freq
     */
    public Integer getFreq()
    {
	return freq;
    }

    /**
     * Sets the freq.
     * 
     * @param freq
     *            the freq to set
     */
    public void setFreq(Integer freq)
    {
	this.freq = freq;
    }

    /**
     * Gets the med seq.
     * 
     * @return the medSeq
     */
    public String getMedSeq()
    {
	return medSeq;
    }

    /**
     * Sets the med seq.
     * 
     * @param medSeq
     *            the medSeq to set
     */
    public void setMedSeq(String medSeq)
    {
	this.medSeq = medSeq;
    }

    /**
     * Instantiates a new child detail info.
     * 
     * @param medicaitonInfo
     *            the medicaiton info
     * @param medSeq
     *            the med seq
     */
    public ChildDetailInfo(String medicaitonInfo, String medSeq) {
	super();
	this.medicaitonName = medicaitonInfo;
	this.medSeq = medSeq;
    }

    /**
     * Instantiates a new child detail info.
     */
    public ChildDetailInfo() {
	super();
    }
}
