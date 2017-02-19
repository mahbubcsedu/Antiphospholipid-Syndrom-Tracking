/*
 * @author  Mahbubur Rahman
 * IT Intern Summer 2014
 * EMMES Corporation
 * Copyright to EMMES Corporation 2014
 */

package com.emmes.aps.medication.fragments;

import com.emmes.aps.R;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

// TODO: Auto-generated Javadoc
/**
 * The Class MedicationListFragment.
 */
public class MedicationListFragment extends Fragment {

	/** The m callback. */
	OnMedicationSelectedListener mCallback;
	MedicationListAdapter adapter;
	int listpostionselected=-1;//only for remove item
	public enum operationType 
	{
		ADD(0),DELETE(1),VIEW(2),ITEMSELECTED(3);
		private int value;
		private operationType(int value)
		{
			this.value=value;
		}

	};
	operationType opMode=operationType.VIEW;

	/** The listview. */
	private ListView listview=null;
	MedicationInformation medinfo=new MedicationInformation();
	/** The btn medication addto list. */
	private Button btnMedicationAddtoList;

	/** The btn medication removefrom list. */
	private Button btnMedicationRemovefromList;
	// The container Activity must implement this interface so the frag can deliver messages
	/**
	 * The listener interface for receiving onMedicationSelected events.
	 * The class that is interested in processing a onMedicationSelected
	 * event implements this interface, and the object created
	 * with that class is registered with a component using the
	 * component's <code>addOnMedicationSelectedListener<code> method. When
	 * the onMedicationSelected event occurs, that object's appropriate
	 * method is invoked.
	 *
	 * @see OnMedicationSelectedEvent
	 */
	public interface OnMedicationSelectedListener {

		/**
		 *  Called by HeadlinesFragment when a list item is selected.
		 *
		 * @param position the position
		 */
		public void onMedicationSelected(int position,operationType opMode);
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onActivityCreated(android.os.Bundle)
	 */
	/*@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		//listview=getListView();

		adapter=new MedicationListAdapter(getActivity(),MedicationInformation.getArrayList());
		listview.setAdapter(adapter);
		adapter.notifyDataSetChanged();


		//listview.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, MedicationInformation.Medicationname));
		//listview.setOnItemClickListener(mMessageClickedHandler); 
	}*/

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, 
			ViewGroup container, Bundle savedInstanceState) {

		// Inflate the layout for this fragment
		View view =  inflater.inflate(R.layout.medication_masterlist, 
				container, false);
		listview=(ListView) view.findViewById(R.id.lvmed_madilist);
		//View header = (View) inflater.inflate(R.layout.listheader,null);
		//ls.addHeaderView(header);

		adapter=new MedicationListAdapter(view.getContext(),medinfo.getArrayList());
		listview.setAdapter(adapter);
		//listview.addHeaderView(header);
		//listview.setAdapter(new MedicationListAdapter(getActivity(), MedicationInformation.getArrayList()));
		listview.setOnItemClickListener(mMessageClickedHandler); 
		btnMedicationAddtoList=(Button)view.findViewById(R.id.btnmed_add);
		btnMedicationAddtoList.setOnClickListener(mAddtoListHandler);
		//btnMedicationRemovefromList=(Button)view.findViewById(R.id.btnmed_meddelete);

		//btnMedicationRemovefromList.setOnClickListener(mRemovefromListHandler);
		return view;
	}

	// Create a message handling object as an anonymous class. 
	/** The m message clicked handler. */
	private OnItemClickListener mMessageClickedHandler = new OnItemClickListener() {     
		public void onItemClick(AdapterView parent, View v, int position, long id) 
		{         // Do something in response to the click  
			opMode=operationType.ITEMSELECTED;
			mCallback.onMedicationSelected(position,opMode);

			listview.setItemChecked(position, true);
			
			listpostionselected=position;
		} 
	};  	

	private OnClickListener mAddtoListHandler = new OnClickListener() {     
		public void onClick(View v) 
		{         // Do something in response to the click  
			opMode=operationType.ADD;
			mCallback.onMedicationSelected(-1,opMode);

		} 
	}; 

	private OnClickListener mRemovefromListHandler = new OnClickListener() {     
		public void onClick(View v) 
		{         // Do something in response to the click  
			if(!opMode.equals(operationType.ITEMSELECTED)){
				Toast.makeText(getActivity(), "item should be select for remove", Toast.LENGTH_SHORT).show();
			}
			else{
				opMode=operationType.DELETE;
				mCallback.onMedicationSelected(listpostionselected,opMode);
			}
		} 
	}; 


	/*   @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.medication_masterlist2);

        // We need to use a different list item layout for devices older than Honeycomb
       // int layout = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ?
         //       android.R.layout.simple_list_item_activated_1 : android.R.layout.simple_list_item_1;
        //int layout=
       // setContentView(R.layout.activity_medication);
        // Create an array adapter for the list view, using the Ipsum headlines array
        //setListAdapter(new ArrayAdapter<String>(getActivity(), layout, MedicationInformation.Medicationname));
    }*/

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onStart()
	 */
	@Override
	public void onStart() {
		super.onStart();

		// When in two-pane layout, set the listview to highlight the selected list item
		// (We do this during onStart because at the point the listview is available.)
		if (getFragmentManager().findFragmentById(R.id.medicationdetail_fragment) != null) {
			// getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
			listview.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		}
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onAttach(android.app.Activity)
	 */
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		// This makes sure that the container activity has implemented
		// the callback interface. If not, it throws an exception.
		try {
			mCallback = (OnMedicationSelectedListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnHeadlineSelectedListener");
		}
	}


	public void updateMedicationListView() {
		Toast.makeText(getActivity(), "List updated", Toast.LENGTH_SHORT).show();
		adapter.notifyDataSetChanged();
	}
	/*  @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // Notify the parent activity of selected item
        mCallback.onMedicationSelected(position);

        // Set the item as checked to be highlighted when in two-pane layout
        getListView().setItemChecked(position, true);
    }*/
}