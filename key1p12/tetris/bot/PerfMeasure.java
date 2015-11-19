package key1p12.tetris.bot;
import key1p12.tetris.game.Game;


public interface PerfMeasure 
{
	public int getPerf (Game.SimulGame state);
	
	public int mayExceed (Game.SimulGame known, Game.SimulGame test);
}
