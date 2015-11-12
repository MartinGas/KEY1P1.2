
public interface PerfMeasure 
{
	public int getPerf (Game state);
	
	public int mayExceed (Game known, Game test);
}
