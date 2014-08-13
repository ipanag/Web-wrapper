package com.periplanisi.android.webwrapper.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.periplanisi.android.webwrapper.R;

/**
 * Settings helper singleton
 * 
 * @author Ioannis Panagiotopoulos <ipanag+android@gmail.com>
 * 
 */
public class SettingsHelper {
	
	private static SettingsHelper sInstance;
	
	private final Context context;
	private SharedPreferences prefs;
	
	/**
	 * Get singleton instance
	 * 
	 * @param context
	 * @return
	 */
	public static SettingsHelper getInstance(Context context) {
		if(sInstance == null) {
			sInstance = new SettingsHelper(context);
		}
		
		return sInstance;
	}
	
	private SettingsHelper(Context context) {
		this.context = context.getApplicationContext();
		prefs = PreferenceManager.getDefaultSharedPreferences(this.context);
	}
	
	public boolean isJavascriptEnabled() {
		return getBoolValue(R.string.prefs_key_js_enabled);
	}

	public boolean isShowActionBar() {
		return getBoolValue(R.string.prefs_key_action_bar);
	}

	public boolean isOpenSameHostLinks() {
		return getBoolValue(R.string.prefs_key_open_same_host);
	}

	public boolean isOpenOtherHostLinks() {
		return getBoolValue(R.string.prefs_key_open_other_host);
	}

	public boolean isIgnoreSslError() {
		return getBoolValue(R.string.prefs_key_ignore_ssl_error);
	}

	public boolean isBuiltInZoomControls() {
		return getBoolValue(R.string.prefs_key_built_in_zoom);
	}

	public boolean isDisplayZoomControls() {
		return getBoolValue(R.string.prefs_key_display_zoom);
	}

	/**
	 * Get boolean value from settings
	 * 
	 * @param keyResId
	 *            the android resource id which holds the preference key name
	 * @return
	 */
	private boolean getBoolValue(int keyResId) {
		String key = context.getString(keyResId);
		
		return getBoolValue(key);
	}
	
	/**
	 * Get boolean value from settings
	 * 
	 * @param key
	 *            preference key name
	 * @return
	 */
	public boolean getBoolValue(String key) {
		return prefs.getBoolean(key, getDefaultValue(key));
	}
	
	/**
	 * Get the default value for preference
	 * 
	 * @param key
	 *            preference key name
	 * @return
	 */
	private boolean getDefaultValue(final String key) {
		if(context.getString(R.string.prefs_key_js_enabled).equals(key)) {
			return false;
			
		} else if(context.getString(R.string.prefs_key_action_bar).equals(key)) {
			return true;
			
		} else if(context.getString(R.string.prefs_key_open_same_host).equals(key)) {
			return true;

		} else if(context.getString(R.string.prefs_key_open_other_host).equals(key)) {
			return false;

		} else if(context.getString(R.string.prefs_key_ignore_ssl_error).equals(key)) {
			return false;

		} else if(context.getString(R.string.prefs_key_built_in_zoom).equals(key)) {
			return true;

		} else if(context.getString(R.string.prefs_key_display_zoom).equals(key)) {
			return false;

		} else {
			return false;
		}
	}

}
