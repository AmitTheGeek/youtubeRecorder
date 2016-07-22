package com.example.mypromolta;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PreferenceUtil {

	private SharedPreferences sp;
	private SharedPreferences.Editor editor;

	public PreferenceUtil(Context ctx) {
		sp = PreferenceManager.getDefaultSharedPreferences(ctx);
		editor = sp.edit();
	}
	
	public String getEmail(){
		String email = sp.getString("email", null);
		return email;
	}
	
	private void setEmail(String email){
		editor.putString("email", email);
		editor.commit();
	}
	

	public String getLocation() {
		String location = sp.getString("location", null);
		return location;
	}

	
	private void setLocation(String location) {
		editor.putString("location", location);
		editor.commit();
	}
	
	public String getFbid() {
		String location = sp.getString("fbid", null);
		return location;
	}

	
	private void setFbid(String fbid) {
		editor.putString("fbid", fbid);
		editor.commit();
	}
	
	public void clearData(){
		editor.remove("email");
		editor.remove("location");
		editor.remove("fbid");
		editor.commit();
	}
	
	public void setData(String email, String location, String fbid){
		setEmail(email);
		setLocation(location);
		setFbid(fbid);
	}
}
