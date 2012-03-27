package com.cfsearch;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

public class CoffeeSearchActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Button mButton01 = (Button)findViewById(R.id.btn_index); 
        mButton01.setOnClickListener(new Button.OnClickListener() 
        { 
          public void onClick(View v) 
          {
        		Intent intent = new Intent();
          		intent.setClass(CoffeeSearchActivity.this, StoreList.class);
          		startActivity(intent);                 	  
          } 
        } ); 
        
    }
    
    public boolean onKeyDown(int keyCode, KeyEvent event) 
    {
    	if(keyCode==KeyEvent.KEYCODE_BACK)
    	{  
    		openOptionsDialog();
    		return true;
    	}
		
		return super.onKeyDown(keyCode, event);  
    }

    //show message, ask exit yes or no
    private void openOptionsDialog() {
      
      new AlertDialog.Builder(this)
        .setTitle("Exit?")
        .setMessage("Exit?")
        .setNegativeButton("No",
            new DialogInterface.OnClickListener() {
            
              public void onClick(DialogInterface dialoginterface, int i) 
              {
              }
        }
        )
     
        .setPositiveButton("Yes",
            new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialoginterface, int i) 
            {
              android.os.Process.killProcess(android.os.Process.myPid());           
              finish();
            }
            
        }
        )
        
        .show();
    }    
}