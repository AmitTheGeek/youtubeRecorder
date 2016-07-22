package com.example.mypromolta;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.Provider;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.gson.GsonBuilder;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

public class YoutubeViewActivity extends  YouTubeBaseActivity implements
YouTubePlayer.OnInitializedListener {

	private YouTubePlayerView youTubePlayerView;
	private YouTubePlayer mPlayer;
	private String mVideo;
	Timer timer = new Timer();


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.activity_youtube_view);
		Bundle extra = getIntent().getExtras();
		mVideo = extra.getString("video");
		youTubePlayerView = (YouTubePlayerView)findViewById(R.id.youtubeplayerview);
	    youTubePlayerView.initialize(YoutubeSearch.APIKEY, this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.youtube_view, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onInitializationFailure(Provider arg0, YouTubeInitializationResult arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onInitializationSuccess(Provider arg0, YouTubePlayer player, boolean arg2) {
		 mPlayer = player;
		 mPlayer.setFullscreen(true);
		 mPlayer.setShowFullscreenButton(false);
		 mPlayer.loadVideo(mVideo);
		 
		 timer.scheduleAtFixedRate(task, 0, 5000);
	}
	
	final TimerTask task = new TimerTask() {
        @Override
        public void run() {

        	if(mPlayer == null){
        		return;
        	}
        	
        	try{
		        int t = (mPlayer.getCurrentTimeMillis()/1000);
		        System.out.println("The T is" + t);
				if(t>= 30){
					execute();
					show30secMessage();
					timer.cancel();
	                timer.purge();
				}else{
					System.out.println("TIMER WORKING");
				}
        	}catch(Exception e){
        		e.printStackTrace();
        	}
				
        }
    };
    
    public void show30secMessage(){
    	runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				Toast.makeText(getApplicationContext(), "30 secs passed..Sending Request", Toast.LENGTH_LONG);
				
			}
		});
    }


	@Override
	public void finish() {
		timer.cancel();
	    timer.purge();
	    super.finish();
	}
	
	@Override
	public void onDestroy(){
		mPlayer  = null;
		super.onDestroy();
	}
	
	public void execute() {
		PreferenceUtil pf = new PreferenceUtil(this);

	    Map<String, String> data = new HashMap<String, String>();
	    data.put("FacebookId", pf.getFbid());
	    data.put("Email", pf.getEmail());
	    data.put("Location", pf.getLocation());
	    data.put("Videoid", mVideo);
	    String json = new GsonBuilder().create().toJson(data, Map.class);
	    makeRequest("http://requestb.in/1c4wuz11", json);
	}

	public static org.apache.http.HttpResponse makeRequest(String uri, String json) {
	    try {
	        HttpPost httpPost = new HttpPost(uri);
	        httpPost.setEntity(new StringEntity(json));
	        httpPost.setHeader("Accept", "application/json");
	        httpPost.setHeader("Content-type", "application/json");
	        return new DefaultHttpClient().execute(httpPost);
	    } catch (UnsupportedEncodingException e) {
	        e.printStackTrace();
	    } catch (ClientProtocolException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    return null;
	}
}
