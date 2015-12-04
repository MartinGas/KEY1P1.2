package key1p12.tetris.gui;

//java API imports
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.text.JTextComponent;

//own imports
import key1p12.tetris.game.Game;
import key1p12.tetris.game.HScore;

public class TetrisGui extends JFrame 
{	
	public static abstract class GameSetupListener implements ActionListener, ItemListener
	{
		public void setupInputs (JTextField playerNameField, JTextField widthInputField, JTextField heightInputField)
		{
			mNameInputField = playerNameField;
			mWidthInputField = widthInputField;
			mHeightInputField = heightInputField;
		}
		
		/**
		 * Listens to human radio box
		 * changes state of name input box
		 */
		public void itemStateChanged(ItemEvent e) 
		{
			if (e.equals(ItemEvent.SELECTED))
			{
				mPType = PlayerType.HUMAN;
				mNameInputField.setText ("bot chooses name");
				mNameInputField.setEditable(false);
			}
			else if (e.equals(ItemEvent.DESELECTED))
			{
				mPType = PlayerType.BOT;
				mNameInputField.setText ("bot chooses name");
				mNameInputField.setEditable(false);
			}
		}
	
		/**
		 * Listens to start button
		 */
		public abstract void actionPerformed(ActionEvent e);
		
		public PlayerType getPlayerType()
		{
			return mPType;
		}
		
		public String getPlayerName()
		{
			return mNameInputField.getText();
		}
		
		public int getInputWidth()
		{
			return Integer.parseInt (mWidthInputField.getText());
		}
		
		public int getInputHeight()
		{
			return Integer.parseInt (mHeightInputField.getText());
		}
		
		private PlayerType mPType;
		
		private JTextField  mNameInputField, mWidthInputField, mHeightInputField;
	}
	
	public static final Dimension DIALOG_DIM = new Dimension (300, 450);
	
	public enum PlayerType {HUMAN, BOT};
	
	public enum ScreenType {GAME, SETUP, PAUSE, HIGHSCORE, START};
	
	public TetrisGui (Dimension windowSize, String caption, Image icon)
	{
		setSize(windowSize);
		setTitle (caption);
		//TODO setIconImage(icon);
	}
	
	public void setUpGamePanel (Game state, ActionListener pauseButtonListener)
	{
		mGamePanel = new JPanel (new BorderLayout());
		TetStatPanel statPnl = new TetStatPanel (pauseButtonListener);
		statPnl.updateCurrentScore (state.getCurrScore());
		statPnl.updateHighScore (state.getHighScore());
		mGamePanel.add (statPnl, BorderLayout.NORTH);
		mGamePanel.add (new TetBoardPanel (state), BorderLayout.CENTER);
		mGamePanel.revalidate();
	}
	
	public void setUpMainMenuPanel (ActionListener playListener, ActionListener hsListener, ActionListener quitListener)
	{
		mMainMenuPanel = new JPanel (new GridLayout (3, 1));
		
		JButton playButton, hsButton, quitButton;
		playButton = new JButton();
		playButton.setText ("play game!");
		playButton.addActionListener (playListener);
		hsButton = new JButton();
		hsButton.setText ("show highscores!");
		hsButton.addActionListener (hsListener);
		quitButton = new JButton();
		quitButton.setText ("exit game");
		quitButton.addActionListener (quitListener);
		
		mMainMenuPanel.add (playButton);
		mMainMenuPanel.add (hsButton);
		mMainMenuPanel.add (quitButton);
	}
	
