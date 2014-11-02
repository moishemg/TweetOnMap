package com.moisesmg.tweetmap;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import oauth.signpost.OAuthProvider;
import oauth.signpost.basic.DefaultOAuthProvider;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.json.JSONArray;
import org.json.JSONObject;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Credentials;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

public class DownloadTweetsTask extends AsyncTask<String, Void, String> {
	private final String CALLBACK_URL = "app://twitter";
    final static String CONSUMER_KEY = "6WtOSooOhtHDlluPDLVkYwKp2";
    final static String CONSUMER_SECRET = "HuiRNTj48C5zn4wsfGguJMOXMJZJEpucCJzIqf9ynjYrivqNGL";
    final static String TWITTER_REQUEST_TOKEN_URL = "http://twitter.com/oauth/request_token";
    final static String TWITTER_ACCESS_TOKEN_URL = "http://twitter.com/oauth/access_token";
    final static String TWITTER_AUTHORIZE_URL = "http://twitter.com/oauth/authorize";

    final static String TwitterTokenURL = "https://api.twitter.com/oauth2/token";
    final static String TwitterStreamURL = "https://stream.twitter.com/1.1/statuses/filter.json?track=";
    
    private Activity activity;
    private StoreTweets storage;
    private ProgressDialog pDialog;
    public DownloadTweetsTask(Activity activity,StoreTweets storage) {
    	this.activity = activity;
    	this.storage = storage;
    }
    
    @Override
    protected void onPreExecute() {
        // TODO Auto-generated method stub
        pDialog = new ProgressDialog(this.activity);
        pDialog.setMessage(this.activity.getString(R.string.loadingTweets));
        pDialog.show();
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... screenNames) {
        String result = null;

        if (screenNames.length > 0) {
            result = getTwitterStream(screenNames[0]);
        }
        return result;
    }

    // onPostExecute convert the JSON results into a Twitter object (which is an Array list of tweets
    @Override
    protected void onPostExecute(String result) {

    // converts a string of JSON data into a list objects
       // ArrayList< <listItems> = new ArrayList<ListModel>();
        if (result != null && result.length() > 0) {
            try{
                JSONArray sessions = new JSONArray(result);
                Log.i("Result Array", "Result : "+result);
                for (int i = 0; i < sessions.length(); i++) {
                        JSONObject session = sessions.getJSONObject(i);
                            //ListModel lsm = new ListModel();
                            //lsm.setTxt_content(session.getString("text"));

                            String dte = session.getString("created_at");
                            SimpleDateFormat dtformat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzzzz yyyy");
                            Date d = dtformat.parse(dte);
                            SimpleDateFormat dtfm = new SimpleDateFormat("EEE, MMM dd, hh:mm:ss a yyyy");
                            String date = dtfm.format(d);

                            //lsm.setTxt_date(date);
                            //listItems.add(lsm);

                }

                }
            catch (Exception e){  }
        } 
        
        pDialog.dismiss();

        
    }


    private String getResponseBody(HttpRequestBase request) {
        StringBuilder sb = new StringBuilder();
        try {

            DefaultHttpClient httpClient = new DefaultHttpClient(new BasicHttpParams());
            HttpResponse response = httpClient.execute(request);
            int statusCode = response.getStatusLine().getStatusCode();
            String reason = response.getStatusLine().getReasonPhrase();

            if (statusCode == 200) {

                HttpEntity entity = response.getEntity();
                InputStream inputStream = entity.getContent();

                BufferedReader bReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
                String line = null;
                while ((line = bReader.readLine()) != null) {
                    sb.append(line);
                }
            } else {
                sb.append(reason);
            }
        } catch (Exception ex) { }
        return sb.toString();
    }

    private String getTwitterStream(String searchTerm) {
        String results = null;

        // Step 1: Encode consumer key and secret
        try {
        	/*
        	CommonsHttpOAuthConsumer httpOauthConsumer = new CommonsHttpOAuthConsumer(DownloadTweetsTask.CONSUMER_KEY, DownloadTweetsTask.CONSUMER_SECRET);
            OAuthProvider httpOauthprovider = new DefaultOAuthProvider(TWITTER_REQUEST_TOKEN_URL,TWITTER_ACCESS_TOKEN_URL,TWITTER_AUTHORIZE_URL);
            httpOauthprovider.setOAuth10a(true);
            
           // String authUrl = httpOauthprovider.retrieveRequestToken(httpOauthConsumer, "mdw://twitter");
           // this.activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(authUrl)));
        
            
            HttpPost post = new HttpPost(DownloadTweetsTask.TwitterStreamURL + searchTerm);
            HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setUseExpectContinue(params, false);
            post.setParams(params);
            // sign the request to authenticate
            httpOauthConsumer.sign(post);
            //String responsex = this.getResponseBody(post);
            //JSONArray array = new JSONArray(responsex);
            */
        	
            // URL encode the consumer key and secret
            String urlApiKey = URLEncoder.encode(DownloadTweetsTask.CONSUMER_KEY, "UTF-8");
            String urlApiSecret = URLEncoder.encode(DownloadTweetsTask.CONSUMER_SECRET, "UTF-8");

            // Concatenate the encoded consumer key, a colon character, and the
            // encoded consumer secret
            String combined = urlApiKey + ":" + urlApiSecret;

            // Base64 encode the string
            String base64Encoded = Base64.encodeToString(combined.getBytes(), Base64.NO_WRAP);
            
            HttpPost httpPost = new HttpPost();
            httpPost.setURI(new java.net.URI(DownloadTweetsTask.TwitterTokenURL));

            // Step 2: Obtain a bearer token
            //HttpPost httpPost = new HttpPost(DownloadTweetsTask.TwitterTokenURL);
            httpPost.setHeader("Authorization", "Basic " + base64Encoded);  
            //httpPost.setHeader("Authorization" ,"OAuth oauth_consumer_key='6WtOSooOhtHDlluPDLVkYwKp2', oauth_nonce='90dfd85094cf5338b88a2a843f4d3035', oauth_signature='R3sruFbwl5lfJ3pXMpmHOuDtX58%3D', oauth_signature_method='HMAC-SHA1', oauth_timestamp='1414870782', oauth_token='2854750893-TZzILO3CrTVFzWAXoBK2maittv4EYExw8UStqnA', oauth_version='1.0'");
            httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
            httpPost.setEntity(new StringEntity("grant_type=client_credentials"));
            
            String rawAuthorization = getResponseBody(httpPost);
           
            
            JSONObject session = new JSONObject(rawAuthorization);

            // Applications should verify that the value associated with the
            // token_type key of the returned object is bearer
            if (session.getString("token_type").equals("bearer")) {

                // Step 3: Authenticate API requests with bearer token
                HttpGet httpGet = new HttpGet(DownloadTweetsTask.TwitterStreamURL + searchTerm);

                // construct a normal HTTPS request and include an Authorization
                // header with the value of Bearer <>
                httpGet.setHeader("Authorization", "Bearer " + session.getString("access_token"));
               // httpGet.setHeader("Authorization", "Bearer " + ACCESS_TOKEN);
                httpGet.setHeader("Content-Type", "application/json");
                // update the results with the body of the response
                results = getResponseBody(httpGet);
            }
        } catch (Exception ex) {
            Log.i("Exception", ex.toString());
        }
        return results;
    }

}
