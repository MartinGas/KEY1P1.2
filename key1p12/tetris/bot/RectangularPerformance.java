package key1p12.tetris.bot;

import key1p12.tetris.game.Game.SimulGame;

public class RectangularPerformance implements PerfMeasure 
{

	/**
	 * @param state current state of game
	 * @return number in [0, 1]
	 */
	@Override
	public double getPerf (SimulGame state) 
	{
		int cRects = 0;
		int lastHeight = state.getHeight();
		for (int cCol = 0; cCol < state.getWidth(); ++cCol)
		{
			int h = state.getFilledHeight(cCol);
			if (h != lastHeight)
			{
				++cRects;
				lastHeight = h;
			}
		}
		return (double)(state.getWidth() - cRects) / (double)state.getWidth();
	}

	@Override
	public boolean mayExceed (SimulGame known, SimulGame test) 
	{
		// TODO Auto-generated method stub
		return true;
	}

}
