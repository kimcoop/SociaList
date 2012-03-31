package edu.pitt.cs1635group3.Activities;
import edu.pitt.cs1635group3.R;
import edu.pitt.cs1635group3.R.id;
import edu.pitt.cs1635group3.R.layout;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class RegistrationResultActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.c2dm_result);
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			String registrationId = extras.getString("registration_id");
			if (registrationId != null && registrationId.length() > 0) {
				TextView view = (TextView) findViewById(R.id.result);
				view.setText(registrationId);
			}
		}

		super.onCreate(savedInstanceState);
	}
}