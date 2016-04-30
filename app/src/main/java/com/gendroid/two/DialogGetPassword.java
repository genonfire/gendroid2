package com.gendroid.two;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;

public class DialogGetPassword extends Activity {
	private View mOKButton;
	private View mCancelButton;
	private EditText mPassword;
	private SharedPreferences mPref;
	
	private View.OnClickListener mOK = new View.OnClickListener() {
        public void onClick(View v) {
        	String inputPassword = mPassword.getText().toString().trim();
        	String prefPassword = getPasswordFromPreference();
        	if (prefPassword.equals(inputPassword) == true) {
        		setResult(RESULT_OK);
        	}
        	else {
        		setResult(RESULT_CANCELED);
        	}
        	SecurityOne.setAccessing(false);
        	finish();
        }
    };

    private View.OnClickListener mCancel = new View.OnClickListener() {
        public void onClick(View v) {
       	
        	SecurityOne.setAccessing(false);
        	finish();
        }
    };
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);     
        
        setContentView(R.layout.password);
        setTitle(R.string.input_password);
        
        mPassword = (EditText) findViewById(R.id.password);
        SecurityOne.setAccessing(true);
        
        mOKButton = findViewById(R.id.OK);
        mOKButton.setOnClickListener(mOK);

        mCancelButton = findViewById(R.id.cancel);
        mCancelButton.setOnClickListener(mCancel);
    }
    
    private String getPasswordFromPreference()
    {
    	mPref = PreferenceManager.getDefaultSharedPreferences(this); 
        String prefPassword = mPref.getString("password", "");
        
        return prefPassword;
    }
}