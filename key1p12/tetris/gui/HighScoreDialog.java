package key1p12.tetris.gui;

import java.util.ArrayList;
import javax.swing.*;
import java.awt.*;

import key1p12.tetris.game.HScore;
import key1p12.tetris.game.Score;
import key1p12.tetris.game.Game;

@SuppressWarnings("serial")
public class HighScoreDialog extends JDialog 
{
	public HighScoreDialog (JFrame owner, boolean modal)
	{
		super (owner, modal);
		mDisplayContents = new ArrayList <ArrayList <JLabel>>();
	}
	
	public void setup (HScore hslist)
	{
		mDisplayContents.clear();
		int entries = hslist.getNumberEntries();
		super.getContentPane().setLayout(new GridLayout (entries, 3));
		for (int cEntries = 0; cEntries < entries; ++cEntries)
		{
			ArrayList <JLabel> newEntry = new ArrayList <JLabel>();
			JLabel rank = new JLabel();
			newEntry.add(rank);
			add (rank);
			JLabel score = new JLabel();
			newEntry.add (score);
			add (score);
			JLabel name = new JLabel();
			newEntry.add (name);
			add (name);
			mDisplayContents.add (newEntry);
		}
	}
	
	public void update (HScore hslist)
	{
		for (int cEntries = 0; cEntries < hslist.getNumberEntries(); cEntries++)
		{
			Score s = hslist.getScore(cEntries);
			ArrayList <JLabel> comps = mDisplayContents.get (cEntries);
			assert (comps.size() == 3);
			comps.get (0).setText ("" + (cEntries + 1));
			comps.get (1).setText ("" + s.getScore());
			comps.get (2).setText (s.getName());
		}
	}
	
	public void paint (Graphics g)
	{
		
		super.paint (g);
	}
	
	private ArrayList <ArrayList <JLabel>> mDisplayContents;
}
