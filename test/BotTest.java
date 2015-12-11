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
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException
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
		
		PerfMeasureFactory pmFax = new PerfMeasureFactory();
		pmFax.setMaxScore ((int) scoreComp.calculateScore(maxShapeHeight));
		pmFax.setTolerance(maxShapeHeight - 2);
		
		ArrayList <PerfMeasure> pMeasures = new ArrayList<PerfMeasure>();
		pMeasures.add (pmFax.getPMeasure (PerfMeasureType.SCORE));
		pMeasures.add (pmFax.getPMeasure (PerfMeasureType.HEIGHTDIFFERENCE));
		pMeasures.add (pmFax.getPMeasure (PerfMeasureType.DENSITY));
		pMeasures.add (pmFax.getPMeasure (PerfMeasureType.ISOLATED));
		pMeasures.add (pmFax.getPMeasure (PerfMeasureType.RECTANGLE));
		String measuresUsed = "Score/HDiff/Density/Isolated/Rect";
		
		ArrayList <double[]> weightList = generateWeightPermutations(new double[pMeasures.size()], 0, 1.0, 0.2);
		
		int testRuns = 2;
		File testResultFile = new File ("TestResults.csv");
		generateExportFile(testResultFile, testRuns);
		int processCounter = 0;
		
		for (int cWeight = 0; cWeight < weightList.size(); ++cWeight)
		{
			ArrayList <Thread> threads = new ArrayList <Thread>();
			ArrayList <BotTest> parallelTests = new ArrayList<BotTest>();
			for (int cThread = 0; cThread < 16 && cWeight < weightList.size(); ++cThread, ++cWeight)
			{
				ArrayList <Double> weights = new ArrayList <Double> ();
				String setupMessage = measuresUsed + " ";
				for (double w : weightList.get(cWeight))
				{
					weights.add (w);
					setupMessage = setupMessage + w + "/";
				}
				PlayerFactory pFax = new GreedyBotFactory (pMeasures, weights, new File ("nameDataBase.txt"));
				
				GameSetup setup = new GameSetup (setupMessage);
				setup.loadBlocks(Pentomino.createsPentList());
				setup.loadBoard (new Board (10, 15));
				setup.loadHighScore (hsList);
				setup.loadPlayerFactory(pFax);
				
				try
				{
					BotTest testing = new BotTest (setup, testRuns);
					parallelTests.add (testing);
					Thread t = new Thread (testing);
					threads.add(t);
					t.start();
				}
				catch (Exception e)
				{
					System.out.println ("Something went wrong while testing");
					System.out.println (e.getMessage());
					e.printStackTrace();
					return;
				}
			}
			
			for (int cThread = 0; cThread < threads.size(); ++cThread)
			{
				try
				{
					threads.get (cThread).join();

					parallelTests.get(cThread).exportToCvs (testResultFile);
					
					++processCounter;
					System.out.println ("Process " + processCounter + " is done");
					
				}
				catch (InterruptedException ie)
				{
					System.out.println ("Thread was interrupted, test is gone");
					System.out.println (ie.getMessage());
					ie.printStackTrace();
				}
				catch (BadFormatExc bfe)
				{
					System.out.println ("CSV file has bad format");
					System.out.println (bfe.getMessage());
					bfe.printStackTrace();
				}
				catch (IOException ioe)
				{
					System.out.println ("Could not load/read/write properly");
					System.out.println (ioe.getMessage());
					ioe.printStackTrace();
				}
				
			}
		}
	}
	
	public static ArrayList <double[]> generateWeightPermutations (double[] weights, int position, double remain, double delta)
	{
		assert (position >= 0 && position < weights.length);
		ArrayList <double[]> storage = new ArrayList<double[]>();
		if (position < weights.length - 1)
		{
			for (double cRemain = remain; cRemain >= 0; cRemain -= delta)
			{
				weights[position] = cRemain;
				double[] copyWeights = new double[weights.length];
				System.arraycopy(weights, 0, copyWeights, 0, weights.length);
				storage.addAll (generateWeightPermutations (copyWeights, position + 1, remain - cRemain, delta));
			}
		}
		else
		{
			weights[position] = remain;
			storage.add (weights);
		}
		return storage;
	}
	
	public static void botWaitTest()
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
		//pMeasures.add (new ScorePerformance (scoreComp.calculateScore (maxShapeHeight)));
		//pMeasures.add (new HeightDiffPerformance (maxShapeHeight - 1));
		pMeasures.add (new DensityPerformance());
		ArrayList <Double> pmWeights = new ArrayList<Double>();
		//pmWeights.add (new Double (25.0));
		//pmWeights.add (new Double (1.2));
		pmWeights.add (new Double (1.0));
		PlayerFactory pFax = new GreedyBotFactory (pMeasures, pmWeights, new File ("nameDataBase.txt"));
		
		Board specificBoard = new Board (10, 20);
		ArrayList <Pentomino> pents = Pentomino.createsPentList();
		Position p = new Position (0, specificBoard.getHeight() - 5);
		specificBoard.placePent (pents.get(11), p.getPosNum (specificBoard.getHeight()));
		p.addX(1);
		specificBoard.placePent (pents.get(11), p.getPosNum (specificBoard.getHeight()));
		p.addX(1);
		specificBoard.placePent (pents.get(11), p.getPosNum (specificBoard.getHeight()));
		p.addX(1);
		specificBoard.placePent (pents.get(11), p.getPosNum (specificBoard.getHeight()));
		p.addX(3);
		p.addY(2);
		pents.get(2).rotate();
		pents.get(2).rotate();
		specificBoard.placePent (pents.get(2), p.getPosNum (specificBoard.getHeight()));
		p.addY (-5);
		specificBoard.placePent (pents.get(11), p.getPosNum (specificBoard.getHeight()));
		p.addX(1);
		specificBoard.placePent (pents.get(11), p.getPosNum (specificBoard.getHeight()));
		p.addX(1);
		specificBoard.placePent (pents.get(11), p.getPosNum (specificBoard.getHeight()));
		p.addX(1);
		specificBoard.placePent (pents.get(11), p.getPosNum (specificBoard.getHeight()));
		
		GameSetup setup = new GameSetup ("test");
		setup.loadBlocks(Pentomino.createsPentList());
		setup.loadBoard (new Board (10, 15));
		setup.loadHighScore (hsList);
		setup.loadPlayerFactory(pFax);
		
		try
		{
			BotTest testing = new BotTest (setup, 1);
			testing.run();
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
	
	public static final int PLACE_DELAY = 20, FALL_DELAY = 100;
	
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
			System.out.println();
			
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
