package com.gendroid.two;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class IconPicker extends Activity {
	
	private View.OnClickListener mButtonDefault = new View.OnClickListener() {
        public void onClick(View v) {
        	setResult(0);
        	finish();
        }
    };
    private View.OnClickListener mButton1 = new View.OnClickListener() {
        public void onClick(View v) {
        	setResult(1);
        	finish();
        }
    };
    private View.OnClickListener mButton2 = new View.OnClickListener() {
        public void onClick(View v) {
        	setResult(2);
        	finish();
        }
    };
    private View.OnClickListener mButton3 = new View.OnClickListener() {
        public void onClick(View v) {
        	setResult(3);
        	finish();
        }
    };
    private View.OnClickListener mButton4 = new View.OnClickListener() {
        public void onClick(View v) {
        	setResult(4);
        	finish();
        }
    };
    
    private View.OnClickListener mButton5 = new View.OnClickListener() {
        public void onClick(View v) {
        	setResult(5);
        	finish();
        }
    };
    
    private View.OnClickListener mButton6 = new View.OnClickListener() {
        public void onClick(View v) {
        	setResult(6);
        	finish();
        }
    };
    private View.OnClickListener mButton7 = new View.OnClickListener() {
        public void onClick(View v) {
        	setResult(7);
        	finish();
        }
    };
    private View.OnClickListener mButton8 = new View.OnClickListener() {
        public void onClick(View v) {
        	setResult(8);
        	finish();
        }
    };
    private View.OnClickListener mButton9 = new View.OnClickListener() {
        public void onClick(View v) {
        	setResult(9);
        	finish();
        }
    };
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.icon_dialog);
        this.setTitle(R.string.pick_icon);

        ImageButton buttonDefault = (ImageButton)findViewById(R.id.icon_default);
        buttonDefault.setOnClickListener(mButtonDefault);
        
        ImageButton button1 = (ImageButton)findViewById(R.id.icon_1);
        button1.setOnClickListener(mButton1);
        
        ImageButton button2 = (ImageButton)findViewById(R.id.icon_2);
        button2.setOnClickListener(mButton2);
        
        ImageButton button3 = (ImageButton)findViewById(R.id.icon_3);
        button3.setOnClickListener(mButton3);
        
        ImageButton button4 = (ImageButton)findViewById(R.id.icon_4);
        button4.setOnClickListener(mButton4);
        
        ImageButton button5 = (ImageButton)findViewById(R.id.icon_5);
        button5.setOnClickListener(mButton5);
        
        ImageButton button6 = (ImageButton)findViewById(R.id.icon_6);
        button6.setOnClickListener(mButton6);
        
        ImageButton button7 = (ImageButton)findViewById(R.id.icon_7);
        button7.setOnClickListener(mButton7);
        
        ImageButton button8 = (ImageButton)findViewById(R.id.icon_8);
        button8.setOnClickListener(mButton8);
        
        ImageButton button9 = (ImageButton)findViewById(R.id.icon_9);
        button9.setOnClickListener(mButton9);
    }
}
