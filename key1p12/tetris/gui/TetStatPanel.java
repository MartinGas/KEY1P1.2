package key1p12.tetris.gui;

//java API imports
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import key1p12.tetris.game.Game;
import key1p12.tetris.game.GameAction;
import key1p12.tetris.game.HScore;
import key1p12.tetris.game.Score;
//own imports
import key1p12.tetris.game.IGameListener;

public class TetStatPanel extends JPanel 
{
	public static final String maxPlayerPath = new String ("images/maxPlayer.png");
	public static final String currPlayerPath = new String ("images/currPlayer.png");
	public static final String pausePath = new String ("images/pause");
	
	public class ScoreListener implements IGameListener
	{
		public boolean isSensitive(GameAction event) {
			if (event == GameAction.CLEAR)
			{
				System.out.println ("score listener was positively asked for sensitivity");
				return true;
			}
				
			return false;
		}

		@Override
		public void performAction(Game state, GameAction event) 
		{
			updateCurrentScore (state.getCurrScore());
			updateHighScore (state.getHighScore());
			repaint();
		}
		
	}
	
	public TetStatPanel (ActionListener pauseListener)
	{
		int panelRows = 1, panelCols = 3;
		int subPanelRows = 3, subPanelCols = 1;
		//mLabelMaxName = new JLabel (new ImageIcon (maxPlayerPath));
		//mLabelCurrName = new JLabel (new ImageIcon (currPlayerPath));
		mLabelMaxName = new JLabel ();
		mLabelCurrName = new JLabel ();
		mLabelMaxScore = new JLabel();
		mLabelCurrScore = new JLabel();
		JButton pauseButton = new JButton (pausePath);
		pauseButton.addActionListener (pauseListener);
		
		JPanel maxScorePanel = new JPanel (new GridLayout (subPanelRows, subPanelCols));
		maxScorePanel.add (new JLabel ("high score"));
		maxScorePanel.add (mLabelMaxName);
		maxScorePanel.add (mLabelMaxScore);
		
		JPanel currScorePanel = new JPanel (new GridLayout (subPanelRows, subPanelCols));
		currScorePanel.add (new JLabel ("current score"));
		currScorePanel.add (mLabelCurrName);
		currScorePanel.add (mLabelCurrScore);
		
		setLayout (new GridLayout (panelRows, panelCols));
		this.add (maxScorePanel);
		this.add (currScorePanel);
		this.add (pauseButton);
	}
	
	public void updateAll (HScore scoreList)
	{
		updateCurrentScore (new Score (scoreList.getScore(), scoreList.getName()));
		updateHighScore (scoreList.getScore(0));
	}
	
	public void updateCurrentScore (Score current)
	{
		mLabelCurrName.setText ("player: " + current.getName());
		mLabelCurrScore.setText ("score: " + current.getScore());
	}
	
	public void updateHighScore (Score high)
	{
		mLabelMaxName.setText ("player: " + high.getName());
		mLabelMaxScore.setText ("score: " + high.getScore());
	}
	
	//private Score mMax, mCurr;
	private JLabel mLabelMaxName, mLabelMaxScore, mLabelCurrName, mLabelCurrScore;
}
