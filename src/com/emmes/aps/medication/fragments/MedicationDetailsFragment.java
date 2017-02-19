/*
 * @author  Mahbubur Rahman
 * IT Intern Summer 2014
 * EMMES Corporation
 * Copyright to EMMES Corporation 2014
 */

package com.emmes.aps.medication.fragments;

import com.emmes.aps.R;
import com.emmes.aps.medication.fragments.MedicationListFragment.operationType;

import android.support.v4.app.Fragment;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

// TODO: Auto-generated Javadoc
/**
 * The Class MedicationDetailsFragment.
 */
public class MedicationDetailsFragment extends Fragment {

	/** The Constant ARG_POSITION. */
	final static String ARG_POSITION = "position";
	final static String OP_MODE="opmode";

	EditText medicationName;// = (EditText) getActivity().findViewById(R.id.tvmed_medicationname);
	EditText medicationDosage;// = (EditText) getActivity().findViewById(R.id.edtmed_dosage);
	EditText medicationFreq;// = (EditText) getActivity().findViewById(R.id.edtmed_freq);
	//CheckBox cbmedicationTaken;//=(CheckBox) getActivity().findViewById(R.id.cbmed_taken);
	Button sbmedicationSubmit;// =(Button) getActivity().findViewById(R.id.btnmed_submit);
	Button sbmedicationRemove;// =(Button) getActivity().findViewById(R.id.btnmed_submit);

	TextView tvmed_medicationlabel;//=(TextView)getActivity().findViewById(R.id.tvmedlabel_medname);
	TextView tvmed_dosagelabel;//=(TextView)getActivity().findViewById(R.id.tvmedlabel_dosage);
	TextView tvmed_freqlabel;//=(TextView)getActivity().findViewById(R.id.tvmedlabel_freq);

	OnSubmitSelectedListener mCallBackSubmit;
	MedicationInformation medinfo=new MedicationInformation();
	/** The m current position. */
	int mCurrentPosition = -1;
	MedicationListFragment.operationType mCurrentMode=operationType.VIEW;


	public interface OnSubmitSelectedListener {

