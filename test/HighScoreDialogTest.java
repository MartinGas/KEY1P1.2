package test;

import java.io.File;
import java.io.IOException;

import key1p12.tetris.gui.*;
import key1p12.tetris.game.*;
import javax.swing.*;

public class HighScoreDialogTest 
{
	public static void main (String[] args)
	{
		File hsfile = new File ("highscores.txt");
		try 
		{
			HScore.generateHighScoreFile(hsfile, 5);
			HScore hslist = new HScore (hsfile, "xyz", new ExponentialScore (2, 1, 1));
			JFrame mframe = new JFrame();
			setupFrame (mframe);
			HighScoreDialog hsdialog = new HighScoreDialog(mframe, true);
			hsdialog.setDefaultCloseOperation (JDialog.HIDE_ON_CLOSE);
			hsdialog.setup (hslist);
			hsdialog.setSize (200, 200);
			mframe.setVisible (true);
			hsdialog.setVisible(true);
			
			hslist.increaseScore(10);
			hslist.writeToFile();
			hsdialog.update (hslist);
			hsdialog.setVisible(true);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
	}
	
	public static void setupFrame (JFrame frame)
	{
		frame.setSize (400, 400);
		frame.setTitle ("High score dialog test");
		frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
	}
	
}
