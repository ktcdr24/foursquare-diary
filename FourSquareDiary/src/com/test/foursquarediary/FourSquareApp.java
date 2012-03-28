package com.test.foursquarediary;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.test.foursquarediary.FourSquareDialog.FSqDialogListener;
import com.test.foursquarediary.classes.FourSquareVenue;
import com.test.foursquarediary.util.AppConstants;
import com.test.foursquarediary.util.Util;

public class FourSquareApp {
	
	private FourSquareSession mSession;
	private ProgressDialog mProgress;
	private String mAccessToken;
	private String mAuthUrl;
	private FsqListener listener;
	private FSqDialogListener dialogListener;
	private FourSquareDialog mDialog;
	
	private static final String TAG = "FourSquareApp";
	
	public static List<FourSquareVenue> mVenues;
	
	public FourSquareApp(Context context, String clientId, String callbackUrl) {
		mSession = new FourSquareSession(context);
		mAccessToken = mSession.getAccessToken();
		Log.d(TAG, "name = " + mSession.getUserName() + ", token = " + mAccessToken);
		mAuthUrl = AppConstants.URL_AUTH + "&client_id=" + clientId + "&redirect_uri=" + callbackUrl;
		
		dialogListener = new FSqDialogListener() {
			@Override
			public void onPageLoaded() {
				listener.onSuccess(" Page Loaded ");
			}
			
			@Override
			public void onError() {
				
			}

			@Override
			public void onComplete(String accessToken) {				
				String username;
				try {
					username = fetchUserName(accessToken);
					saveToSession(username, accessToken);
					mAccessToken = accessToken;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onClose(String msg) {
				listener.onAuthSuccess(msg);
			}
		};
		mDialog = new FourSquareDialog(context, mAuthUrl, dialogListener);
		
		mProgress = new ProgressDialog(context);
		mProgress.setCancelable(false);
		
	}
	
	public boolean hasAccessToken() {
		return (mAccessToken==null) ? false : true;
	}
	
//	public void getAccessToken() {
//		mProgress.setMessage("Getting Access Token");
//		mProgress.show();
//		
//		new Thread() {
//			public void run() {
//				try {
//					//URL url = new URL(AppConstants.URL_TOKEN + "&code");
//					Thread.sleep(5000);
//					mHandler.sendMessage(mHandler.obtainMessage(0, 0, 0));
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		}.start();
//	}
	
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if(msg.what == 0 && msg.arg1 == 0) {
				mProgress.dismiss();
				listener.onSuccess("success - handleMessage - what = 0, arg1 = 0");
			} else if(msg.what == 0 && msg.arg1 == 1) {
				mProgress.dismiss();
				//listener.onSuccess("success - handleMessage - what = 0, arg1 = 1");
				listener.onSuccessVenues(mVenues);
			}
		}
	};
	
	public void saveToSession(String username, String accessToken) {
		mSession.storeAccessToken(username, accessToken);
	}
	
	public String fetchUserName(String accessToken) throws IOException {
		Log.d(TAG, "attempting to fetch username");
		String username = null;
		try {

			String urlString = AppConstants.URL_API + "/users/self?oauth_token=" + accessToken;
			Log.d(TAG, "Opening URL " + urlString);
			
			String response		= Util.getResultForRequest(urlString);
			Log.d(TAG, "response = " + response);
			JSONObject jsonObj 	= (JSONObject) new JSONTokener(response).nextValue();
			JSONObject resp		= (JSONObject) jsonObj.get("response");
			JSONObject user		= (JSONObject) resp.get("user");
			
			String firstName 	= user.getString("firstName");
	    	String lastName		= user.getString("lastName");
	    	Log.i(TAG, "Got user name: " + firstName + " " + lastName);
	    	username = firstName + " " + lastName;
	    	
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (ProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			Log.d(TAG, ">> " + e.getMessage());
			if(e.getMessage().equalsIgnoreCase("Received authentication challenge is null"))
				throw e;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return username;
	}
	
	public void authorize() {
		mDialog.show();
	}
	
	public boolean isUserTokenValid() {
		boolean toRet = false;
		String uName;
		try {
			uName = fetchUserName(mAccessToken);
			if(mSession.getUserName().equalsIgnoreCase(uName)){
				toRet = true;
			}
		} catch (IOException e) {
			e.printStackTrace();
			toRet = false;
		} 
		return toRet;
	}
	
	public List<FourSquareVenue> getVenueHistory() {
    	List<FourSquareVenue> venues = new ArrayList<FourSquareVenue>();
    	try {
    		String url = AppConstants.URL_SELF_VENUE_HISTORY  + AppConstants.API_V_PARAMETER + "&oauth_token=" + mAccessToken ;
    		Log.d(TAG, " venueHisotry URL - " + url);
			String response = Util.getResultForRequest(url);
			Log.d(TAG, " venueHisotry response - " + response);
			JSONObject jsonObj 	= (JSONObject) new JSONTokener(response).nextValue();
			
			JSONObject venuesInResponse = jsonObj.getJSONObject("response").getJSONObject("venues");
			JSONArray items = venuesInResponse.getJSONArray("items");
			
			JSONObject item, venueItem, locationItem;
			FourSquareVenue venue;
			for(int i=0; i<items.length(); ++i) {
				try {
					item = items.getJSONObject(i);
					venue = new FourSquareVenue();
					venue.setBeenHere(item.getInt("beenHere"));

					venueItem = item.getJSONObject("venue");
					venue.setId(venueItem.getString("id"));
					venue.setName(venueItem.getString("name"));
					if(venue.getName().indexOf("untitled") != -1) continue; 
					
					locationItem = venueItem.getJSONObject("location");

					venue.setLocation(
							Double.valueOf(locationItem.getString("lat")),
							Double.valueOf(locationItem.getString("lng")));
				} catch (JSONException e) {
					continue;
				}
				venues.add(venue);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
    	Log.d(TAG, " done with getting venueHistory - " + venues.size());
    	mVenues = venues;
    	return mVenues;
    }
	
	public void getVenueHistory1() {
		mProgress.setMessage("Getting Venues...");
		mProgress.show();
		
		new Thread() {
			public void run() {
				try {
					List<FourSquareVenue> venues = new ArrayList<FourSquareVenue>();
			    	try {
			    		String url = AppConstants.URL_SELF_VENUE_HISTORY  + AppConstants.API_V_PARAMETER + "&oauth_token=" + mAccessToken ;
			    		Log.d(TAG, " venueHisotry URL - " + url);
						String response = Util.getResultForRequest(url);
						Log.d(TAG, " venueHisotry response - " + response);
						JSONObject jsonObj 	= (JSONObject) new JSONTokener(response).nextValue();
						
						JSONObject venuesInResponse = jsonObj.getJSONObject("response").getJSONObject("venues");
						JSONArray items = venuesInResponse.getJSONArray("items");
						
						JSONObject item, venueItem, locationItem;
						FourSquareVenue venue;
						for(int i=0; i<items.length(); ++i) {
							try {
								item = items.getJSONObject(i);
								venue = new FourSquareVenue();
								venue.setBeenHere(item.getInt("beenHere"));

								venueItem = item.getJSONObject("venue");
								venue.setId(venueItem.getString("id"));
								venue.setName(venueItem.getString("name"));
								if(venue.getName().indexOf("untitled") != -1) continue; 
								
								locationItem = venueItem.getJSONObject("location");

								venue.setLocation(
										Double.valueOf(locationItem.getString("lat")),
										Double.valueOf(locationItem.getString("lng")));
							} catch (JSONException e) {
								continue;
							}
							venues.add(venue);
						}
					} catch (IOException e) {
						e.printStackTrace();
					} catch (JSONException e) {
						e.printStackTrace();
					}
			    	Log.d(TAG, " done with getting venueHistory - " + venues.size());
			    	mVenues = venues;
			    	mHandler.sendMessage(mHandler.obtainMessage(0, 1, 0));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
	}
	
	public void setListner(FsqListener listener) {
		this.listener = listener;
	}
	
	public FourSquareSession getSession() {
		return mSession;
	}
	
	public interface FsqListener {
		public abstract void onSuccess(String msg);
		public abstract void onFail(String error);
		public abstract void onSuccessVenues(List<FourSquareVenue> venues);
		public abstract void onAuthSuccess(String msg);
	}	
}
