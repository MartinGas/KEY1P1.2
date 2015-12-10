package test;

import java.io.*;
import java.util.*;

import key1p12.tetris.game.*;
import key1p12.tetris.bot.*;
import key1p12.tetris.gui.*;

public class BotTest implements Runnable
{
	public static class BadFormatExc extends IOException
	{
		public BadFormatExc ()
		{
			super();
		}
		
		public BadFormatExc (String message)
		{
			super (message);
		}
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		int maxShapeHeight = 5;
		HScore hsList = null;
		ScoreCountable scoreComp = new ExponentialScore (2, 1, 1);
		try
		{
			File hsFile = new File ("mockScores.txt");
			HScore.generateHighScoreFile(hsFile, 5);
			hsList = new HScore (hsFile, "Tester", scoreComp);
		}
		catch (FileNotFoundException fnfe)
		{
			System.out.println ("Quit testing, could not load mock high score file");
			return;
		}
		catch (IOException ioe)
		{
			System.out.println ("Quit testing, could not create mock high score file");
			return;
		}
		
		
		ArrayList <PerfMeasure> pMeasures = new ArrayList<PerfMeasure>();
		pMeasures.add (new ScorePerformance (scoreComp.calculateScore (maxShapeHeight)));
		pMeasures.add (new HeightDiffPerformance (maxShapeHeight - 1));
		pMeasures.add (new DensityPerformance());
		ArrayList <Double> pmWeights = new ArrayList<Double>();
		pmWeights.add (new Double (25.0));
		pmWeights.add (new Double (1.2));
		pmWeights.add (new Double (1.0));
		PlayerFactory pFax = new GreedyBotFactory (pMeasures, pmWeights, new File ("nameDataBase.txt"));
		
		GameSetup setup = new GameSetup ("test");
		setup.loadBlocks(Pentomino.createsPentList());
		setup.loadBoard (new Board (10, 15));
		setup.loadHighScore (hsList);
		setup.loadPlayerFactory(pFax);
		
		Thread t = null;
		try
		{
			BotTest testing = new BotTest (setup, 10);
			t = new Thread (testing);
			t.setDaemon(true);
			t.start();
			t.join();
			testing.exportToCvs (new File ("testingTest.csv"));
		}
		catch (Exception e)
		{
			System.out.println ("Something went wrong while testing");
			System.out.println (e.getMessage());
			e.printStackTrace();
			return;
		}
	}
	
	public static void generateExportFile (File cvsFile, int entries) throws IOException
	{
		BufferedWriter bw = null;
		try
		{
			try
			{
				if (!cvsFile.exists())
					cvsFile.createNewFile();
				
				bw = new BufferedWriter (new FileWriter (cvsFile));
				
				bw.write ("Repetition,\n");
				for (int cEntry = 0; cEntry < entries; ++cEntry)
					bw.write(cEntry + ",\n");
			}
			catch (IOException ioe)
			{
				cvsFile.delete();
				throw ioe;
			}
		}
		finally
		{
			if (bw != null)
				bw.close();
		}
	}
	
	public static final int PLACE_DELAY = 50, FALL_DELAY = 100;
	
	public BotTest (GameSetup setup, int repetitions)
	{
		mSetup = setup;
		mRepetitions = repetitions;
		scores = new ArrayList <Integer>();
	}
	
	public void run()
	{
		Game.mFALL_TIME = FALL_DELAY;
		Game.mPLACE_TIME = PLACE_DELAY;
		
		for (int cRep = 0; cRep < mRepetitions; ++cRep)
		{
			Game game = mSetup.construct();
			while (!game.isGameOver())
				game.play();
			scores.add ((int) game.getCurrScore().getScore());
			}
	}
	
	public void exportToCvs (File cvsFile) throws BadFormatExc, IOException
	{
		if (!cvsFile.exists())
			BotTest.generateExportFile(cvsFile, mRepetitions);
		
		BufferedReader bReader = new BufferedReader (new FileReader (cvsFile));
		ArrayList <String> contents = new ArrayList<String>();
		
		for (int cRep = 0; cRep < mRepetitions + 1; ++cRep)
		{
			if (bReader.ready())
				contents.add (bReader.readLine());
			else
				throw new BadFormatExc ("Not enough lines to fit current setup, stopped at line " + (cRep + 1) + " of " + (mRepetitions + 1));
		}
		bReader.close();
		
		BufferedWriter bWriter = new BufferedWriter (new FileWriter (cvsFile));
		bWriter.write (contents.get(0) + mSetup.getMessage() + ",\n");
		for (int cRep = 0; cRep < mRepetitions; ++cRep)
			bWriter.append (contents.get (cRep + 1) + scores.get(cRep) + ",\n");
		bWriter.close();
	}
	
	private GameSetup mSetup;
	private int mRepetitions;
	private ArrayList <Integer> scores;
}
