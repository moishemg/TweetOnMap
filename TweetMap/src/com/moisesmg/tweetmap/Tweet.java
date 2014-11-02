package com.moisesmg.tweetmap;

public class Tweet {
	private String user;
	private String text;
	private double latitude;
	private double longitude;
	private long idTweet;
	
	public Tweet(){}
	public Tweet(long idTweet,String user,String text,double latitude,double longitude) {
		this.setIdTweet(idTweet);
		this.setUser(user);
		this.setText(text);
		this.setLatitude(latitude);
		this.setLongitude(longitude);
	}
	
	public long getIdTweet(){
		return this.idTweet;
	}
	
	public void setIdTweet(long value){
		this.idTweet = value;
	}
	
	public String getUser(){
		return this.user;
	}
	
	public void setUser(String value){
		if (value.substring(1, 2)!="@") value = "@" + value; 
		this.user = value;
	}
	
	public String getText(){
		return this.text;
	}
	
	public void setText(String value){
		this.text = value;
	}
	
	public double getLatitude(){
		return this.latitude;
	}
	
	public void setLatitude(double value){
		this.latitude = value;
	}
	
	public double getLongitude(){
		return this.longitude;
	}
	
	public void setLongitude(double value){
		this.longitude = value;
	}
}
