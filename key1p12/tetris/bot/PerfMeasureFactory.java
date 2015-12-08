package key1p12.tetris.bot;


public class PerfMeasureFactory 
{
	public PerfMeasureFactory (int maxScore) 
	{
		mMaxScore = maxScore;
	}
	
	public PerfMeasure getPMeasure (PerfMeasureType p)
	{
		switch (p)
		{
		case SCORE: return new ScorePerformance(mMaxScore);
		case RECTANGLE: return new RectangularPerformance();
		case HEIGHTDIFFERENCE: return new HeightDiffPerformance();
		case DENSITY: return new DensityPerformance();
		}
		
		return null;
	}
	
	private int mMaxScore;
}
