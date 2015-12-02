package test;

import java.util.ArrayList;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.*;

import key1p12.tetris.game.*;
import key1p12.tetris.gui.*;

public class TetStatPanelTest 
{
	public static class PauseListener implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e) 
		{
			System.out.println ("Pause button pressed");
		}
	}
	
	public static void main (String[] args) throws IOException
	{
		Board board = new Board (10, 10);
		ArrayList <Pentomino> pents = Pentomino.createsPentList();
		Player player = new HumanPlayer("tester");
		File hsfile = new File ("highscores.txt");
		HScore.generateHighScoreFile(hsfile, 5);
		HScore hslist = new HScore (hsfile, "xyz", new ExponentialScore (2, 1, 1));
		
		Game game = new Game(board, pents, player, hslist);
		
		TetStatPanel pnl = new TetStatPanel (new PauseListener());
		pnl.updateCurrentScore (new Score (hslist.getScore(), hslist.getName()));
		pnl.updateHighScore(hslist.getScore(0));
		
		JFrame mframe = new JFrame();
		mframe.setSize (500, 75);
		mframe.add (pnl);
		mframe.setVisible (true);
		
		try
		{
			Thread.sleep(2000);
		}catch (Exception e ) {}
		hslist.increaseScore(2);
		pnl.updateCurrentScore (new Score (hslist.getScore(), hslist.getName()));
		pnl.updateHighScore (hslist.getScore(0));
	}
}
