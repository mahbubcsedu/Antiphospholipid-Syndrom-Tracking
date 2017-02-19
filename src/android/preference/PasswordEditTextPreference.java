package android.preference;

import com.emmes.aps.R;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.EditTextPreference;
import android.preference.SecurePreferences;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;

public class PasswordEditTextPreference extends EditTextPreference
{

    private SecurePreferences obfuscator = new SecurePreferences();
    SharedPreferences mPrefs;
    Editor mEditor;
// SecureSharedpreference();
    private String seed;
    private Context context;

    public PasswordEditTextPreference(Context context) {
	super(context);
	this.context = context;

    }

    public PasswordEditTextPreference(Context context, AttributeSet attrs, int defStyle) {
	super(context, attrs, defStyle);
	this.context = context;
    }

    public PasswordEditTextPreference(Context context, AttributeSet attrs) {

	super(context, attrs);
	this.context = context;
    }

    /* (non-Javadoc)
     * 
     * @see android.preference.EditTextPreference#getEditText() */
    @Override
    public EditText getEditText()
    { // TODO Auto-generated method stub
	EditText etx = super.getEditText();

	etx.setFilters(new InputFilter[] { new InputFilter.LengthFilter(4) });

	return etx;
    }

    @Override
    protected void onDialogClosed(boolean positiveResult)
    {
	try
	{

	    mPrefs = PreferenceManager.getDefaultSharedPreferences(this.context);
	    mEditor = mPrefs.edit();
	    mEditor.putString(this.context.getResources().getString(R.string.spref_pref_pin_encrypted),
		    obfuscator.encrypt(getEditText().getText().toString()));
	    mEditor.commit();
	    super.getEditText().setText(obfuscator.encrypt(getEditText().getText().toString()));

	} catch (Exception e)
	{
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	super.onDialogClosed(positiveResult);
    }

    @Override
    protected void onBindDialogView(View view)
    {
	super.onBindDialogView(view);
	try
	{
	    final EditText ext = this.getEditText();

	    ext.addTextChangedListener(new TextWatcher() {
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count)
		{
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after)
		{
		}

		/* @Override public void afterTextChanged(Editable s) {
	         * 
	         * } */

		@Override
		public void afterTextChanged(Editable s)
		{
		    // TODO Auto-generated method stub
		    if (s.length() > 4)
		    {
			ext.setError("Length must be 4");
		
			ext.setText(ext.getText().toString().substring(0, 4));
			//onDialogClosed(false);
		    }
		}
	    });

	    mPrefs = PreferenceManager.getDefaultSharedPreferences(this.context);
	    mPrefs.getString(this.context.getResources().getString(R.string.spref_pref_pin_encrypted), "");

	    /* mEditor.putString(this.context.getResources().getString(R.string.
	     * spref_pref_pin
	     * ),obfuscator.encrypt(getEditText().getText().toString())); */
	    this.getEditText().setText(
		    obfuscator.decrypt(mPrefs.getString(this.context.getResources().getString(R.string.spref_pref_pin), "")));
	    // this.getEditText().setText(obfuscator.decrypt(getEditText().getText().toString()));
	    // this.getEditText().setText(SecureSharedpreference.decrypt(this.seed,getEditText().getText().toString()));
	} catch (Exception e)
	{
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

}
