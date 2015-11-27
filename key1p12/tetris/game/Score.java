package key1p12.tetris.game;

public class Score implements Comparable <Score>
{
	/**
	 * Creates empty score
	 */
	public Score ()
	{
		mScoreComputer = null;
		mScore = 0;
		mName = null;
		mLock = true;
	}
	
	/**
	 * Creates modifyable score
	 * @param name player's name
	 * @param scoreComputer object used to compute score
	 */
	public Score (String name, ScoreCountable scoreComputer)
	{
		mScoreComputer = scoreComputer;
		mScore = 0;
		mName = name;
		mLock = false;
	}
	
	/**
	 * Creates constant score
	 * @param score score
	 * @param name player who scored score
	 */
	public Score (int score, String name)
	{
		mScoreComputer = null;
		mScore = score;
		mName = name;
		mLock = true;
	}
	
	/**
	 * @return score stored
	 */
	public long getScore()
	{
		return mScore;
	}
	
	/**
	 * @param compare Score object to compare to
	 * @return 0 if objects are equal, 1 if compare is larger, -1 if compare is smaller
	 */
	public int compareTo(Score compare) 
	{
		if (compare.getScore() == this.getScore())
			return 0;
		else if (this.getScore() > compare.getScore())
			return -1;
		else
			return 1;
	}
	
	/**
	 * @return name stored
	 */
	public String getName()
	{
		return mName;
	}
	
	/**
	 * Represents internal state as string
	 */
	public String toString()
	{
		return "score " + mScore + " name " + mName;
	}
	
	/**
	 * Increase the score using the score computer
	 * Precondition: object is not locked
	 * @param rowsCleared numbers of rows cleared
	 */
	public void increaseScore (int rowsCleared)
	{
		if (!mLock)
			mScore += mScoreComputer.calculateScore (rowsCleared);
	}
	
	/**
	 * Turns object non-modifyable
	 */
	public void lock()
	{
		mLock = true;
	}
	
	
	private ScoreCountable mScoreComputer;
	private long mScore;
	private String mName;
	private boolean mLock;
}
