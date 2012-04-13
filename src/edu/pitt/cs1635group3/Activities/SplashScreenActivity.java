package edu.pitt.cs1635group3.Activities;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import zebrafish.util.DBHelper;
import zebrafish.util.JSONfunctions;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import edu.pitt.cs1635group3.CustomList;
import edu.pitt.cs1635group3.Item;
import edu.pitt.cs1635group3.R;
import edu.pitt.cs1635group3.User;

public class SplashScreenActivity extends Activity {
	protected boolean _active = true;
	protected int _splashTime = 5000;

	protected Context context;
	private static final String TAG = "SplashScreenActivity";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		context = this;

		// thread for displaying the SplashScreen
		Thread splashTread = new Thread() {
			@Override
			public void run() {
				try {
					/*
					 * int waited = 0; while(_active && (waited < _splashTime))
					 * { sleep(100); if(_active) { waited += 100; } }
					 */

					JSONfunctions.getLists(context);

				} finally {
					finish();
					startActivity(new Intent(
							"edu.pitt.cs1635group3.HomeActivity"));
					stop();
				}
			}
		};
		splashTread.start();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			_active = false;
		}
		return true;
	}

}