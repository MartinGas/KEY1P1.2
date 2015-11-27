package key1p12.tetris.game;

public class ExponentialScore implements ScoreCountable 
{
	public ExponentialScore (double base, double baseCoefficient, double exponentCoefficient)
	{
		mBase = base;
		mBaseCoefficient = baseCoefficient;
		mExponentCoefficient = exponentCoefficient;
	}
	
	
	/**
	 * @param number number of rows cleared
	 * @return exponential score depending on number
	 */
	public long calculateScore(int number) 
	{
		
		return (long)(mBaseCoefficient * Math.pow (mBase, mExponentCoefficient * number));
	}
	
	
	double mBase, mBaseCoefficient, mExponentCoefficient;
}
