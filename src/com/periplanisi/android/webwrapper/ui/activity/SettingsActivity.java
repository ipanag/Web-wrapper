package com.periplanisi.android.webwrapper.ui.activity;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.view.MenuItem;

import com.periplanisi.android.webwrapper.R;
import com.periplanisi.android.webwrapper.settings.SettingsHelper;

/**
 * Settings activity supporting both old and new API
 * 
 * @author Ioannis Panagiotopoulos <ipanag+android@gmail.com>
 *
 */
public class SettingsActivity extends PreferenceActivity {

	private SettingsHelper settingsHelper;
	
	public static Intent getStartIntent(Context context) {
		Intent intent = new Intent(context, SettingsActivity.class);
		
		return intent;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		settingsHelper = SettingsHelper.getInstance(this);
		
		if(isOldApiPreferences()) {
			setupSimplePreferencesScreen();
		} else {
			setupNewApiPreferences();
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int itemId = item.getItemId();
		
	    if(itemId == android.R.id.home) {
	        finish();
	        return true;
	    }
	    
	    return super.onOptionsItemSelected(item);
	}
	
	private static boolean isOldApiPreferences() {
		return Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB;
	}
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupNewApiPreferences() {
        getFragmentManager().beginTransaction().replace(android.R.id.content, new WebPreferenceFragment()).commit();
        
		ActionBar actionBar = getActionBar();
		if(actionBar != null) {
			actionBar.setDisplayHomeAsUpEnabled(true);
		}
	}

	@SuppressWarnings("deprecation")
	private void setupSimplePreferencesScreen() {
		addPreferencesFromResource(R.xml.prefs_web);
		
		initBoolPref(R.string.prefs_key_action_bar, true);
		initBoolPref(R.string.prefs_key_js_enabled, true);
		initBoolPref(R.string.prefs_key_open_same_host, true);
		initBoolPref(R.string.prefs_key_open_other_host, true);
		initBoolPref(R.string.prefs_key_ignore_ssl_error, true);
		initBoolPref(R.string.prefs_key_built_in_zoom, true);
		initBoolPref(R.string.prefs_key_display_zoom, false);
	}

	@Override
	public boolean onIsMultiPane() {
		return false;
	}

	@SuppressWarnings("deprecation")
	private void initBoolPref(int keyResId, boolean enabled) {
		String key = getString(keyResId);
		boolean value = settingsHelper.getBoolValue(key);
		
		Preference pref = findPreference(key);
		pref.setEnabled(enabled);
		((CheckBoxPreference) pref).setChecked(value);
	}
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public static class WebPreferenceFragment extends PreferenceFragment {
		
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			
			addPreferencesFromResource(R.xml.prefs_web);

			initBoolPref(R.string.prefs_key_action_bar);
			initBoolPref(R.string.prefs_key_js_enabled);
			initBoolPref(R.string.prefs_key_open_same_host);
			initBoolPref(R.string.prefs_key_open_other_host);
			initBoolPref(R.string.prefs_key_ignore_ssl_error);
			initBoolPref(R.string.prefs_key_built_in_zoom);
			initBoolPref(R.string.prefs_key_display_zoom);
		}
		
		private void initBoolPref(int keyResId) {
			Context context = getActivity();
			SettingsHelper settingsHelper = SettingsHelper.getInstance(context);
			
			String key = context.getString(keyResId);
			boolean value = settingsHelper.getBoolValue(key);
			
			((CheckBoxPreference)findPreference(key)).setChecked(value);
		}
	}

}
