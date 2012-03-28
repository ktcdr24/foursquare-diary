package com.test.foursquarediary;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

public class FourSquareDiaryTabActivity extends TabActivity {
	
	private TabHost tabHost;
	private TabHost.TabSpec	tabSpec;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_tab_view);
		
		tabHost = getTabHost();
		
//		Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
//		tabSpec = tabHost.newTabSpec("Login").setIndicator("Login", getResources().getDrawable(R.drawable.home_tab_view_res));
//		tabHost.addTab(tabSpec);
//		
//		Intent venuesIntent = new Intent(getApplicationContext(), VenuesActivity.class);
//		tabSpec = tabHost.newTabSpec("Venues").setIndicator("Venues", getResources().getDrawable(R.drawable.home_tab_view_res));
//		tabHost.addTab(tabSpec);
		
	}

}
