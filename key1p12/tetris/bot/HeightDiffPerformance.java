package key1p12.tetris.bot;

import key1p12.tetris.game.Game.SimulGame;

public class HeightDiffPerformance implements PerfMeasure {

	public HeightDiffPerformance (int tolerance)
	{
		mTolerance = tolerance;
	}
	
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
		
		if (lowest - highest < mTolerance)
			return 1;
		else
			return (double)(state.getHeight() - lowest + highest) / (double)(state.getHeight() - mTolerance);
	}

	@Override
	public boolean mayExceed (SimulGame known, SimulGame test) {
		// TODO Auto-generated method stub
		return true;
	}
	
	private int mTolerance;
}
