package com.moisesmg.tweetmap;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import android.os.AsyncTask;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterStreamTask extends AsyncTask<String, Void, Boolean> {
	final static String CONSUMER_KEY = "6WtOSooOhtHDlluPDLVkYwKp2";
    final static String CONSUMER_SECRET = "HuiRNTj48C5zn4wsfGguJMOXMJZJEpucCJzIqf9ynjYrivqNGL";
    final static String ACCESS_TOKEN = "2854750893-TZzILO3CrTVFzWAXoBK2maittv4EYExw8UStqnA";
    final static String ACCESS_TOKEN_SECRET = "WVOEVdON0P269EMPO3NGnzzfAEtfX99GmVxMgKvZ2XyDp";
	private ConfigurationBuilder cb;
	
    private StoreTweets storage;
    private ActivityMap activity;
    public TwitterStreamTask(ActivityMap activity,StoreTweets storage) {
    	this.activity = activity;
    	this.storage = storage;
    	cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true);
        cb.setOAuthConsumerKey(CONSUMER_KEY);
        cb.setOAuthConsumerSecret(CONSUMER_SECRET);
        cb.setOAuthAccessToken(ACCESS_TOKEN);
        cb.setOAuthAccessTokenSecret(ACCESS_TOKEN_SECRET);
    }
    
    @Override
    protected void onPreExecute() {
        // TODO Auto-generated method stub
        super.onPreExecute();
    }

    @Override
    protected Boolean doInBackground(String... patterns) {
    	boolean result = false;

        if (patterns.length > 0) {
            result = searchTweets(patterns[0]);
        }
        return result;
    }
	
	public Boolean searchTweets(String pattern) {
		boolean mReturnLst = true;
		
		Twitter twitter = new TwitterFactory(cb.build()).getInstance();
		Query query = new Query(pattern);
		query.setCount(50);
		// 100 kms from Málaga
		//query.setGeoCode(new GeoLocation(36.733519,-4.422194), 100, Query.KILOMETERS);
		Calendar cal = new GregorianCalendar();
		//cal.add(Calendar.HOUR, -5);
		query.since(cal.get(Calendar.DAY_OF_MONTH)+"/"+cal.get(Calendar.MONTH)+"/"+cal.get(Calendar.YEAR));
		//query.since("02/11/2014");
        
        try {
        	QueryResult result;
	        do {
	        	result = twitter.search(query);
	            List<twitter4j.Status> tweets = result.getTweets();
	            for (twitter4j.Status tweet : tweets) {
	            	GeoLocation geo = tweet.getGeoLocation(); 
	            	if (geo!=null) {
	            		this.storage.saveTweet(new Tweet(tweet.getId(),tweet.getUser().getScreenName(),tweet.getText(),geo.getLatitude(),geo.getLongitude()));
	            	}
	            }
	        } while ((query = result.nextQuery()) != null);
	    } catch (TwitterException e) { mReturnLst = false; }
        
		return mReturnLst;
	}
	
	@Override
    protected void onPostExecute(Boolean result) {
		this.activity.finishTaskGetTweet();
	}
}
