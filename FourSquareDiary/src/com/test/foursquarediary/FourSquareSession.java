package com.test.foursquarediary;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.test.foursquarediary.util.AppConstants;

public class FourSquareSession {
	private static final String TAG = "FourSquareSession";
	SharedPreferences preferences;
	private Editor editor;
	
	public FourSquareSession(Context context) {
		preferences = context.getSharedPreferences(AppConstants.PREFS_4SQ_NAME, Context.MODE_PRIVATE);
		editor = preferences.edit();
	}
	
	public String getAccessToken() {
		return preferences.getString(AppConstants.PREFS_4SQ_KEY_ACCESS_TOKEN, null);
	}
	
	public String getUserName() {
		return preferences.getString(AppConstants.PREFS_4SQ_KEY_USERNAME, null);
	}
	
	public void storeAccessToken(String username, String accessToken) {
		Log.d(TAG, "storing: username = "+username+", access token = "+accessToken);
		editor.putString(AppConstants.PREFS_4SQ_KEY_USERNAME, username);
		editor.putString(AppConstants.PREFS_4SQ_KEY_ACCESS_TOKEN, accessToken);
		editor.commit();
	}
	
	public void clearAccessToken() {
		editor.putString(AppConstants.PREFS_4SQ_KEY_USERNAME, null);
		editor.putString(AppConstants.PREFS_4SQ_KEY_ACCESS_TOKEN, null);
		editor.commit();
	}

}
