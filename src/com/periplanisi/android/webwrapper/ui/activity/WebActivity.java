package com.periplanisi.android.webwrapper.ui.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import com.periplanisi.android.webwrapper.R;
import com.periplanisi.android.webwrapper.settings.SettingsHelper;
import com.periplanisi.android.webwrapper.web.WebPageLoadListener;
import com.periplanisi.android.webwrapper.web.WrapperWebViewClient;

/**
 * Main activity for displaying the web wrapper
 * 
 * @author Ioannis Panagiotopoulos <ipanag+android@gmail.com>
 *
 */
public class WebActivity extends BaseActivity implements WebPageLoadListener {

	private static final String URL_KEY = "url_key";
	private static final String AUTH_KEY = "auth_key";
	private static final String USER_KEY = "user_key";
	private static final String PASS_KEY = "pass_key";
	
	private WebView webView;
	private WrapperWebViewClient webClient;
	private SettingsHelper settingsHelper;
	private TextView errorView;
	private View loadingView;
	
	private static enum VIEW_STATE {
		LOADING,
		CONTENT,
		ERROR;
	}
	
	private VIEW_STATE state = VIEW_STATE.CONTENT;
	
	public static Intent getStartIntent(Context context, String url, boolean authRequired,
			String username, String password) {
		Intent intent = new Intent(context, WebActivity.class);
		intent.putExtra(URL_KEY, url);
		intent.putExtra(AUTH_KEY, authRequired);
		intent.putExtra(USER_KEY, username);
		intent.putExtra(PASS_KEY, password);
		
		return intent;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web);
		
		settingsHelper = SettingsHelper.getInstance(this);

		webView = (WebView) findViewById(R.id.webview);
		errorView = (TextView) findViewById(R.id.errorText);
		loadingView = findViewById(R.id.loader);
		
		switchToState(state);
		
		webClient = new WrapperWebViewClient(this, settingsHelper);
		webView.setWebViewClient(webClient);
		
		// http://stackoverflow.com/questions/3998916/android-webview-leaves-space-for-scrollbar
		webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
		
		updateWebViewSettings();
		
		CookieManager.getInstance().setAcceptCookie(true);
		
		String url = getIntent().getStringExtra(URL_KEY);
		boolean authRequired = getIntent().getBooleanExtra(AUTH_KEY, false);
		if(authRequired) {
			String username = getIntent().getStringExtra(USER_KEY);
			String password = getIntent().getStringExtra(PASS_KEY);
			webClient.setUsernamePassword(username, password);
		}
		
		webView.loadUrl(url);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
	    	switchToState(VIEW_STATE.CONTENT);
	    	webView.stopLoading();
	    	webView.goBack();
	    	return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.web, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int itemId = item.getItemId();
		if (itemId == R.id.action_refresh) {
			webView.reload();
			return true;
	    }
	    
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onSettingsUpdate() {
		super.onSettingsUpdate();
		
		updateWebViewSettings();
		
		webView.reload();
	}
	
	@SuppressLint("SetJavaScriptEnabled")
	private void updateWebViewSettings() {
		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(settingsHelper.isJavascriptEnabled());
		webSettings.setBuiltInZoomControls(settingsHelper.isBuiltInZoomControls());
		
		if(!isOldApiZoomControls()) {
			setNewApiZoomControls(webSettings, settingsHelper.isDisplayZoomControls());
		}
		
		ActionBar actionBar = getSupportActionBar();
		if(actionBar != null && settingsHelper.isShowActionBar()) {
			actionBar.show();
		} else {
			actionBar.hide();
		}
	}

	@Override
	public void onPageLoadedSuccessfully() {
		setTitle(webView.getTitle());
		switchToState(VIEW_STATE.CONTENT);
	}

	@Override
	public void onPageLoadedFailure(int errorCode, String description, String failingUrl) {
		errorView.setText("errorCode: " + errorCode + "\ndescription: " + description + "\nfailingUrl: " + failingUrl);
		switchToState(VIEW_STATE.ERROR);
	}

	@Override
	public void onPageStartLoading() {
		switchToState(VIEW_STATE.LOADING);
	}
	
	private void switchToState(VIEW_STATE newState) {
		switch(newState) {
		case CONTENT:
			loadingView.setVisibility(View.GONE);
			errorView.setVisibility(View.GONE);
			webView.setVisibility(View.VISIBLE);
			break;
		case ERROR:
			loadingView.setVisibility(View.GONE);
			errorView.setVisibility(View.VISIBLE);
			webView.setVisibility(View.GONE);
			break;
		case LOADING:
			loadingView.setVisibility(View.VISIBLE);
			errorView.setVisibility(View.GONE);
			webView.setVisibility(View.GONE);
			break;
		default:
			break;
		
		}
		state = newState;
	}
	
	private static boolean isOldApiZoomControls() {
		return Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB;
	}
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setNewApiZoomControls(WebSettings webSettings, boolean enabled) {
		webSettings.setDisplayZoomControls(enabled);
	}
	
}
