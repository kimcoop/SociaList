package edu.pitt.cs1635group3.Activities;
import edu.pitt.cs1635group3.R;
import edu.pitt.cs1635group3.R.id;
import edu.pitt.cs1635group3.R.layout;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class MessageReceivedActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.c2dm_main);
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			String message = extras.getString("payload");
			if (message != null && message.length() > 0) {
				TextView view = (TextView) findViewById(R.id.result);
				view.setText(message);
			}
		}

		super.onCreate(savedInstanceState);
	}

}