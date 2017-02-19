/*
 * @author  Mahbubur Rahman
 * IT Intern Summer 2014
 * EMMES Corporation
 * Copyright to EMMES Corporation 2014
 */

package com.emmes.aps.medication.fragments;

import java.util.ArrayList;

// TODO: Auto-generated Javadoc
/**
 * The Class MedicationInformation.
 */
public class MedicationInformation {

	static ArrayList<String> medicationName;//=new ArrayList<String>();
	boolean[] dataTaken;//=new boolean[20];
	int[] dosage;
	int[] freq;

	public MedicationInformation(){
		medicationName=new ArrayList<String>();
		medicationName.add("Benadryl ");
		medicationName.add("Tagamet  ");
		medicationName.add("Folic acid ");

		dataTaken=new boolean[20];
		for(int i=0;i<dataTaken.length;i++)
			dataTaken[i]=false;

		dosage=new int[20];
		for(int i=0;i<dosage.length;i++)
			dosage[i]=-1;
		freq=new int[20];
		for(int i=0;i<freq.length;i++)
			freq[i]=-1;
	};

	/** The Headlines. */
	static String[] Headlines = {
		"Article One",
		"Article Two"
	};

	/** The Details. */
	static String[] Details = {
		"Article One\n\nExcepteur pour-over occaecat squid biodiesel umami gastropub, nulla laborum salvia dreamcatcher fanny pack. Ullamco culpa retro ea, trust fund excepteur eiusmod direct trade banksy nisi lo-fi cray messenger bag. Nesciunt esse carles selvage put a bird on it gluten-free, wes anderson ut trust fund twee occupy viral. Laboris small batch scenester pork belly, leggings ut farm-to-table aliquip yr nostrud iphone viral next level. Craft beer dreamcatcher pinterest truffaut ethnic, authentic brunch. Esse single-origin coffee banksy do next level tempor. Velit synth dreamcatcher, magna shoreditch in american apparel messenger bag narwhal PBR ennui farm-to-table.",
		"Article Two\n\nVinyl williamsburg non velit, master cleanse four loko banh mi. Enim kogi keytar trust fund pop-up portland gentrify. Non ea typewriter dolore deserunt Austin. Ad magna ethical kogi mixtape next level. Aliqua pork belly thundercats, ut pop-up tattooed dreamcatcher kogi accusamus photo booth irony portland. Semiotics brunch ut locavore irure, enim etsy laborum stumptown carles gentrify post-ironic cray. Butcher 3 wolf moon blog synth, vegan carles odd future."
	};

	/** The Medicationname. */
	static String[] Medicationname = {
		"Benadryl",
		"Tagamet",
		"Folic acid ",
		"Medication 4"
	};

	/** The Dosage. */
	static String[] Dosage = {
		"50mg",
		"200mg",
		"1mg",
		"4"
	};

	/** The Freq. */
	static String[] Freq = {
		"2x/day",
		"1x/day",
		"1x/day",
		"4"
	};
	/**
	 * @return the dataTaken
	 */
	public boolean[] getDataTaken() {
		return dataTaken;
	}
	/**
	 * @param dataTaken the dataTaken to set
	 */
	public void setDataTaken(boolean[] dataTaken) {
		this.dataTaken = dataTaken;
	}
	/**
	 * @return the dosage
	 */
	public int[] getDosage() {
		return dosage;
	}
	/**
	 * @param dosage the dosage to set
	 */
	public void setDosage(int[] dosage) {
		this.dosage = dosage;
	}
	/**
	 * @return the freq
	 */
	public int[] getFreq() {
		return freq;
	}
	/**
	 * @param freq the freq to set
	 */
	public void setFreq(int[] freq) {
		this.freq = freq;
	}
	/**
	 * @return the medicationname
	 */
	public static String[] getMedicationname() {
		return Medicationname;
	}
	/**
	 * @param medicationname the medicationname to set
	 */
	public static void setMedicationname(String[] medicationname) {
		Medicationname = medicationname;
	}
	public ArrayList<String> getArrayList(){

		return medicationName;
	}
	public boolean getTakenData(int pos)
	{
		return dataTaken[pos];
	}
	
	public void addMedication(String name,int dosagetoday,int frequency,boolean taken)
	{
		dataTaken[medicationName.size()]=taken;
		
		freq[medicationName.size()]=frequency;
		dosage[medicationName.size()]=dosagetoday;
		medicationName.add(name);
	}
	
	public void RemoveMedication(int position)
	{
		dataTaken[position]=false;
		
		freq[position]=-1;
		dosage[position]=-1;
		medicationName.remove(position);
	}
	
}
