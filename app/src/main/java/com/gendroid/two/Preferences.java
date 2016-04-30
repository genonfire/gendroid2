package com.gendroid.two;

import android.os.Bundle;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.preference.CheckBoxPreference;


public class Preferences extends PreferenceActivity {
	
	SharedPreferences mPref;
	EditTextPreference mPassword;
	ListPreference mTapBehavior;
	ListPreference mFontColor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		addPreferencesFromResource(R.xml.preferences);
		
		mPref = PreferenceManager.getDefaultSharedPreferences(this); 
		
		mPassword = (EditTextPreference)findPreference("password");		
		mPassword.setEnabled(mPref.getBoolean("use_password", false));
		
		Resources r = getResources();
		mTapBehavior = (ListPreference)findPreference("tap_behavior");
		String[] entriesTap = new String[3];
		entriesTap[0] = r.getString(R.string.pref_tap_nothing);
		entriesTap[1] = r.getString(R.string.pref_tap_launch_app);
		entriesTap[2] = r.getString(R.string.pref_tap_hide);
		mTapBehavior.setEntries(entriesTap);
		
		mFontColor = (ListPreference)findPreference("font_color");
		String[] entriesColor = new String[11];
		entriesColor[0] = r.getString(R.string.pref_font_color_black);
		entriesColor[1] = r.getString(R.string.pref_font_color_white);
		entriesColor[2] = r.getString(R.string.pref_font_color_dkgray);
		entriesColor[3] = r.getString(R.string.pref_font_color_gray);
		entriesColor[4] = r.getString(R.string.pref_font_color_ltgray);
		entriesColor[5] = r.getString(R.string.pref_font_color_red);
		entriesColor[6] = r.getString(R.string.pref_font_color_green);
		entriesColor[7] = r.getString(R.string.pref_font_color_blue);
		entriesColor[8] = r.getString(R.string.pref_font_color_yellow);
		entriesColor[9] = r.getString(R.string.pref_font_color_cyan);
		entriesColor[10] = r.getString(R.string.pref_font_color_magenta);
		mFontColor.setEntries(entriesColor);
	}
	
	@Override
	public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen,
			Preference preference) {
		
		if(preference.equals((CheckBoxPreference)findPreference("use_password"))) {
			mPassword.setEnabled(mPref.getBoolean("use_password", false));
		}
		
		return super.onPreferenceTreeClick(preferenceScreen, preference);
	}	
}