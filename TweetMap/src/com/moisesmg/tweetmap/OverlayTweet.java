package com.moisesmg.tweetmap;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;

public class OverlayTweet extends com.google.android.maps.Overlay  {
	private long idTweet;
	private GeoPoint geoPoint;
	private Activity activity;
	public OverlayTweet(Activity activity,GeoPoint geoPoint,long idTweet){
		super();
		this.activity = activity;
		this.geoPoint = geoPoint;
		this.idTweet = idTweet;
	}
	
	public long getIdTweet(){
		return this.idTweet;
	}
	
	@Override
    public void draw(Canvas canvas, MapView mapView, boolean shadow) {                             
        super.draw(canvas, mapView, shadow);

        if (!shadow) {                                                                              
            Point point = new Point();
            mapView.getProjection().toPixels(geoPoint, point);                                      
            Bitmap bmp = android.graphics.BitmapFactory.decodeResource(activity.getResources(), R.drawable.ic_launcher);   
            int x = point.x - bmp.getWidth() / 2;                                                   
            int y = point.y - bmp.getHeight();                                                      
            canvas.drawBitmap(bmp, x, y, null);                                                     
        }
    }
	
	public boolean equals(Object o) {
		return ((o instanceof OverlayTweet) && ((OverlayTweet)o).getIdTweet()==this.getIdTweet());
	}
}
