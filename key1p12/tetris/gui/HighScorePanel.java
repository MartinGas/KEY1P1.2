package key1p12.tetris.gui;

import java.util.ArrayList;
import javax.swing.*;
import java.awt.*;

import key1p12.tetris.game.HScore;
import key1p12.tetris.game.Score;
import key1p12.tetris.game.Game;

@SuppressWarnings("serial")
public class HighScorePanel extends JPanel 
{
	public HighScorePanel ()
	{
		mDisplayContents = new ArrayList <ArrayList <JLabel>>();
	}
	
	public void setup (HScore hslist)
	{
		mDisplayContents.clear();
		int entries = hslist.getNumberEntries();
		setLayout(new GridLayout (entries, 3));
		for (int cEntries = 0; cEntries < entries; ++cEntries)
		{
			ArrayList <JLabel> newEntry = new ArrayList <JLabel>();
			JLabel rank = new JLabel ();
			rank.setHorizontalAlignment(SwingConstants.CENTER);
			rank.setVerticalAlignment (SwingConstants.CENTER);
			newEntry.add(rank);
			add (rank);
			JLabel score = new JLabel();
			score.setHorizontalAlignment(SwingConstants.CENTER);
			score.setVerticalAlignment (SwingConstants.CENTER);
			newEntry.add (score);
			add (score);
			JLabel name = new JLabel();
			name.setHorizontalAlignment(SwingConstants.CENTER);
			name.setVerticalAlignment (SwingConstants.CENTER);
			newEntry.add (name);
			add (name);
			mDisplayContents.add (newEntry);
		}
		update (hslist);
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
	
	private ArrayList <ArrayList <JLabel>> mDisplayContents;
}
