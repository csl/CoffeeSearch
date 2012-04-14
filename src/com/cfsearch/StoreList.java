package com.cfsearch;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class StoreList extends Activity {

	
	private ArrayList<HashMap<String, Object>> store_list;

	private ListView show_view;

    private static int DB_VERSION = 1;
    
	private SQLiteDatabase db;
	private SQLiteHelper dbHelper;
	private Cursor cursor;
	
	String TAG = "StoreList";
		
	
	public void onCreate(Bundle savedInstanceState) 
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.storelist);

        try{
            dbHelper = new SQLiteHelper(this, SQLiteHelper.DB_NAME, null, DB_VERSION);
            db = dbHelper.getWritableDatabase();
          }
          catch(IllegalArgumentException e){
            e.printStackTrace();
            ++ DB_VERSION;
            dbHelper.onUpgrade(db, --DB_VERSION, DB_VERSION);
          }        
        //Display: create ListView class
        show_view = (ListView)findViewById(R.id.listview);
        
        store_list = getStoreList(); 
    
        
        if (store_list != null)
        {
	        SimpleAdapter listitemAdapter=new SimpleAdapter(this,  
	        								    store_list, 
	    										R.layout.no_listview_style,
	    										new String[]{"ItemTitle","ItemText"}, 
	    										new int[]{R.id.topTextView,R.id.bottomTextView}  
	    										);  
	    
	        show_view.setAdapter(listitemAdapter);
	        show_view.setOnItemClickListener(new OnItemClickListener() 
	        {          
	    	   @Override  
	    	   public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,  
	    	     long arg3) 
	    	   {
	    	        Bundle bundle = new Bundle();
	    	        bundle.putInt("name", arg2+1);
	    	          
    	    		Intent intent = new Intent();
    	    		intent.setClass(StoreList.this, StoreView.class);
    	    		intent.putExtras(bundle);
    	    		startActivity(intent);
    	    		finish();
	    	   }  
	        });
        }
	}
	
	public ArrayList<HashMap<String, Object>> getStoreList() 
	{
		int i=0;
		ArrayList<HashMap<String, Object>> listitem = new ArrayList<HashMap<String,Object>>();

		ArrayList<store_list> cstore_list = new ArrayList<store_list>();
		
		  try{
	          cursor = db.query(SQLiteHelper.TB_NAME, null, null, null, null, null, null);

	        cursor.moveToFirst();
	        
	        //no data
	        if (cursor.isAfterLast())
	        {
	        	openOptionsDialog("查無data, 請更新database");
	        	return null;
	        }
	        
	        while(!cursor.isAfterLast())
	        {
	          store_list sitem = new store_list();
	          sitem.id = cursor.getString(0);
	          sitem.name = cursor.getString(1);
	          sitem.commit = cursor.getString(2);
	          Log.i(TAG, sitem.id);
	          cstore_list.add(sitem);
	          cursor.moveToNext();
	        }		
		  }catch(IllegalArgumentException e){
			e.printStackTrace();
			++ DB_VERSION;
			dbHelper.onUpgrade(db, --DB_VERSION, DB_VERSION);
		  }
		  
		for (int j=0; j<cstore_list.size(); j++)
		{
			HashMap<String, Object> map = new HashMap<String, Object>();
			Log.i("VALUE", cstore_list.get(j).id);
			map.put("ItemTitle", cstore_list.get(j).name);
			map.put("ItemText",  cstore_list.get(j).id);
			listitem.add(map);			
		}
		  
		return listitem;
	}

	//error message
	private void openOptionsDialog(String info)
	{
	    new AlertDialog.Builder(this)
	    .setTitle("message")
	    .setMessage(info)
	    .setPositiveButton("OK",
	        new DialogInterface.OnClickListener()
	        {
	         public void onClick(DialogInterface dialoginterface, int i)
	         {
	        	finish();
	         }
	         }
	        )
	    .show();
	}	
}