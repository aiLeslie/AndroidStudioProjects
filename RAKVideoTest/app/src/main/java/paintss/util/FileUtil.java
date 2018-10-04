package paintss.util;

import java.io.File;

/**
 * Created by admin on 2/2/2015.
 */
public class FileUtil {
	public static void deleteFolder(File path) {
		if (!path.exists()) return;

		if (path.isDirectory())
			for (File child : path.listFiles())
				deleteFolder(child);

		path.delete();
	}
}
