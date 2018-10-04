package paintss.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.view.WindowManager;

public class DialogUtil {
	public static AlertDialog alert(Activity activity,
	                                String title, String message,
	                                View view, boolean showKeyboard,
	                                boolean hasOkButton,
	                                boolean hasCancelButton,
	                                final Runnable okButtonClick) {
		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity);
		if (title != null && !title.matches("")) dialogBuilder.setTitle(title);
		if (message != null && !message.matches("")) dialogBuilder.setMessage(message);
		final AlertDialog dialog;
		dialogBuilder.setView(view);

		if (hasCancelButton) {
			dialogBuilder.setNegativeButton("取消",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.cancel();
						}
					});
		}

//		if (hasOkButton) {
//			dialogBuilder.setPositiveButton(activity.getString(R.string.ok),
//					new DialogInterface.OnClickListener() {
//						public void onClick(DialogInterface dialog, int which) {}
//					});
//		}

		dialog = dialogBuilder.create();

		if (showKeyboard) {
			dialog.getWindow().setSoftInputMode(
					WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
		}

		if (okButtonClick != null) {
			dialog.setOnShowListener(new DialogInterface.OnShowListener() {
				@Override
				public void onShow(DialogInterface dialogInterface) {
					dialog.getButton(AlertDialog.BUTTON_POSITIVE).
							setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									okButtonClick.run();
								}
							});
				}
			});
		}

		return dialog;
	}

	public static AlertDialog alert(Activity activity, View view) {
		return alert(activity, null, null, view, false, false, false, null);
	}
}
