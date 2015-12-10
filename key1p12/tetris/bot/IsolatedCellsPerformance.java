package key1p12.tetris.bot;

import key1p12.tetris.game.Board;
import key1p12.tetris.game.Game.SimulGame;
import key1p12.tetris.game.MatrixHandler;

public class IsolatedCellsPerformance implements PerfMeasure {

	@Override
	/**
	 * @param state state of game
	 * @return ratio of rows with isolated cells over number of rows total
	 */
	public double getPerf (SimulGame state) 
	{
		int[][] mat = new int[state.getWidth()][state.getHeight()];
		for (int cCol = 0; cCol < state.getWidth(); ++cCol)
		{
			for (int cRow = 0; cRow < state.getHeight(); ++cRow)
				mat[cCol][cRow] = state.getElement(cCol, cRow);
		}
		MatrixHandler matHandler = new MatrixHandler(mat);
		
		fillFreeArea (matHandler);
		int cRow = state.getHeight() - 1;
		int cIsolatedCellRow = 0;
		while (cRow > 0 && !matHandler.isRowEmpty (cRow))
		{
			if (!matHandler.isRowFilled(cRow))
				++cIsolatedCellRow;
			--cRow;
		}
		return ((state.getHeight() - cIsolatedCellRow) / (double) state.getHeight());
	}

	@Override
	public boolean mayExceed(SimulGame known, SimulGame test) 
	{
		// TODO Auto-generated method stub
		return true;
	}
	
	private void fillFreeArea (MatrixHandler fill)
	{
		for (int cCol = 0; cCol < fill.getWidth(); ++cCol)
			recursiveFill (fill, cCol, 0);
	}
	
	private void recursiveFill (MatrixHandler fill, int x, int y)
	{
		if (fill.getCell(x, y) == 0)
		{
			fill.setCell(x, y, -1);
			for (int cAdCol = -1; cAdCol <= 1; ++cAdCol)
			{
				for (int cAdRow = -1; cAdRow <= 1; ++cAdRow)
				{
					if (cAdCol >= 0 && cAdCol < fill.getWidth() &&
						cAdRow >= 0 && cAdRow < fill.getHeight() &&
						(cAdCol != 0 || cAdRow != 0))
						recursiveFill (fill, x + cAdCol, y + cAdRow);
				}
			}
		}
	}
}
