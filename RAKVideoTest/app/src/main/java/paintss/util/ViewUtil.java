package paintss.util;

import android.view.View;
import android.view.ViewGroup;

/**
 * Created by admin on 2/2/2015.
 */
public class ViewUtil {
	public static void removeFromParent(View view) {
		ViewGroup parent = (ViewGroup)view.getParent();
		if (parent != null)
			parent.removeView(view);
	}
}
