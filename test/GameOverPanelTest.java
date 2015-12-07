package test;

import javax.swing.*;
import java.awt.*;
import java.io.File;

import key1p12.tetris.game.ExponentialScore;
import key1p12.tetris.game.HScore;
import key1p12.tetris.gui.GameOverPanel;


public class GameOverPanelTest 
{
	
	public static void main (String[] args) throws Exception
	{
		JFrame mainFrame = new JFrame();
		setupFrame (mainFrame);
		
		HScore hsList = loadHS();
		
		GameOverPanel goPanel = new GameOverPanel();
		mainFrame.add (goPanel);
		mainFrame.setVisible(true);
		
		goPanel.setup (hsList);
		goPanel.update (hsList);
		
		
		
	}
	
	public static void setupFrame (JFrame frame)
	{
		frame.setSize (600, 400);
		frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
	}
	
	public static HScore loadHS () throws Exception
	{
		File hsFile = new File ("highScores.txt");
		if (!hsFile.exists())
			HScore.generateHighScoreFile(hsFile, 10);
		
		HScore scores = new HScore(hsFile, "xyz", new ExponentialScore(1, 1, 1));
		return scores;
	}
}
