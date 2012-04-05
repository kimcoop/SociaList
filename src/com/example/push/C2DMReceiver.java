package com.example.push;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.provider.Settings;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.c2dm.C2DMBaseReceiver;
import com.google.android.c2dm.C2DMessaging;

import edu.pitt.cs1635group3.R;
import edu.pitt.cs1635group3.Activities.HomeActivity;

// TODO: Auto-generated Javadoc
/**
 * Broadcast receiver that handles Android Cloud to Data Messaging (AC2DM)
 * messages, initiated by the JumpNote App Engine server and routed/delivered by
 * Google AC2DM servers. The only currently defined message is 'sync'.
 */
public class C2DMReceiver extends C2DMBaseReceiver {

	/** The Constant TAG. */
	static final String TAG = C2DMConfig.makeLogTag(C2DMReceiver.class);

	/**
	 * Instantiates a new c2 dm receiver.
	 */
	public C2DMReceiver() {
		super(C2DMConfig.C2DM_GOOGLE_ACCOUNT);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.google.android.c2dm.C2DMBaseReceiver#onError(android.content.Context,
	 * java.lang.String)
	 */
	@Override
	public void onError(Context context, String errorId) {
		Toast.makeText(context, "Messaging registration error: " + errorId,
				Toast.LENGTH_LONG).show();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.content.BroadcastReceiver#onReceive(android.content.Context,
	 * android.content.Intent)
	 */
	@Override
	public void onReceive(Context context, Intent intent) {

		super.onHandleIntentRecieved(context, intent);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.google.android.c2dm.C2DMBaseReceiver#onMessage(android.content.Context
	 * , android.content.Intent)
	 */
	@Override
	protected void onMessage(Context context, Intent intent) {
		
		
		String message = intent.getExtras().getString("message");//message
		Log.i("PUSH", message);
		String strTitle = intent.getExtras().getString("title");//title
		if (message != null) {
			String app_name = (String) context.getText(R.string.app_name);
			// Use the Notification manager to send notification 
			NotificationManager notificationManager = (NotificationManager) 
			context.getSystemService(Context.NOTIFICATION_SERVICE); 
			// Create a notification using android stat_notify_chat icon. 
			Notification notification = new 
			Notification(R.drawable.icon, app_name + ": " + 
			message, 0); 
			 Intent app; 
			 
			//message="upload"; 
			if (message.contains("upload")) 
				app = new Intent(context, HomeActivity.class); 
			else 
				app = new Intent(context, HomeActivity.class); 
			
			// Create a pending intent to call the when the notification is clicked 
			PendingIntent pendingIntent = PendingIntent.getActivity(context, -1, app, 
			Intent.FLAG_ACTIVITY_NEW_TASK); 
			notification.when = System.currentTimeMillis(); 
			notification.flags |= Notification.FLAG_AUTO_CANCEL; 
			// Set the notification and register the pending intent to it 
			notification.setLatestEventInfo(context, app_name, message, pendingIntent); 
			notification.contentIntent = pendingIntent; 
			 // Trigger the notification 
			notificationManager.notify(0, notification);
			
		}
			
			/*
			if (HomeActivity.getCurrentActivity() == null
					|| (HomeActivity.getCurrentActivity().hasWindowFocus() == false))
				return;

			final Dialog dialog = new Dialog(
					HomeActivity.getCurrentActivity());

			dialog.setContentView(R.layout.dialog_ok);
			dialog.setOwnerActivity(HomeActivity.getCurrentActivity());

			Button okBtn = (Button) dialog.findViewById(R.id.buttonOK);
			okBtn.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					dialog.dismiss();
				}
			});
			TextView msg = (TextView) dialog
					.findViewById(R.id.textViewDialogMessage);

			if (strTitle.contains(context.getResources().getString(
					R.string.app_name))) {
				dialog.setTitle(R.string.app_name);
				msg.setText(message);
			} else {
				dialog.setTitle(R.string.app_name);
				msg.setText(strTitle + " sent you a message: " + message);
			}
			dialog.show();
		}*/
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.google.android.c2dm.C2DMBaseReceiver#onRegistered(android.content
	 * .Context, java.lang.String)
	 */
	@Override
	public void onRegistered(Context context, String registrationId)
			throws IOException {
		// TODO
		super.onRegistered(context, registrationId);

		/*
		 * CUSTOM DIALOG
		 * 
		 * final Dialog dialog = new Dialog(
		 * PushDemoActivity.getCurrentActivity());
		 * 
		 * dialog.setContentView(R.layout.auth_id);
		 * dialog.setOwnerActivity(PushDemoActivity.getCurrentActivity());
		 * 
		 * Button okBtn = (Button) dialog.findViewById(R.id.buttonOK);
		 * okBtn.setOnClickListener(new OnClickListener() {
		 * 
		 * public void onClick(View v) { dialog.dismiss(); } }); TextView msg =
		 * (TextView) dialog .findViewById(R.id.authID); msg.setText("AUTHID= "
		 * + registrationId);
		 * 
		 * dialog.setTitle(R.string.app_name);
		 * 
		 * dialog.show();
		 * 
		 * /***
		 */

		System.out.println("REG ID " + registrationId);
		Log.e(TAG, ">>>>id recieved" + registrationId);
		TelephonyManager telephonyManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);

		String deviceId = telephonyManager.getDeviceId();

		if (deviceId == null || deviceId == "") {
			deviceId = "e0a85841763c1192";
		}

		System.out.println("Device ID " + deviceId);

		Log.e(TAG, ">>>>device unique id " + deviceId);
		// send to server
		BufferedReader in = null;
		try {
			HttpClient client = new DefaultHttpClient();
			HttpGet request = new HttpGet();
			try {
				request.setURI(new URI(
						"http://www.zebrafishtec.com/register_token.php?deviceid="
								+ URLEncoder.encode(telephonyManager
										.getDeviceId()) + "&devicetoken="
								+ URLEncoder.encode(registrationId).toString()));
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
			HttpResponse response = client.execute(request);
			in = new BufferedReader(new InputStreamReader(response.getEntity()
					.getContent()));
			StringBuffer sb = new StringBuffer("");
			String line = "";
			String NL = System.getProperty("line.separator");
			while ((line = in.readLine()) != null) {
				sb.append(line + NL);
			}
			in.close();
			String page = sb.toString();
			System.out.println(page);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.google.android.c2dm.C2DMBaseReceiver#onUnregistered(android.content
	 * .Context)
	 */
	@Override
	public void onUnregistered(Context context) {
		super.onUnregistered(context);

	}

	/**
	 * Register or unregister based on phone sync settings. Called on each
	 * performSync by the SyncAdapter.
	 * 
	 * @param context
	 *            the context
	 * @param autoSyncDesired
	 *            the auto sync desired
	 */
	public static void refreshAppC2DMRegistrationState(Context context,
			boolean register) {
		// Determine if there are any auto-syncable accounts. If there are, make
		// sure we are
		// registered with the C2DM servers. If not, unregister the application.

		if (Build.VERSION.SDK_INT < 8) {
			return;
		} else {

			if (register) {
				C2DMessaging.register(context, C2DMConfig.C2DM_GOOGLE_ACCOUNT);
			} else {
				C2DMessaging.unregister(context);
			}

		}
	}
}
