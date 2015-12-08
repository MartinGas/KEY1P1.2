package test;

import java.io.*;
import java.util.ArrayList;

import key1p12.tetris.bot.DensityPerformance;
import key1p12.tetris.bot.HeightDiffPerformance;
import key1p12.tetris.bot.RectangularPerformance;
import key1p12.tetris.game.*;
import key1p12.tetris.game.Game.SimulGame;

public class PerfMeasureTest 
{
	public static void main (String[] args) throws IOException
	{
		Board b = new Board (13, 10);
		ArrayList <Pentomino> ps = Pentomino.createsPentList();
		
		for (int cPent = 0; cPent < 4; ++cPent)
		{
			Pentomino place = ps.get(cPent);
			Position bottomPos = new Position (3 * cPent, b.getHeight() - place.getHeight());
			b.placePent(place, bottomPos.getPosNum(b.getHeight()));
		}
		
		b.printBoard();
		
		File hsFile = new File ("highscores.txt");
		if (!hsFile.exists())
			HScore.generateHighScoreFile(hsFile, 10);
		HScore hsList = new HScore(hsFile, "bla", new ExponentialScore(1, 1, 1));
		
		Game g = new Game (b, ps, new HumanPlayer(""), hsList);
		SimulGame sg = new Game.SimulGame (g);
		
		testHeightDiff(sg);
		testRect(sg);
		testDensity(sg);
		
	}
	
	public static void testHeightDiff (SimulGame g)
	{
		System.out.print ("height difference performance ");
		System.out.println (new HeightDiffPerformance().getPerf(g));
	}
	
	public static void testRect (SimulGame g)
	{
		System.out.print ("rectangular performance ");
		System.out.println (new RectangularPerformance().getPerf(g));
	}
	
	public static void testDensity (SimulGame g)
	{
		System.out.print ("Density performance ");
		System.out.println (new DensityPerformance().getPerf (g));
	}
	
	
	
}
