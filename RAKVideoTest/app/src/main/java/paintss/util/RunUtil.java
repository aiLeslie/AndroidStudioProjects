package paintss.util;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by admin on 2/2/2015.
 */
public class RunUtil {
	public static void delayRun(final Runnable runnable, int time, TimeUnit unit) {
		ScheduledExecutorService worker =
				Executors.newSingleThreadScheduledExecutor();

		worker.schedule(new Runnable() {
			@Override
			public void run() {
				runnable.run();
			}
		}, time, unit);
	}
}
