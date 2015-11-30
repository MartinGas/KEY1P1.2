package key1p12.tetris.gui;

import java.util.ArrayList;
import javax.swing.*;
import java.awt.*;

import key1p12.tetris.game.HScore;
import key1p12.tetris.game.Score;

@SuppressWarnings("serial")
public class HighScoreDialog extends JDialog 
{
	public HighScoreDialog (HScore source, Frame owner, boolean modal)
	{
		super (owner, modal);
		mDisplayContents = new ArrayList <ArrayList <JLabel>>();
		mSource = source;
	}
	
	public void setup()
	{
		int entries = mSource.getNumberEntries();
		super.getContentPane().setLayout(new GridLayout (entries, 3));
		for (int cEntries = 0; cEntries < entries; ++cEntries)
		{
			ArrayList <JLabel> newEntry = new ArrayList <JLabel>();
			JLabel rank = new JLabel();
			newEntry.add(rank);
			JLabel score = new JLabel();
			newEntry.add (score);
			JLabel name = new JLabel();
			newEntry.add (name);
			mDisplayContents.add (newEntry);
		}
	}
	
	public void paint (Graphics g)
	{
		for (int cEntries = 0; cEntries < mSource.getNumberEntries(); cEntries++)
		{
			Score s = mSource.getScore(cEntries);
			ArrayList <JLabel> comps = mDisplayContents.get (cEntries);
			assert (comps.size() == 3);
			comps.get (0).setText ("" + (cEntries + 1));
			comps.get (1).setText ("" + s.getScore());
			comps.get (2).setText (s.getName());
		}
		super.paint (g);
	}
	
	private ArrayList <ArrayList <JLabel>> mDisplayContents;
	private HScore mSource;
}
