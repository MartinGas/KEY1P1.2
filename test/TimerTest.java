import static org.junit.Assert.*;
import org.junit.Test;

public class TimerTest
{
	public final long mEPSILON = 3;	
	@Test
	public void defaultConstructTest()
	{
		Timer standard = new Timer(50);
		assertTrue (standard.getElapsedTime() < mEPSILON);
	}
	
	@Test
	public void pInTimeConstructTest()
	{
		long delay = 50;
		Timer custom = new Timer (System.currentTimeMillis() - delay, 100);
		assertTrue (custom.getElapsedTime() > delay - mEPSILON && custom.getElapsedTime() < delay + mEPSILON);
	}
	
	@Test
	public void getElapsedTimeTest()
	{
		long delay = 100;
		Timer test = new Timer(50);
		try
		{
			Thread.sleep (delay);
			assertTrue (test.getElapsedTime() > delay - mEPSILON && test.getElapsedTime() < delay + mEPSILON);
		}
		catch (Exception e) {}
	}
	
	@Test
	public void hasElapsedTest()
	{
		int delay = 50;
		Timer test = new Timer (delay);
		try
		{
			Thread.sleep (delay + mEPSILON);
			assertTrue (test.hasElapsed());
		}
		catch (Exception e) {}
	}
	
	@Test
	public void resetTest()
	{
		int delays[] = {25, 10};
		Timer test = new Timer (50);
		try
		{
			Thread.sleep(delays[0]);
			test.reset();
			Thread.sleep (delays[1]);
			assertTrue (test.getElapsedTime() > delays[1] - mEPSILON && test.getElapsedTime() < delays[1] + mEPSILON);
		}
		catch (Exception e) {}
	}
}