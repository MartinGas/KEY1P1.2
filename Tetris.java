
//java API imports
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.*;

//own imports
import key1p12.tetris.game.*;
import key1p12.tetris.bot.*;
import key1p12.tetris.gui.*;

//class initializing and controling the application
public class Tetris 
{
	public final static Dimension mMAIN_FRAME_DIM = new Dimension (400, 700);
	public final static String mCAPTION = new String ("Tetris!");
	public final static String iconImgPath = "res/image/appIcon.png";
	public final static String hsFilePath = "res/hscores.txt";
	
	public final static int mBOARD_COLS = 5, mBOARD_ROWS = 15;
	
	public static void main (String args[])
	{
		Tetris app = null;
		try
		{
			app = new Tetris();
		}
		catch (IOException ioe)
		{
			System.err.println ("could not read file, application terminates now");
		}
		
		app.showMainMenu();
	}
	
	public class PauseButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e) 
		{
			Tetris.this.pauseGame();
		}
	}
	
	public class ResumeButtonListener implements ActionListener
	{
		public void actionPerformed (ActionEvent e)
		{
			Tetris.this.resumeGame();
		}
	}
	
	public class BackToMainMenuListener implements ActionListener
	{
		public void actionPerformed (ActionEvent e)
		{
			//go back to main menu
		}
	}
	
	public class LaunchGameListener implements ActionListener
	{
		public void actionPerformed (ActionEvent e)
		{
			//start the game
		}
	}
	
	public class LaunchHSListener implements ActionListener
	{
		public void actionPerformed (ActionEvent e)
		{
			//display high score list
		}
	}
	
	public class QuitGameListener implements ActionListener
	{
		public void actionPerformed (ActionEvent e)
		{
			mGui.setVisible(false);
			mGui.dispose();
		}
	}
	
	
	
	//public section
	public Tetris () throws IOException
	{
		//construct gui
		BufferedImage iconImg = ImageIO.read(new File (iconImgPath));
		mGui = new TetrisGui (mMAIN_FRAME_DIM, mCAPTION, iconImg);
		//lauch main menu
		
		//construct player
		
		
		//construct highscore
		//construct game board
		Board tetrisBoard = new Board (mBOARD_COLS, mBOARD_ROWS);
		//construct list of pentominoes
		ArrayList <Pentomino> pentList = Pentomino.createsPentList();
		//construct game
		
		mGui.setUpGamePanel (mGame, new PauseButtonListener());
		mGui.setUpMainMenuPanel(playListener, hsListener, quitListener);
		
		//add player listener
		
		/*control flow:
		*launch main menu
		*selection
		*	play game: launch setup menu => (via listener) play game
		*	display high scores: launch highscore frame, return: (via listener) launch main menu
		*	quit: exit game
		*/
		
	}
	
	//protected section
	
	
	//private section
	
	private void showMainMenu()
	{
		mGui.loadPanel (TetrisGui.ScreenType.START);
		if (!mGui.isVisible())
			mGui.setVisible (true);
	}
	
	private void playGame()
	{
		while (!mGame.isGameOver() && !mGame.isGamePaused())
		{
			mGame.play();
		}
	}
	
	private void pauseGame()
	{
		mGame.pause();
		//show pause screen
	}
	
	private void resumeGame()
	{
		playGame();
	}
	
	private Game mGame;
	//private HScore mHighestScores;
	private TetrisGui mGui;
}
