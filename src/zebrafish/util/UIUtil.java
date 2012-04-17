package zebrafish.util;

import java.io.IOException;

import android.content.Context;
import android.widget.Toast;
import edu.pitt.cs1635group3.R;

public final class UIUtil {

	public static void showMessage(Context context, Throwable exception) {
		if (exception instanceof IOException) {
			showMessage(context, R.string.network_access_failure);
		} else {
			showMessage(context, exception.getMessage());
		}
	}

	public static void showMessage(Context context, String message) {
		Toast.makeText(context, message, Toast.LENGTH_LONG).show();
	}

	public static void showMessage(Context context, int resId) {
		showMessage(context, context.getResources().getString(resId));
	}

	public static int getMessageId(Throwable exception) {
		if (exception instanceof IOException) {
			return R.string.network_access_failure;
		} else {
			return R.string.unknown_failure;
		}
	}

	private UIUtil() {
	}
}
