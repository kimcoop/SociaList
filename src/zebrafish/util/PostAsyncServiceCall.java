package zebrafish.util;

import android.app.Activity;


public abstract class PostAsyncServiceCall<P, G, R> 
	extends AsyncServiceCall<P, G, R> {
	

	public PostAsyncServiceCall(Activity context) {
		super(context);
		
		setProgressStringId(edu.pitt.cs1635group3.R.string.processing_wait);
	}
}