	public void setUpGameSetupPanel (GameSetupListener setupListener)
	{	
		mGameSetupDialog = new JDialog (this, true);
		mGameSetupDialog.setTitle ("Game setup");
		mGameSetupDialog.setDefaultCloseOperation (DISPOSE_ON_CLOSE);
		mGameSetupDialog.setSize(DIALOG_DIM);
		
		Container cpane = mGameSetupDialog.getContentPane();
		
		cpane.setLayout (new GridBagLayout());
		
		JLabel playerDes = new JLabel ("Choose the type of player and a name if relevant");
		JRadioButton isHuman, isBot;
		isHuman = new JRadioButton ("Human plays", true);
		isBot = new JRadioButton ("Bot plays", false);
		ButtonGroup playerSelection = new ButtonGroup();
		JTextField enterPlayerName = new JTextField ("Player's name");
		
		isHuman.addItemListener (setupListener);
		
		
		playerSelection.add (isHuman);
		playerSelection.add (isBot);
		
		JPanel playerPanel = new JPanel (new GridLayout (3, 1));
		playerPanel.add (playerDes);
		playerPanel.add (isHuman);
		playerPanel.add (isBot);
		playerPanel.add (enterPlayerName);
		
		
		
		cpane.setLayout (new GridLayout (1,1));
		
		JLabel playerDes = new JLabel ("Choose the type of player and a name if relevant");
		JRadioButton isHuman = new JRadioButton ("Human plays", true);
		isHuman.addItemListener (setupListener);
		JRadioButton isBot = new JRadioButton ("Bot plays", false);
		ButtonGroup playerSelection = new ButtonGroup();
		playerSelection.add (isHuman);
		playerSelection.add (isBot);
		JTextField enterPlayerName = new JTextField ("Player's name");
		JPanel playerPanel = new JPanel (new GridLayout (3, 1));
		playerPanel.add (playerDes);
		playerPanel.add (isHuman);
		playerPanel.add (isBot);
		playerPanel.add (enterPlayerName);
		
		JLabel boardDes = new JLabel ("Select the size of the board");
		JTextField enterWidth = new JTextField ();
		JTextField enterHeight = new JTextField ();
		JPanel boardPanel = new JPanel (new GridLayout (3, 1));
		boardPanel.add (boardDes);
		boardPanel.add (enterWidth);
		boardPanel.add (enterHeight);
		
		JButton startButton = new JButton ("go!");
		startButton.addActionListener (setupListener);
		
		mGameSetupDialog.add (playerPanel);
		mGameSetupDialog.add (boardPanel);
		mGameSetupDialog.add (startButton);
		
		setupListener.setupInputs(enterPlayerName, enterWidth, enterHeight);
	}
	
	public void setUpPauseMenuPanel (ActionListener resumeListener, ActionListener quitListener)
	{
		mPauseMenuDialog = new JDialog (this, true);
		mPauseMenuDialog.setTitle ("Game paused");
		mPauseMenuDialog.setSize (DIALOG_DIM);
		mPauseMenuDialog.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		mPauseMenuDialog.getContentPane().setLayout (new GridLayout ());
		
		JButton resumeButton = new JButton();
		resumeButton.addActionListener (resumeListener);
		JButton quitButton = new JButton();
		quitButton.addActionListener (quitListener);
		
		mPauseMenuDialog.add(resumeButton);
		mPauseMenuDialog.add(quitButton);
	}
	
	public void setUpHighScorePanel (HScore scoreList)
	{
		HighScoreDialog hsdiag = new HighScoreDialog (this, true);
		hsdiag.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		hsdiag.setTitle ("high scores");
		hsdiag.setSize(DIALOG_DIM);
		hsdiag.setup (scoreList);
		
		hsdiag.update (scoreList);
		mHighScoreDialog = hsdiag; 
	}
	
	
	
	public void showPanel (ScreenType panelToLoad)
	{
		assert (panelToLoad == ScreenType.GAME || panelToLoad == ScreenType.START);
		JPanel swapIn = null;
		switch (panelToLoad)
		{
		case GAME:
			assert (mGamePanel != null);
			swapIn = mGamePanel;
			break;
		case START:
			assert (mMainMenuPanel != null);
			swapIn = mMainMenuPanel;
			break;
		default: assert (false);
			break;
		}
		
		getContentPane().removeAll();
		add (swapIn);
		revalidate();
		repaint();
	}
	
	public void showDialog (ScreenType diag)
	{
		assert (diag == ScreenType.HIGHSCORE || diag == ScreenType.PAUSE || diag == ScreenType.SETUP);
		switch (diag)
		{
		case HIGHSCORE: 	mHighScoreDialog.setVisible (true);
		break;
		case PAUSE: 		mPauseMenuDialog.setVisible (true);
		break;
		case SETUP: 		mGameSetupDialog.setVisible (true);
		break;
		default: 
		}
	}
	
	private JPanel mGamePanel, mMainMenuPanel;
	private JDialog mGameSetupDialog, mPauseMenuDialog, mHighScoreDialog;
}
