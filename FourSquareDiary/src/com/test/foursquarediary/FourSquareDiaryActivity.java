package com.test.foursquarediary;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.test.foursquarediary.FourSquareApp.FsqListener;
import com.test.foursquarediary.classes.FourSquareVenue;
import com.test.foursquarediary.util.AppConstants;

public class FourSquareDiaryActivity extends Activity {
	
	private static final String TAG = "FourSquareDiaryActivity";
	
	private FourSquareApp fourSquareApp;
	private ListView rListView; 
	private Button rLoginButton;
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);
        
        rListView = (ListView) findViewById(R.id.venueListView);
        rLoginButton = (Button) findViewById(R.id.loginButton);
        
        FsqListener listener = new FsqListener() {
			@Override
			public void onSuccess(String msg) {
				Log.i(TAG, "FsqListener - onSuccess - " + msg);
				Toast.makeText(FourSquareDiaryActivity.this, " Success - " + msg , Toast.LENGTH_SHORT).show();
			}
			
			@Override
			public void onFail(String error) {
				
			}

			@Override
			public void onSuccessVenues(List<FourSquareVenue> venues) {
				Toast.makeText(getApplicationContext(), " Retrieved " + venues.size() + " venues. ", Toast.LENGTH_SHORT).show();
				addToListView(getApplicationContext(), venues);
			}
			
			@Override
			public void onAuthSuccess(String msg) {
				String name[] = fourSquareApp.getSession().getUserName().split(" ");
				rLoginButton.setText("Welcome! " + name[0]);
				Toast.makeText(getApplicationContext(), "Logged In", Toast.LENGTH_SHORT).show();
			}
		}; 
        
        fourSquareApp = new FourSquareApp(this, AppConstants.CLIENT_ID, AppConstants.URL_CALLBACK);
        fourSquareApp.setListner(listener);
        
        if(fourSquareApp.isUserTokenValid()) {
        	Log.d(TAG, "Token is Valid");
        	String name[] = fourSquareApp.getSession().getUserName().split(" ");
        	rLoginButton.setText("Welcome! " + name[0]);
        }
    }
    
    public void onClickLogin4Sq(View view) {
    	if(rLoginButton.getText().toString().startsWith("Login")) {
    		Log.i(TAG, " Login 4sq clicked ");
        	//Toast.makeText(this, " Logging In " , Toast.LENGTH_SHORT).show();
        	fourSquareApp.authorize();
    	} else {
    		fourSquareApp.getSession().clearAccessToken();
    		Toast.makeText(this, " Logged out " , Toast.LENGTH_SHORT).show();
    		rListView.setAdapter(new ArrayAdapter<FourSquareVenue>(getApplicationContext(), R.layout.venues_list_item, new ArrayList<FourSquareVenue>()));
    		rLoginButton.setText("Login to 4Sq");
    	}
    }
    
    public void onClickVenueHistory(View view) {
    	Log.i(TAG, " Attempting to get Venues ");
//    	List<FourSquareVenue> venues = fourSquareApp.getVenueHistory();
    	fourSquareApp.getVenueHistory1();
    	//addToListView(view, venues);
    }
    
    private void addToListView(Context c, List<FourSquareVenue> venues) {
    	Log.i(TAG, " Addding Venues to ListView ");
    	rListView.setAdapter(new ArrayAdapter<FourSquareVenue>(c, R.layout.venues_list_item, venues));
    	Log.i(TAG, " Added Venues ");
    }
    
    public void onClickMap(View view) {
    	Log.i(TAG, " Attempting to Map Venues ");
    	Intent venueMapperIntent = new Intent(view.getContext(), VenueMapperActivity.class);
    	startActivity(venueMapperIntent);
    }
    
}