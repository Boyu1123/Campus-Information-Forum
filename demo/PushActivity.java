package com.ustb.demo;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

public class PushActivity extends Activity {
	private String mDeviceID;
	public static final String TAG = "DemoPushService";
	private SharedPreferences mPrefs;
	private String classify;
	ImageView imageView;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.main);

//		setTitle("Client");
		mDeviceID = "123";

		Editor editor = getSharedPreferences(PushService.TAG, MODE_PRIVATE)
				.edit();
		editor.putString(PushService.PREF_DEVICE_ID, mDeviceID);
		editor.commit();
		PushService.actionStart(getApplicationContext());

		// 停止方法
		// PushService.actionStop(getApplicationContext());

	}

}