package key1p12.tetris.bot;

import key1p12.tetris.game.Game.SimulGame;

public class DensityPerformance implements PerfMeasure {

	@Override
	public double getPerf (SimulGame state) 
	{
		if (state.isGameOver())
			return 0;
		//store start & end of current segment
		int cColStart = 0, cColEnd = 0;
		//store number of filled & total cells
		int cellsFilled = 0, cellsTotal = 0;
		while (cColStart < state.getWidth())
		{
			//store height of current segment
			int h = state.getFilledHeight(cColStart);
			do
			{
				//count number of filled cells in this column
				for (int cRow = h + 1; cRow < state.getHeight(); ++cRow)
				{
					if (state.getElement (cColEnd, cRow) != 0)
						++cellsFilled;
				}
				//go to next column => increase segment
				++cColEnd;
			} while (cColEnd < state.getWidth() && state.getFilledHeight (cColEnd) == h);
			//calculate area of segment = width * height
			cellsTotal += (cColEnd - cColStart) * (state.getHeight() - 1 - h);
			//next segment
			cColStart = cColEnd;
		}
		return ((double)cellsFilled / (double)cellsTotal);
	}

	@Override
	public boolean mayExceed(SimulGame known, SimulGame test) {
		// TODO Auto-generated method stub
		return true;
	}

}
