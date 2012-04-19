package edu.pitt.cs1635group3.Activities;

import service.SplashScreenTask;
import zebrafish.util.IOUtil;
import zebrafish.util.UIUtil;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
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
		
		if (IOUtil.isOnline(context)) {
			if (userID > 0) {
				new SplashScreenTask().getListsAndInvites(context);
			} else {
				// User isn't registered. TODO: register here
				//new SplashScreenTask().getLists(context);
				Intent i = new Intent(this, HomeActivity.class);
				startActivity(i);
			}
			
			
		} else {
			 informConnectionIssue();
		}

		
		
		
		// thread for displaying the SplashScreen
		/*Thread splashTread = new Thread() {
		 * 
		 * ROB - can this be removed? - Kim
		 * 
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
		splashTread.start();*/
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			_active = false;
		}
		return true;
	}
	
	public void informConnectionIssue() {
		final Dialog dialogok = new Dialog(context);

		dialogok.setContentView(R.layout.dialog_ok);
		dialogok.setOwnerActivity(this);
		dialogok.setTitle(getString(R.string.connect_title));
		TextView tv = (TextView) dialogok
				.findViewById(R.id.textViewDialogMessage);
		tv.setText(getString(R.string.connect_issue));
		Button ok_btn = (Button) dialogok.findViewById(R.id.buttonOK);
		OnClickListener l = new OnClickListener() {
			public void onClick(View v) {
				dialogok.dismiss();
				
				Intent i = new Intent(context, HomeActivity.class);
				startActivity(i);
				
			}
		};
		ok_btn.setOnClickListener(l);
		dialogok.show();
		
		
	}

}
