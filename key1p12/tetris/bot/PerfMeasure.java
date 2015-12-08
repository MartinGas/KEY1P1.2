package key1p12.tetris.bot;
import key1p12.tetris.game.Game.SimulGame;;


public interface PerfMeasure 
{
	public double getPerf (SimulGame state);
	
	public boolean mayExceed (SimulGame known, SimulGame test);
}
