package com.gendroid.two;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Toast;


public class WidgetConfigure extends Activity {
	
    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    public static final String PREFS_NAME = "com.gendroid.two_widget";
    public static final String PREF_PREFIX_TITLE_KEY = "title_";
    public static final String PREF_PREFIX_DATE_KEY = "dday_";
    public static final String PREF_PREFIX_VISIBILITY_KEY = "visible_";
    public static final String PREF_PREFIX_COLOR_KEY = "color_";
    
    private static final int PICK_LIST = 3;
    private static final int LOGIN_BRIDGE = 4;

    public WidgetConfigure() {
        super();
    }
    
    static void saveWidgetPref(Context context, int appWidgetId, String title, long date, boolean visible, String color) {
        SharedPreferences.Editor editPrefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        editPrefs.putString(PREF_PREFIX_TITLE_KEY + appWidgetId, title);
        editPrefs.putLong(PREF_PREFIX_DATE_KEY + appWidgetId, date);
        editPrefs.putString(PREF_PREFIX_COLOR_KEY + appWidgetId, color);
        editPrefs.putBoolean(PREF_PREFIX_VISIBILITY_KEY + appWidgetId, visible);        
        editPrefs.commit();
    }
    
    static String loadTitle(Context context, int appWidgetId) {
    	String title = new String();
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        title = prefs.getString(PREF_PREFIX_TITLE_KEY + appWidgetId, "error");
        
        return title;
    }
    
    static long loadDate(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        long date = prefs.getLong(PREF_PREFIX_DATE_KEY + appWidgetId, 0);
        
        return date;
    }
    
    static String loadColor(Context context, int appWidgetId) {
    	SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
    	String color = prefs.getString(PREF_PREFIX_COLOR_KEY + appWidgetId, "black");
    	
    	return color;
    }
    
    static boolean loadVisibility(Context context, int appWidgetId) {
    	SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
    	boolean visible = prefs.getBoolean(PREF_PREFIX_VISIBILITY_KEY + appWidgetId, true);
    	
    	return visible;
    }
    
    static void deleteWidgetPref(Context context, int appWidgetId) {
    	SharedPreferences.Editor editPrefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
    	editPrefs.remove(PREF_PREFIX_TITLE_KEY + appWidgetId);
    	editPrefs.remove(PREF_PREFIX_DATE_KEY + appWidgetId);
    	editPrefs.remove(PREF_PREFIX_COLOR_KEY + appWidgetId);
    	editPrefs.remove(PREF_PREFIX_VISIBILITY_KEY + appWidgetId);    	
    	editPrefs.commit();
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        setResult(RESULT_CANCELED);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        
        if (extras != null)
            mAppWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);

        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID)
            finish();
        else {
        	SharedPreferences defaultPrefs = PreferenceManager.getDefaultSharedPreferences(this);
	        if (defaultPrefs.getBoolean("use_password", false)) {
	        	Intent intent_password = new Intent(this, DialogGetPassword.class);
		        startActivityForResult(intent_password, LOGIN_BRIDGE);
		        
		        SecurityOne.setAccessing(true);
		        
		        setContentView(R.layout.main);
	        }
	        else {
	        	Intent intent_list = new Intent(this, ListContents.class);
		        intent_list.putExtra("pick", true);
		    	startActivityForResult(intent_list, PICK_LIST);
	        }
        }
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {    	
    	switch (requestCode) {
    	case PICK_LIST:
    		if (resultCode == RESULT_OK && data != null) {
    			final Context context = WidgetConfigure.this;
    			String title = data.getStringExtra("title");
    			long date = data.getLongExtra("date", 0);
    			
    			SharedPreferences defaultPrefs = PreferenceManager.getDefaultSharedPreferences(this);
    			String color = defaultPrefs.getString("font_color", "black");
    			
    			AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);    			
    			WidgetProvider.updateAppWidget(context, appWidgetManager, mAppWidgetId, title, date, color, true );

    			saveWidgetPref(context, mAppWidgetId, title, date, true, color);
    			
    			Intent resultValue = new Intent();
                resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
                
                setResult(RESULT_OK, resultValue);
    		}
    		else {
    			setResult(RESULT_CANCELED);
    		}
    		finish();
    		break;
    		
    	case LOGIN_BRIDGE:
    		if (resultCode == RESULT_OK) {
    			Toast.makeText(this, R.string.access_granted, Toast.LENGTH_SHORT).show();
    			Intent intent_list = new Intent(this, ListContents.class);
    	        intent_list.putExtra("pick", true);
    	    	startActivityForResult(intent_list, PICK_LIST);
            } else {
            	Toast.makeText(this, R.string.access_denied, Toast.LENGTH_SHORT).show();
            	finish();
            }
    		break;
    		
		default:
			break;
    	}
    }
}
