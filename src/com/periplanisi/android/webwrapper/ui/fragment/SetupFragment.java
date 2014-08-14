package com.periplanisi.android.webwrapper.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;

import com.periplanisi.android.webwrapper.R;
import com.periplanisi.android.webwrapper.ui.activity.WebActivity;

/**
 * Fragment to display initial setup screen
 * 
 * @author Ioannis Panagiotopoulos <ipanag+android@gmail.com>
 *
 */
public class SetupFragment extends Fragment implements OnClickListener, TextWatcher, OnCheckedChangeListener {

	private static final String PREFS = "prefs_setup_screen";
	private static final String URL_KEY = "last_used_url";
	private static final String AUTH_KEY = "last_used_auth_key";
	private static final String USER_KEY = "last_used_user_key";
	private static final String PASS_KEY = "last_used_pass_key";
	
	private static final String DEFAULT_URL = "http://beta.html5test.com/";
	
	private EditText inputView, usernameView, passwordView;
	private CheckBox authCheckBox;
	private View goButton;
	private SharedPreferences prefs;
	
	public static Fragment newInstance() {
		return new SetupFragment();
	}
	
	public SetupFragment() {
	}

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_setup, container, false);
		
		inputView = (EditText) rootView.findViewById(R.id.input_url);
		goButton = rootView.findViewById(R.id.go_button);
		goButton.setOnClickListener(this);
		
		prefs = getActivity().getApplicationContext().getSharedPreferences(PREFS, Context.MODE_PRIVATE);
		
		String url = DEFAULT_URL;
		boolean isAuthRequired = false;
		String username = null;
		String password = null;
		if(savedInstanceState != null && savedInstanceState.containsKey(URL_KEY)) {
			url = savedInstanceState.getString(URL_KEY);
			isAuthRequired = savedInstanceState.getBoolean(AUTH_KEY, isAuthRequired);
			username = savedInstanceState.getString(USER_KEY);
			password = savedInstanceState.getString(PASS_KEY);
		} else {
			url = prefs.getString(URL_KEY, url);
			isAuthRequired = prefs.getBoolean(AUTH_KEY, isAuthRequired);
			username = prefs.getString(USER_KEY, username);
			password = prefs.getString(PASS_KEY, password);
		}
		
		inputView.setText(url);
		goButton.setEnabled(!TextUtils.isEmpty(url));
		
		authCheckBox = (CheckBox) rootView.findViewById(R.id.auth_check);
		usernameView = (EditText) rootView.findViewById(R.id.username);
		passwordView = (EditText) rootView.findViewById(R.id.password);
		
		authCheckBox.setChecked(isAuthRequired);
		if(isAuthRequired) {
			usernameView.setText(username);
			passwordView.setText(password);
		}
		
		updateAuth(authCheckBox.isChecked());
		authCheckBox.setOnCheckedChangeListener(this);
		
		return rootView;
	}
	
	private void updateAuth(final boolean required) {
		final int visibility = (required ? View.VISIBLE : View.GONE);
		usernameView.setVisibility(visibility);
		passwordView.setVisibility(visibility);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		inputView.addTextChangedListener(this);
	}
	
	@Override
	public void onPause() {
		inputView.removeTextChangedListener(this);
		
		super.onPause();
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		String url = inputView.getText().toString();
		outState.putString(URL_KEY, url);
		outState.putBoolean(AUTH_KEY, authCheckBox.isChecked());
		outState.putString(USER_KEY, usernameView.getText().toString());
		outState.putString(PASS_KEY, passwordView.getText().toString());
		
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onClick(View v) {
		int viewId = v.getId();
		if(viewId == R.id.go_button) {
			// store url for next launch
			String url = inputView.getText().toString();
			prefs.edit()
			.putString(URL_KEY, url)
			.putBoolean(AUTH_KEY, authCheckBox.isChecked())
			.putString(USER_KEY, usernameView.getText().toString())
			.putString(PASS_KEY, passwordView.getText().toString())
			.commit();
			
			// start webview
			Intent intent = WebActivity.getStartIntent(getActivity(), url, authCheckBox.isChecked(),
					usernameView.getText().toString(), passwordView.getText().toString());
			getActivity().startActivity(intent);
		}
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		// Do nothing
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// Do nothing
	}

	@Override
	public void afterTextChanged(Editable s) {
		goButton.setEnabled(!TextUtils.isEmpty(s.toString()));
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		updateAuth(isChecked);
	}
}

