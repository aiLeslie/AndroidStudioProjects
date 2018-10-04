package paintss.util;

import android.graphics.Bitmap;
import android.graphics.Matrix;

public class DrawUtil {
	public static Bitmap rotateImage(Bitmap paramBitmap, Matrix paramMatrix) {
		return Bitmap.createBitmap(
				paramBitmap, 0, 0,
				paramBitmap.getWidth(),
				paramBitmap.getHeight(),
				paramMatrix, true);
	}
}
