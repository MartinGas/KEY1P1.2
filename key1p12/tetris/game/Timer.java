package key1p12.tetris.game;

//class telling time between the last reset/instantiation and now
//works with milliseconds
public class Timer implements Cloneable
{
	/** @return time in format used by Timer class**/	
	static long getSystemTime()
	{
		return System.currentTimeMillis();
	}	
	
	/** constructs Timer using the current time as reference
	 * @param triggerTime total delay of timer in milliseconds
	 * **/
	public Timer (long triggerTime)
	{
		mTriggerTime = triggerTime;
		mLastTime = 0;
		mStopTime = 0;	
		//timer state: timer initially paused
	}
	
	/**constructs Timer using a point in time
	* @param pointInTime point when timer was started/reset last in milliseconds
	* @param triggerTime total delay of timer in milliseconds**/
	public Timer (long pointInTime, long triggerTime)
	{
		assert (pointInTime <= Timer.getSystemTime());
		mTriggerTime = triggerTime;
		mLastTime = pointInTime;
		mStopTime = Timer.getSystemTime();
	}
	
	/**resets the reference of the Timer to the current time
	 * Postcondition: elapsed time = 0 
	**/
	public void reset()
	{
		//if timer was paused
		if (isStopped())
			mLastTime = mStopTime;
		else
			mLastTime = Timer.getSystemTime();
	}
	
	/**starts the timer**/
	public void start()
	{
		if (isStopped())
		{
			mStopTime = 0;
			mLastTime = Timer.getSystemTime() - mLastTime + mStopTime;
		}
	}
	
	
	public void stop()
	{
		mStopTime = Timer.getSystemTime();
	}
	
	public boolean isStopped()
	{
		return (mLastTime <= mStopTime);
	}
	
	/** @return the time elapsed between the point in time referenced and now **/
	public long getElapsedTime()
	{
		if (mStopTime < mLastTime)
			return (getSystemTime() - mLastTime);
		else
			return (mStopTime - mLastTime);
	}
	
	public boolean hasElapsed()
	{
		return (getElapsedTime() >= mTriggerTime);
	}
	
	//stores reference to point in time as obtainted by getSystemTime
	private long mLastTime, mStopTime, mTriggerTime;
}
