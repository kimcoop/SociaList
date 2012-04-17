package zebrafish.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public final class IOUtil {

	public static boolean isOnline(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		//
		return netInfo != null && netInfo.isConnectedOrConnecting();
	}

	private IOUtil() {
	}
}