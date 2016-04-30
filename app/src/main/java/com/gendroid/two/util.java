package com.gendroid.two;

import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;

public class util extends Activity {
	
	public static int getIconResource(int icon) {
    	int iconID;
    	
    	switch(icon) {
    	case 1:
    		iconID = R.drawable.item_icon_1;
    		break;
    	case 2:
    		iconID = R.drawable.item_icon_2;
    		break;
    	case 3:
    		iconID = R.drawable.item_icon_3;
    		break;
    	case 4:
    		iconID = R.drawable.item_icon_4;
    		break;
    	case 5:
    		iconID = R.drawable.item_icon_5;
    		break;
    	case 6:
    		iconID = R.drawable.item_icon_6;
    		break;
    	case 7:
    		iconID = R.drawable.item_icon_7;
    		break;
    	case 8:
    		iconID = R.drawable.item_icon_8;
    		break;
    	case 9:
    		iconID = R.drawable.item_icon_9;
    		break;
    	default:
    		iconID = R.drawable.item_icon_default;
    		break;
    	}
    	
    	return iconID;
    }
	
	public static int getColor(String string) {
		int color = Color.BLACK;
		
		if (string.equals("black"))
			color = Color.BLACK;
		else if (string.equals("white"))
			color = Color.WHITE;
		else if (string.equals("dkgray"))
			color = Color.DKGRAY;
		else if (string.equals("gray"))
			color = Color.GRAY;
		else if (string.equals("ltgray"))
			color = Color.LTGRAY;
		else if (string.equals("red"))
			color = Color.RED;
		else if (string.equals("green"))
			color = Color.GREEN;
		else if (string.equals("blue"))
			color = Color.BLUE;
		else if (string.equals("yellow"))
			color = Color.YELLOW;
		else if (string.equals("cyan"))
			color = Color.CYAN;
		else if (string.equals("magenta"))
			color = Color.MAGENTA;
			
		return color;
	}
	
	public static int calculateDay(Date date, Context context) {
    	Date today = new Date();
    	long diffTime;
    	int dday = 1;
    	
    	today.setHours(3);
    	today.setMinutes(0);
    	today.setSeconds(0);
    	
    	if (today.before(date)) {
    		today.setHours(1);
    		diffTime = date.getTime() - today.getTime();
    		dday = -1;
    	}
    	else {
    		diffTime = today.getTime() - date.getTime();
    	}
    	
    	dday *= (int)(diffTime/24/3600/1000);
    	
    	if (dday >= 0) {
    		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
            boolean isUseOneday = pref.getBoolean("use_oneday", false);
            
            if (isUseOneday)
            	dday += 1;
    	}
    	
    	return dday;
    }
	
	public static Date calculateDate(Date in_date, int day) {
		Date date = new Date();
		
		long varTime = in_date.getTime();
		long acom = (long)day*(24*3600*1000);
		varTime += acom;
		date.setTime(varTime);
		
		return date;
	}
}