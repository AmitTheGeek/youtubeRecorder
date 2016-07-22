package com.example.mypromolta;

import java.util.ArrayList;

import com.facebook.Profile;
import com.facebook.login.LoginManager;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

public class SearchActivity extends Activity  {

	private ListView listview;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		
		listview = (ListView)findViewById(R.id.youtubelist);
		
		TextView tv = (TextView) findViewById(R.id.empty);
		String name = "Friend";
		if(Profile.getCurrentProfile() != null)
		 name = Profile.getCurrentProfile().getFirstName();
		tv.setText("Hey " + name + "! \n Search Youtube Videos Above!!");
		
		listview.setEmptyView(tv);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search, menu);
		
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
	    final SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
	    // Assumes current activity is the searchable activity
	   // searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
	    
	    
	    SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() 
	    {
	        public boolean onQueryTextChange(String newText) 
	        {
	            // this is your adapter that will be filtered
//	            listAdapter.getFilter().filter(newText);
	            return true;
	        }

	        public boolean onQueryTextSubmit(String query) 
	        {
	            // this is your adapter that will be filtered
//	            listAdapter.getFilter().filter(query);
	        	searchView.clearFocus();
	        	System.out.println("query 1 is " + query);
	        	new FetchVideoList().execute(query);
	        	
	            return true;
	        }
	    };
	    searchView.setOnQueryTextListener(queryTextListener);
	    
	    menu.findItem(R.id.menu_search).setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
			
			@Override
			public boolean onMenuItemActionExpand(MenuItem item) {
				// TODO Auto-generated method stub
				return true;
			}
			
			@Override
			public boolean onMenuItemActionCollapse(MenuItem item) {
				// TODO Auto-generated method stub
				return true;
			}
		});
	    
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			LoginManager.getInstance().logOut();
			PreferenceUtil pf = new PreferenceUtil(this);
			pf.clearData();
			backActivity();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void backActivity(){
			Intent main = new Intent(SearchActivity.this, SplashScreen.class);
	        startActivity(main);
	        finish();	    
	}
	
	private class FetchVideoList extends AsyncTask<String, Void, ArrayList<VideoProperties>> {
        @Override
        protected ArrayList<VideoProperties> doInBackground(String... params) {
            String query = params[0];
        	ArrayList<VideoProperties> videos = YoutubeSearch.getResults(query);
            return videos;
        }

        @Override
        protected void onPostExecute(ArrayList<VideoProperties> result) {
        	System.out.println("The result is " + result);
        	
        	YoutubeListAdapter adapter = new YoutubeListAdapter(SearchActivity.this, result);
        	listview.setAdapter(adapter);
        }

        @Override
        protected void onPreExecute() {
        }

    }
}
