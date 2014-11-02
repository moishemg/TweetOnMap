package com.moisesmg.tweetmap;

import java.util.ArrayList;
import java.util.List;

public class OverlayTweets {
	private List<OverlayTweet> lstOverlaysTweet;
	public OverlayTweets(){ }
	
	public void add(OverlayTweet pin) {
		if (this.lstOverlaysTweet==null) this.lstOverlaysTweet = new ArrayList<OverlayTweet>();
		if (!this.lstOverlaysTweet.contains(pin)) this.lstOverlaysTweet.add(pin);
	}
	
	public void remove(OverlayTweet pin) {
		if (this.lstOverlaysTweet!=null) {
			if (this.lstOverlaysTweet.contains(pin)) this.lstOverlaysTweet.remove(pin);
		}
	}
	
	public List<OverlayTweet> getAll(){
		return this.lstOverlaysTweet;
	}
	
	public OverlayTweet get(long idTweet) {
		OverlayTweet pin = null;
		if (this.lstOverlaysTweet!=null) {
			int i = 0;
			while (pin!=null && i<this.lstOverlaysTweet.size()) {
				if (this.lstOverlaysTweet.get(i).getIdTweet()==idTweet) {
					pin = this.lstOverlaysTweet.get(i);
				}
				i++;
			}
		}
		return pin;
	}
}
