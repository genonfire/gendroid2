package com.gendroid.two;

import android.app.Activity;

public class SecurityOne extends Activity {
	
	private static boolean mAccessing = false;
	private static boolean mAccessGranted = false;
	
    public static boolean getAccessGranted() {
    	return mAccessGranted;
    }
    
    public static void setAccessGranted(boolean bAccessGranted) {
    	mAccessGranted = bAccessGranted;
    }
    
    public static boolean getAccessing() {
    	return mAccessing;
    }
    
    public static void setAccessing(boolean bAccessing) {
    	mAccessing = bAccessing;
    }
    
    public static void clearAllAccess() {
    	mAccessing = false;
    	mAccessGranted = false;
    }
}