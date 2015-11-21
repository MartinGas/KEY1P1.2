
//java API imports
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

//own imports
import key1p12.tetris.game.*;
import key1p12.tetris.bot.*;
import key1p12.tetris.gui.*;

//class initializing and controling the application
public class Tetris 
{
	
	public class PauseButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e) 
		{
			Tetris.this.pauseGame();
		}
	}
	
	//public section
	public Tetris ()
	{
		//construct player
		//construct highscore
		//construct game board
		//construct list of pentominoes
		//construct game
		//construct gui
		
		mIsGamePaused = false;
		//launch game
	}
	
	//protected section
	
	
	//private section
	/*private HScore initHScores()
	{
		
	}*/
	
	private void playGame()
	{
		while (!mIsGamePaused)
		{
			mGame.play();
		}
	}
	
	private void pauseGame()
	{
		mIsGamePaused = true;
		//show pause screen
	}
	
	private Game mGame;
	//private HScore mHighestScores;
	
	private JFrame mGameFrame;
	private boolean mIsGamePaused;
}
