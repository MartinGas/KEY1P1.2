package key1p12.tetris.gui;

//java API imports
import java.util.HashMap;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.text.JTextComponent;

//own imports
import key1p12.tetris.game.Game;
import key1p12.tetris.game.HScore;
import key1p12.tetris.game.PlayerType;
import key1p12.tetris.bot.BotType;
import key1p12.tetris.bot.PerfMeasureType;

public class TetrisGui extends JFrame 
{	
	
	public static abstract class GameSetupListener implements ActionListener
	{
		public void setupBoardInputs (JTextField widthInputField, JTextField heightInputField)
		{
			mWidthInputField = widthInputField;
			mHeightInputField = heightInputField;
		}
		
		public void setupPlayerNameInput (JTextField nameInputField)
		{
			mNameInputField = nameInputField;
		}
		
		public void setupTypeInput (JComboBox <PlayerType> playerTypeInput, JComboBox <BotType> botTypeInput, JComboBox <PerfMeasureType> PerfMeasureTypeInput)
		{
			mPlayerTypeInput = playerTypeInput;
			mBotTypeInput = botTypeInput;
			mPerfMeasureTypeInput = PerfMeasureTypeInput;
		}
		
		public void setupCustomBotListener (CustomBotListener cbl)
		{
			mCBL = cbl;
		}
	
		/**
		 * Listens to start button
		 */
		public abstract void actionPerformed(ActionEvent e);
		
		public PlayerType getPlayerType()
		{
			return (PlayerType)mPlayerTypeInput.getSelectedItem();
		}
		
		public HashMap <PerfMeasureType, Double> getCustomPMeasure()
		{
			return mCBL.getTypesAndWeights();
		}
		
		public BotType getBotType()
		{
			return (BotType)mBotTypeInput.getSelectedItem();
		}
		
		public PerfMeasureType getPerfMeasureType()
		{
			return (PerfMeasureType)mPerfMeasureTypeInput.getSelectedItem();
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
		
		private JComboBox <PlayerType> mPlayerTypeInput;
		private JComboBox <BotType> mBotTypeInput;
		private JComboBox <PerfMeasureType> mPerfMeasureTypeInput;
		private CustomBotListener mCBL;
		private JTextField  mNameInputField, mWidthInputField, mHeightInputField;
	}
	
	public class CustomBotListener implements ItemListener, ActionListener
	{
		CustomBotListener (JButton adder)
		{
			mAddPerfMeasures = adder;
			mTypeToWeight = new HashMap <PerfMeasureType, Double>();
		}
		
		@Override
		public void actionPerformed(ActionEvent e) 
		{

			PerfMeasureType selection = (PerfMeasureType)JOptionPane.showInputDialog (mGameSetupDialog, "performance measure", "Select...", JOptionPane.QUESTION_MESSAGE, null, PerfMeasureType.values(), PerfMeasureType.values()[0]);
			double weight = Double.parseDouble(JOptionPane.showInputDialog (mGameSetupDialog, "Enter corresponding weight "));
			mTypeToWeight.put (selection, weight);
		}

		@Override
		public void itemStateChanged(ItemEvent e) 
		{
			if (e.getItem() == PerfMeasureType.CUSTOM)
				mAddPerfMeasures.setVisible(true);
			else
			{
				mAddPerfMeasures.setVisible(false);
				mTypeToWeight.clear();
			}
		}
		
		public HashMap <PerfMeasureType, Double> getTypesAndWeights()
		{
			return mTypeToWeight;
		}
		
		private JButton mAddPerfMeasures;
		private HashMap <PerfMeasureType, Double> mTypeToWeight;
	}
	
	public static final Dimension DIALOG_DIM = new Dimension (300, 450);
	public static final Color DEFAULT_BLOCK_COLOR = Color.BLACK;
	
	public enum ScreenType {GAME, SETUP, PAUSE, HIGHSCORE, START, OVER};
	
	
	public TetrisGui (Dimension windowSize, String caption, Image icon)
	{
		setSize(windowSize);
		setTitle (caption);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//TODO setIconImage(icon);
	}
	
