package paintss.util;

import android.app.Activity;
import android.view.WindowManager;

/**
 * Created by lewis on 2015/1/25.
 */
public class KeyboardUtil {
	public static void show(Activity activity) {
		activity.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
	}

	public static void hide(Activity activity) {
		activity.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	}
}
