package com.gendroid.two;

import java.util.ArrayList;
import java.util.Date;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ListContents extends ListActivity {
	
	private DBHelper mDBHelper;
	private ItemAdapter m_adapter;
	private int mCount;
	private int mPosition;
	private boolean mPick;
	
	static final int ADD_NEW = 1;
	static final int EDIT = 2;
		
	private void buildLayout() {
		setContentView(R.layout.emptylist);
        
        ListView list = (ListView)findViewById(android.R.id.list);
        
        ArrayList<DBItem> listItems = new ArrayList<DBItem>();
        
        mDBHelper = new DBHelper(this);
        mDBHelper.open(DBHelper.READABLE);
        Cursor cursor = mDBHelper.getAllEntries();
        //startManagingCursor(cursor);
        mCount = cursor.getCount();
        
        if (mCount > 0) {
	        DBItem[] items = new DBItem[mCount];
	        int i = 0;
	
	        while (cursor.moveToNext()) {
	        	String title = cursor.getString(DBHelper.TITLE_COLUMN);
	        	long date = cursor.getLong(DBHelper.DATE_COLUMN);
	        	int icon = cursor.getInt(DBHelper.ICON_COLUMN);
	        	items[i] = new DBItem(title, date, icon);
	        	listItems.add(items[i]);
	        	i++;
	        }
        }
        else {
        	Toast.makeText(this, R.string.nodata_create, Toast.LENGTH_LONG).show();
        }
        
        m_adapter = new ItemAdapter(this, R.layout.listitem, listItems);
        list.setAdapter(m_adapter);
        
        if (mPick) {
        	list.setOnItemClickListener(mPickListener);
        }
        else {
	        list.setOnItemClickListener(mClickListener);
	        list.setOnCreateContextMenuListener(this);
        }
        
        cursor.close();
		mDBHelper.close();
	}
	
	private AdapterView.OnItemClickListener mPickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        	if (arg2 < mCount) {
        		DBItem db = m_adapter.getItem(arg2);
        		
        		Intent resultIntent = new Intent();
        		resultIntent.putExtra("title", db.getTitle());
        		resultIntent.putExtra("date", db.getDate());
        		setResult(RESULT_OK, resultIntent);
        	}
        	else {
        		setResult(RESULT_CANCELED);		
        	}
        	finish();
        }
	};
	
	private AdapterView.OnItemClickListener mClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        	if (arg2 < mCount) {
        		DBItem db = m_adapter.getItem(arg2);
        		Intent intent_more = new Intent(ListContents.this, MoreDialog.class);
        		intent_more.putExtra("date", db.getDate());
	    		startActivity(intent_more);
        	}
        }
	};

    private class ItemAdapter extends ArrayAdapter<DBItem> {

        private ArrayList<DBItem> items;

        public ItemAdapter(Context context, int textViewResourceId, ArrayList<DBItem> items) {
                super(context, textViewResourceId, items);
                this.items = items;
        }
        
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
			View itemView = convertView;
			if (itemView == null) {
			    LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			    itemView = vi.inflate(R.layout.listitem, null);
			}
			DBItem item = items.get(position);
			if (item != null) {
				TextView viewTitle = (TextView)itemView.findViewById(R.id.title);
				TextView viewDate = (TextView)itemView.findViewById(R.id.value);
				ImageView viewIcon = (ImageView)itemView.findViewById(R.id.icon);
				
				Date date = new Date();
				date.setTime(item.getDate());
				
				if (viewTitle != null){					
					String dateStr = DateFormat.getDateFormat(ListContents.this).format(date);
					viewTitle.setText(item.getTitle()+" ("+dateStr+")");
				}
				if (viewDate != null) {
					int diffDay = util.calculateDay(date, getBaseContext());
					
					if (diffDay == 0) {
						Resources r = getResources();
						String dday = r.getString(R.string.dday);
						viewDate.setText(dday);
					}
					else {
						String dday = (diffDay>0) ? "D+" : "D";
						viewDate.setText(dday+diffDay);
					}
				}
				if (viewIcon != null) {
					viewIcon.setImageResource(item.getIconId());
				}
			}
			return itemView;
        }
    }

    class DBItem {
        private String title;
        private long date;
        private int icon;
        private int iconId;
        
        private void setIconResource(int in_icon) {
        	iconId = util.getIconResource(in_icon);
        }  
        
        public DBItem(String in_title, long in_date, int in_icon){
        	title = in_title;
        	date = in_date;
        	icon = in_icon;
        	setIconResource(in_icon);
        }
        
        public String getTitle() {
            return title;
        }

        public long getDate() {
            return date;
        }
        
        public int getIcon() {
        	return icon;
        }
        
        public int getIconId() {
        	return iconId;
        }
    }
    
    void askDelete() {        
        final int postion = mPosition;
        
        mDBHelper.open(DBHelper.READABLE);
		final DBItem db = m_adapter.getItem(postion);
		final String title = db.getTitle();
		final long date = db.getDate();
		final int icon = db.getIcon();
		final int iconId = db.getIconId();		
    	
    	String df= DateFormat.getDateFormat(this).format(new Date(db.getDate()));    	
		String titleText = String.format("%s (%s)", db.getTitle(), df);

    	AlertDialog dialog = new AlertDialog.Builder(this)
            .setTitle(titleText)
            .setIcon(iconId)
            .setMessage(R.string.delete_desc)
            .setPositiveButton(R.string.ok,
                    new DialogInterface.OnClickListener() {
                         public void onClick(DialogInterface dialog, int whichButton) {
							if (postion+1 <= mCount && title != null) {
							    Boolean result = mDBHelper.removeEntry(title, date, icon);
							    if (result)
							    	buildLayout();
							    else
							    	Toast.makeText(ListContents.this, "removeEntry failed", Toast.LENGTH_LONG).show();
							}
							else
								Toast.makeText(ListContents.this, "mCount failed", Toast.LENGTH_LONG).show();
							
							mDBHelper.close();
							return;
                    }})
            .setNegativeButton(R.string.cancel,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                        	mDBHelper.close();
                    }})
            .create();
        dialog.show();
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Intent intent = getIntent();
        if (intent != null)
        	mPick = intent.getBooleanExtra("pick", false);

        buildLayout();
    }
    
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
    	AdapterView.AdapterContextMenuInfo av = (AdapterView.AdapterContextMenuInfo) menuInfo;
    	DBItem db = m_adapter.getItem(av.position);
    	Date date = new Date(db.getDate());;
    	
    	String df= DateFormat.getDateFormat(this).format(date);    	
    	String title = String.format("%s (%s)", db.getTitle(), df);
    	
    	MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.listcontext, menu);

        menu.setHeaderIcon(db.getIconId());
        menu.setHeaderTitle(title);
    }
    
    @Override
    public boolean onContextItemSelected(MenuItem item) {
    	AdapterView.AdapterContextMenuInfo av = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
    	mPosition = av.position;
    	
    	switch (item.getItemId()) {
    	case R.id.listcontext_edit:
    		final DBItem db = m_adapter.getItem(mPosition);
    		final String title = db.getTitle();
    		final long date = db.getDate();
    		final int icon = db.getIcon();
    		final int iconId = db.getIconId();	
    		
    		Intent intent_edit = new Intent(ListContents.this, CreateNewD.class);
    		intent_edit.putExtra("edit", true);
    		intent_edit.putExtra("title", title);
    		intent_edit.putExtra("date", date);
    		intent_edit.putExtra("icon", icon);
    		intent_edit.putExtra("iconId", iconId);
    		startActivityForResult(intent_edit, EDIT);
    		break;
    			
    	case R.id.listcontext_delete:
    		askDelete();
    		break;
    	default:
            return super.onContextItemSelected(item);
    	}
    	
    	return true;
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	//if (mPick)
    		//return false;
    	
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
    		case R.id.menu_add:
    			Intent intent_create = new Intent(ListContents.this, CreateNewD.class);
    			intent_create.putExtra("edit", false);
	    		startActivityForResult(intent_create, ADD_NEW);
    			break;
    			
	    	case R.id.menu_preferences:
	    		Intent intent_pref = new Intent(ListContents.this, Preferences.class);
	    		startActivity(intent_pref);
				break;
			
	    	case R.id.menu_exit:
	    		setResult(RESULT_OK);
	    		finish();
	    		break;
	    	}

    	return true;
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {    	
    	switch (requestCode) {
    	case ADD_NEW:
    	case EDIT:
    		if (resultCode == RESULT_OK) {
    			buildLayout();
    		}
    		break;
    		
		default:
			break;
    	}
    }
}
