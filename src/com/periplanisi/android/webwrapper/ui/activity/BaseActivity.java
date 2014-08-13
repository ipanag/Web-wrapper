package com.periplanisi.android.webwrapper.ui.activity;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;

import com.periplanisi.android.webwrapper.R;

/**
 * Base activity with support for opening Settings
 * 
 * @author Ioannis Panagiotopoulos <ipanag+android@gmail.com>
 *
 */
public abstract class BaseActivity extends ActionBarActivity {

	private static final int SETTINGS_REQUEST_CODE = 1;
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			Intent intent = SettingsActivity.getStartIntent(this);
			startActivityForResult(intent, SETTINGS_REQUEST_CODE);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == SETTINGS_REQUEST_CODE) {
			onSettingsUpdate();
		}
		
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	/**
	 * Override to act upon setting update
	 */
	protected void onSettingsUpdate() {
	}
	
}
