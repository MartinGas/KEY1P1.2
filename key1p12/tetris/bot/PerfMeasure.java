package key1p12.tetris.bot;
import Game;


public interface PerfMeasure 
{
	public int getPerf (Game state);
	
	public int mayExceed (Game known, Game test);
}
