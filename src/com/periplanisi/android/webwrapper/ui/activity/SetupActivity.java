package com.periplanisi.android.webwrapper.ui.activity;

import android.os.Bundle;
import android.view.Menu;

import com.periplanisi.android.webwrapper.R;
import com.periplanisi.android.webwrapper.ui.fragment.SetupFragment;

/**
 * Initial activity for setup
 * 
 * @author Ioannis Panagiotopoulos <ipanag+android@gmail.com>
 *
 */
public class SetupActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.generic_fragment_container);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, SetupFragment.newInstance()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.setup, menu);
		return true;
	}
	
}
