package key1p12.tetris.game;

import java.io.File;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;

public class HScore extends Score
{
	public static final int ENTRIES = 5;
	
	
	public HScore(File file, String playerName, ScoreCountable scoreComputer)throws FileNotFoundException{
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
			for (int cEntries = 0; cEntries < ENTRIES; cEntries++)
			{
				if (scanner.hasNext())
				{					
					int score = scanner.nextInt();
					String name = scanner.next();
					mHighScores.add (new Score (score, name));
					System.out.print ("load new score");
					System.out.println (mHighScores.get (mHighScores.size() - 1).toString());
				}
				else
				{
					mHighScores.add (new Score());
					System.out.println ("create empty score");
				}
			}
		}
		finally
		{
			scanner.close();
		}
	}
	
	//writes the new values to the file
	public void writeToFile () throws IOException
	{
		super.lock();
		insertCurrentToList();
		PrintWriter fileWriter = new PrintWriter (mStorage);
		for (ListIterator<Score> li = mHighScores.listIterator(); li.hasNext(); )
		{
			Score write = li.next();
			fileWriter.println (write.getScore() + "," + write.getName());
		}
		fileWriter.close();
	}
	
	private void insertCurrentToList()
	{
		int iComp = 0;
		while (iComp < mHighScores.size() && super.compareTo (mHighScores.get (iComp)) >= 0)
		{
			++iComp;
		}
		if (super.compareTo (mHighScores.get (iComp)) < 0)
		{
			copyScoresDown (iComp);
			mHighScores.set (iComp, this);
		}
	}
	
	private void copyScoresDown (int i)
	{
		for (int cCopy = mHighScores.size() - 2; cCopy >= i; cCopy--)
		{
			mHighScores.set(cCopy + 1, mHighScores.get (cCopy));
		}
	}
	
	private ArrayList <Score> mHighScores;
	private File mStorage;
	/*
	private int hScore1;
	private int hScore2;
	private int hScore3;
	private String hPlayer1;
	private String hPlayer2;
	private String hPlayer3;*/
}
