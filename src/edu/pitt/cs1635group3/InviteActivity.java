package edu.pitt.cs1635group3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Iterator;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class InviteActivity extends Activity {

	private static final String DEBUG_TAG = "InviteActivity";
	private static final int CONTACT_PICKER_RESULT = 1001;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.invite);
	}

	public void doLaunchContactPicker(View view) {
		Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,
				Contacts.CONTENT_URI);
		startActivityForResult(contactPickerIntent, CONTACT_PICKER_RESULT);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case CONTACT_PICKER_RESULT:
				Cursor cursor = null;
				String email = "";
				try {
					Bundle extras = data.getExtras();
					Set<String> keys = extras.keySet();
					Iterator<String> iterate = keys.iterator();
					while (iterate.hasNext()) {
						String key = iterate.next();
						Log.v(DEBUG_TAG, key + "[" + extras.get(key) + "]");
					}

					Uri result = data.getData();
					Log.v(DEBUG_TAG,
							"Got a contact result: " + result.toString());

					// get the contact id from the Uri
					String id = result.getLastPathSegment();

					// query for everything email
					cursor = getContentResolver().query(Email.CONTENT_URI,
							null, Email.CONTACT_ID + "=?", new String[] { id },
							null);

					int emailIdx = cursor.getColumnIndex(Email.DATA);

					// let's just get the first email
					if (cursor.moveToFirst()) {

						/*
						 * Iterate all columns. :) String columns[] =
						 * cursor.getColumnNames(); for (String column :
						 * columns) { int index = cursor.getColumnIndex(column);
						 * Log.v(DEBUG_TAG, "Column: " + column + " == [" +
						 * cursor.getString(index) + "]"); }
						 */

						email = cursor.getString(emailIdx);

						Log.v(DEBUG_TAG, "Got email: " + email);

					} else {
						Log.w(DEBUG_TAG, "No results");
					}
				} catch (Exception e) {
					Log.e(DEBUG_TAG, "Failed to get email data", e);
				} finally {
					if (cursor != null) {
						cursor.close();
					}
					EditText emailEntry = (EditText) findViewById(R.id.invite_email);
					emailEntry.setText(email);
					if (email.length() == 0) {
						Toast.makeText(this, "No email found for contact.",
								Toast.LENGTH_LONG).show();
					}

				}

				break;
			}

		} else {
			Log.w(DEBUG_TAG, "Warning: activity result not ok");
		}
	}

}