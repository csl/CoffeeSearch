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
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class StoreView extends Activity {

	
	private ArrayList<HashMap<String, Object>> store_list;
	private ArrayList<store_item> storeitem;

	private ListView show_view;

    private static int DB_VERSION = 1;
    
	private SQLiteDatabase db;
	private SQLiteHelper dbHelper;
	private Cursor cursor;
	
	private int nameid;
		
	
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
         
         storeitem = new ArrayList<store_item>();

         Intent intent=this.getIntent();
         Bundle bunde = intent.getExtras();
         if (bunde != null)
         {
        	 nameid = bunde.getInt("name");
         }
         else
         {
        	 nameid = 0;
         }
        //Display: create ListView class
        show_view = (ListView)findViewById(R.id.listview);
        
        store_list = getStoreList(nameid); 
    
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
	    	        bundle.putString("name", storeitem.get(arg2).name);
	    	        bundle.putString("addr", storeitem.get(arg2).addr);
	    	          
    	    		Intent intent = new Intent();
    	    		intent.setClass(StoreView.this, MyGoogleMap.class);
    	    		intent.putExtras(bundle);
    	    		startActivity(intent);
    	    		finish();
	    		   
	    	   }  
	        });
        }
	}
	
	public ArrayList<HashMap<String, Object>> getStoreList(int name) 
	{
		int i=0;
		ArrayList<HashMap<String, Object>> listitem = new ArrayList<HashMap<String,Object>>();
		
		  try{
	          cursor = db.query(SQLiteHelper.TB2_NAME, null, store_item.NAME + "=" + name, null, null, null, null);

	        cursor.moveToFirst();
	        
	        //no data
	        if (cursor.isAfterLast())
	        {
	        	return null;
	        }
	        
	        while(!cursor.isAfterLast())
	        {
	          store_item sitem = new store_item();
	          sitem.id = cursor.getString(0);
	          sitem.name = cursor.getString(1);
	          sitem.sname = cursor.getString(2);
	          sitem.time = cursor.getString(3);
	          sitem.addr = cursor.getString(4);
	          sitem.phone = cursor.getString(5);
	          sitem.commit = cursor.getString(6);
	          storeitem.add(sitem);
	        }		
		  }catch(IllegalArgumentException e){
			e.printStackTrace();
			++ DB_VERSION;
			dbHelper.onUpgrade(db, --DB_VERSION, DB_VERSION);
		  }
		  
		for (int j=0; j<store_list.size(); j++)
		{
			HashMap<String, Object> map = new HashMap<String, Object>();
			//Log.i("VALUE", item.getWid());
			map.put("ItemTitle", storeitem.get(i).sname + "/" + storeitem.get(i).time + "/" + storeitem.get(i).phone);
			map.put("ItemText",  storeitem.get(i).addr);
			listitem.add(map);			
		}
		  
		return listitem;
	}

	//error message
	private void openOptionsDialog(String info)
	{
	    new AlertDialog.Builder(this)
	    .setTitle("Inquire")
	    .setMessage(info)
	    .setPositiveButton("OK",
	        new DialogInterface.OnClickListener()
	        {
	         public void onClick(DialogInterface dialoginterface, int i)
	         {
	         }
	         }
	        )
	    .show();
	}	
}