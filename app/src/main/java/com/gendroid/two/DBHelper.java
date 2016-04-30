package com.gendroid.two;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper {
	private static final String DATABASE_NAME = "gendroid.db";
	private static final String DATABASE_TABLE = "main";
	private static final int DATABASE_VERSION = 1;
	
	public static final String KEY_ID = "_id";
	public static final int KEY_COLUMN = 0;
	public static final String KEY_TITLE = "title";
	public static final int TITLE_COLUMN = 1;
	public static final String KEY_DATE = "date";
	public static final int DATE_COLUMN = 2;
	public static final String KEY_ICON = "icon";
	public static final int ICON_COLUMN = 3;
	
	public static final int READABLE = 0;
	public static final int WRITABLE = 1;
	
	private static final String DATABASE_CREATE = "CREATE TABLE " + DATABASE_TABLE + " (" +
			KEY_ID + " INTEGER PRIMARY KEY," +
			KEY_TITLE + " TEXT," +
			KEY_DATE + " LONG," +
			KEY_ICON + " INTEGER" +
			");";
	
	private SQLiteDatabase db;
	private DBOpenHelper dbHelper;

	public DBHelper(Context context) {
		dbHelper = new DBOpenHelper (context,DATABASE_NAME, null, DATABASE_VERSION); 
	}
	
	public DBHelper open(int openMode) throws SQLException {
		if (openMode == READABLE) db = dbHelper.getWritableDatabase();
		else db = dbHelper.getReadableDatabase();

		return this;
	}
	
	public void close() {
		db.close();
	}
	
	public long insertEntry(String title, long date, int icon) {
		ContentValues content = new ContentValues();
		content.put(KEY_TITLE, title);
		content.put(KEY_DATE, date);
		content.put(KEY_ICON, icon);
		return db.insert(DATABASE_TABLE, null, content);
	}
	
	public boolean removeEntry(String title, long date, int icon) {
		String[] whereArgs = {title, String.format("%d", date), String.format("%d", icon)};
		return db.delete(DATABASE_TABLE, KEY_TITLE+"=? and "+KEY_DATE+"=? and "+KEY_ICON+"=?", whereArgs) > 0;
	}
	
	public Cursor getEntries(String title, long date, int icon) {
		String[] whereArgs = {title, String.format("%d", date), String.format("%d", icon)};
		String query = "select * from "+DATABASE_TABLE+" where "+KEY_TITLE+"=? and "+KEY_DATE+"=? and "+KEY_ICON+"=?";
		
		return db.rawQuery(query, whereArgs);
	}
	
	public Cursor getAllEntries() {
		String query = "select * from "+DATABASE_TABLE;
		return db.rawQuery(query, null);
	}

	public int updateEntry(int index, String title, long date, int icon) {
		String where = KEY_ID + "=" + index;
		ContentValues content = new ContentValues();
		content.put(KEY_TITLE, title);
		content.put(KEY_DATE, date);
		content.put(KEY_ICON, icon);
		return db.update(DATABASE_TABLE, content, where, null);
	}
	
	/* DBOpenHelper */
	private static class DBOpenHelper extends SQLiteOpenHelper {
		public DBOpenHelper(Context context, String name,
				CursorFactory factory, int version) {
			super(context, name, factory, version);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(DATABASE_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS "+DATABASE_TABLE);
			db.execSQL(DATABASE_CREATE);
		}
	}
}
