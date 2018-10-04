package paintss.util;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.view.Surface;
import android.view.WindowManager;

public class ActivityUtil {
	public static int getScreenOrientation(Context context) {
		int orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
		switch (((WindowManager) context.getSystemService(Context.WINDOW_SERVICE))
				.getDefaultDisplay().getRotation()) {
			case Surface.ROTATION_90:
				orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
				break;
			case Surface.ROTATION_180:
				orientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
				break;
			case Surface.ROTATION_270:
				orientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
				break;
		}

		return orientation;
	}

	public static void lockScreenOrientation(android.app.Activity activity, boolean lock) {
		if (lock) {
			switch (((WindowManager) activity.getApplicationContext()
					.getSystemService(Context.WINDOW_SERVICE))
					.getDefaultDisplay().getRotation()) {
				case Surface.ROTATION_90:
					activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
					break;
				case Surface.ROTATION_180:
					activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT);
					break;
				case Surface.ROTATION_270:
					activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
					break;
				default:
					activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			}
		} else activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
	}
}
