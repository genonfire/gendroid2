package com.gendroid.two;

import java.util.Date;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.view.View;
import android.widget.RemoteViews;

public class WidgetProvider extends AppWidgetProvider {

	@Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		final int N = appWidgetIds.length;
        for (int i=0; i<N; i++) {
            int appWidgetId = appWidgetIds[i];
            String title = WidgetConfigure.loadTitle(context, appWidgetId);
            long date = WidgetConfigure.loadDate(context, appWidgetId);
            String color = WidgetConfigure.loadColor(context, appWidgetId);
            boolean visible = WidgetConfigure.loadVisibility(context, appWidgetId);
            
            if (!title.equals("error") && date != 0)
            	updateAppWidget(context, appWidgetManager, appWidgetId, title, date, color, visible);
        }
	}
	/**
	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);
		
		String action = intent.getAction();
		
		if (action.equals("android.intent.action.USER_PRESENT")) {

		}
	}**/
	
    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
    	final int N = appWidgetIds.length;
        for (int i=0; i<N; i++) {
            int appWidgetId = appWidgetIds[i];
            WidgetConfigure.deleteWidgetPref(context, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(
                new ComponentName("com.gendroid.two", ".WidgetBroadcastReceiver"),
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }

    @Override
    public void onDisabled(Context context) {
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(
                new ComponentName("com.gendroid.two", ".WidgetBroadcastReceiver"),
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId, 
    								String in_title, long in_date, String in_color, boolean in_visible) {    	
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);
        
        views.setTextViewText(R.id.widget_title, in_title);
        
        Date date = new Date();
		date.setTime(in_date);
		int day = util.calculateDay(date, context);
        
        String formStr = (day>0) ? "D+" : "D";
		String dateStr;
		if (day == 0)
			dateStr = "D-day";
		else
			dateStr = String.format("%s%d", formStr, day);
        
        views.setTextViewText(R.id.widget_dday, dateStr);
        
        int color = util.getColor(in_color);
        
        views.setTextColor(R.id.widget_title, color);
        views.setTextColor(R.id.widget_dday, color);
        
        if (in_visible == false) {
        	views.setViewVisibility(R.id.widget_title, View.INVISIBLE);
        	views.setViewVisibility(R.id.widget_dday, View.INVISIBLE);
        }
        
        Intent intent = new Intent(context, IntentBridge.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, appWidgetId, intent, 0);
        views.setOnClickPendingIntent(R.id.GendroidWidget, pendingIntent); 

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }
    
    static void toggleWidgetVisibility(Context context, AppWidgetManager appWidgetManager, int appWidgetId, boolean visible) {
    	RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);
    	//ComponentName comp = new ComponentName(context, WidgetProvider.class);
    	
    	if (visible) {
    		views.setViewVisibility(R.id.widget_title, View.VISIBLE);
    		views.setViewVisibility(R.id.widget_dday, View.VISIBLE);
    	}
    	else {
    		views.setViewVisibility(R.id.widget_title, View.INVISIBLE);
    		views.setViewVisibility(R.id.widget_dday, View.INVISIBLE);
    	}
    	
    	appWidgetManager.updateAppWidget(appWidgetId, views);
    }
}
