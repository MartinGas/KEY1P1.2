package test;

import java.io.*;
import java.util.ArrayList;

import org.junit.Test;
import static org.junit.Assert.*;

import key1p12.tetris.bot.*;
import key1p12.tetris.game.*;
import key1p12.tetris.game.Game.SimulGame;

public class PerfMeasureTest 
{
	public static double EPSILON = 0.0005;
	
	public PerfMeasureTest() throws IOException
	{
		Board b = new Board (13, 10);
		ArrayList <Pentomino> ps = Pentomino.createsPentList();
		
		for (int cPent = 0; cPent < 4; ++cPent)
		{
			Pentomino place = ps.get(cPent);
			Position bottomPos = new Position (3 * cPent, b.getHeight() - place.getHeight());
			b.placePent(place, bottomPos.getPosNum(b.getHeight()));
		}
		
		//b.printBoard();
		
		File hsFile = new File ("highscores.txt");
		if (!hsFile.exists())
			HScore.generateHighScoreFile(hsFile, 10);
		ScoreCountable sComputer = new ExponentialScore (2, 1, 1);
		HScore hsList = new HScore(hsFile, "bla", sComputer);
		
		mGame = new Game (b, ps, new HumanPlayer(""), hsList);
		mSimulGame = new Game.SimulGame (mGame);
		
		int maxHeight = 5, heightTolerance = 1;
		mPFax = new PerfMeasureFactory();
		mPFax.setMaxScore ((int)sComputer.calculateScore(maxHeight));
		mPFax.setTolerance (heightTolerance);
	}
	/*	test Board
	 * 	0 0 0 0 0 0 0 0 0 0 0 0 0 
		0 0 0 0 0 0 0 0 0 0 0 0 0 
		0 0 0 0 0 0 0 0 0 0 0 0 0 
		0 0 0 0 0 0 0 0 0 0 0 0 0 
		0 0 0 0 0 0 0 0 0 0 0 0 0 
		0 0 0 0 0 0 0 0 0 0 0 0 0 
		0 0 0 0 0 0 0 0 0 0 0 0 0 
		0 2 0 0 3 3 4 0 0 5 0 0 0 
		2 2 2 3 3 0 4 0 0 5 5 0 0 
		0 2 0 0 3 0 4 4 4 0 5 5 0 
	 */
	
	@Test
	public void testHeightDiff ()
	{
		PerfMeasure hdp = mPFax.getPMeasure (PerfMeasureType.HEIGHTDIFFERENCE);
		double perf = hdp.getPerf (mSimulGame);
		double expected = 7.0 / 9.0;
		assertTrue (perf >= expected - EPSILON && perf <= expected + EPSILON);
	}
	
	@Test
	public void testTotalHeight ()
	{
		PerfMeasure hp = mPFax.getPMeasure (PerfMeasureType.HEIGHT);
		double perf = hp.getPerf(mSimulGame);
		double expected = 7.0 / 9.0;
		assertTrue (perf >= expected - EPSILON && perf <= expected + EPSILON);
	}
	
	@Test
	public void testRect ()
	{
		PerfMeasure rp = mPFax.getPMeasure (PerfMeasureType.RECTANGLE);
		double perf = rp.getPerf(mSimulGame);
		double expected = (13.0 - 9.0) / 13.0;
		assertTrue (perf >= expected - EPSILON && perf <= expected + EPSILON);
	}
	
	@Test
	public void testDensity ()
	{
		PerfMeasure dp = mPFax.getPMeasure (PerfMeasureType.DENSITY);
		double perf = dp.getPerf(mSimulGame);
		double expected = 20.0 / 26.0;
		assertTrue (perf >= expected - EPSILON && perf <= expected + EPSILON);
	}
	
	@Test
	public void testIsolatedCells()
	{
		PerfMeasure ic = mPFax.getPMeasure (PerfMeasureType.ISOLATED);
		double perf = ic.getPerf(mSimulGame);
		double expected = 8.0 / 10.0;
		assertTrue (perf >= expected - EPSILON && perf <= expected + EPSILON);
	}
	
	
	private PerfMeasureFactory mPFax;
	private Game mGame;
	private Game.SimulGame mSimulGame;
}
