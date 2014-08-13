package com.periplanisi.android.webwrapper.web;

/**
 * Listener to be notified after loading a web page 
 * 
 * @author Ioannis Panagiotopoulos <ipanag+android@gmail.com>
 *
 */
public interface WebPageLoadListener {

	/**
	 * Called when a page starts loading
	 */
	void onPageStartLoading();
	
	/**
	 * Called when a page has finished loading successfully
	 */
	void onPageLoadedSuccessfully();
	
	/**
	 * Called when a page has finished loading with failure
	 * 
	 * @param errorCode
	 * @param description
	 * @param failingUrl
	 */
	void onPageLoadedFailure(int errorCode, String description, String failingUrl);
}
