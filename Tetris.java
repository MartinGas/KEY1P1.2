
//java API imports
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

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
	public final static String hsFilePath = "highscores.txt";
	
	public final static int mBOARD_COLS = 5, mBOARD_ROWS = 15, mHSLIST_ENTRIES = 10;
	
	public static void main (String args[])
	{
		//Game.DEBUG = true;
		
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
			mGui.showDialog (TetrisGui.ScreenType.PAUSE);
		}
	}
	
	public class ResumeButtonListener implements ActionListener
	{
		public void actionPerformed (ActionEvent e)
		{
			Thread t = new Thread (new GameRunner());
			mGui.hideDialog (TetrisGui.ScreenType.PAUSE);
			t.start();
		}
	}
	
	public class LaunchGameSetupListener implements ActionListener
	{
		public void actionPerformed (ActionEvent e)
		{
			mGui.showDialog (TetrisGui.ScreenType.SETUP);
		}
	}
	
	public class LaunchHSListener implements ActionListener
	{
		public void actionPerformed (ActionEvent e)
		{
			if (mGame == null)
			{
				try
				{
					File hsfile = new File (hsFilePath);
					if (!hsfile.exists())
						HScore.generateHighScoreFile (hsfile, mHSLIST_ENTRIES);
					HScore hslist = new HScore (hsfile, "", new ExponentialScore(0, 0, 0));
					mGui.setUpHighScorePanel (hslist);
				}
				catch (IOException eio)
				{
					System.out.println ("Game does not exist");
					System.out.println ("Problem creating high score list.");
					System.out.println (eio.getMessage());
				}
			}
			mGui.showDialog (TetrisGui.ScreenType.HIGHSCORE);
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
	
	public class GameSetupListener extends TetrisGui.GameSetupListener
	{
		public void actionPerformed (ActionEvent e) 
		{
			constructGame (this);
			mGui.setUpHighScorePanel (mHSList);
			mGui.setUpGamePanel (mGame, new PauseButtonListener());
			
			mGui.hideDialog(TetrisGui.ScreenType.SETUP);
			mGui.showPanel (TetrisGui.ScreenType.GAME);
			
			Thread t = new Thread (new GameRunner());
			t.start();
			/*mGui.setVisible (false);
			mGui.dispose();*/
		}
	}
	
	public class GameRunner implements Runnable
	{
		public void run() 
		{
			mGui.setFocusable(true);
			mGui.requestFocus();
			
			if (mGame.isGamePaused())
				mGame.play();
			
			while (!mGame.isGameOver() && !mGame.isGamePaused())
			{
				mGame.play();
				if (!mGui.hasFocus())
					System.out.println ("lost focus");
			}
			
			if (mGame.isGameOver())
			{
				try
				{
					mHSList.writeToFile();
				}
				catch (IOException e)
				{
					System.err.println ("Unable to save changes to high score");
				}
				mGui.setUpGameOverPanel(mHSList);
				mGui.showPanel (TetrisGui.ScreenType.OVER);
			}
		}
		
	}
	
	
	
	//public section
	/**
	 * Sets up gui (except for high score panel)
	 * @throws IOException
	 */
	public Tetris () throws IOException
	{
		//construct gui
		//TODOBufferedImage iconImg = ImageIO.read(new File (iconImgPath));
		//mGui = new TetrisGui (mMAIN_FRAME_DIM, mCAPTION, iconImg);
		mGui = new TetrisGui (mMAIN_FRAME_DIM, mCAPTION, new BufferedImage(1, 1, 1));
		mGui.setUpMainMenuPanel(new LaunchGameSetupListener(), new LaunchHSListener(), new QuitGameListener());
		mGui.setUpPauseMenuPanel(new ResumeButtonListener(), new QuitGameListener());
		mGui.setUpGameSetupPanel(new GameSetupListener());
		
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
		mGui.showPanel (TetrisGui.ScreenType.START);
		if (!mGui.isVisible())
			mGui.setVisible (true);
	}
	
	/**
	 * Constructs player, highscore, game and attaches relevant listeners
	 */
	private void constructGame (TetrisGui.GameSetupListener setup)
	{
		ArrayList <IGameListener> gameListeners = new ArrayList <IGameListener>();
		//create player
		Player player = null;
		if (setup.getPlayerType() == TetrisGui.PlayerType.HUMAN)
		{
			HumanPlayer hplayer = new HumanPlayer(setup.getPlayerName());
			//construct and add keyboard input listener
			mGui.addKeyListener (hplayer.new InputListener());
			player = hplayer;
		}
		/*else
			player = <bot constructors>*/
		assert (player != null);
		//create & load high score
		try
		{
			File hsFile = new File (hsFilePath);
			if (!hsFile.exists())
				HScore.generateHighScoreFile(hsFile, mHSLIST_ENTRIES);
			mHSList = new HScore(hsFile, player.getName(), new ExponentialScore(2, 1, 1));
		}
		//if generating the high score file does not work
		catch (IOException e1) 
		{
			System.err.println ("Could not generate high score file");
		}
		
		//create board
		Board gameBoard = new Board (setup.getInputWidth(), setup.getInputHeight());
		//create pentominoes
		ArrayList <Pentomino> pentsToUse = Pentomino.createsPentList();
		//create game
		mGame = new Game(gameBoard, pentsToUse, player, mHSList);
		addGameListeners (gameListeners);
	}
	
	private void addGameListeners (ArrayList <IGameListener> gameListeners)
	{
		 for (IGameListener l : gameListeners)
			 mGame.addListener(l);
	}
	
	private void pauseGame()
	{
		mGame.pause();
		//show pause screen
	}

	private HScore mHSList;
	private Game mGame;
	//private HScore mHighestScores;
	private TetrisGui mGui;
}
