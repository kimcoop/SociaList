package service;

import java.util.ArrayList;

import org.apache.http.NameValuePair;

import com.example.push.C2DMReceiver;
import com.google.android.c2dm.C2DMessaging;

import zebrafish.util.IOUtil;
import zebrafish.util.JSONItem;
import zebrafish.util.JSONUser;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.example.push.C2DMReceiver;
import com.google.android.c2dm.C2DMessaging;

import edu.pitt.cs1635group3.R;
import edu.pitt.cs1635group3.Activities.HomeActivity;
import edu.pitt.cs1635group3.Activities.Classes.Item;
import edu.pitt.cs1635group3.Activities.Classes.User;

public class UserTask {
	public static final String TAG = "UserTask";
	protected Context context;
	private static ArrayList<NameValuePair> params;
	private static User currUser;
	private static String name;
	private static String email;
	private static String phoneNumber;
	private static boolean allowPush;
	private static int STORE_USER = 1;
	private static int UPDATE_USER = 2; //Not used yet, but this will replace UserUpdateTask.java

	private class DoItemTask extends AsyncTask<Integer, Void, String> {
		@Override
		protected String doInBackground(Integer... params) {
			int retVal = -1;
			
			if (params[0]==STORE_USER) {

					HomeActivity.storeUser(name, email, phoneNumber, allowPush);
			
			}/* else if (params[0]==UPDATE_USER) {

				JSONUSER.updateUser(currUser.getID());
			} */
			return ""+params[0];

			//return ""+params[0];
		}
		@Override
		protected void onPostExecute(String result) {
			
			//Log.i(TAG, "Unique ID " +result.intValue()+ " from storeUser()");
			//JSONUser.uID = result.intValue();
			if (allowPush) {
				registerPushNotification();
			}
		}
	}

	public void store(Context c,String n, String e, String pn,
			boolean ap) {
		context = c;
		name = n;
		email = e;
		phoneNumber = pn;
		allowPush = ap;
		DoItemTask task = new DoItemTask();
		task.execute(STORE_USER);

	}

	public void update(User u) {
		currUser = u;
		DoItemTask task = new DoItemTask();
		task.execute(UPDATE_USER);
		
	}
	public void registerPushNotification() {

		Log.d(TAG, "in registerPushNotification()");

		final SharedPreferences c2dmPrefs = context.getSharedPreferences(
				C2DMessaging.PREFERENCE, Context.MODE_PRIVATE);
		if (C2DMessaging.shouldRegisterForPush(context)) {
			if ((Build.VERSION.SDK_INT >= 8)
					&& (C2DMessaging
							.shouldRegisterForPush(context))) {
				C2DMReceiver.refreshAppC2DMRegistrationState(
						context, true);
			}
		}

		if (c2dmPrefs.contains("dm_registration")) {
			Log.i(TAG, "Device already registered.");
		} else { // don't reshow the push notifications prompt if they've
			// already registered
			Log.i(TAG, "Registering device");
			if (Build.VERSION.SDK_INT >= 8) {

				Editor e = c2dmPrefs.edit();
				e.putString("dm_registration", "");
				e.commit();
				C2DMessaging.setRegisterForPush(context, true);
				C2DMReceiver.refreshAppC2DMRegistrationState(context, true);

			} else {
				final Dialog dialogok = new Dialog(context);

				dialogok.setContentView(R.layout.dialog_ok);
				dialogok.setOwnerActivity((Activity)context);
				dialogok.setTitle(context.getString(R.string.app_name));
				TextView tv = (TextView) dialogok
						.findViewById(R.id.textViewDialogMessage);
				tv.setText("This Phone does not support push notification");
				Button ok_btn = (Button) dialogok.findViewById(R.id.buttonOK);
				OnClickListener l = new OnClickListener() {
					public void onClick(View v) {
						dialogok.dismiss();
						Editor e = c2dmPrefs.edit();
						e.putString("dm_registration", "");
						e.commit();
						C2DMessaging.setRegisterForPush(context, false);
					}
				};
				ok_btn.setOnClickListener(l);
				dialogok.show();
			}
		}
	} // end showPushNotificationDialog()
}