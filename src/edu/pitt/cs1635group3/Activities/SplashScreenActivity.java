package edu.pitt.cs1635group3.Activities;

import zebrafish.util.JSONfunctions;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import edu.pitt.cs1635group3.R;
import edu.pitt.cs1635group3.Activities.Classes.User;

public class SplashScreenActivity extends Activity {
	protected boolean _active = true;
	protected int _splashTime = 5000;
	private int userID;

	protected Context context;
	private static final String TAG = "SplashScreenActivity";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		context = this;
		userID = User.getCurrUser(context);

		// thread for displaying the SplashScreen
		Thread splashTread = new Thread() {
			@Override
			public void run() {
				try {

					JSONfunctions.getLists(context);
					Log.i(TAG, "User ID for getting invites is " +userID);
					
					if (userID > 0) {
						JSONfunctions.getInvites(context);
					} else {
					 // new user, no invites
						Log.i(TAG, "Unrecognized user, so no invites");
					}

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