package paintss.common;

import android.content.Context;

public class Toast {
	public static void show(Context context, String text) {
		android.widget.Toast.makeText(context,
				text,
				android.widget.Toast.LENGTH_SHORT).show();
	}
}
