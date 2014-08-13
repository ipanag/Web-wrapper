package com.periplanisi.android.webwrapper.web;

import java.lang.ref.WeakReference;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.util.Log;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.periplanisi.android.webwrapper.settings.SettingsHelper;

/**
 * Basic WebViewClient implementation
 * 
 * @author Ioannis Panagiotopoulos <ipanag+android@gmail.com>
 *
 */
public class WrapperWebViewClient extends WebViewClient {
	
	private static final String TAG = "WebClient";
	
	private final WeakReference<Activity> activityWeakRef;
	private String loadUrl;
	private SettingsHelper settingsHelper;
	private boolean loading = false;
	
	public WrapperWebViewClient(Activity activity, SettingsHelper settingsHelper) {
		this.activityWeakRef = new WeakReference<Activity>(activity);
		this.settingsHelper = settingsHelper;
	}
	
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
    	Log.d(TAG, "shouldOverride: " + url);
    	
    	boolean openExternal = settingsHelper.isOpenOtherHostLinks();
    	boolean openSameHost = settingsHelper.isOpenSameHostLinks();
    	
    	if(!openExternal || !openSameHost) {
	    	try {
	    		final Activity activity = activityWeakRef.get();
	        	if (activity != null) {
	        		Uri uri = Uri.parse(url);
	            	Uri loadedUri = Uri.parse(loadUrl);
	            	
	            	String loadedUriHost = loadedUri.getHost();
	            	String currentUriHost = uri.getHost();
	            	
	            	Log.d(TAG, "loadedUriHost: " + loadedUriHost + " currentUriHost: " + currentUriHost);
	            	
	            	if (loadedUriHost.equals(currentUriHost)) {
		        		if(!openSameHost) {
		        			fireUriIntent(activity, uri);
		        			return true;
		        		}
	            	} else {
	            		if(!openExternal) {
		        			fireUriIntent(activity, uri);
		        			return true;
		        		}
	            	}
	        	}
	        	
	    	} catch(final NullPointerException e) {
		    	e.printStackTrace();
		    }
    	}
    	return false;
    }
    
	/**
	 * Open Uri in external application (ie browser)
	 * 
	 * @param activity
	 * @param uri
	 */
    private void fireUriIntent(Activity activity, Uri uri) {
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		activity.startActivity(intent);
    }
    
    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
    	super.onPageStarted(view, url, favicon);
    	
    	Log.d(TAG, "onPageStarted: " + url);
    	
    	// update stored url, in case of redirection
    	loadUrl = url;
    	loading = true;
    	
    	final Activity activity = activityWeakRef.get();
    	if (activity != null && activity instanceof WebPageLoadListener) {
    		((WebPageLoadListener)activity).onPageStartLoading();
    	}
    }
    
    @Override
    public void onPageFinished(WebView view, String url) {
    	super.onPageFinished(view, url);
    	
    	Log.d(TAG, "onPageFinished: " + url);
    	
    	if(loading) {
	    	final Activity activity = activityWeakRef.get();
	    	if (activity != null && activity instanceof WebPageLoadListener) {
	    		((WebPageLoadListener)activity).onPageLoadedSuccessfully();
	    	}
    	}
    	
    	loading = false;
    }
    
    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
    	super.onReceivedError(view, errorCode, description, failingUrl);
    	
    	Log.w(TAG, "onReceivedError: " + failingUrl);
    	
    	loading = false;
    	
    	final Activity activity = activityWeakRef.get();
    	if (activity != null && activity instanceof WebPageLoadListener) {
    		((WebPageLoadListener)activity).onPageLoadedFailure(errorCode, description, failingUrl);
    	}
    }
    
    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
    	if(settingsHelper.isIgnoreSslError()) {
    		handler.proceed();
    	} else {
    		handler.cancel();
    	}
    }
    
}
