package key1p12.tetris.bot;

import key1p12.tetris.game.Game.SimulGame;

public class HeightDiffPerformance implements PerfMeasure {

	@Override
	public double getPerf (SimulGame state) 
	{
		//store highest stack and lowest stack of cells
		int highest = state.getHeight(), lowest = 0;
		for (int cCol = 0; cCol < state.getWidth(); ++cCol)
		{
			int height = state.getFilledHeight (cCol) + 1;
			if (height < highest)
				highest = height;
			else if (height > lowest)
				lowest = height;
		}
		return (double)(state.getHeight() - lowest + highest) / (double)state.getHeight();
	}

	@Override
	public boolean mayExceed (SimulGame known, SimulGame test) {
		// TODO Auto-generated method stub
		return true;
	}

}
