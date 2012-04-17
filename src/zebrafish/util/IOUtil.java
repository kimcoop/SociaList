package zebrafish.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public final class IOUtil {

	public static boolean isOnline(Context context) {
		Log.i("IOUtil", "checkign isOnline");
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		//
		return netInfo != null && netInfo.isConnectedOrConnecting();
	}

	private IOUtil() {
	}
}