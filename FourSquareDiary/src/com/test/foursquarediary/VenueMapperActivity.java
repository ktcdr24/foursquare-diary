package com.test.foursquarediary;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;
import com.test.foursquarediary.classes.FourSquareVenue;

public class VenueMapperActivity extends MapActivity {

	public static final String TAG = "VenueMapperActivity";
	MapView mapView = null;
	MarkerOverlay markerOverlay = null;
	ProgressDialog mProgressDialog;
	
	@Override
	protected boolean isRouteDisplayed() {		
		return false;
	}
	
	@Override
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.venue_mapper);
//		mProgressDialog = new ProgressDialog(getApplicationContext());
//		mProgressDialog.setMessage("Generating Map of Checkins...");
//		mProgressDialog.show();
		mapView = (MapView) findViewById(R.id.venueMapview);
		mapView.setBuiltInZoomControls(true);
        mapView.setSatellite(false);
        mapView.setLongClickable(false);
        markerOverlay = new MarkerOverlay(this.getResources().getDrawable(R.drawable.pin), this);
        addVenuesToMap();
//        mProgressDialog.dismiss();
	}
	
	private void addVenuesToMap() {
		List<FourSquareVenue> venues = FourSquareApp.mVenues;
		GeoPoint p = null;
		OverlayItem overlayItem = null;
		//int index = 0;
		for(FourSquareVenue v : venues) {
			Log.d(TAG, " adding overlay - " + v);
			p = new GeoPoint((int)(v.getLocation().getLatitude() * 1E6), (int)(v.getLocation().getLongitude() * 1E6));
			overlayItem = new OverlayItem(p, v.getName(), v.getId());
			markerOverlay.addOverlay(overlayItem);
//			if(index == 100)
//				break;
			//index++;
		}
		mapView.getOverlays().add(markerOverlay);
	}
	
	private class MarkerOverlay extends ItemizedOverlay<OverlayItem> {

		private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
		Context mContext = null;

		public MarkerOverlay(Drawable arg0) {
			super(boundCenterBottom(arg0));
		}
		
		public MarkerOverlay(Drawable arg0, Context context) {
			super(boundCenterBottom(arg0));
			mContext = context;
		}

		@Override
		protected OverlayItem createItem(int i) {
			return mOverlays.get(i);
		}

		@Override
		public int size() {
			return mOverlays.size();
		}
		
		public void addOverlay(OverlayItem overlayItem){
			mOverlays.add(overlayItem);
			populate();
		}
		
		@Override
		protected boolean onTap(int index) {
			OverlayItem item = mOverlays.get(index);
			AlertDialog.Builder alertDiag = new AlertDialog.Builder(mContext);
			alertDiag.setTitle(item.getTitle());
			alertDiag.setMessage(item.getSnippet());
			alertDiag.show();
			return true;
		}
		
	}
	
}
