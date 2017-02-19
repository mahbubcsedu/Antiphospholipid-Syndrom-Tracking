/* 
  * Copyright 2014 (C) The EMMES Corporation 
  *  
  * Created on : 02-06-2014
  * Last Update: Jul 7, 2014 3:28:25 PM
  * Author     : Mahbubur Rahman
  * Title      : Summer intern 2014 
  * Project    : Daily Diary Android Application
  * 
  */
package com.emmes.aps.util;


import com.emmes.aps.R;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;



/**
* A dialog that prompts the user for the message deletion limits.
*/
@TargetApi(11)
public class NumberPickerDialog extends AlertDialog implements OnClickListener {

    private static final String NUMBER = "number";

    /**
* The callback interface used to indicate the user is done filling in
* the time (they clicked on the 'Set' button).
*/
    public interface OnNumberSetListener {

        /**
* @param number The number that was set.
*/
        void onNumberSet(int dialogId, int number);
    }

    private final NumberPicker mNumberPicker;
    private final OnNumberSetListener mCallback;
    private final int mDialogId;

    /**
* @param context Parent.
* @param callBack How parent is notified.
* @param number The initial number.
*/
    public NumberPickerDialog(Context context,
            OnNumberSetListener callBack,
            int number,
            int rangeMin,
            int rangeMax,
            int title,
            int units,
            int dialogId) {
	
        this(context,0,callBack, number, rangeMin, rangeMax, title, units, dialogId);
    }

    /**
* @param context Parent.
* @param theme the theme to apply to this dialog
* @param callBack How parent is notified.
* @param number The initial number.
*/
    public NumberPickerDialog(Context context,
            int theme,
            OnNumberSetListener callBack,
            int number,
            int rangeMin,
            int rangeMax,
            int title,
            int units,
            int dialogId) {
        super(context, theme);
        mCallback = callBack;
        mDialogId = dialogId;

        setTitle(title);
        Context themeContext = getContext();
        setButton(DialogInterface.BUTTON_POSITIVE, themeContext.getText(R.string.set), this);
        setButton(DialogInterface.BUTTON_NEGATIVE, themeContext.getText(R.string.cancel), mCancel);
        LayoutInflater inflater =
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.number_picker_dialog, null);
        setView(view);
        mNumberPicker = (NumberPicker) view.findViewById(R.id.number_picker);

        if (units != 0) {
            TextView unit = (TextView) view.findViewById(R.id.unit);
            unit.setText(units);
            unit.setVisibility(View.VISIBLE);
        }

        // initialize state
        mNumberPicker.setMinValue(rangeMin);
        mNumberPicker.setMaxValue(rangeMax);
        mNumberPicker.setValue(number);
       
        
    }

    public OnClickListener mCancel=new OnClickListener(){
	@Override
	public void onClick(DialogInterface dialog, int which) 
	{
	    dialog.dismiss();
	}
    };
    @Override
	public void onClick(DialogInterface dialog, int which) {
        if (mCallback != null) {
            mNumberPicker.clearFocus();
            mCallback.onNumberSet(mDialogId, mNumberPicker.getValue());
            dialog.dismiss();
        }
    }

    @Override
    public Bundle onSaveInstanceState() {
        Bundle state = super.onSaveInstanceState();
        state.putInt(NUMBER, mNumberPicker.getValue());
        return state;
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        int number = savedInstanceState.getInt(NUMBER);
        mNumberPicker.setValue(number);
    }
}