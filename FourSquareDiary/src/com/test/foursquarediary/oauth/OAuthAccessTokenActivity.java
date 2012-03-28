package com.test.foursquarediary.oauth;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.test.foursquarediary.R;
import com.test.foursquarediary.app.HomeActivity;
import com.test.foursquarediary.util.AppConstants;

public class OAuthAccessTokenActivity extends Activity {
	
	private static final String TAG = "OAuthAccessTokenActivity";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.oauth_webview);

        String url = "https://foursquare.com/oauth2/authenticate" + 
                "?client_id=" + AppConstants.CLIENT_ID + 
                "&response_type=token" + 
                "&redirect_uri=" + AppConstants.URL_CALLBACK;
        
        // If authentication works, we'll get redirected to a url with a pattern like:  
        //    http://YOUR_REGISTERED_REDIRECT_URI/#access_token=ACCESS_TOKEN
        // We can override onPageStarted() in the web client and grab the token out.
        WebView webview = (WebView)findViewById(R.id.oauthWebView);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.setWebViewClient(new WebViewClient() {
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                String fragment = "#access_token=";
                int start = url.indexOf(fragment);
                if (start > -1) {
                    // You can use the accessToken for api calls now.
                    String accessToken = url.substring(start + fragment.length(), url.length());
        			
                    Log.v(TAG, "OAuth complete, token: [" + accessToken + "].");
                    
                    SharedPreferences preferences = getSharedPreferences(AppConstants.PREFS_4SQ_NAME, MODE_PRIVATE);
                	SharedPreferences.Editor preferencesEditor = preferences.edit();
                    preferencesEditor.putString(AppConstants.PREFS_4SQ_KEY_ACCESS_TOKEN, accessToken);
                    preferencesEditor.commit();
                    
                    Log.i(TAG, preferences.contains(AppConstants.PREFS_4SQ_KEY_ACCESS_TOKEN)+"");
                	
                    Toast.makeText(OAuthAccessTokenActivity.this, "Token: " + accessToken, Toast.LENGTH_SHORT).show();
                    
                    Intent intent = new Intent(OAuthAccessTokenActivity.this, HomeActivity.class);
                	startActivity(intent);
                }
            }
        });
        
        webview.loadUrl(url);
        
    }
    
    
}
