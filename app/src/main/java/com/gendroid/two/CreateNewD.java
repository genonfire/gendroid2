package com.gendroid.two;

import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class CreateNewD extends Activity {
	private View mOKButton;
	private View mCancelButton;
	private Button mPickicon;
	private EditText mTitle;
	private TextView mDateDisplay; 
	private int mYear;
    private int mMonth;
    private int mDay;
    private DBHelper mDBHelper;
    private int mIcon = 0;
    
    private String mTitle_replace;
    private long mDate_replace;
    private int mIcon_replace;
    
    private boolean mEdit = false;
	
	static final int DATE_DIALOG_ID = 1;
	static final int ICON_DIALOG_ID = 2;
	
	private View.OnClickListener mOK = new View.OnClickListener() {
        public void onClick(View v) {
        	mTitle = (EditText)findViewById(R.id.title);
        	String title = mTitle.getText().toString().trim();
        	
        	if (mDateDisplay.length() <= 0) {
        		Toast.makeText(CreateNewD.this, R.string.no_pick_date, Toast.LENGTH_SHORT).show();
        	}
        	else if (title.length() <= 0) {
	        	Toast.makeText(CreateNewD.this, R.string.no_title, Toast.LENGTH_SHORT).show();
        	}
        	else {
	        	Date date = new Date();
	        	date.setYear(mYear-1900);
	        	date.setDate(mDay);
	        	date.setMonth(mMonth);
	        	date.setHours(2);
	        	date.setMinutes(0);
	        	date.setSeconds(0);

	        	long time = date.getTime();
	        	time = time / 1000 * 1000;

	        	mDBHelper.open(DBHelper.WRITABLE);
	        	Cursor cursor = mDBHelper.getEntries(title, time, mIcon);

	        	if (cursor.getCount() > 0) {
	        		if (mEdit == false)
	        			Toast.makeText(CreateNewD.this, R.string.already_exist, Toast.LENGTH_SHORT).show();
	        		
	        		cursor.close();
		        	mDBHelper.close();
		        	
		        	if (mEdit) {
		        		setResult(RESULT_OK);
			        	finish();
		        	}
	        	}
	        	else {
	        		if (mEdit) {
	        			Cursor editCursor = mDBHelper.getEntries(mTitle_replace, mDate_replace, mIcon_replace);
	        			editCursor.moveToNext();

	        			int index = editCursor.getInt(DBHelper.KEY_COLUMN);
        				mDBHelper.updateEntry(index, title, time, mIcon);
	        			editCursor.close();
	        			Toast.makeText(CreateNewD.this, R.string.edit_done, Toast.LENGTH_SHORT).show();
	        		}
	        		else
	        			mDBHelper.insertEntry(title, time, mIcon);
	        		
	        		cursor.close();
		        	mDBHelper.close();
		        	setResult(RESULT_OK);
		        	finish();
	        	}
        	}
        }
    };

    private View.OnClickListener mCancel = new View.OnClickListener() {
        public void onClick(View v) {
        	setResult(RESULT_CANCELED);
        	finish();
        }
    };
    
    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            
            Date date = new Date();
        	date.setYear(year-1900);
        	date.setDate(dayOfMonth);
        	date.setMonth(monthOfYear);
        	date.setDate(dayOfMonth); // work-around
            
            String dateStr = DateFormat.getDateFormat(CreateNewD.this).format(date);
            mDateDisplay.setText(dateStr);
        }
    };
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.create_item);        
        
        Intent intent = getIntent();
        mEdit = intent.getBooleanExtra("edit", false);
        
        if (mEdit)
        	setTitle(R.string.title_edit);
        else
        	setTitle(R.string.title);
        
        Button pickDate = (Button) findViewById(R.id.pickDate);
        pickDate.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                showDialog(DATE_DIALOG_ID);
            }
        });
        
        mPickicon = (Button) findViewById(R.id.pickIcon);
        mPickicon.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
            	Intent intent_icon = new Intent(CreateNewD.this, IconPicker.class);
	    		startActivityForResult(intent_icon, ICON_DIALOG_ID);
            }
        });
        
        mDateDisplay = (TextView)findViewById(R.id.dateDisplay);
        
        if (mEdit) {
        	mTitle_replace = intent.getStringExtra("title");
        	mDate_replace = intent.getLongExtra("date", 0);
        	mIcon_replace = intent.getIntExtra("icon", 0);
        	
        	Date date = new Date();
        	date.setTime(mDate_replace);
        	mYear = date.getYear()+1900;
        	mMonth = date.getMonth();
        	mDay = date.getDate();
        	
        	String dateStr = DateFormat.getDateFormat(CreateNewD.this).format(date);
            mDateDisplay.setText(dateStr);
            
            mTitle = (EditText)findViewById(R.id.title);
            mTitle.setText(mTitle_replace);
        	
        	mPickicon.setBackgroundResource(util.getIconResource(mIcon_replace));
    		mPickicon.setText("");
    		mPickicon.setPadding(72, 0, 0, 0);
        }
        else {
	        final Calendar c = Calendar.getInstance();
	        mYear = c.get(Calendar.YEAR);
	        mMonth = c.get(Calendar.MONTH);
	        mDay = c.get(Calendar.DAY_OF_MONTH);
        }
        
        mOKButton = findViewById(R.id.OK);
        mOKButton.setOnClickListener(mOK);

        mCancelButton = findViewById(R.id.cancel);
        mCancelButton.setOnClickListener(mCancel);
        
        mDBHelper = new DBHelper(this); 
    }
    
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                return new DatePickerDialog(this, mDateSetListener, mYear, mMonth, mDay);
        }
        return null;
    }
    
    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {
        switch (id) {
            case DATE_DIALOG_ID:
                ((DatePickerDialog) dialog).updateDate(mYear, mMonth, mDay);
                break;
        }
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {    	
    	switch (requestCode) {
    	case ICON_DIALOG_ID:
    		mIcon = resultCode;
    		mPickicon.setBackgroundResource(util.getIconResource(mIcon));
    		mPickicon.setText("");
    		mPickicon.setPadding(72, 0, 0, 0); // work-around
    		break;
    		
		default:
			break;
    	}
    }
}
