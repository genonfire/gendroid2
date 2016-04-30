package com.gendroid.two;

import java.util.ArrayList;
import java.util.Date;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;


public class MoreDialog extends ListActivity {
	private ItemAdapter m_adapter;
	private Date mDate;
	private boolean mIsTouchForFooter;
	private boolean mIsTouchForHeader;
	private boolean mIsScrolling;
	
	public final int NO_LIST = 8;
	public final int NO_MORELIST = 10;
	
    private class ItemAdapter extends ArrayAdapter<MoreItem> {

        private ArrayList<MoreItem> items;

        public ItemAdapter(Context context, int textViewResourceId, ArrayList<MoreItem> items) {
                super(context, textViewResourceId, items);
                this.items = items;
        }
        
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
			View itemView = convertView;
			if (itemView == null) {
			    LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			    itemView = vi.inflate(R.layout.morelist, null);
			}
			MoreItem item = items.get(position);
			if (item != null) {
				TextView viewTitle = (TextView)itemView.findViewById(R.id.day);
				TextView viewDate = (TextView)itemView.findViewById(R.id.date);

				if (viewTitle != null){
					viewTitle.setText(item.getTitle());
				}
				if (viewDate != null) {					
					viewDate.setText(item.getDateStr());
				}
			}
			return itemView;
        }
    }
    
    class MoreItem {
    	private int day;
    	private long time;
        private String title;
        private String dateStr;
        
        public MoreItem(int in_day, long in_time){
        	day = in_day;
        	setTitle(in_day);
        	time = in_time;
    		setDateStr(in_time);
        }
        
        private void setTitle(int in_day) {
        	String dayForm = (in_day>0) ? "D+" : "D";
        	if (in_day == 0)
        		title = dayForm+"-day";
        	else
        		title = String.format("%s%d", dayForm, in_day);
        }
        
        private void setDateStr(long in_time) {
        	Date date = new Date();
        	date.setTime(in_time);
        	dateStr = DateFormat.getDateFormat(MoreDialog.this).format(date);
        }
        
        public int getDay() {
        	return day;
        }
        
        public long getTime() {
        	return time;
        }
        
        public String getTitle() {
            return title;
        }

        public String getDateStr() {
            return dateStr;
        }
    }
    
    private void buildDefaultItems(Date date) {
    	ListView list = (ListView)findViewById(android.R.id.list);
        ArrayList<MoreItem> listItems = new ArrayList<MoreItem>();
        MoreItem[] items = new MoreItem[NO_LIST];
        
        Date varDate = new Date();
        int dday = util.calculateDay(date, getBaseContext());
        
        for (int i=0; i<NO_LIST; i++ ) {
        	items[i] = new MoreItem(dday, varDate.getTime());
        	listItems.add(items[i]);
        	
        	if (dday % 100 != 0)
        		dday = (dday + ((dday>0)?100:0)) / 100 * 100;
        	else
        		dday += 100;
        	
        	varDate = util.calculateDate(date, dday);
        }

        View footer = getLayoutInflater().inflate(R.layout.more_footer, null, false);
        list.addFooterView(footer);
        
        m_adapter = new ItemAdapter(this, R.layout.morelist, listItems);
        list.setAdapter(m_adapter);
        list.setOnScrollListener(mOnScrollListener);
        list.setOnTouchListener(mOnTouchListener);
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.emptylist);
        
        Intent intent = getIntent();
        long in_date = intent.getLongExtra("date", 0);
        
        mDate = new Date();
        mDate.setTime(in_date);
        
		buildDefaultItems(mDate);
		
		mIsTouchForFooter = false;
		mIsTouchForHeader = false;
    }
    
    OnScrollListener mOnScrollListener = new OnScrollListener() {
    	private int prevArg = 0;
		public void onScrollStateChanged(AbsListView arg0, int arg1) {
			mIsScrolling = (arg1==1) ? true : false;
		}
    	
		public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {
    		if (arg1+arg2 >= arg3 && arg1>= prevArg) {
    			prevArg = arg1;
    			mIsTouchForFooter = true;
    		}
    		else if (arg1 == 0) {
    			Log.v("GENDROID", "onScroll ,"+arg1+"("+mIsScrolling+"), "+arg2+", "+arg3);
    			mIsTouchForHeader = true;
    		}
    	}
    };
    
    OnTouchListener mOnTouchListener = new OnTouchListener() {
    	public boolean onTouch(View v, MotionEvent event) {
    		if(event.getAction() == MotionEvent.ACTION_UP) {
    			if (mIsTouchForFooter && mIsScrolling)
    				new LoadMoreThread(true).execute();
    			else if (mIsTouchForHeader && mIsScrolling)
    				new LoadMoreThread(false).execute();

    			mIsTouchForFooter = false;
    			mIsTouchForHeader = false;
    		}
    		
    		return false;
    	}
    };
    
    private class LoadMoreThread extends AsyncTask<Void, Void, Void> {
    	private boolean footer;
    	private TextView tv;
    	private MoreItem[] items;
    	
    	public LoadMoreThread(boolean in_flag) {
    		super();
    		footer = in_flag;
    	}
    	
    	private void addMoreItems(MoreItem in_item, boolean footer) {
        	items = new MoreItem[NO_MORELIST];
        	Date varDate = new Date();
        	int dday = in_item.getDay();
        	
        	if (footer) {    	
    	    	for (int i=0; i<NO_MORELIST; i++) {
    	    		if (dday % 100 != 0)
    	        		dday = (dday + ((dday>0)?100:0)) / 100 * 100;
    	        	else
    	        		dday += 100;
    	    		
    	    		varDate = util.calculateDate(mDate, dday);
    	    		items[i] = new MoreItem(dday, varDate.getTime());
    	    	}
        	}
        	else {
        		for (int i=0; i<NO_MORELIST; i++) {
    	    		if (dday % 100 != 0)
    	        		dday = dday / 100 * 100;
    	        	else
    	        		dday -= 100;
    	    		
    	    		varDate = util.calculateDate(mDate, dday);
    	    		items[i] = new MoreItem(dday, varDate.getTime());
    	    	}
        	}
        }
    	
    	@Override
    	protected void onPreExecute() {
    		tv = (TextView)findViewById(R.id.footer);
    		if (tv != null) {
    			tv.setText(R.string.more);
    		}
    	}
    	
        @Override
        public Void doInBackground(Void... args) {
        	if (footer)
        		addMoreItems(m_adapter.getItem(m_adapter.getCount()-1), true);
        	else
        		addMoreItems(m_adapter.getItem(0), false);
        	
        	/**try {
        		Thread.sleep(300);
        	}
        	catch (InterruptedException e) {
        	}**/
        	
            return null;
        }
        
        @Override
        protected void onPostExecute(Void arg) {
        	loadMore(footer);
        	if (tv != null)
        		tv.setText("");
        }
        
        private void loadMore(boolean footer) {
        	if (footer) {
        		for (int i=0; i<NO_MORELIST; i++)
        			m_adapter.add(items[i]);
        	}
        	else {
        		for (int i=0; i<NO_MORELIST; i++)
        			m_adapter.insert(items[i], 0);
        	}
        }
    }

}
