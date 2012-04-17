package zebrafish.util;

import java.util.Calendar;
import java.util.TimeZone;

import android.content.Context;
import android.text.format.DateFormat;
import edu.pitt.cs1635group3.R;

/** Usage:
 *	(DateUtil.formatDate(Long.parseLong("my date", context));
 */

public final class DateUtil {
	

	public static String formatDate(long time, Context context) {
		final long OS = 1000;
		final long OM = OS * 60;
		final long OH = OM * 60;
		final long T4H = OH * 24;
		//
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(time);
		//
		TimeZone z = c.getTimeZone();
		final int offset = z.getRawOffset();
		final int offsetHrs = offset / 1000 / 60 / 60;
		final int offsetMins = offset / 1000 / 60 % 60;
		//
		c.add(Calendar.HOUR_OF_DAY, offsetHrs);
		c.add(Calendar.MINUTE, offsetMins);
		//
		time = c.getTimeInMillis();
		//
		long ellapsed = System.currentTimeMillis() - time;
		//
		if (ellapsed < 0) {
			return "1 " + context.getString(R.string.second);
		}
		//
		if (ellapsed < OM) {
			ellapsed = ellapsed / OS;
			if (ellapsed == 1) {
				return ellapsed + " " + context.getString(R.string.second);
			} else {
				return ellapsed + " " + context.getString(R.string.seconds);
			}
		} else if (ellapsed < OH) {
			ellapsed = ellapsed / OM;
			if (ellapsed == 1) {
				return ellapsed + " " + context.getString(R.string.minute);
			} else {
				return ellapsed + " " + context.getString(R.string.minutes);
			}
		} else if (ellapsed < T4H) {
			ellapsed = ellapsed / OH;
			if (ellapsed == 1) {
				return ellapsed + " " + context.getString(R.string.hour);
			} else {
				return ellapsed + " " + context.getString(R.string.hours);
			}
		} else {
			return DateFormat.format("dd/MM", time).toString();
		}
	}

	/**
	 * 
	 */
	private DateUtil() {
	}
}
