package test;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.io.*;

import key1p12.tetris.gui.*;

public class BlockTest extends JFrame {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception
	{
		BlockTest bt = new BlockTest();
		bt.newVals(10);
		bt.setVisible(true);
		
		Thread.sleep (2500);
		
		bt.newVals(10);
		bt.repaint();
	}
	
	public BlockTest() throws Exception
	{
		mBlocks = new ArrayList<Block>();
		
		setSize (400, 400);
		setLayout (new GridLayout (2, 2));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		HashMap <Integer, Color> sharedColMap = new HashMap <Integer, Color>();
		sharedColMap.put(0, Color.blue);
		
		for (int cBlock = 0; cBlock < 4; cBlock++)
		{
			mBlocks.add (new Block (sharedColMap));
			add (mBlocks.get(mBlocks.size() - 1));
		}
	}
	
	public void newVals (int maxVal)
	{
		Random randVal = new Random();
		for (Block b : mBlocks)
			b.setState (randVal.nextInt (maxVal + 1));
	}
	
	ArrayList <Block> mBlocks;
}
