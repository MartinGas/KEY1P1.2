
//class telling time between the last reset/instantiation and now
//works with milliseconds
public class Timer
{
	/** @return time in format used by Timer class**/	
	static long getSystemTime()
	{
		return System.currentTimeMillis();
	}	
	
	/** constructs Timer using the current time as reference**/
	public Timer (long triggerTime)
	{
		mTriggerTime = triggerTime;
		this.reset();
	}
	
	/**constructs Timer using a point in time
	* @param pointInTime Time to be used by Timer. Cannot be in the future**/
	public Timer (long pointInTime, long triggerTime)
	{
		assert (pointInTime <= Timer.getSystemTime());
		mTriggerTime = triggerTime;
		mLastTime = pointInTime;
	}
	
	/** resets the reference of the Timer to the current time**/
	public void reset()
	{
		mLastTime = Timer.getSystemTime();
	}
	
	/** @return the time elapsed between the point in time referenced and now **/
	public long getElapsedTime()
	{
		return (getSystemTime() - mLastTime);
	}
	
	public boolean hasElapsed()
	{
		return (getElapsedTime() >= mTriggerTime);
	}
	
	//stores reference to point in time as obtainted by getSystemTime
	private long mLastTime, mTriggerTime;
}
