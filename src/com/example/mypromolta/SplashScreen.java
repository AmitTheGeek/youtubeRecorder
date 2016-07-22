package com.example.mypromolta;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;

public class SplashScreen extends Activity {

	private CallbackManager callbackManager;
	private AccessTokenTracker accessTokenTracker;
	private ProfileTracker profileTracker;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		 FacebookSdk.sdkInitialize(getApplicationContext());
		accessTokenTracker = new AccessTokenTracker() {
		    @Override
		    protected void onCurrentAccessTokenChanged(AccessToken oldToken, AccessToken newToken) {
		    	accessTokenTracker.stopTracking();
		    }
		 };
	
		profileTracker = new ProfileTracker() {
		    @Override
		    protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {
		    	Profile.setCurrentProfile(newProfile);
		        nextActivity(newProfile);
		        profileTracker.stopTracking();
		    }
		};
		accessTokenTracker.startTracking();
		profileTracker.startTracking();

		callbackManager = CallbackManager.Factory.create();
		setContentView(R.layout.activity_splash_screen);
		
		
		
		LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
		loginButton.setReadPermissions(Arrays.asList("public_profile", "email", "user_location"));

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

			@Override
			public void onSuccess(LoginResult result) {
				// TODO Auto-generated method stub
				System.out.println("Login successful" + result);
				
				final Profile profile = Profile.getCurrentProfile();
				 // Facebook Email address
	            GraphRequest request = GraphRequest.newMeRequest(
	            		result.getAccessToken(),
	                    new GraphRequest.GraphJSONObjectCallback() {
	                        @Override
	                        public void onCompleted(
	                                JSONObject object,
	                                GraphResponse response) {
	                        	
	                        	
	                            Log.v("LoginActivity Response ", response.toString());

	                            try {
	                            	
	                            	String id = object.getString("id");
	                                String Name = object.getString("name");

	                                String FEmail = object.getString("email");
	                                String location = "Not Available";
	                                if(!object.isNull("location")){
		                                JSONObject obj = object.getJSONObject("location");
	                                	location = obj.getString("name");
	                                }else{
	                                	//Write GPS code
	                                	location = getCityName();
	                                	System.out.println("In ELSE LOCATION BY GPS IS" + location);
	                                }
	                                 
	                                
	                                Log.v("ID = ", id);
	                                Log.v("Email = ", " " + FEmail);
	                                Log.v("Location = ", " " + location);
	                                
	                                new PreferenceUtil(getApplicationContext()).setData(FEmail, location, id);
	            			        nextActivity(profile);


	                            } catch (JSONException e) {
	                                e.printStackTrace();
	                            }
	                        }
	                    });
	            Bundle parameters = new Bundle();
	            parameters.putString("fields", "id,name,email,location");
	            request.setParameters(parameters);
	            request.executeAsync();
			}

			@Override
			public void onCancel() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onError(FacebookException error) {
				// TODO Auto-generated method stub
				
			}  });
		
	}
	
	@Override
	protected void onResume() {
	    super.onResume();
	    Profile profile = Profile.getCurrentProfile();
	    nextActivity(profile);
	}

	@Override
	protected void onPause() {

	    super.onPause();
	}

	protected void onStop() {
	    super.onStop();

	}
	
	public String getCityName(){
		 // Getting LocationManager object from System Service LOCATION_SERVICE
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        // Creating a criteria object to retrieve provider
        Criteria criteria = new Criteria();

        // Getting the name of the best provider
        String provider = locationManager.getBestProvider(criteria, true);

        // Getting Current Location
        Location location = locationManager.getLastKnownLocation(provider);
        
        return getLocationName(location.getLatitude(), location.getLongitude());
	}
	
	public String getLocationName(double lattitude, double longitude) {
		
	    String cityName = "Not Available";
	    Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
	    try {

	        List<Address> addresses = gcd.getFromLocation(lattitude, longitude,
	                10);

	        for (Address adrs : addresses) {
	            if (adrs != null) {

	                String city = adrs.getLocality();
	                String country = adrs.getCountryName();
	                if (city != null && !city.equals("")) {
		                if (country != null && !country.equals("")) {
		                    cityName = city + ", " + country;

		                }else{
		                	cityName = city;
		                }
	                    System.out.println("city ::  " + cityName);
	                } else {
	                	
	                }
	                
	               
	                // // you should also try with addresses.get(0).toSring();

	            }

	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    return cityName;

	}
	
	private void nextActivity(Profile profile){
	    if(profile != null){
	    	new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
					Intent i = new Intent(SplashScreen.this, SearchActivity.class);
					startActivity(i);
					finish();
				}
			}, 2000);
	    }
	}
	
	@Override
	 protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);
	    if (callbackManager.onActivityResult(requestCode, resultCode, data)) {
	        return;
	    }	
	}

}