		/**
		 *  Called by HeadlinesFragment when a list item is selected.
		 *
		 * 
		 */
		public void onSelectSubmitButton();
	}


	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, 
			Bundle savedInstanceState) {

		// If activity recreated (such as from screen rotate), restore
		// the previous article selection set by onSaveInstanceState().
		// This is primarily necessary when in the two-pane layout.
		if (savedInstanceState != null) {
			mCurrentPosition = savedInstanceState.getInt(ARG_POSITION);
			mCurrentMode=savedInstanceState.getParcelable(OP_MODE);
		}

		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.medicationdetail_view, container, false);
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onStart()
	 */
	@Override
	public void onStart() {
		super.onStart();

		// During startup, check if there are arguments passed to the fragment.
		// onStart is a good place to do this because the layout has already been
		// applied to the fragment at this point so we can safely call the method
		// below that sets the article text.
		Bundle args = getArguments();
		if (args != null) {
			// Set article based on argument passed in
			updateMedicationDetailView(args.getInt(ARG_POSITION),(operationType) args.getSerializable(OP_MODE));
		} else if (mCurrentPosition != -1) {
			// Set article based on saved instance state defined during onCreateView
			updateMedicationDetailView(mCurrentPosition,mCurrentMode);
		}
	}

	/**
	 * Update article view.
	 *
	 * @param position the position
	 */
	public void updateMedicationDetailView(int position,MedicationListFragment.operationType opMode) {
		medicationName = (EditText) getActivity().findViewById(R.id.actvmed_medicationname);
		medicationDosage = (EditText) getActivity().findViewById(R.id.actvmed_dosage);
		medicationFreq = (EditText) getActivity().findViewById(R.id.edtmed_freq);
		//cbmedicationTaken=(CheckBox) getActivity().findViewById(R.id.cbmed_taken);
		sbmedicationSubmit =(Button) getActivity().findViewById(R.id.btnmed_submit);
		sbmedicationRemove=(Button) getActivity().findViewById(R.id.btn_med_remove);
		tvmed_medicationlabel=(TextView)getActivity().findViewById(R.id.tvmedlabel_medname);
		tvmed_dosagelabel=(TextView)getActivity().findViewById(R.id.tvmedlabel_dosage);
		tvmed_freqlabel=(TextView)getActivity().findViewById(R.id.tvmedlabel_freq);
		sbmedicationSubmit.setOnClickListener(mSubmitHandler);
		sbmedicationRemove.setOnClickListener(mRemoveHandler);

		/**
		 * update fields based on the opMode
		 */
		if(opMode.equals(MedicationListFragment.operationType.ADD))
		{
			medicationName.setVisibility(View.VISIBLE);
			medicationDosage.setVisibility(View.VISIBLE);
			medicationFreq.setVisibility(View.VISIBLE);
			//cbmedicationTaken.setVisibility(View.VISIBLE);
			tvmed_medicationlabel.setVisibility(View.VISIBLE);
			tvmed_dosagelabel.setVisibility(View.VISIBLE);
			tvmed_freqlabel.setVisibility(View.VISIBLE);
			sbmedicationSubmit.setVisibility(View.VISIBLE);
			sbmedicationRemove.setVisibility(View.VISIBLE);
			medicationName.setText(null);
			medicationName.setEnabled(true);
			medicationDosage.setText(null);
			medicationDosage.setEnabled(true);
			medicationFreq.setText(null);
			medicationFreq.setEnabled(true);
			//cbmedicationTaken.setChecked(false);
			sbmedicationSubmit.setText("Done");
		}
		else if(opMode.equals(MedicationListFragment.operationType.DELETE))
		{
			medicationName.setText(MedicationInformation.Medicationname[position]);
			medicationName.setEnabled(false);
			medicationDosage.setText(MedicationInformation.Dosage[position], TextView.BufferType.EDITABLE);
			medicationDosage.setEnabled(false);
			medicationFreq.setText(MedicationInformation.Freq[position], TextView.BufferType.EDITABLE);
			medicationFreq.setEnabled(false);
			//cbmedicationTaken.setEnabled(false);
			//cbmedicationTaken.setSelected(false);
			//sbmedicationSubmit.setText("Confirm?");
		}
		else if(opMode.equals(MedicationListFragment.operationType.VIEW)){
			medicationName.setVisibility(View.GONE);
			medicationDosage.setVisibility(View.GONE);
			medicationFreq.setVisibility(View.GONE);
			//cbmedicationTaken.setVisibility(View.GONE);
			tvmed_medicationlabel.setVisibility(View.GONE);
			tvmed_dosagelabel.setVisibility(View.GONE);
			tvmed_freqlabel.setVisibility(View.GONE);
			sbmedicationSubmit.setVisibility(View.GONE);
			sbmedicationRemove.setVisibility(View.GONE);

		}
		else if(opMode.equals(MedicationListFragment.operationType.ITEMSELECTED))
		{
			medicationName.setVisibility(View.VISIBLE);
			medicationDosage.setVisibility(View.VISIBLE);
			medicationFreq.setVisibility(View.VISIBLE);
			//cbmedicationTaken.setVisibility(View.VISIBLE);
			tvmed_medicationlabel.setVisibility(View.VISIBLE);
			tvmed_dosagelabel.setVisibility(View.VISIBLE);
			tvmed_freqlabel.setVisibility(View.VISIBLE);
			sbmedicationSubmit.setVisibility(View.VISIBLE);
			sbmedicationSubmit.setText("Submit");
			sbmedicationRemove.setVisibility(View.VISIBLE);

			medicationName.setText(MedicationInformation.Medicationname[position]);
			medicationName.setEnabled(false);
			medicationDosage.setText(MedicationInformation.Dosage[position], TextView.BufferType.EDITABLE);
			medicationDosage.setEnabled(true);
			medicationFreq.setText(MedicationInformation.Freq[position], TextView.BufferType.EDITABLE);
			medicationFreq.setEnabled(true);
			//cbmedicationTaken.setSelected(false);
			//cbmedicationTaken.setEnabled(true);
		}
		mCurrentPosition = position;
		mCurrentMode=opMode;
	}
	/**
	 * Handling button event
	 */
	private OnClickListener mSubmitHandler = new OnClickListener() {     
		public void onClick(View v) 
		{         // Do something in response to the click  
			if(mCurrentMode.equals(operationType.VIEW)||mCurrentMode.equals(operationType.ITEMSELECTED)){
				Toast.makeText(getActivity(), "View selected", Toast.LENGTH_SHORT).show();
			}
			else if(mCurrentMode.equals(operationType.ADD)){



				//mCallBackSubmit.onSelectSubmitButton();
				Toast.makeText(getActivity(), "ADD selected", Toast.LENGTH_SHORT).show();
			}else if(mCurrentMode.equals(operationType.DELETE)){
				//mCallBackSubmit.onSelectSubmitButton();
				Toast.makeText(getActivity(), "REMOVE selected", Toast.LENGTH_SHORT).show();
			}

		} 
	}; 
	/**
	 * Handling button event
	 */
	private OnClickListener mRemoveHandler = new OnClickListener() {     
		public void onClick(View v) 
		{         // Do something in response to the click  
			//if(mCurrentMode.equals(operationType.VIEW)||mCurrentMode.equals(operationType.ITEMSELECTED)){
				Toast.makeText(getActivity(), "you seleted to delete", Toast.LENGTH_SHORT).show();
			//}
			//else if(mCurrentMode.equals(operationType.ADD)){



				//mCallBackSubmit.onSelectSubmitButton();
				Toast.makeText(getActivity(), "ADD selected", Toast.LENGTH_SHORT).show();
			//}else if(mCurrentMode.equals(operationType.DELETE)){
				//mCallBackSubmit.onSelectSubmitButton();
			//	Toast.makeText(getActivity(), "REMOVE selected", Toast.LENGTH_SHORT).show();
			//}

		} 
	}; 
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onSaveInstanceState(android.os.Bundle)
	 */
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		// Save the current article selection in case we need to recreate the fragment
		outState.putInt(ARG_POSITION, mCurrentPosition);
	}


	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		// This makes sure that the container activity has implemented
		// the callback interface. If not, it throws an exception.
		try {
			mCallBackSubmit = (OnSubmitSelectedListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnHeadlineSelectedListener");
		}
	}
}