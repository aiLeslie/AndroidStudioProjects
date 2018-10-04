package paintss.common;

import android.view.ViewTreeObserver;

public class Compatibility {
	public static void removeOnGlobalLayoutListener(
			ViewTreeObserver viewTreeObserver,
			ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener) {
		if (android.os.Build.VERSION.SDK_INT >=
				android.os.Build.VERSION_CODES.JELLY_BEAN)
			viewTreeObserver.removeOnGlobalLayoutListener(onGlobalLayoutListener);
		else viewTreeObserver.removeGlobalOnLayoutListener(onGlobalLayoutListener);
	}
}
