package com.moisesmg.tweetmap;

import java.util.ArrayList;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import android.os.AsyncTask;

public class ShowTweets extends AsyncTask<String, Void, Boolean> {
	private StoreTweets storage;
	private MapView  mMap;
	private ActivityMap activity;
	public ShowTweets(ActivityMap activity,StoreTweets storage,MapView  mMap) {
		this.storage = storage;
		this.mMap = mMap;
	}
	
    @Override
    protected void onPreExecute() {
        // TODO Auto-generated method stub
        super.onPreExecute();
    }
    
	@Override
	protected Boolean doInBackground(String... params) {
		// TODO Auto-generated method stub
		ArrayList<Tweet> lstTweets = this.storage.getTweetsOnTime();
		if (lstTweets!=null && lstTweets.size()>0) {
			for (Tweet tweet : lstTweets) {
				// show tweet in map
				GeoPoint geoPoint = new GeoPoint((int) (tweet.getLatitude() * 1E6), (int) (tweet.getLongitude() * 1E6));
				OverlayTweet pin = new OverlayTweet(activity,geoPoint,tweet.getIdTweet());
				this.activity.getPins().add(pin);
				this.mMap.getOverlays().add(pin);
			} // for
			this.mMap.invalidate();
		} // if
		return null;
	}
	
	@Override
    protected void onPostExecute(Boolean result) {
		this.activity.finishTaskShowTweet();
	}
}
