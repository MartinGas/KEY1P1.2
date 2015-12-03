package test;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.io.*;

import key1p12.tetris.gui.*;
import key1p12.tetris.game.*;

public class TetBoardPanelTest extends JFrame {

	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		try
		{
			TetBoardPanelTest testframe = new TetBoardPanelTest();
		}
		catch (Exception e) 
		{
			System.out.println ("oh oh");
			e.printStackTrace();
		}

	}
	
	public TetBoardPanelTest () throws IOException, InterruptedException
	{
		setLayout (new GridLayout (1,1));
		setSize (500, 500);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Board board = new Board (10, 10);
		ArrayList <Pentomino> pents = Pentomino.createsPentList();
		board.placePent (pents.get(0), 1);
		board.placePent (pents.get(1), 6);
		
		
		mPnl = new TetBoardPanel (board);
		HashMap <Integer, Color> sharedColMap = new HashMap <Integer, Color>();
		sharedColMap.put(0, Color.GRAY);
		mPnl.setup (sharedColMap);
		mPnl.update(board);
		
		System.out.println ("Getting first frame on screen");
		add (mPnl);
		setVisible(true);
		Thread.sleep (2500);
		
		System.out.println ("Getting second frame on screen");
		board.placePent(pents.get (2), 52);
		mPnl.update(board);
		mPnl.repaint();
	}
	
	
	private TetBoardPanel mPnl;
}
