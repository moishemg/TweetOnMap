package com.moisesmg.tweetmap;

import java.util.ArrayList;
import android.os.AsyncTask;

import com.google.android.maps.MapView;

public class RemoveTweets extends AsyncTask<String, Void, Boolean> {
	private StoreTweets storage;
	private MapView  mMap;
	private ActivityMap activity;
	public RemoveTweets(ActivityMap activity,StoreTweets storage,MapView  mMap) {
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
		ArrayList<Tweet> lstTweets = this.storage.getTweetsOffTime();
		if (lstTweets!=null && lstTweets.size()>0) {
			for (Tweet tweet : lstTweets) {
				// remove tweet in map
				OverlayTweet pin = this.activity.getPins().get(tweet.getIdTweet());
				if (pin!=null) this.mMap.getOverlays().remove(pin);
			} // for
			this.mMap.invalidate();
		} // if
		return true;
	}

	@Override
    protected void onPostExecute(Boolean result) {
		this.activity.finishTaskRemoveTweet();
	}
}
