package com.moisesmg.tweetmap;

import android.os.Bundle;
import android.os.Handler;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;

public class ActivityMap extends MapActivity {

    private MapView map = null;
    private StoreTweets storage;
    private OverlayTweets lstTweets;
    private boolean finishedTaskGetTweet;
    private boolean finishedTaskShowTweet;
    private boolean finishedTaskRemoveTweet;
    private Handler mHandlerTimer;
    private String searchTerm;
    private ActivityMap instance;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        this.instance = this;
        this.finishedTaskGetTweet = false;
        this.finishedTaskShowTweet = false;
        this.finishedTaskRemoveTweet = false;
        
        this.mHandlerTimer = new Handler();
        this.mHandlerTimer.removeCallbacks(throwTasks);
        this.mHandlerTimer.postDelayed(throwTasks, 1000 * 60 * 5); // every 5 minutes
        
        this.map = (MapView)findViewById(R.id.map);
        this.map.getController().setCenter(new GeoPoint((int)(36.733519* 1E6),(int)(-4.422194* 1E6)));
        this.map.getController().setZoom(14);
        this.map.setBuiltInZoomControls(true);
        this.map.setSatellite(true);
        this.storage = new StoreTweets(this);
        this.storage.removeTweets();
        
        this.lstTweets = new OverlayTweets();
        this.searchTerm = (String)getIntent().getExtras().get(MainActivity.TAG_SEARCH_TERM);
		new TwitterStreamTask(this,storage).execute(this.searchTerm);
		new ShowTweets(this,storage,this.map).execute();
		new RemoveTweets(this,storage,this.map).execute();
    }
    
    @Override
    protected void onDestroy() {
       super.onDestroy(); 
       this.mHandlerTimer.removeCallbacks(throwTasks); 
    }
    
    private Runnable throwTasks = new Runnable()
    { 
        public void run() {
        	if (finishedTaskGetTweet) new TwitterStreamTask(instance,storage).execute(searchTerm);
        	if (finishedTaskShowTweet) new ShowTweets(instance,storage,map).execute();
        	if (finishedTaskRemoveTweet) new RemoveTweets(instance,storage,map).execute();

           mHandlerTimer.removeCallbacks(throwTasks); 
           mHandlerTimer.postDelayed(this, 1000 * 60 * 5); // every 5 minutes
        } 
    } ;
    
	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
	
	public OverlayTweets getPins(){
		return this.lstTweets;
	}
	
	public void finishTaskShowTweet(){
		this.finishedTaskShowTweet = true;
	}
	
	public void finishTaskGetTweet(){
		this.finishedTaskGetTweet = true;
	}
	
	public void finishTaskRemoveTweet(){
		this.finishedTaskRemoveTweet = true;
	}
	
}

