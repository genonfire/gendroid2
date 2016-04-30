package com.gendroid.two;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;


public class IntentBridge extends Activity {

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setResult(RESULT_CANCELED);
        
        final Context context = getBaseContext();
        
        SharedPreferences defaultPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String tapBehavior = defaultPrefs.getString("tap_behavior", "nothing");
        
        boolean refreshOnClick = defaultPrefs.getBoolean("refresh_on_click", false);
        
        if (refreshOnClick) {
        	Intent intent = getIntent();
            Bundle extras = intent.getExtras();
            int appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
            
            if (extras != null)
            	appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
            
            if (appWidgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {
            	AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            	String title = WidgetConfigure.loadTitle(context, appWidgetId);
            	long date = WidgetConfigure.loadDate(context, appWidgetId);
            	String color = WidgetConfigure.loadColor(context, appWidgetId);
            	boolean visible = WidgetConfigure.loadVisibility(context, appWidgetId);
            	
            	WidgetProvider.updateAppWidget(context, appWidgetManager, appWidgetId, title, date, color, visible);
            }
        }
        
        if (tapBehavior.equals("app")) {
        	Intent intent = new Intent(context, GendroidActivity.class);
        	startActivity(intent);
        }
        else if (tapBehavior.equals("hide")) {
        	Intent intent = getIntent();
            Bundle extras = intent.getExtras();
            int appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
            
            if (extras != null)
            	appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
            
            if (appWidgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {
	        	SharedPreferences prefs = context.getSharedPreferences(WidgetConfigure.PREFS_NAME, 0);
	        	SharedPreferences.Editor editPrefs = context.getSharedPreferences(WidgetConfigure.PREFS_NAME, 0).edit();

	        	AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
	        	boolean visible = !(prefs.getBoolean(WidgetConfigure.PREF_PREFIX_VISIBILITY_KEY + appWidgetId, true));
	        	
	        	editPrefs.putBoolean(WidgetConfigure.PREF_PREFIX_VISIBILITY_KEY + appWidgetId, visible);
	        	editPrefs.commit();
	        	WidgetProvider.toggleWidgetVisibility(context, appWidgetManager, appWidgetId, visible);
            }
        }
        
        finish();
	}
}