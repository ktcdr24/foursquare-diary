package com.test.foursquarediary;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.test.foursquarediary.util.AppConstants;

/**
 * Display Foursquare authentication dialog.
 * 
 * @author Lorensius W. L. T <lorenz@londatiga.net>
 *
 */
public class FourSquareDialog extends Dialog {
	
	private static final String TAG = "FourSquareDialog"; 
	private static final float[] DIMENSIONS_LANDSCAPE = {460, 260};
	private static final float[] DIMENSIONS_PORTRAIT = {280, 420};
	//private static final int MARGIN = 4;
	//private static final int PADDING = 2;
    
    private LinearLayout mContent;
    private WebView mWebView;
    private ProgressDialog mSpinner;
    private String authUrl;
    private FSqDialogListener listener;

	public FourSquareDialog(Context context, String authUrl, FSqDialogListener listener) {
		super(context);
		this.authUrl = authUrl;
		this.listener = listener;
	}
	
	@Override 
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		
		mContent = new LinearLayout(getContext());
		mContent.setOrientation(LinearLayout.VERTICAL);
		mSpinner = new ProgressDialog(getContext());
		
		Display display 	= getWindow().getWindowManager().getDefaultDisplay();
        final float scale 	= getContext().getResources().getDisplayMetrics().density;
        float[] dimensions 	= (display.getWidth() < display.getHeight()) ? DIMENSIONS_PORTRAIT : DIMENSIONS_LANDSCAPE;
		
        initializeWebView();      
        
		addContentView(mContent, new FrameLayout.LayoutParams((int)(dimensions[0]*scale), (int)(dimensions[1]*scale)));
	}
	
	private void initializeWebView() {
		mWebView = new WebView(getContext());
		
		mWebView.setClickable(true);
		mWebView.setVerticalScrollBarEnabled(true);
		mWebView.setHorizontalScrollBarEnabled(true);
		mWebView.setWebViewClient(new FourSquareWebViewClient());
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.loadUrl(authUrl);
		mWebView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
		
		mContent.addView(mWebView);
	}
	
	private class FourSquareWebViewClient extends WebViewClient
	{
		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			Log.i(TAG, "FourSquareWebViewClient - onPageStarted - " + url);
			mSpinner.setMessage("Loading page");
			mSpinner.show();
			super.onPageStarted(view, url, favicon);
		}
		
		@Override
		public void onPageFinished(WebView view, String url) {
			Log.i(TAG, "FourSquareWebViewClient - onPageFinished");
			super.onPageFinished(view, url);
			mSpinner.dismiss();
		}
		
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			Log.d(TAG, "FourSquareWebViewClient - shouldOverrideUrlLoading - url = " + url);
			if(url.startsWith(AppConstants.URL_CALLBACK)) {
				String fragment = "#access_token=";
                int start = url.indexOf("#access_token=");
                if (start > -1) {
                    String accessToken = url.substring(start + fragment.length(), url.length());
                    Log.v(TAG, "OAuth complete, token: [" + accessToken + "].");
                    listener.onComplete(accessToken);
                }
                listener.onClose("Got User Details");
				FourSquareDialog.this.dismiss();
        		return true;	
			}
			return false;
		}
	}
	
	public interface FSqDialogListener {
		public void onPageLoaded();
		public void onError();
		public void onComplete(String accessToken);
		public void onClose(String msg);
	}
	
}