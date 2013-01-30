package tools;

public class StopWatch
{
	private long startTime;
	private long pausedTime;
	private boolean paused;
	private boolean started;
	
	public StopWatch()
	{
		startTime = 0;
		pausedTime = 0;
		paused = false;
		started = false;
	}
	
	public void start()
	{
		startTime = System.currentTimeMillis();
		started = true;
		paused = false;
	}
		
	public void stop()
	{
		started = false;
		paused = false;
	}
	
	public void pause()
	{
		if(started && !paused)
		{
			pausedTime = System.currentTimeMillis() - startTime;
			paused = true;
		}
	}
	
	public void unpause()
	{
		if(paused)
		{
			startTime = System.currentTimeMillis() - pausedTime;
			paused = false;
			pausedTime = 0;
		}
	}
	
	public boolean isStarted()
	{
		return started;
	}
	
	public boolean isPaused()
	{
		return paused;
	}
	
	public long getElapsed()
	{
		if(started)
		{
			if(paused)
				return pausedTime;
			else
				return System.currentTimeMillis() - startTime;
		}
		return 0; //timer isn't running
	}
}
