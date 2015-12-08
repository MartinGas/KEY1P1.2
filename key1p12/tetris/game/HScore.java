package key1p12.tetris.game;

import java.io.File;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;

public class HScore extends Score
{
	/**
	 * Creates file for storing a high score list
	 * @param file file where list is to be stored
	 * @param entries number of entries the list should contain
	 * @throws IOException
	 */
	public static void generateHighScoreFile (File file, int entries) throws IOException
	{
		if (!file.exists())
			file.createNewFile();
		PrintWriter w = null;
		try
		{
			w = new PrintWriter (file);
			Score nullScore = new Score();
			w.println (entries);
			for (int cEntries = 0; cEntries < entries; ++cEntries)
				w.println (nullScore.getScore() + " " + nullScore.getName());
		}
		finally
		{
			w.close();
		}
	}
	
	/**
	 * Constructor
	 * @param file file the highscore is to be read from and written to
	 * @param playerName name of the current player
	 * @param scoreComputer way to compute current score (independently of scores already stored)
	 * @throws FileNotFoundException
	 */
	public HScore(File file, String playerName, ScoreCountable scoreComputer) throws FileNotFoundException {
		super (playerName, scoreComputer);
		mHighScores = new ArrayList <Score>();
		mStorage = file;
		Scanner scanner = null;
		try
		{
			try
			{
				scanner = new Scanner (mStorage);
				
			}
			catch (Exception e)
			{
				throw new FileNotFoundException("file not found!");
			}
			
			mEntries = 0;
			if (scanner.hasNext())
			{
				mEntries = scanner.nextInt();
				scanner.nextLine();
			}
			for (int cEntries = 0; cEntries < mEntries; cEntries++)
			{
				if (scanner.hasNext())
				{					
					int score = scanner.nextInt();
					String name = scanner.next();
					mHighScores.add (new Score (score, name));
					//System.out.print ("load new score");
					//System.out.println (mHighScores.get (mHighScores.size() - 1).toString());
				}
				else
				{
					mHighScores.add (new Score());
					//System.out.println ("create empty score");
				}
			}
		}
		finally
		{
			scanner.close();
		}
	}
	
	/**
	 * Represents contents as string
	 */
	public String toString()
	{
		String out = new String();
		out += "current score " + getScore() + " " + getName() + "\n";
		for (Score s : mHighScores)
			out += s.toString() + "\n";
		return out;
	}
	
	
	public HScore clone()
	{
		return new HScore (this);
	}
	
	/**
	 * locks the object and writes changes to file
	 * @throws IOException
	 */
	public void writeToFile () throws IOException
	{
		assert (!super.isLocked());
		if (!super.isLocked())
		{
			super.lock();
		}
		insertCurrentToList();
		PrintWriter fileWriter = new PrintWriter (mStorage);
		fileWriter.println (getNumberEntries());
		for (ListIterator<Score> li = mHighScores.listIterator(); li.hasNext(); )
		{
			Score write = li.next();
			fileWriter.println (write.getScore() + " " + write.getName());
		}
		fileWriter.close();
	}
	
	/**
	 * @return number of entries of the high score list the object maintains
	 */
	public int getNumberEntries()
	{
		return mEntries;
	}
	
	/**
	 * @param index index of score object in high score list
	 * @return clone of score at index
	 */
	public Score getScore (int index)
	{
		return mHighScores.get(index).clone();
	}
	
	/**
	 * Clone constructor
	 */
	private HScore (HScore copy)
	{
		super (copy);
		mHighScores = (ArrayList<Score>)copy.mHighScores;
		mStorage = copy.mStorage;
		mEntries = copy.mEntries;
	}
	
	//if possible inserts current score into sorted array list
	private void insertCurrentToList()
	{
		int iComp = 0;
		while (iComp < mHighScores.size() && super.compareTo (mHighScores.get (iComp)) >= 0)
		{
			++iComp;
		}
		if (iComp < mHighScores.size() && super.compareTo (mHighScores.get (iComp)) < 0)
		{
			copyScoresDown (iComp);
			mHighScores.set (iComp, this);
		}
	}
	
	//to clear one entry, lower high scores are copied to the next entry
	//the last entry is erased
	private void copyScoresDown (int i)
	{
		for (int cCopy = mHighScores.size() - 2; cCopy >= i; cCopy--)
		{
			mHighScores.set(cCopy + 1, mHighScores.get (cCopy));
		}
	}
	
	private ArrayList <Score> mHighScores;
	private File mStorage;
	private int mEntries;
	/*
	private int hScore1;
	private int hScore2;
	private int hScore3;
	private String hPlayer1;
	private String hPlayer2;
	private String hPlayer3;*/
}
