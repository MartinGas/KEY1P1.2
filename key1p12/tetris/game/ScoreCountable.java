package key1p12.tetris.game;

public interface ScoreCountable extends Cloneable
{
	/**
	 * @param number input number
	 * @return score based on input number
	 */
	public long calculateScore (int number);

	/**
	 * @return cloned score countable object
	 */
	public ScoreCountable clone();
}
