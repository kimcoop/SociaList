package edu.pitt.cs1635group3.Activities;

import zebrafish.util.UIUtil;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

import edu.pitt.cs1635group3.R;
import edu.pitt.cs1635group3.Activities.Classes.User;

public class SettingsActivity extends SherlockActivity {

	private Context context;
	private static String TAG = "SettingsActivity";
	private static int userID;

	private EditText name, email;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_settings);
		context = this;

		userID = User.getCurrUser(context);

		getSupportActionBar();
		setTitle("Settings");

		name = (EditText) findViewById(R.id.txtvName);
		name.setText(User.getCurrUsername(context));
		
		email = (EditText) findViewById(R.id.txtvEmail);
		email.setText(User.getCurrEmail(context));

	}

	@Override
	public void onBackPressed() {
		saveUser();
		finish();
		Intent intent = new Intent(this, HomeActivity.class);
		startActivity(intent);
	}

	public void saveUser() {
		// TODO
		
		
		
		UIUtil.showMessage(context, "User updated.");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.blank_menu, menu);

		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featuredId, MenuItem item) {
		Intent intent;
		if (item.getItemId() == android.R.id.home) {
			intent = new Intent(this, HomeActivity.class);
			startActivity(intent);
			return true;
		} else {
			return false;
		}

	}
}
