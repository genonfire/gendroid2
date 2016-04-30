package com.gendroid.two;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.util.Linkify;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class GendroidActivity extends Activity {
	
	private View mPasswordButton;
	private boolean mBackPressed;
	private static final int INPUT_PASSWORD = 1;
	private static final int BUILD_LAYOUT = 2;
	
	SharedPreferences mPref;

	private View.OnClickListener mInputPasswordManually = new View.OnClickListener() {
        public void onClick(View v) {
        	Intent intent = new Intent(GendroidActivity.this, DialogGetPassword.class);
            startActivityForResult(intent, INPUT_PASSWORD);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        mPref = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isUsePassword = mPref.getBoolean("use_password", false);
        
        mBackPressed = false;
        
        if (isUsePassword) {
	        if (SecurityOne.getAccessGranted() == true) {
	        	SecurityOne.setAccessGranted(false);
	        }
	        else if (SecurityOne.getAccessing() == false) {
		        Intent intent = new Intent(GendroidActivity.this, DialogGetPassword.class);
		        startActivityForResult(intent, INPUT_PASSWORD);
	
		        SecurityOne.setAccessing(true);
	        }
	        
	        setContentView(R.layout.main);
			
			mPasswordButton = findViewById(R.id.Button_input_password_manually);
	        mPasswordButton.setOnClickListener(mInputPasswordManually);

	        TextView tv = (TextView)findViewById(R.id.Text_url);	        
	        Linkify.addLinks(tv, Linkify.WEB_URLS);
        }
        else {
			setContentView(R.layout.main_nopassword);
	        
	        TextView tv = (TextView)findViewById(R.id.Text_url);	        
	        Linkify.addLinks(tv, Linkify.WEB_URLS);

        	Intent intent = new Intent(GendroidActivity.this, ListContents.class);
        	startActivityForResult(intent, BUILD_LAYOUT);
        }
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	switch(keyCode) {
	        case KeyEvent.KEYCODE_BACK:
	        	if (mBackPressed) {
	        		SecurityOne.clearAllAccess();
	        		finish();
	        	}
	        	else {        		
	        		Toast.makeText(this, R.string.back_onemore, Toast.LENGTH_SHORT).show();
	        		mBackPressed = true;        		
	        	}
	        	break;
        	
    		default:
    			//mBackPressed = false;
    			break;
    	}
    	return true;
    }
       
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {    	
    	switch (requestCode) {
    	case INPUT_PASSWORD:
    		if (resultCode == RESULT_OK) {
    			Toast.makeText(this, R.string.access_granted, Toast.LENGTH_SHORT).show();
            	Intent intent = new Intent(GendroidActivity.this, ListContents.class);
            	startActivityForResult(intent, BUILD_LAYOUT);
            } else {
            	Toast.makeText(this, R.string.access_denied, Toast.LENGTH_SHORT).show();
            }
    		break;
    		
    	case BUILD_LAYOUT:
    		if (resultCode == RESULT_OK) {
    			SecurityOne.clearAllAccess();
        		finish();
    		}
    		break;
    		
		default:
			break;
    	}
    }
}
