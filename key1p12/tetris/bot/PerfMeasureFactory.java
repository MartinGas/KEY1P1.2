package key1p12.tetris.bot;


public class PerfMeasureFactory 
{
	
	public void setMaxScore (int maxScore)
	{
		mMaxScore = maxScore;
	}
	
	public void setTolerance (int tolerance)
	{
		mTolerance = tolerance;
	}
	
	public PerfMeasure getPMeasure (PerfMeasureType p)
	{
		switch (p)
		{
		case SCORE: return new ScorePerformance(mMaxScore);
		case HEIGHT: return new TotalHeightPerformance (mTolerance);
		case RECTANGLE: return new RectangularPerformance();
		case HEIGHTDIFFERENCE: return new HeightDiffPerformance (mTolerance);
		case DENSITY: return new DensityPerformance();
		case ISOLATED: return new IsolatedCellsPerformance();
		}
		
		return null;
	}
	
	private int mMaxScore, mTolerance;
}
