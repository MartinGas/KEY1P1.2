package key1p12.tetris.bot;

import key1p12.tetris.game.Game.SimulGame;

public class ScorePerformance implements PerfMeasure {

	public ScorePerformance (double maxScore)
	{
		mMaxScore = maxScore;
	}
	
	@Override
	/**
	 * Measures performance by means of current score
	 * @return number between [0, 1]
	 */
	public double getPerf (SimulGame state) 
	{
		return state.getLastScoreDifference() / mMaxScore;
	}

	@Override
	public boolean mayExceed(SimulGame known, SimulGame test) 
	{
		// TODO Auto-generated method stub
		return true;
	}
	
	private double mMaxScore;
}
