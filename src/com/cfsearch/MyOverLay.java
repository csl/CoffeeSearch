package com.cfsearch; 

import java.util.Iterator;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.Paint.Style;
import android.util.Log;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;


public class MyOverLay  extends Overlay {

	/**
	 * Stored as global instances as one time initialization is enough
	 */
    private Bitmap mBubbleIcon, mShadowIcon;
    
    private Bitmap mNowIcon;
    
    private MyGoogleMap mLocationViewers;
    
    private Paint	mInnerPaint, mBorderPaint, mTextPaint;
	
    private int infoWindowOffsetX;
    private int infoWindowOffsetY;
    
    private boolean showWinInfo;
    
    private String TAG = "MyOverLay";
    
	/**
	 * It is used to track the visibility of information window and clicked location is known location or not 
	 * of the currently selected Map Location
	 */
    //private MapLocation mSelectedMapLocation;  
    
	public MyOverLay(MyGoogleMap mLocationViewers) {
		
		this.mLocationViewers = mLocationViewers;
		
		mBubbleIcon = BitmapFactory.decodeResource(mLocationViewers.getResources(),R.drawable.bubble);
		mShadowIcon = BitmapFactory.decodeResource(mLocationViewers.getResources(),R.drawable.shadow);
		mNowIcon = BitmapFactory.decodeResource(mLocationViewers.getResources(),R.drawable.mappin_blue);
		showWinInfo = false;
	}
	
	@Override
	public boolean onTap(GeoPoint p, MapView mapView)  {
		
		/**
		 * Track the popup display
		 */
		/*
		boolean isRemovePriorPopup = mSelectedMapLocation != null;  

		mSelectedMapLocation = getHitMapLocation(mapView, p);
		if ( isRemovePriorPopup || mSelectedMapLocation != null) 
		{
	    mapView.invalidate();
	    if (showWinInfo == true && mSelectedMapLocation != null)
	    {
	      //show
	      mLocationViewers.showPlaceDiag(mSelectedMapLocation);
	      mSelectedMapLocation = null;
	      showWinInfo = false;
	    }
		}		
		else
		{
		  showWinInfo = false;
		}
		
		 
		return mSelectedMapLocation != null;*/
		return true;
	}
	
    @Override
	public void draw(Canvas canvas, MapView	mapView, boolean shadow) 
  {
      drawNowGeoMap(canvas, mapView, shadow);
   	  drawMapLocations(canvas, mapView, shadow);
   	  //drawInfoWindow(canvas, mapView, shadow);
  }


    /*
    private MapLocation getHitMapLocation(MapView	mapView, GeoPoint	tapPoint) {
    	
    	MapLocation hitMapLocation = null;
		
    	RectF hitTestRecr = new RectF();
		  Point screenCoords = new Point();
    	Iterator<MapLocation> iterator = mLocationViewers.getMapLocations(false).iterator();
    	while(iterator.hasNext()) {
    		MapLocation testLocation = iterator.next();
    		
    		mapView.getProjection().toPixels(testLocation.getPoint(), screenCoords);

	    	// Create a testing Rectangle with the size and coordinates of our icon
	    	// Set the testing Rectangle with the size and coordinates of our on screen icon
    		hitTestRecr.set(-mBubbleIcon.getWidth()/2,-mBubbleIcon.getHeight(),mBubbleIcon.getWidth()/2,0);
    		hitTestRecr.offset(screenCoords.x,screenCoords.y);

	    	//  At last test for a match between our Rectangle and the location clicked by the user
    		mapView.getProjection().toPixels(tapPoint, screenCoords);
    		
    		if (hitTestRecr.contains(screenCoords.x,screenCoords.y)) {
    			hitMapLocation = testLocation;
    			break;
    		}
    	}
    	
    	//  Finally clear the new MouseSelection as its process finished
    	tapPoint = null;
    	
    	return hitMapLocation; 
    }
*/

    private void drawNowGeoMap(Canvas canvas, MapView mapView, boolean shadow) 
    {
      Paint paint = new Paint();
      Point myScreenCoords = new Point();
      
      if (mLocationViewers.nowGeoPoint != null)
      {
     
	      mapView.getProjection().toPixels(mLocationViewers.nowGeoPoint, myScreenCoords);
	      paint.setStrokeWidth(1);
	      paint.setARGB(255, 255, 0, 0);
	      paint.setStyle(Paint.Style.STROKE);
	
	      canvas.drawBitmap(mNowIcon, myScreenCoords.x, myScreenCoords.y, paint);
	      canvas.drawText("現在位置", myScreenCoords.x, myScreenCoords.y, paint);
      }
    }

    
    private void drawMapLocations(Canvas canvas, MapView	mapView, boolean shadow) 
    {
        Paint paint = new Paint();
        Point myScreenCoords = new Point();
        
 
        if (mLocationViewers.StoreGeoPoint != null)
        {
            Log.i(TAG, "address find it");
	        mapView.getProjection().toPixels(mLocationViewers.StoreGeoPoint, myScreenCoords);
	        paint.setStrokeWidth(1);
	        paint.setARGB(255, 255, 0, 0);
	        paint.setStyle(Paint.Style.STROKE);
	
	        canvas.drawBitmap(mNowIcon, myScreenCoords.x, myScreenCoords.y, paint);
	        canvas.drawText(mLocationViewers.StoreName, myScreenCoords.x, myScreenCoords.y, paint);
        }
    }
    
    /*
    private void drawInfoWindow(Canvas canvas, MapView	mapView, boolean shadow) {
    	
    	if ( mSelectedMapLocation != null) {
    		if ( shadow) {
    			//  Skip painting a shadow
    		} else {
				//  First we need to determine the screen coordinates of the selected MapLocation
				Point selDestinationOffset = new Point();
				mapView.getProjection().toPixels(mSelectedMapLocation.getPoint(), selDestinationOffset);
		    	
		    	//  Setup the info window
				int INFO_WINDOW_WIDTH = 175;
				int INFO_WINDOW_HEIGHT = 40;
				
				RectF infoWindowRect = new RectF(0,0,INFO_WINDOW_WIDTH,INFO_WINDOW_HEIGHT);
				
				infoWindowOffsetX = selDestinationOffset.x-INFO_WINDOW_WIDTH/2;
				infoWindowOffsetY = selDestinationOffset.y-INFO_WINDOW_HEIGHT-mBubbleIcon.getHeight();
				infoWindowRect.offset(infoWindowOffsetX,infoWindowOffsetY);

				//  Drawing the inner info window
				canvas.drawRoundRect(infoWindowRect, 10, 10, getmInnerPaint());
				
				//  Drawing the border for info window
				canvas.drawRoundRect(infoWindowRect, 10, 10, getmBorderPaint());
					
				//  Draw the MapLocation's name
				int TEXT_OFFSET_X = 10;
				int TEXT_OFFSET_Y = 15;

        int TEXT1_OFFSET_X = 20;
        int TEXT1_OFFSET_Y = 25;

				canvas.drawText(mSelectedMapLocation.getName(), infoWindowOffsetX+TEXT_OFFSET_X,infoWindowOffsetY+TEXT_OFFSET_Y, getmTextPaint());
        canvas.drawText("請點選地標, 並選擇", infoWindowOffsetX+TEXT1_OFFSET_X,infoWindowOffsetY+TEXT1_OFFSET_Y, getmTextPaint());
        showWinInfo = true;
			}
    	}
    }
*/
}