	public void setUpGamePanel (Game state, ActionListener pauseButtonListener)
	{
		mGamePanel = new JPanel (new BorderLayout());
		TetStatPanel statPnl = new TetStatPanel (pauseButtonListener);
		statPnl.updateCurrentScore (state.getCurrScore());
		statPnl.updateHighScore (state.getHighScore());
		
		TetBoardPanel boardPnl = new TetBoardPanel (state);
		HashMap <Integer, Color> sharedColMap = new HashMap <Integer, Color> ();
		sharedColMap.put(new Integer (0), DEFAULT_BLOCK_COLOR);
		boardPnl.setup (sharedColMap);
		
		mGamePanel.add (statPnl, BorderLayout.NORTH);
		mGamePanel.add (boardPnl, BorderLayout.CENTER);
		state.addListener (statPnl.new ScoreListener());
		state.addListener (boardPnl.new GameListener());
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
	
	public void setUpGameOverPanel (HScore hsList)
	{
		GameOverPanel goPanel = new GameOverPanel();
		goPanel.setup (hsList);
		
		mGameOverPanel = goPanel;
	}
	
	public void setUpGameSetupPanel (GameSetupListener setupListener)
	{	
		int boardSizeMaxDigits = 3;
		
		class PlayerSelectionListener implements ItemListener
		{
			/**
			 * Constructor
			 * @param tabs panel whose state should be changed when event fired
			 */
			public PlayerSelectionListener (JPanel tabs)
			{
				mTabs = tabs;
			}
			
			public void itemStateChanged (ItemEvent e) 
			{
				PlayerType ptype = (PlayerType)e.getItem();
				CardLayout cl = (CardLayout) mTabs.getLayout();
				assert (cl.getClass() == CardLayout.class);
				cl.show (mTabs, ptype.toString());
			}
			
			private JPanel mTabs;
		}
		
		mGameSetupDialog = new JDialog (this, true);
		mGameSetupDialog.setTitle ("Game setup");
		mGameSetupDialog.setDefaultCloseOperation (DISPOSE_ON_CLOSE);
		mGameSetupDialog.setSize(DIALOG_DIM);
		
		Container cpane = mGameSetupDialog.getContentPane();
		
		cpane.setLayout (new GridBagLayout());
		
		JLabel playerDes = new JLabel ("Configure player");
		
		JComboBox <BotType> botType = new JComboBox <BotType> (BotType.values());
		JComboBox <PerfMeasureType> pmType = new JComboBox <PerfMeasureType> (PerfMeasureType.values());
		
		JButton addCustomPM = new JButton ("+");
		addCustomPM.setVisible(false);
		CustomBotListener customAdder = new CustomBotListener (addCustomPM);
		addCustomPM.addActionListener(customAdder);
		pmType.addItemListener(customAdder);
		
		JTextField enterPlayerName = new JTextField ("name");
		enterPlayerName.selectAll();
		setupListener.setupPlayerNameInput(enterPlayerName);
		
		JPanel botConfigPanel = new JPanel (new GridBagLayout());
		GridBagConstraints cBotType = new GridBagConstraints();
		cBotType.gridx = 0;
		cBotType.gridy = 0;
		cBotType.gridwidth = 1;
		cBotType.gridheight = 1;
		cBotType.fill = GridBagConstraints.BOTH;
		
		GridBagConstraints cPerfMeasureType = (GridBagConstraints) cBotType.clone();
		cPerfMeasureType.gridy = 1;
		GridBagConstraints cCustomAdder = (GridBagConstraints) cBotType.clone();
		cCustomAdder.gridx = 1;
		botConfigPanel.add (botType, cBotType);
		botConfigPanel.add (pmType, cPerfMeasureType);
		botConfigPanel.add (addCustomPM, cCustomAdder);
		setupListener.setupCustomBotListener (customAdder);
		
		JPanel playerConfigPanel = new JPanel (new CardLayout());
		playerConfigPanel.add (enterPlayerName, PlayerType.HUMAN.toString());
		playerConfigPanel.add (botConfigPanel, PlayerType.BOT.toString());
		
		JComboBox <PlayerType> playerType = new JComboBox <PlayerType> (PlayerType.values());
		PlayerSelectionListener listenPlayerSelection = new PlayerSelectionListener (playerConfigPanel);
		playerType.addItemListener (listenPlayerSelection);
		setupListener.setupTypeInput(playerType, botType, pmType);
		
		JLabel boardDes = new JLabel("Configure board");
		JTextField enterWidth = new JTextField ("10");
		enterWidth.setColumns (boardSizeMaxDigits);
		JTextField enterHeight = new JTextField("15");
		enterHeight.setColumns (boardSizeMaxDigits);
		setupListener.setupBoardInputs(enterWidth, enterHeight);
		
		JButton startGameButton = new JButton ("start");
		startGameButton.addActionListener(setupListener);
		
		GridBagConstraints cDesPSelect = new GridBagConstraints();
		cDesPSelect.gridx = 0;
		cDesPSelect.gridy = 0;
		cDesPSelect.gridwidth = 1;
		cDesPSelect.gridheight = 1;
		cDesPSelect.fill = GridBagConstraints.BOTH;
		//cDesPSelect.anchor = GridBagConstraints.PAGE_START;
		
		GridBagConstraints cPTypeSelect = (GridBagConstraints) cDesPSelect.clone();
		cPTypeSelect.gridy = 1;
		cPTypeSelect.anchor = GridBagConstraints.CENTER;
		
		GridBagConstraints cPlayerSelection;
		cPlayerSelection = (GridBagConstraints) cDesPSelect.clone();
		cPlayerSelection.gridy = 2;
		cPlayerSelection.gridheight = 1;
		
		GridBagConstraints cDesBoardConfig = (GridBagConstraints) cDesPSelect.clone();
		cDesBoardConfig.gridx = 1;
		cDesBoardConfig.gridy = 0;
		cDesBoardConfig.gridwidth = 2;
		cDesBoardConfig.gridheight = 1;
		
		GridBagConstraints cBoardWInput;
		cBoardWInput = (GridBagConstraints) cDesBoardConfig.clone();
		cBoardWInput.gridy = 1;
		cBoardWInput.gridwidth = 1;
		
		GridBagConstraints cBoardHInput;
		cBoardHInput = (GridBagConstraints) cBoardWInput.clone();
		cBoardHInput.gridx = 2;
		
		GridBagConstraints cStartButton;
		cStartButton = (GridBagConstraints) cDesPSelect.clone();
		cStartButton.gridx = 1;
		cStartButton.gridy = 2;
		cStartButton.gridwidth = 2;
		cStartButton.gridheight = 1;
		
		mGameSetupDialog.add (playerDes, cDesPSelect);
		mGameSetupDialog.add (playerType, cPTypeSelect);
		mGameSetupDialog.add (playerConfigPanel, cPlayerSelection);
		mGameSetupDialog.add (boardDes, cDesBoardConfig);
		mGameSetupDialog.add (enterWidth, cBoardWInput);
		mGameSetupDialog.add (enterHeight, cBoardHInput);
		mGameSetupDialog.add (startGameButton, cStartButton);
	}
	
	public void setUpPauseMenuPanel (ActionListener resumeListener, ActionListener quitListener)
	{
		mPauseMenuDialog = new JDialog (this, true);
		mPauseMenuDialog.setTitle ("Game paused");
		mPauseMenuDialog.setSize (DIALOG_DIM);
		mPauseMenuDialog.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		mPauseMenuDialog.getContentPane().setLayout (new GridLayout ());
		
		JButton resumeButton = new JButton ("resume");
		resumeButton.addActionListener (resumeListener);
		JButton quitButton = new JButton ("quit");
		quitButton.addActionListener (quitListener);
		
		mPauseMenuDialog.add(resumeButton);
		mPauseMenuDialog.add(quitButton);
	}
	
	public void setUpHighScorePanel (HScore scoreList)
	{
		mHighScoreDialog = new JDialog (this, true);
		mHighScoreDialog.setDefaultCloseOperation (JDialog.DISPOSE_ON_CLOSE);
		mHighScoreDialog.setTitle ("high scores");
		mHighScoreDialog.setSize (DIALOG_DIM);
		mHighScoreDialog.setLayout (new BorderLayout());
		
		HighScorePanel hsPanel = new HighScorePanel();
		hsPanel.setup (scoreList);
		
		mHighScoreDialog.add (hsPanel, BorderLayout.CENTER);
		
		hsPanel.update (scoreList);
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
		case OVER:
			assert (mGameOverPanel != null);
			swapIn = mGameOverPanel;
			break;
		default: assert (false);
			break;
		}
		
		getContentPane().removeAll();
		add (swapIn);
		revalidate();
		repaint();
		requestFocus();
		assert (isFocusOwner());
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
		}
	}
	
	public void hideDialog (ScreenType diag)
	{
		assert (diag == ScreenType.HIGHSCORE || diag == ScreenType.PAUSE || diag == ScreenType.SETUP);
		switch (diag)
		{
		case HIGHSCORE: 	mHighScoreDialog.setVisible (false);
		break;
		case PAUSE: 		mPauseMenuDialog.setVisible (false);
		break;
		case SETUP: 		mGameSetupDialog.setVisible (false);
		break;
		}
	}
	
	private JPanel mGamePanel, mMainMenuPanel, mGameOverPanel;
	private JDialog mGameSetupDialog, mPauseMenuDialog, mHighScoreDialog;
}
