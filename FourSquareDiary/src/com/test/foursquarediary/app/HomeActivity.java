package com.test.foursquarediary.app;

import android.app.Activity;
import android.os.Bundle;

import com.test.foursquarediary.R;

public class HomeActivity extends Activity {
	
	public static final String TAG = "HomeActivity";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_screen);
		
		getVenueHistory();
	}

	private void getVenueHistory() {
		
	}
	
}
