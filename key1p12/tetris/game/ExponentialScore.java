package key1p12.tetris.game;

public class ExponentialScore implements ScoreCountable 
{
	/**
	 * @param base base of exponential term
	 * @param baseCoefficient base's coeffiecient
	 * @param exponentCoefficient coefficient of exponent
	 */
	public ExponentialScore (double base, double baseCoefficient, double exponentCoefficient)
	{
		mBase = base;
		mBaseCoefficient = baseCoefficient;
		mExponentCoefficient = exponentCoefficient;
	}
	
	public ExponentialScore clone()
	{
		return new ExponentialScore (mBase, mBaseCoefficient, mExponentCoefficient);
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
