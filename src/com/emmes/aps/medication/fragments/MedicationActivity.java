/*
 * @author  Mahbubur Rahman
 * IT Intern Summer 2014
 * EMMES Corporation
 * Copyright to EMMES Corporation 2014
 */

package com.emmes.aps.medication.fragments;

import com.emmes.aps.R;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

// TODO: Auto-generated Javadoc
/**
 * The Class MedicationActivity.
 */
public class MedicationActivity extends FragmentActivity 
        implements MedicationListFragment.OnMedicationSelectedListener, MedicationDetailsFragment.OnSubmitSelectedListener{
	
	String firstFragtag;

    /**
     *  Called when the activity is first created.
     *
     * @param savedInstanceState the saved instance state
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medication);

        // Check whether the activity is using the layout version with
        // the fragment_container FrameLayout. If so, we must add the first fragment
        if (findViewById(R.id.fragment_container) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            // Create an instance of ExampleFragment
            MedicationListFragment firstFragment = new MedicationListFragment();

            // In case this activity was started with special instructions from an Intent,
            // pass the Intent's extras to the fragment as arguments
            firstFragment.setArguments(getIntent().getExtras());
            //firstFragtag=firstFragment.getTag();
            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, firstFragment).commit();
            //firstFragtag=firstFragment.getTag();
        }
    }

    /* (non-Javadoc)
     * @see com.emmes.aps.medication.MedicationListFragment.OnMedicationSelectedListener#onMedicationSelected(int)
     */
    public void onMedicationSelected(int position,MedicationListFragment.operationType opMode) {
        // The user selected the headline of an article from the HeadlinesFragment

        // Capture the article fragment from the activity layout
        MedicationDetailsFragment medicationFrag = (MedicationDetailsFragment)
                getSupportFragmentManager().findFragmentById(R.id.medicationdetail_fragment);

        if (medicationFrag != null) {
            // If article frag is available, we're in two-pane layout...
            // Call a method in the ArticleFragment to update its content
        	medicationFrag.updateMedicationDetailView(position,opMode);

        } else {
            // If the frag is not available, we're in the one-pane layout and must swap frags...

            // Create fragment and give it an argument for the selected article
            MedicationDetailsFragment newFragment = new MedicationDetailsFragment();
            Bundle args = new Bundle();
            args.putInt(MedicationDetailsFragment.ARG_POSITION, position);
            args.putSerializable(MedicationDetailsFragment.OP_MODE, opMode);
            newFragment.setArguments(args);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            // Replace whatever is in the fragment_container view with this fragment,
            // and add the transaction to the back stack so the user can navigate back
            transaction.replace(R.id.fragment_container, newFragment);
            transaction.addToBackStack(null);

            // Commit the transaction
            transaction.commit();
        }
    }

	@Override
	public void onSelectSubmitButton() {
		// TODO Auto-generated method stub
		
		 // Capture the article fragment from the activity layout
		MedicationListFragment medicationFrag = (MedicationListFragment)
				getSupportFragmentManager().findFragmentById(R.id.medication_list);//.findFragmentById(R.id.medication_list);

        if (medicationFrag != null) {
            // If article frag is available, we're in two-pane layout...
            // Call a method in the ArticleFragment to update its content
        	medicationFrag.updateMedicationListView();

        }else {
            // If the frag is not available, we're in the one-pane layout and must swap frags...

            // Create fragment and give it an argument for the selected article
        	MedicationListFragment newFragment = new MedicationListFragment();
            //Bundle args = new Bundle();
            //args.putInt(MedicationDetailsFragment.ARG_POSITION, position);
            //args.putSerializable(MedicationDetailsFragment.OP_MODE, opMode);
            //newFragment.setArguments(args);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            // Replace whatever is in the fragment_container view with this fragment,
            // and add the transaction to the back stack so the user can navigate back
            transaction.replace(R.id.fragment_container, newFragment);
            transaction.addToBackStack(null);

            // Commit the transaction
            transaction.commit();
        }
		
	}
}