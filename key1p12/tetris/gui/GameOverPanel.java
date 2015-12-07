package key1p12.tetris.gui;

import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;

import key1p12.tetris.game.Game;
import key1p12.tetris.game.GameAction;
import key1p12.tetris.game.HScore;
import key1p12.tetris.game.IGameListener;

public class GameOverPanel extends JPanel 
{
	public static Rectangle2D textBox = new Rectangle2D.Double (0.05, 0.1, 0.9, 0.3);
	public static Font GameOverFont = new Font (Font.SANS_SERIF, Font.BOLD, 36);
	public static Font HighScoreFont = new Font (Font.SANS_SERIF, Font.PLAIN, 24);
	
	public GameOverPanel ()
	{
		mHsPanel = new HighScorePanel ();	
	}
	
	public void setup (HScore hsList)
	{
		mHsPanel.setup(hsList);
		
		Dimension textPanelSize = new Dimension ((int) (textBox.getWidth() * getWidth()), (int) (textBox.getHeight() * getHeight()));
		TextPanel gameOverText = new TextPanel ("Game over");
		gameOverText.setFont (GameOverFont);
		TextPanel highScoreText = new TextPanel ("high scores");
		highScoreText.setFont (HighScoreFont);
		
		setLayout (new GridBagLayout());
		GridBagConstraints cGameOverText = new GridBagConstraints();
		cGameOverText.gridx = 0; 
		cGameOverText.gridy = 0;
		cGameOverText.gridwidth = 1;
		cGameOverText.gridheight = 1;
		cGameOverText.weightx = 0.5;
		cGameOverText.weighty = 0.5;
		cGameOverText.fill = GridBagConstraints.BOTH;
		cGameOverText.anchor = GridBagConstraints.CENTER;
		
		GridBagConstraints cHighScoreText = (GridBagConstraints) cGameOverText.clone();
		cHighScoreText.gridy = 1;
		
		GridBagConstraints cHsPanel = (GridBagConstraints) cGameOverText.clone();
		cHsPanel.gridy = 2;
		
		add (gameOverText, cGameOverText);
		add (highScoreText, cHighScoreText);
		add (mHsPanel, cHsPanel);
	}
	
	public void update (HScore hsList)
	{
		mHsPanel.update(hsList);
		repaint();
	}
	
	public void paintComponent (Graphics g)
	{
		Graphics2D g2 = (Graphics2D)g;
		
		super.paintComponent(g2);
	}
	
	private static class TextPanel extends JPanel
	{
		private static Font defaultFont = new Font (Font.SANS_SERIF, Font.PLAIN, 36);
		
		public TextPanel (String text)
		{
			mText = text;
			mFont = defaultFont;
		}
		
		public void setFont (Font newFont)
		{
			mFont = newFont;
		}
		
		public void paintComponent (Graphics g)
		{
			Graphics2D g2 = (Graphics2D)g;
			g2.setFont(mFont);
			
			Rectangle2D clip = new Rectangle2D.Double();
			FontMetrics fm = g2.getFontMetrics();
			int drawW = fm.stringWidth(mText);
			int drawH = fm.getHeight();
			int drawX = (int) Math.floor ((getWidth() - drawW) / 2);
			int drawY = (int) Math.floor ((getHeight() + drawH) / 2);
			g2.drawString (mText, drawX, drawY);
		}
		
		private String mText;
		private Font mFont;
	}
	
	private HighScorePanel mHsPanel;
}
