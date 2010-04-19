package jds.l2infoj.threads;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 05/01/2010
 * Time: 8:43:23
 */
public class ThreadPoolManager
{
	private static ThreadPoolManager _instance;
	private ThreadPoolExecutor _executor;

	public static ThreadPoolManager getInstance()
	{
		if (_instance == null)
		{
			_instance = new ThreadPoolManager();
		}
		return _instance;
	}

	private ThreadPoolManager()
	{
	 	_executor = new ThreadPoolExecutor(10, 12, 15L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(), new PriorityThreadFactory("Executor", Thread.NORM_PRIORITY));
	}

	public void execute(Runnable rub)
	{
		_executor.execute(rub);
	}

	public void shutdown()
	{
		_executor.shutdown();
	}
}
