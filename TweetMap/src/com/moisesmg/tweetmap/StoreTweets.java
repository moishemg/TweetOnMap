package com.moisesmg.tweetmap;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class StoreTweets extends SQLiteOpenHelper {
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "storeTweets";
	private static final Integer LIFESPAN_OFF = 120000; // two minutes
	private enum STATES {PENDING, SHOWN, DELETED};
	
	public StoreTweets(Context context) {
		super(context,DATABASE_NAME,null,DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase dataBase) {
		try {
			this.createDataBase(dataBase);
		} catch (Exception e) {}
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}
	
	public void close() {
	    if (this.getWritableDatabase() != null) {
	    	this.getWritableDatabase().close();
	    }
	}
	
	public SQLiteDatabase storage() {
		return this.getWritableDatabase();
	}
	
	//** maintenance method **/
	private void createDataBase(SQLiteDatabase db){
		try {
			String CREATE_TABLE_NAME = "CREATE TABLE IF NOT EXISTS TWEETS ";
			CREATE_TABLE_NAME += "(id integer primary key autoincrement,idTweet integer, ";
			CREATE_TABLE_NAME += "user text,content text,latitude double,longitude double,createAt integer,int state);";
			db.execSQL(CREATE_TABLE_NAME);
		}
		catch (Exception e) {
			// if error we do nothing... the table aready exists
		}
	}
	
	//** Access methods **//
	public boolean existsTweet(Tweet tweet) {
		boolean exists = false;
		try {
			 String sql = "SELECT COUNT(id) FROM TWEETS WHERE (idTweet='" + tweet.getIdTweet() + "')";
             Cursor cVeces = this.storage().rawQuery(sql, null);
             exists = (cVeces.getCount()>0);
             cVeces.close();
		} catch (Exception e) { }
		return exists;
	}
	
	public boolean saveTweet(Tweet tweet) {
		boolean mResult = true;
		try {
			if (!this.existsTweet(tweet)) {
				ContentValues values = new ContentValues();
				values.put("idTweet", tweet.getUser());
				values.put("user", tweet.getUser());
				values.put("content", tweet.getText());
				values.put("latitude", tweet.getLatitude());
				values.put("longitude", tweet.getLongitude());
				values.put("createAt", System.currentTimeMillis());
				values.put("state", StoreTweets.STATES.PENDING.toString());
				
				mResult = (this.storage().update("TWEETS", values, "" , null) > 0);
			}
		} catch (Exception e) {
			mResult = false;
		}
		return mResult;
	}
	
	public ArrayList<Tweet> getTweetsOnTime(){
		return this.getTweets(StoreTweets.STATES.SHOWN);
	}
	
	public ArrayList<Tweet> getTweetsOffTime(){
		return this.getTweets(StoreTweets.STATES.DELETED);
	}	
	
	private ArrayList<Tweet> getTweets(StoreTweets.STATES state){
		ArrayList<Tweet> lstTweets = new ArrayList<Tweet>();
		try {
			
			String[] fields = new String[] { "id", "idTweet" ,"user", "content" , "latitude" , "longitude" };
			String where = "";
			if (state==StoreTweets.STATES.PENDING) {
				where = "(state=" + StoreTweets.STATES.PENDING + ")";
			} else {
				where = "(createAt<" + (System.currentTimeMillis()-StoreTweets.LIFESPAN_OFF) + ") AND (state=" + StoreTweets.STATES.SHOWN + ")";
			}
			String orderBy = "id";
			
			Cursor cData = this.storage().query("TWEETS", fields, where, null, null, null, orderBy);
			int numRows = cData.getCount();
			cData.moveToFirst();
			for (int i = 0; i < numRows; ++i) {
				Tweet tweet = new Tweet();
				long id = cData.getLong(0);
				tweet.setIdTweet(cData.getLong(1));
				tweet.setUser(cData.getString(2));
                tweet.setText(cData.getString(3));
                tweet.setLatitude(cData.getDouble(4));
                tweet.setLongitude(cData.getDouble(5));
                lstTweets.add(tweet);
                
                this.changeStateTweet(id,state);
                // next record  
                cData.moveToNext();
			} // for
			cData.close();
		} catch (Exception e) {}
		
		return lstTweets;
	}	
	
	private void changeStateTweet(long id,StoreTweets.STATES newState) {
		ContentValues values = new ContentValues();
		values.put("state", newState.toString());
		this.storage().update("TWEETS", values, "id="+id, null);
	}
	
	public void removeTweets(){
		this.storage().delete("TWEETS", null, null);
	}
	
}
